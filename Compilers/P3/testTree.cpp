#include "testTree.h"
#include <string>
#include <iostream>

void preorder(node_t *node, int level)
{
  if (node == NULL)
    return;

  //The output line that will be printed to the screen revealing
  //the node's label and tokens.
  string line;

  //Add two spaces for each level of the node. If the node
  //is on the first level (level 0), there will be no spaces
  //If the node is on level 3, there will be 6 spaces (2 spaces * 3 levels).
  //This is used for indentation purposes when printing the tree,
  //which makes it easier to visualize the output.
  for (int counter = 0; counter < level; counter++)
  {
    line.append("  ");
  }

  //Append the node's label to the line to provide what production
  //the node represents.
  line.append(node->label);
  line.append(" ");

  //Append the tokens of a node to the output line.
  for (int counter = 0; counter < node->tokens.size(); counter++)
  {
    line.append(node->tokens[counter].desc);

    //Separate multiple tokens on a node by a comma, as long
    //as the tokens are integers or identifiers.
    if ( ((counter + 1) != node->tokens.size()) && 
      (node->tokens[counter].ID != OPtk))
      line.append(",");

    line.append(" ");
  }

  cout << line << "\n";
  
  //Print the children of the node, where the children go from
  //left to right; that is, child_1 is the leftmost child node, child_2
  //is the second leftmost child node, child_3 is the child node next to the
  //rightmost child node, and child_4 is the rightmost child node.
  preorder(node->child_1, level + 1);
  preorder(node->child_2, level + 1);
  preorder(node->child_3, level + 1);
  preorder(node->child_4, level + 1);
}
