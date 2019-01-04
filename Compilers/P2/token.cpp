#include "token.h"
#include <string>
#include <iostream>
#include <map>

using namespace std;

//Extern map variable declared in token.h
map<string, string> operator_map;

//Extern variable declared in token.h
map<string, string> keyword_map;

void populate_operator_map()
{
  operator_map.insert(make_pair("=", "EQUALStk"));
  operator_map.insert(make_pair("<", "LESSTHANtk"));
  operator_map.insert(make_pair(":", "COLONtk"));
  operator_map.insert(make_pair(">", "GREATERTHANtk"));
  operator_map.insert(make_pair("+", "PLUStk"));
  operator_map.insert(make_pair("-", "MINUStk"));
  operator_map.insert(make_pair("*", "ASTERIKtk"));
  operator_map.insert(make_pair("/", "SLASHtk"));
  operator_map.insert(make_pair("%", "MODULUStk"));
  operator_map.insert(make_pair(".", "PERIODtk"));
  operator_map.insert(make_pair("(", "LEFTPARENtk"));
  operator_map.insert(make_pair(")", "RIGHTPARENtk"));
  operator_map.insert(make_pair(",", "COMMAtk"));
  operator_map.insert(make_pair("{", "LEFTBRACEtk"));
  operator_map.insert(make_pair("}", "RIGHTBRACEtk"));
  operator_map.insert(make_pair(";", "SEMICOLONtk"));
  operator_map.insert(make_pair("[", "LEFTBRACKETtk"));
  operator_map.insert(make_pair("]", "RIGHTBRACKETtk"));
}

void populate_keyword_map()
{
  keyword_map.insert(make_pair("begin", "BEGINtk"));
  keyword_map.insert(make_pair("end", "ENDtk"));
  keyword_map.insert(make_pair("iter", "ITERtk"));
  keyword_map.insert(make_pair("void", "VOIDtk"));
  keyword_map.insert(make_pair("var", "VARtk"));
  keyword_map.insert(make_pair("return", "RETURNtk"));
  keyword_map.insert(make_pair("read", "READtk"));
  keyword_map.insert(make_pair("print", "PRINTtk"));
  keyword_map.insert(make_pair("program", "PROGRAMtk"));
  keyword_map.insert(make_pair("cond", "CONDtk"));
  keyword_map.insert(make_pair("then", "THENtk"));
  keyword_map.insert(make_pair("let", "LETtk"));
}

//Display the line number of the token, followed by the category of the
//token (Identifier, Integer, Operator, etc), followed by the specific
//token name (IDtk, THENtk, etc), followed by the token description 
//(value of the token).
void display_token(Token token)
{
  cout << "Line number: " << token.line_number << " " << token_names[token.ID] 
    << " " << token.desc << "\n";
}

//Assign the operator token type (ie GREATERTHANtk) to the description
//of the token
void get_operator(Token &token)
{
  token.desc.assign(operator_map[token.desc]);
}

int is_operator(char ch)
{
  for (int counter = 0; counter < NUM_OPERATORS; counter++)
  {
    //If the input character matches any of the operators in the operators[]
    //array of characters, return 1 to indicate a match.
    if (ch == operators[counter])
      return 1;
  }

  //Return 0 to indicate that the input character is not an operator.
  return 0;
}

int is_keyword(Token &token)
{
  //Iterate through the string keywords array, and compare each string
  //entry to the function argument to determine if the identifier is
  //a keyword.
  for (int counter = 0; counter < NUM_KEYWORDS; counter++)
  {
    //If so, return the index corresponding to the keyword.
    if (token.desc == keywords[counter])
    {
      token.desc = keyword_map[token.desc];
      return counter;
    }
  } 

  //Otherwise, return -1.
  return -1;
}
