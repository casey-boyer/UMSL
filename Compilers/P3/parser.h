#ifndef PARSER_H
#define PARSER_H
#include "token.h"
#include "node.h"
#include <string>

using namespace std;

//This token will be used to store the token
//returned by the scanner during each call to scanner().
extern Token tk;

//Auxillary function to begin scanning the program using the starting
//non-terminal, <program>.
node_t *parser();

//The BNF for the program.
node_t *program();
node_t *block();
node_t *vars();
node_t *expr();
node_t *A();
node_t *M();
node_t *R();
node_t *stats();
node_t *mStat();
node_t *stat();
node_t *in();
node_t *out();
node_t *cond();
node_t *loop();
node_t *assign();
node_t *RO();

//Print an error if the incorrect token was receieved
//while processing the input program.
void parser_error();

//Create a new node_t node.
node_t *create_node(string);

#endif
