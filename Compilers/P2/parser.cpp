#include "parser.h"
#include "token.h"
#include "scanner.h"
#include "node.h"
#include <iostream>
#include <stdlib.h>

using namespace std;

Token tk;
static string expected_token;
static Token EMPTY_TOKEN;

node_t *parser()
{
  //The description of the empty token
  EMPTY_TOKEN.desc = "EMPTY";

  //Get the first token of the program from the
  //scanner
  scanner(tk);

  //Create the root node
  node_t *root = NULL;

  //Assign the root node the return value of
  //<program>
  root = program();

  //Check that the last token of the program
  //returned by the scanner is the EOF token. If not,
  //print an error and terminate the program.
  if (tk.ID != EOFtk)
  {
    expected_token.assign("EOF");
    parser_error();
  }
  else
  {
    //Otherwise, indicate that parsing was successful.
    //Return the root node and its subtree to main
    cout << "Parse OK\n";
    return root;
  }
}

//For the <program> -> void <vars> <block> production
node_t *program()
{
  //Create the node for the <program> production
  node_t *node = create_node("<program>");

  //Check that the token returned by the scanner is the
  //'void' keyword
  if ((tk.ID == KEYWORDtk) && (keyword_map[tk.desc] == "VOIDtk"))
  {
    //Get the token from the scanner
    scanner(tk);

    //The child_1 of <program> is <vars>, and the child_2 of <program>
    //is <block>.
    node->child_1 = vars();
    node->child_2 = block();
    return node;
  }
  else
  {
    expected_token.assign("void");
    parser_error();
  }
}

//For the <vars> -> empty | let Identifier = Integer <vars> production
node_t *vars()
{
  //Create the node associated with the <vars> production
  node_t *node = create_node("<vars>");

  //Check that the token returned by the scanner is the
  //'let' keyword.
  if ((tk.ID == KEYWORDtk) && (keyword_map[tk.desc] == "LETtk"))
  {
    //Get the next token from the scanner
    scanner(tk);

    //Check that the token returned by the scanner is
    //an identifier
    if (tk.ID == IDtk)
    {
      //Store the identifier in this node's vector of tokens
      node->tokens.push_back(tk);

      //Get the next token from the scanner
      scanner(tk);

      //Check that the token returned by the scanner is the
      //'=' operator
      if ((tk.ID == OPtk) && (operator_map[tk.desc] == "EQUALStk"))
      {
        //Get the next token from the scanner
        scanner(tk);

        //Check that the token returned by the scanner is an
        //integer
        if (tk.ID == INTtk)
        {
          //Store the integer in this node's vector of tokens
          node->tokens.push_back(tk);

          //Get the next token from the scanner
          scanner(tk);

          //The child_1 of this node will be <vars>
          node->child_1 = vars();

          return node;
        }
        else
        {
          expected_token.assign("Integer");
          parser_error();
        }
      }
      else
      {
        expected_token.assign("=");
        parser_error();
      }
    }
    else
    {
      expected_token.assign("Identifier");
      parser_error();
    }
  }
  else
  {
    //An empty production.
    node->tokens.push_back(EMPTY_TOKEN);
    return node;
  }
}

//For the <block> -> begin <vars> <stats> end production
node_t *block()
{
  //Create the node for the <block> production
  node_t *node = create_node("<block>");

  //Check that the token returned by the scanner is the 'begin'
  //keyword
  if ((tk.ID == KEYWORDtk) && (keyword_map[tk.desc] == "BEGINtk"))
  {
    //Get the next token from the scanner
    scanner(tk);

    //Invoke <vars>, and set the child_1 of the <block> node 
    //to the node returned by vars
    node->child_1 = vars();

    //Invoke <stats>, and set the child_2 of the <block> node 
    //to the node returned by the <stats> production
    node->child_2 = stats();

    //Check that the token returned by the scanner is the 'end' keyword
    if ((tk.ID == KEYWORDtk) && (keyword_map[tk.desc] == "ENDtk"))
    {
      //Get the next token from the scanner
      scanner(tk);
      return node;
    }
    else
    {
      expected_token.assign("end");
      parser_error();
    }
  }
  else
  {
    expected_token.assign("begin");
    parser_error();
  }
}

//For the <expr> -> <A> / <expr> | <A> * <expr> | <A> production
node_t *expr()
{
  //Create the node for the <expr> production
  node_t *node = create_node("<expr>");

  //Invoke A(), and set the child_1 of the <expr> node to the
  //node returned by <A>
  node->child_1 = A();

  //Check that the token returned by the scanner is an operator
  if (tk.ID == OPtk)
  {
    //Check if the operator is a '/' or '*'
    if (operator_map[tk.desc] == "SLASHtk")
    {
      //Predicts <expr> -> <A> / <expr>

      //Store the '/' operator in the <expr> node
      node->tokens.push_back(tk);

      //Get the next token from the scanner
      scanner(tk);

      //Invoke expr(), and set the child_2 of the <expr> node
      //to the node returned by <expr>
      node->child_2 = expr();
    }
    else if (operator_map[tk.desc] == "ASTERIKtk")
    {
      //Predicts <expr> -> <A> * <expr>
      //Store the '*' operator in the <expr> node
      node->tokens.push_back(tk);

      //Get the next token from the scanner
      scanner(tk);

      //Invoke expr(), and set the child_2 of the <expr> node
      //to the node returned by <expr>
      node->child_2 = expr();
    }
  }

  return node; 
}

//For the A -> <M> + <A> | <M> - <A> | <M> production
node_t *A()
{
  //Create the node for the <A> production
  node_t *node = create_node("<A>");

  //Invoke M(), and set the child_1 node of the <A> production
  //to the node returned by <M>
  node->child_1 = M();

  //Check that the token returned by the scanner is an operator
  if (tk.ID == OPtk)
  {
    if (operator_map[tk.desc] == "PLUStk")
    {
      //Predicts the A -> <M> + <A> production
      //Store the '+' operator in the <A> node
      node->tokens.push_back(tk);

      //Get the next token from the scanner
      scanner(tk);

      //Set the child_2 node of <A> to the node returned by A()
      node->child_2 = A();
    }
    else if (operator_map[tk.desc] == "MINUStk")
    {
      //Predicts the A -> <M> - <A> production
      //Store the '-' operator in the <A> node
      node->tokens.push_back(tk);

      //Get the next token in the scanner
      scanner(tk);

      //Set the child_2 node of <A> to the node returned by A()
      node->child_2 = A();
    }
  }
  
  return node;
}

//For the <M> -> -<M> | <R> production
node_t *M()
{
  //Create the node for the <M> production
  node_t *node = create_node("<M>");

  //Check for unary '-'; then invoke M() if present.
  //Otherwise, invoke R().
  if ((tk.ID == OPtk) && (operator_map[tk.desc] == "MINUStk"))
  {
    //Store the unary '-' in the <M> production
    node->tokens.push_back(tk);

    //Get the next token from the scanner
    scanner(tk);

    //Set the child_1 node of <M> to the node returned by <M>
    node->child_1 = M();
    return node;
  }
 
  //Set the child_1 node of <M> to the node returned by <R>
  node->child_1 = R();
  return node;
}

//For the <R> -> (<expr>) | Identifier | Integer production
node_t *R()
{
  //Create the node for the <R> production
  node_t *node = create_node("<R>");

  //Check if '(' present.
  //	Invoke <expr>. Check again for ')' then return.
  //Check if it is an identifier.
  //Check if it is an integer.
  if ((tk.ID == OPtk) && (operator_map[tk.desc] == "LEFTPARENtk"))
  {
    //Get the next token from the scanner
    scanner(tk);

    //Set the child_1 node of <R> to the node returned by <expr>
    node->child_1 = expr();

    //Check that the token returned by the scanner is a ')' operator
    if ((tk.ID == OPtk) && (operator_map[tk.desc] == "RIGHTPARENtk"))
    {
      //Get the next token from the scanner
      scanner(tk);
      return node;
    }
    else
    {
      expected_token.assign(")");
      parser_error();
    }
  }
  else if (tk.ID == IDtk)
  {
    //If the token returned by the scanner is an identifier, store
    //the identifier in the node
    node->tokens.push_back(tk);

     //Get the next token from the scanner
    scanner(tk);
    return node;
  }
  else if (tk.ID == INTtk)
  {
    //If the token returned by the scanner is an integer, store
    //the integer in the node
    node->tokens.push_back(tk);

    //Get the next token from the scanner
    scanner(tk);
    return node;
  }
  else
  {
    expected_token.assign("( or Identifier or Integer");
    parser_error();
  }
}

//For the <stats> -> <stat> <mStat> production
node_t *stats()
{
  //Create the node for the <stats> production
  node_t *node = create_node("<stats>");

  //Invoke <stat> <mStat>, and set the child1_1 and child_2 nodes
  //to the node(s) returned by these productions
  node->child_1 = stat();
  node->child_2 = mStat(); 

  return node;
}

//For the <mStat> -> empty | <stat> <mStat> production
node_t *mStat()
{
  //Create the node for the <mStat> production
  node_t *node = create_node("<mStat>");

  //Invoke stat() mStat() or empty
  //Also check if the token id is an identifier for <assign>
  if ( ((tk.ID == KEYWORDtk)&& 
    ( (keyword_map[tk.desc] == "PRINTtk") || (keyword_map[tk.desc] == "READtk")
      || (keyword_map[tk.desc] == "BEGINtk") 
      || (keyword_map[tk.desc] == "CONDtk") 
      || (keyword_map[tk.desc] == "ITERtk"))) || (tk.ID == IDtk))
  {
    //If the scanner returns a keyword (print, read, begin, or iter) OR
    //an identifier, invoke <stat> <mStat> and set the respective
    //child nodes of this production
    node->child_1 = stat();
    node->child_2 = mStat();
    return node;
  }
  else
  {
    //Empty production.
    node->tokens.push_back(EMPTY_TOKEN);
    return node;
  }
}

//For the <stat> -> <in> | <out> | <block> 
//| <if> | <loop> | <assign> production
node_t *stat()
{
  //Create the node for the <stat> production
  node_t *node = create_node("<stat>");

  //Predicts read, print, cond, iter, or assign
  if (tk.ID == KEYWORDtk)
  {
    if (keyword_map[tk.desc] == "PRINTtk")
    {
      scanner(tk);

      //Set the child_1 of <stat> to the <out> production
      node->child_1 = out();
      return node;
    }
    else if (keyword_map[tk.desc] == "READtk")
    {
      scanner(tk);

      //Set the child_1 of <stat> to the <in> production
      node->child_1 = in();
      return node;
    }
    else if (keyword_map[tk.desc] == "BEGINtk")
    {
      //scanner(tk);
      //Set the child_1 of <stat> to the <block> production
      node->child_1 = block();
      return node;
    }
    else if (keyword_map[tk.desc] == "CONDtk")
    {
      scanner(tk);

      //Set the child_1 of <stat> to the <if> production
      node->child_1 = cond();
      return node;
    }
    else if (keyword_map[tk.desc] == "ITERtk")
    {
      scanner(tk);

      //Set the child_1 of <stat> to the <loop> production
      node->child_1 = loop();
      return node;
    }
  }
  else if (tk.ID == IDtk)
  {
    //Save the identifier token in a temporary token variable,
    //so that it may be added to the child_1 node of <stat>, which
    //is <assign>, when <assign> returns. This needs to be done because
    //the scanner retrieves the next token before assign is called, which
    //would lose the identifier token if it is not temporarily stored.
    Token temp_token = tk;

    scanner(tk);

    //Set the child_1 of <stat> to the <assign> production
    node->child_1 = assign();

    //Push the identifier token into the tokens of the <assign> production.
    node->child_1->tokens.push_back(temp_token);
    return node;
  }
  else
  {
    expected_token.assign("read or print or cond or iter or Identifier");
    parser_error();
  }
}

//For the <in> -> read (Identifier): production
node_t *in()
{
  //Create the node for the <in> production
  node_t *node = create_node("<in>");

  //Check if the token returned by the scanner is a '(' operator
  if ((tk.ID == OPtk) && (operator_map[tk.desc] == "LEFTPARENtk"))
  {

    //Get the next token from the scanner
    scanner(tk);

    if (tk.ID == IDtk)
    {
      //If the token returned by the scanner is an identifier, store
      //the identifier in the <in> node vector of tokens.
      node->tokens.push_back(tk);

      //Get the next token from the scanner
      scanner(tk);

      //Check that the token returned by the scanner is a ')' operator
      if ((tk.ID == OPtk) && (operator_map[tk.desc] == "RIGHTPARENtk"))
      {
        //Get the next token from the scanner
        scanner(tk);

        //Check that the token returned by the scanner is a ':'
        //operator
        if ((tk.ID == OPtk) && (operator_map[tk.desc] == "COLONtk"))
        {
          //Get the next token from the scanner
          scanner(tk);
          return node;
        }
        else
        {
          expected_token.assign(":");
          parser_error();
        }
      }
      else
      {
        expected_token.assign("(");
        parser_error();
      }
    }
    else
    {
      expected_token.assign("Identifier");
      parser_error();
    }
  }
  else
  {
    expected_token.assign("(");
    parser_error();
  }
}

//For the <out> -> print(<expr>): production
node_t *out()
{
  //Create the node for the <print> production
  node_t *node = create_node("<print>");

   //Check that the token returned by the scanner is a '(' operator
  if ((tk.ID == OPtk) && (operator_map[tk.desc] == "LEFTPARENtk"))
  {
    //Get the next token from the scanner
    scanner(tk);

    //Invoke expr(), and set the node returned by expr() to the
    //child_1 node of <print>
    node->child_1 = expr();

    //Check that the token returned by the scanner is a '(' operator
    if ((tk.ID == OPtk) && (operator_map[tk.desc] == "RIGHTPARENtk"))
    {
      //Get the next token from the scanner
      scanner(tk);

      //Check that the token returned by the scanner is a ':' operator
      if ((tk.ID == OPtk) && (operator_map[tk.desc] == "COLONtk"))
      {
        //Get the next token from the scanner
        scanner(tk);
        return node;
      }
      else
      {
        expected_token.assign(":");
        parser_error();
      }
    }
    else
    {
      expected_token.assign(")");
      parser_error();
    }
  }
  else
  {
    expected_token.assign("(");
    parser_error();
  }
}

//For the <if> -> cond(<expr> <RO> <expr>) <stat> production
node_t *cond()
{
  //Create the node for the <if> production
  node_t *node = create_node("<if>");

  //Check that the token returned by the scanner is a '(' operator
  if ((tk.ID == OPtk) && (operator_map[tk.desc] == "LEFTPARENtk"))
  {
    //Get the next token from the scanner
    scanner(tk);

    //Invoke <expr> <RO> <expr>. Set the nodes returned by these
    //productions to the child_1, child_2, and child_3 node(s) of
    //the <if> node, respectively.
    node->child_1 = expr();
    node->child_2 = RO();
    node->child_3 = expr();

    //Check that the token returned by the scanner is a ')' operator
    if ((tk.ID == OPtk) && (operator_map[tk.desc] == "RIGHTPARENtk"))
    {
      //Get the next token from the scanner
      scanner(tk);

      //Invoke <stat>. Set the child_4 node of the <if> production to
      //the node returned by <stat>.
      node->child_4 = stat();

      return node;
    }
    else
    {
      expected_token.assign(")");
      parser_error();
    }
  }
  else
  {
    expected_token.assign("(");
    parser_error();
  }
}

//For the <loop> -> iter (<expr> <RO> <expr>) <stat>
node_t *loop()
{
  //Create the node for the <loop> production
  node_t *node = create_node("<loop>");

  //Check that the token returned by the scanner is the '(' operator
  if ((tk.ID == OPtk) && (operator_map[tk.desc] == "LEFTPARENtk"))
  {
    //Get the next token from the scanner
    scanner(tk);

    //Invoke <expr> <RO> <expr>. Set the child_1, child_2, and child_3 nodes
    //of the <loop> node to the node(s) returned by <expr> <RO> <expr>,
    //respectively.
    node->child_1 = expr();
    node->child_2 = RO();
    node->child_3 = expr();

    //Check that the node returned by the scanner is a ')' operator
    if ((tk.ID == OPtk) && (operator_map[tk.desc] == "RIGHTPARENtk"))
    {
      //Get the next token from the scanner
      scanner(tk);

      //Invoke <stat>. Set the child_4 node of <loop> to the node
      //returned by stat().
      node->child_4 = stat();
      
      return node;
    }
    else
    {
      expected_token.assign(")");
      parser_error();
    }
  }
  else
  {
    expected_token.assign("(");
    parser_error();
  }
}

//For the Identifier = <expr>: production
node_t *assign()
{
  //Create the node for the <assign> production.
  node_t *node = create_node("<assign>");

  //Check that the token returned by the scanner is a '=' operator
  if ((tk.ID == OPtk) && (operator_map[tk.desc] == "EQUALStk"))
  {
    //Get the next token from the scanner
    scanner(tk);

    //Invoke <expr>. Set the child_1 node of <assign> to the node
    //returned by expr().
    node->child_1 = expr();

    //Check that the node returned by the scanner is a ':' operator
    if ((tk.ID == OPtk) && (operator_map[tk.desc] == "COLONtk"))
    {
      //Get the next token from the scanner
      scanner(tk);
      return node;
    }
    else
    {
      expected_token.assign(":");
      parser_error();
    }
  }
  else
  {
    expected_token.assign("=");
    parser_error();
  }
}

//For the <RO> -> < | < = | > | > = | = = | = production
node_t *RO()
{
  //Create the node for the <RO> production
  node_t *node = create_node("<RO>");

  //Check that the token returned by the scanner is an operator
  if (tk.ID == OPtk)
  {
    if (operator_map[tk.desc] == "LESSTHANtk")
    {
      //Insert the '<' token into the <RO> node's tokens
      node->tokens.push_back(tk);

      //Get the next token from the scanner
      scanner(tk);

      //If the token returned by the scanner is an operator, check that
      //it is a '=' operator.
      if ((tk.ID == OPtk) && (operator_map[tk.desc] == "EQUALStk"))
      {
        //Insert the '=' token into the <RO> node's tokens
        node->tokens.push_back(tk);

        //Get the next token from the scanner
        scanner(tk);
        return node;
      }
      else if ((tk.ID == OPtk) && (operator_map[tk.desc] != "EQUALStk"))
      {
        //Avoid other operator tokens such as '+' or '/' after <
        expected_token.assign("< =");
        parser_error();
      }
      else
        return node;
    }
    else if (operator_map[tk.desc] == "GREATERTHANtk")
    {
      //Insert the '>' token into the <RO> node's tokens
      node->tokens.push_back(tk);

      //Get the next token from the scanner
      scanner(tk);

      //If the token returned by the scanner is an operator, check
      //that it is the '=' operator
      if ((tk.ID == OPtk) && (operator_map[tk.desc] == "EQUALStk"))
      {
        //Insert the '=' token into the <RO> node's tokens
        node->tokens.push_back(tk);

        //Get the next token from the scanner
        scanner(tk);
        return node;
      }
      else if ((tk.ID == OPtk) && (operator_map[tk.desc] != "EQUALStk"))
      {
        expected_token.assign("> = ");
        parser_error();
      }
      else
        return node;
    }
    else if (operator_map[tk.desc] == "EQUALStk")
    {
      //Insert the '=' token into the <RO> node's tokens
      node->tokens.push_back(tk);

      //Get the next token from the scanner
      scanner(tk);

      //If the next token returned from the scanner is an operator,
      //check that it is an '=' operator
      if ((tk.ID == OPtk) && (operator_map[tk.desc] == "EQUALStk"))
      {
        //Insert the '=' token into the <RO> node's tokens
        node->tokens.push_back(tk);

        //Get the next token from the scanner
        scanner(tk);
        return node;
      }
      else if ((tk.ID == OPtk) && (operator_map[tk.desc] != "EQUALStk"))
      {
        expected_token.assign("= =");
        parser_error();
      }
      else
        return node;
    }
    else
    {
      expected_token.assign("< or < = or > or > = or = or = = ");
      parser_error();
    }
  }
  else
  {
    expected_token.assign("< or < = or > or > = or = or = =");
    parser_error();
  }
}

//Print the line number that the error occured on, the expected token,
//and the token recieved.
void parser_error()
{
  cout << "Parser error on line number " << tk.line_number << 
    ": expected '" << expected_token << "' but received '" << tk.desc << "'\n";
  exit(EXIT_FAILURE);
}

//Create a node_t node with the given 'label' provided by the string
//production_name argument, and intitialize all of its children to NULL.
node_t *create_node(string production_name)
{
  node_t *node = new node_t();
  node->child_1 = NULL;
  node->child_2 = NULL;
  node->child_3 = NULL;
  node->child_4 = NULL;

  node->label = production_name;

  return node;
}
