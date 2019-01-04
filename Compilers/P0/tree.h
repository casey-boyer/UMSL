#ifndef TREE_H
#define TREE_H
#include "node.h"
#include <string>

using namespace std;

//Build the BST from the input file or keyboard input.
struct node_t* buildTree(istream&);

//Create an individual node based on the length of the token
struct node_t* create_node(string);

//Insert a node into the BST.
struct node_t* insert(struct node_t*, string);

//Get the levels of all nodes in the tree
void getLevels(struct node_t*);

//Utility function to find the level of a specific node
int findLevel(struct node_t*, int);

//Recur down the tree until the level of a node is found
int assignLevel(struct node_t*, int, int);

#endif
