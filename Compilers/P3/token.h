#ifndef TOKEN_H
#define TOKEN_H
#include <string>
#include <map>

using namespace std;

const int NUM_TOKENS = 5; //Number of tokens
const int NUM_KEYWORDS = 12; //Number of keywords
const int NUM_OPERATORS = 18; //Number of operators

//The current index within a line, used by testScanner when filtering
//the file input.
extern int current_index;

//The index of the current character within a token, used by scanner
//when reading a token.
extern int token_index;

//The index of the current line from testScanner. Used by both testScanner
//and scanner to determine the line that token(s) appear on.
extern int line_index;

//Map the operator (i.e '<', ';', etc) to the token name, such as
//GREATERTHANtk, SEMICOLONtk, etc.
//Extern so that scanner and testScanner may access it.
extern map<string, string> operator_map;

//Map the keyword (i.e. 'then', 'cond') to the token name, such as
//THENtk, CONDtk, etc.
//Extern so that scanner and testScanner may access it.
extern map<string, string> keyword_map;

//The token categories
enum token_ID {IDtk, KEYWORDtk, OPtk, INTtk, EOFtk};

//The name of each token category
const string token_names[NUM_TOKENS] = {"Identifier", "Keyword",
  "Operator", "Integer", "End of File"};

//The name of each keyword
const string keywords[NUM_KEYWORDS] = {"begin", "end", "iter",
  "void", "var", "return", "read", "print", "program", "cond",
  "then", "let"};

//The name of each operator
const char operators[] = {'#', '=', '<', '>', ':', '+',
  '-', '*', '/', '%', '.', '(', ')', ',', '{', '}', ';', '[', ']'};

//The comment symbol
const char COMMENT_DELIM = '#';

struct Token 
{
  token_ID ID; //ID corresponding to the token type
  string desc; //Corresponds to the 'value' of the token
  int line_number; //Line number the token occurred on
};

void populate_operator_map();
void populate_keyword_map();
void display_token(Token);
void get_operator(Token &);
int is_operator(char);
int is_keyword(Token &);
string get_token_desc(Token);

#endif
