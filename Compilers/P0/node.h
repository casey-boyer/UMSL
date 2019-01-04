#ifndef NODE_H
#define NODE_H
#include <string>
#include <set>

using namespace std;

struct node_t
{
  int token_length; //The length of tokens, or strings, at this node
  int level; //The level of this node
  set<string> tokens; //The set of tokens at this node
  struct node_t *left, *right; //The left and right subtrees of this node
};

#endif
