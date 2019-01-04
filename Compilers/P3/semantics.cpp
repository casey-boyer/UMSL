#include "semantics.h"
#include "token.h"
#include "node.h"
#include <iostream>
#include <stdlib.h>
#include <vector>

using namespace std;

//The maximum number of allowed variable definitions.
const int MAX_VARS = 100;

//The scope of a variable or variables
static int var_scope = 0;

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
    //cout << "root is <program>, scope is " << var_scope << "\n";
    semantics(root->child_1); //process <vars>
    semantics(root->child_2); //process <block>

    //Once this point is reached, the entire parse tree was verified
    //for semantic errors. Print a message indicating that the semantics
    //are OK, and return.
    cout << "Semantics OK\n";
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
      semantics(root->child_1); //process <A>
      semantics(root->child_2); //process <expr>
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
      semantics(root->child_1); //process <M>
      semantics(root->child_2); //process <A>
    }
  }

  //<M> -> -<M> | <R>
  if (root->label == "<M>")
    semantics(root->child_1); //process <M> or <R>

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

    //Check to determine if the variable was defined and may be accessed in
    //this scope.
    compare_scope(temp);
    return;
  }

  //<out> -> print ( <expr> ) :
  if (root->label == "<out>")
    semantics(root->child_1); //process <expr>

  //<if> -> cond (<expr> <RO> <expr>) <stat>
  if (root->label == "<if>")
  {
    semantics(root->child_1); //process <expr>
    semantics(root->child_2); //process <RO>
    semantics(root->child_3); //process <expr>
    semantics(root->child_4); //process <stat>
  }

  //<loop> -> iter (<expr> <RO> <expr>) <stat>
  if (root->label == "<loop>")
  {
    semantics(root->child_1); //process <expr>
    semantics(root->child_2); //process <RO>
    semantics(root->child_3); //process <expr>
    semantics(root->child_4); //process <stat>
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

    return;
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
    print_stack();
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
        stack.erase(stack.begin() + counter);
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
