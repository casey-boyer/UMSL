#ifndef NODE_H
#define NODE_H
#include <string>
#include <set>
#include <vector>
#include "token.h"

using namespace std;

struct node_t
{
  //This is the label that corresponds to the nonterminal, or function
  //name that creates the node. ex. the <program> production would
  //create a node and have node.label = "<program>"
  string label;
  int token_length; //The length of tokens, or strings, at this node
  int level; //The level of this node
  //set<string> tokens; //The set of tokens at this node

  //The tokens that need to be stored at this node, such as
  //operators, identifiers, or integers
  vector<Token> tokens;

  //The children of a node. The maximum number of children is 4.
  struct node_t *child_1, *child_2, *child_3, *child_4;
};

#endif
