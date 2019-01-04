#include "semantics.h"
#include "token.h"
#include "node.h"
#include <iostream>
#include <fstream>
#include <stdlib.h>
#include <vector>

using namespace std;

//The maximum number of allowed variable definitions.
const int MAX_VARS = 100;

//The scope of a variable or variables
static int var_scope = 0;

//The number of temporary variables used for local scope
//operations
static int temp_vars = 0;

static int num_repeat = 0;

//This variable will be used to test an individual variable on the
//stack, such as if it has been defined or can be used in a certain
//scope.
static stack_t temp;

//The stack, which will hold all the variables defined.
vector<stack_t> stack;

//The current location on the stack
int stack_location = 0;

//The number of variables currently on the stack
int stack_vars = 0;

/*Recurisively search through the entire parse tree. For
 * each <var> production, verify that an identifier with this
 * name does not already exist in the scope.
 * For each <block> production, increment the scope so local
 * variables defined here are local to this block.
 * For any of the children of a <stat> production, check
 * that the variables being accessed have already been
 * defined and are accessible in this scope.*/
void semantics(node_t *root)
{
  if (root == NULL)
    return;

  //<program> -> void <vars> <block>
  if (root->label == "<program>")
  {
    semantics(root->child_1); //process <vars>
    semantics(root->child_2); //process <block>

    //Program stopping point
    target_file << "STOP\n";

    //Pop the remaining variables (global) from the stack.
    remove_local_vars(var_scope);
    var_scope--;

    //Once this point is reached, the entire parse tree was verified
    //for semantic errors. Print a message indicating that the semantics
    //are OK, and return.
    cout << "Semantics OK\n";

    //For each temporary variable that was allocated for the program,
    //give them an initial value of 0.
    while (temp_vars > 0)
    {
      target_file << "T" << (temp_vars - 1) << " 0\n";
      temp_vars--;
    }

    return;
  }

  //<block> -> begin <vars> <stats> end
  if (root->label == "<block>")
  {
    //Since we are entering a new <block>, increment the scope
    //for all variables defined in this block.
    var_scope++;

    semantics(root->child_1); //process <vars>
    semantics(root->child_2); //process <stats>

    //Leaving this <block> production, so decrement the scope, and
    //remove all local variables that were defined in this scope
    remove_local_vars(var_scope);
    var_scope--;
  }

  //<vars> -> empty | let Identifier = Integer <vars>
  if (root->label == "<vars>")
  {
    //Check if the token is an empty production or not
    temp.token = root->tokens.front();
    temp.scope = var_scope;
    root->tokens.erase(root->tokens.begin());

    if (temp.token.desc != "EMPTY")
    {
      //Check to determine if an identifier with the same name has
      //been declared already in this scope or not.
      check_var(temp);

      stack_vars++; //Increment the number of variables on the stack

      semantics(root->child_1); //process <vars>
    }
  }

  //<expr> -> <A> / <expr> | <A> * <expr> | <A>
  if (root->label == "<expr>")
  {
    //If the tokens vectors is empty, then the production
    //was <expr> - > <A>
    if (root->tokens.empty())
      semantics(root->child_1); //process <A>
    else
    {
      //Get the number for the temporary variable T, and
      //increment the number of temporary variables.
      int local_var = temp_vars;
      temp_vars++;

      //Process <expr>
      semantics(root->child_2);

      //Give the temporary variable the value in the accumulator
      target_file << "STORE T" << local_var << "\n";

      //Process <A>
      semantics(root->child_1);

      //Check if the operator is a '/' or '*'
      if (operator_map[root->tokens.at(0).desc] == "SLASHtk")
      {
        //Divide the temporary variable by the value in the
        //accumulator; the result will be stored in the
        //accumulator
        target_file << "DIV T" << local_var << "\n";
      }
      else if (operator_map[root->tokens.at(0).desc] == "ASTERIKtk")
      {
        //Multiply the temporary variable by the value in
        //the accumulator; the result will be stored in
        //the accumulator
        target_file << "MULT T" << local_var << "\n";
      }

      return;
    }
  }

  //<A> -> <M> + <A> | <M> - <A> | <M>
  if (root->label == "<A>")
  {
    //If the tokens vector of the root is empty, then the
    //production was <A> -> <M>
    if (root->tokens.empty())
      semantics(root->child_1); //process <M>
    else
    {
      //Get the number for the temporary variable T, and
      //increment the number of temporary variables
      int local_var = temp_vars;
      temp_vars++;

      //Process <A>
      semantics(root->child_2);

      //Assign the temporary variable the value in the
      //accumulator
      target_file << "STORE T" << local_var << "\n";

      //Process <M>
      semantics(root->child_1);

      //Check if the operator is '+' or '-'
      if (operator_map[root->tokens.at(0).desc] == "PLUStk")
      {
        //Add the temporary variable to the value in the accumulator;
        //the result will be stored in the accumulator
        target_file << "ADD T" << local_var << "\n";
      }
      else if (operator_map[root->tokens.at(0).desc] == "MINUStk")
      {
        //Subtract the temporary variable from the value in
        //the accumulator; the result will be stored in the
        //accumulator
        target_file << "SUB T" << local_var << "\n";
      }

      return;
    }
  }

  //<M> -> -<M> | <R>
  if (root->label == "<M>")
  {
    semantics(root->child_1); //process <M> or <R>

    if (!root->tokens.empty())
    {
      //If the tokens vector of the root node is not
      //empty, then the production is <M> -> -<M>. Multiply the
      //value in the accumulator by -1 for negation; the
      //result is stored in the accumulator
      target_file << "MULT -1\n";
    }

    return;
  }

  //<R> -> (<expr>) | Identifier | Integer
  if (root->label == "<R>")
  {
    //If the tokens vector of the root is empty, then the
    //production was <R> -> (<expr>)
    if (root->tokens.empty())
      semantics(root->child_1); //process <expr>
    else
    {
      //Otherwise, if an identifier token was used, verify that it
      //was already defined and can be accessed in this scope
      temp.token = root->tokens.front();
      temp.scope = var_scope;

      if (temp.token.ID == IDtk)
      {
        //Check to determine if this variable may be accessed
        //in this scope. (Unless it was declared in a later, inner
        //scope and is not on the stack, then it should be able
        //to be accessed).
        compare_scope(temp);

        //Set the value in the accumulator to the TOS - the location
        //of the identifier on the stack
        target_file << "STACKR " << find_on_stack(temp) << "\n";
      }
      else if (temp.token.ID == INTtk)
      {
        //Load the value of the integer into the accumulator
        target_file << "LOAD " << get_token_int(temp.token) << "\n";
      }

      return;
    }
  }

  //<stats> -> <stat> <mStat>
  if (root->label == "<stats>")
  {
    semantics(root->child_1); //process <stat>
    semantics(root->child_2); //process <mStat>
  }

  //<stat> -> <in> | <out> | <block> | <if> | <loop> | <assign>
  if (root->label == "<stat>")
    semantics(root->child_1); //process the RHS production

  //<mStat> -> empty | <stat> <mStat>
  if (root->label == "<mStat>")
  {
    //The <mStat> production will normally not have any tokens,
    //but tokens.empty() will return FALSE when the empty production
    //is present.
    if (root->tokens.empty())
    {
      semantics(root->child_1); //process <stat>
      semantics(root->child_2); //process <mStat>
    }
  }

  //<in> -> read ( Identifier ):
  if (root->label == "<in>")
  {
    //Verify that the identifier exists, and was declared in or
    //before this scope
    temp.token = root->tokens.front();
    temp.scope = var_scope;

    int local_var = temp_vars;
    temp_vars++;

    //Check to determine if the variable was defined and may be accessed in
    //this scope.
    compare_scope(temp);

    //Read a variable from input. 
    //Read the value from the input into the temporary variable, T#.
    //Then load this value into the accumulator.
    //Then, set the value on the stack at location (TOS - the location
    //of the identifier on the stack) to the value in
    //the accumulator.
    target_file << "READ T" << local_var << "\n";
    target_file << "LOAD T" << local_var << "\n";
    target_file << "STACKW " << find_on_stack(temp) << "\n";

    return;
  }

  //<out> -> print ( <expr> ) :
  if (root->label == "<out>")
  {
    int local_var = temp_vars;
    temp_vars++;

    semantics(root->child_1); //process <expr>

    //Assign the temporary variable the value in
    //the accumulator. Then write the temporary variable
    //value to output.
    target_file << "STORE T" << local_var << "\n";
    target_file << "WRITE T" << local_var << "\n";

    return;
  }

  //<if> -> cond (<expr> <RO> <expr>) <stat>
  if (root->label == "<if>")
  {
    int loop = ++num_repeat;
    int local_var = temp_vars;
    temp_vars++;

    //Declare a variable, called loop#, that will
    //execute the instruction following it
    target_file << "loop" << loop << ": ";

    semantics(root->child_1); //process <expr>

    //Assign the value of the temporary variable to the
    //value in the accumulator
    target_file << "STORE T" << local_var << "\n";

    semantics(root->child_3); //process <expr>

    //Subtract the value accumulator value from the temporary variable;
    //the result is stored in the accumulator.
    target_file << "SUB T" << local_var << "\n";

    semantics(root->child_2); //process <RO>
    semantics(root->child_4); //process <stat>

    //Declare a variable, called branch#, that will execute
    //'NOOP' (nothing, no instruction) when it is branched to
    target_file << "branch" << loop << ": NOOP\n";

    return;
  }

  //<loop> -> iter (<expr> <RO> <expr>) <stat>
  if (root->label == "<loop>")
  {
    int loop = ++num_repeat;
    int local_var = temp_vars;
    temp_vars++;

    //Declare a variable, called loop#, that will execute
    //the instruction following it
    target_file << "loop" << local_var << ": ";

    semantics(root->child_1); //process <expr>

    //Assign the temporary variable the value of the accumulator
    target_file << "STORE T" << local_var << "\n";

    semantics(root->child_3); //process <expr>

    //Subtract the accumulator value from the temporary variable;
    //the result is stored in the accumulator
    target_file << "SUB T" << local_var << "\n";

    semantics(root->child_2); //process <RO>
    semantics(root->child_4); //process <stat>

    //Jump to the loop# variable instruction
    target_file << "BR loop" << loop << "\n";

    //Define the instruction of the variable branch#, which
    //will execute 'NOOP' (nothing, no operation)
    target_file << "branch" << loop << ": NOOP\n";
  }

  //<assign> -> Identifier = <expr> :
  if (root->label == "<assign>")
  {
    //Check if the identifier has been defined
    temp.token = root->tokens.front();
    temp.scope = var_scope;

    //Check to determine if the variable may be accessed in the scope
    //and was already defined.
    compare_scope(temp);

    semantics(root->child_1); //process <expr>

    temp.token = root->tokens.front();
    temp.scope = var_scope;

    //Set the value in the stack at location (TOS - where the
    //identifier is on the stack) to the value in the accumulator
    target_file << "STACKW " << find_on_stack(temp) << "\n";

    return;
  }

  if (root->label == "<RO>")
  {
    temp.token = root->tokens.front();
    temp.scope = var_scope;
    root->tokens.erase(root->tokens.begin());

    //Possible RO operators begin with '<', '>', or '='
    if (operator_map[temp.token.desc] == "LESSTHANtk")
    {
      //If the root node's tokens are not empty, then
      //there is an additional operator
      if (!root->tokens.empty())
      {
        stack_t temp_2;
        temp_2.token = root->tokens.front();
        temp_2.scope = var_scope;
        root->tokens.erase(root->tokens.begin());

        if (operator_map[temp_2.token.desc] == "EQUALStk")
        {
          //Less than or equal to 0, so use BRNEG instruction
          target_file << "BRNEG branch" << num_repeat << "\n";
          return;
        }
      }

      //Less than 0, so use BRZNEG instruction
      target_file << "BRZNEG branch" << num_repeat << "\n";
      return;
    }
    else if (operator_map[temp.token.desc] == "GREATERTHANtk")
    {
      if (!root->tokens.empty())
      {
        stack_t temp_2;
        temp_2.token = root->tokens.front();
        temp_2.scope = var_scope;
        root->tokens.erase(root->tokens.begin());

        if (operator_map[temp_2.token.desc] == "EQUALStk")
        {
          //Greater than or equal to 0, so use BRPOS instruction
          target_file << "BRPOS branch" << num_repeat << "\n";
          return;
        }
      }

      //Greater than 0, so use BRZPOS instruction
      target_file << "BRZPOS branch" << num_repeat << "\n";
      return;
    }
    else if (operator_map[temp.token.desc] == "EQUALStk")
    {
      if (!root->tokens.empty())
      {
        stack_t temp_2;
        temp_2.token = root->tokens.front();
        temp_2.scope = var_scope;
        root->tokens.erase(root->tokens.begin());

        if (operator_map[temp_2.token.desc] == "EQUALStk")
        {
          //==, which is defined as NOT EQUAL according to our semantics
          target_file << "BRZERO branch" << num_repeat << "\n";
          return;
        }
      }

      //Test if is equal, according to our semantics
      target_file << "BRPOS branch" << num_repeat << "\n";
      target_file << "BRNEG branch" << num_repeat << "\n";
      return;
    }
  }
}

//When a variable is defined, check if this variable has been defined
//yet in this scope; if so, print an error. Otherwise, push the
//variable onto the stack
void check_var(stack_t var)
{
  //Before pushing this variable onto the stack, check to determine
  //if it has been defined already in this scope.
  int var_defined = find_var(var);

  //If the variable is already on the stack, then the var_defined
  //location will be where the variable is on the stack; this means
  //that this variable was already found on the stack in the same scope.
  //Print an error to the console and terminate.
  if (var_defined > 0)
  {
    cout << "Semantics Error: The variable \'" << 
      get_token_desc(var.token) << "\' on line " << var.token.line_number 
      << " has already been defined in this scope on line " 
      << stack.at(var_defined).token.line_number << ".\n";
    exit(EXIT_FAILURE);
  }
  else
  {
    //Otherwise, push the variable onto the stack and increment the
    //stack location.
    stack.push_back(var);
    stack_location++;

    //Reserve storage for the identifier that was defined.
    target_file << "PUSH\n";
  }
}

//Search the stack to determine if this variable has already been
//defined before it is used.
int check_var_exists(stack_t var)
{
  for (int counter = 0; counter < stack.size(); counter++)
  {
    //If the identifier in the stack at this counter matches
    //the identifier attempting to be used, return the location
    //of this identifier in the stack.
    if (stack.at(counter).token.desc == var.token.desc)
      return counter;
  }

  return -1;
}

//Find the given variable (with the specified scope) in the stack
//to see if the variable has already been defined in this scope.
//**Only invoked when defining new variables**
int find_var(stack_t var)
{
  for (int counter = 0; counter < stack.size(); counter++)
  {
    //If the given identifier matches the identifier at this location
    //in the stack, and the two identifiers have the same scope,
    //return the location of this variable in the stack
    if ((stack.at(counter).token.desc == var.token.desc) &&
      (stack.at(counter).scope == var.scope))
    {
      return counter;
    }
  }

  return -1;
}

//Find the location of an identifier (that has already been declared)
//on the stack
int find_on_stack(stack_t var)
{
  for (int counter = 0; counter < stack.size(); counter++)
  {
    //If the token at this location in the stack matches the token
    //of the identifier we are searching for, and the scope of the
    //identifier at this location is less than or equal to the
    //scope of the identifier we are looking for, return the location
    //as counter 
    if ((stack.at(counter).token.desc == var.token.desc) &&
      (stack.at(counter).scope <= var.scope))
      return counter;
  }

  cout << "The identifier " << var.token.desc << " was not declared in "
    << "this scope.\n";
  exit(EXIT_FAILURE);
}

//Compare the scope that this variable is being used in to the scope
//where the variable was defined, to ensure that the variable may
//be accessed in this scope
int compare_scope(stack_t var)
{
  //int var_location = find_var(var);
  //Check if the variable has been defined and is on the stack.
  int var_location = check_var_exists(var);

  if (var_location >= 0)
  {
    //If the scope of this variable in the stack is greater than
    //the scope given, then this variable was not declared in this
    //scope (it was declared later) and cannot be accessed here.
    //Print an error to the console and terminate.
    if (stack.at(var_location).scope > var.scope)
    {
      cout << "Semantics Error: The variable \'" << get_token_desc(var.token) 
        << "\' on line " << var.token.line_number << 
        " cannot be accessed in this scope.\n";
      exit(EXIT_FAILURE);
    }
    else
      return 1; //Return 1 for true
  }
  else
  {
    //If the variable was not located in the stack, print an
    //error to the console and terminate.
    cout << "Semantics Error: The variable \'" << get_token_desc(var.token)
      << "\' on line " << var.token.line_number << 
      " is not on the stack, and has either not yet been defined OR "
      << "cannot be accessed in this scope.\n";
    exit(EXIT_FAILURE);
  }
}

//Remove all variables within a given scope when leaving that scope;
//so, when a <block> ends, delete all variables that were defined
//in that block.
void remove_local_vars(int scope)
{
  if (scope > 0)
  {
    //Iterate through the stack.
    for (int counter = 0; counter < stack.size(); counter++)
    {
      //If the variable at this counter in the stack has the scope
      //that is being left, then remove this variable from the stack.
      if (stack.at(counter).scope == scope)
      {
        //Remove storage for the identifier that is being deleted
        //from the stack
        target_file << "POP\n";
        stack.erase(stack.begin() + counter);
      }
    }
  }
  else if (scope == 0)
  {
    int counter = stack.size();
    counter--;
    for (counter; counter >= 0; counter--)
    {
      if (stack.at(counter).scope == scope)
      {
        target_file << "POP\n";
        stack.erase(stack.begin() + counter);
      }
    }
  }
}

//Utility function to print all variables on the stack and their
//scope.
void print_stack()
{
  for (int i = 0; i < stack.size(); i++)
  {
    cout << "\tIndex " << i << " = " << stack.at(i).token.desc << 
      ", scope is " << stack.at(i).scope << "\n";
  }
}
