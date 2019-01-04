#ifndef TRAVERSALS_H
#define TRAVERSALS_H
#include "node.h"
#include <set>
#include <string>
#include <fstream>

//Creates the output file name
char * create_file(string, string);

//Traverses the BST in preorder, inorder, and postorder traversals
void traversals(struct node_t*, string);

//Preorder traversal of the BST 
void traversePreorder(struct node_t*, ofstream&);

//Inorder traversal of the BST
void traverseInorder(struct node_t*, ofstream&);

//Postorder traversal of the BST
void traversePostorder(struct node_t*, ofstream&);

//Process the current node; print the node contents (tokens)
//to the output file
void printTokens(struct node_t*, ofstream&);

#endif
