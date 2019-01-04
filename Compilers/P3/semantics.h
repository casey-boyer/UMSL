#ifndef SEMANTICS_H
#define SEMANTICS_H
#include <vector>
#include "token.h"
#include "node.h"

using namespace std;

//This will define a variable on the stack. This includes the token
//of the variable, the scope of the variable, and the line number
//it is on in the file.
struct stack_t 
{
  Token token;
  int scope;
  int line_number;
};

//The stack that will hold all variables
extern vector<stack_t> stack;

//Traverse the parse tree and verify correct variable definitions
//and access.
void semantics(node_t*);
void check_var(stack_t);
int check_var_exists(stack_t);
int find_var(stack_t);
int compare_scope(stack_t);
void remove_local_vars(int);
void print_stack();

#endif
