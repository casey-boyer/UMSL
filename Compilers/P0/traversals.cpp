#include "traversals.h"
#include "node.h"
#include <iostream>
#include <fstream>
#include <string>
#include <cstring>
#include <cstdio>

using namespace std;

//This method will create the output file name to write the preorder, inorder
//and postorder traversals to. It returns the name of the input file
//(which is the input file supplied at the command line, or 'out' if the
//user redirected the file or is typing from the keyboard) with the
//extension ".preorder", ".inorder", or ".postorder".
char * create_file(string file_name, string extension)
{
  //Append the extension (.preorder, .inorder, or .postorder) to the
  //name of the file.
  string file_fullname = file_name.append(extension);

  //Create a c-string to store the name of the file.
  char * file_output_name = new char[file_fullname.length() + 1];

  //Initialize the c-string with the full filename, including the name
  //of the file and the appropriate extension.
  strcpy(file_output_name, file_fullname.c_str());

  //Return the c-string.
  return file_output_name;
}

//This method will perform preorder, inorder, and postorder traversal
//of the tree. It accepts the root of the tree as an argument, and
//the name of the file. It then creates an output file for each
//traversal (.preorder, .inorder, .postorder), invokes the functions
//to traverse the tree and passes the created file so the traversal
//is written to it, and then closes the file after tree traversal.
void traversals(struct node_t* root, string file_name)
{
  //Traverse preorder
  ofstream preorder_file; //Create the preorder file
  preorder_file.open(create_file(file_name, ".preorder"));
  traversePreorder(root, preorder_file);
  preorder_file.close(); //Close the preorder file

  //Traverse inorder
  ofstream inorder_file; //Create the inorder file
  inorder_file.open(create_file(file_name, ".inorder"));
  traverseInorder(root, inorder_file);
  inorder_file.close(); //Close the inorder file

  //Traverse postorder
  ofstream postorder_file; //Create the postorder file
  postorder_file.open(create_file(file_name, ".postorder"));
  traversePostorder(root, postorder_file);
  postorder_file.close(); //Close the postorder file
}

//This function traverses the BST in a preorder fashion. It accepts
//the root of the tree as an argument and the name of the output file
//to write the preorder traversal to.
void traversePreorder(struct node_t* root, ofstream &in_file)
{
  if (root != NULL)
  {
    //Write the contents of the current node to the preorder file;
    //Process the root
    printTokens(root, in_file);

    //Recur down the left subtree
    traversePreorder(root->left, in_file);

    //Recur down the right subtree
    traversePreorder(root->right, in_file);
  }
}

//This function traverses the BST in an inorder fashion. It accepts the
//root of the tree as an argument and the name of the output file
//to write the inorder traversal to.
void traverseInorder(struct node_t* root, ofstream &in_file)
{
  if (root != NULL)
  {
    //Recur down the left subtree
    traverseInorder(root->left, in_file);

    //Write the contents to the inorder file;
    //Process the root
    printTokens(root, in_file);

    //Recur down the right subtree
    traverseInorder(root->right, in_file);
  }
}

//This function traverses the BST in the postorder fashion. It accepts
//the root of the tree as an argument and the name of the output file
//to write the postorder traversal to.
void traversePostorder(struct node_t* root, ofstream &in_file)
{
  if (root != NULL)
  {
    //Recur down the left subtree
    traversePostorder(root->left, in_file);

    //Recur down the right subtree
    traversePostorder(root->right, in_file);

    //Write the contents to the postorder file;
    //Process the root
    printTokens(root, in_file);
  }
}

//This function will print the contents of each node (given by the first
//function parameter) and will write the node contents to the output file
//(given by the second function parameter). The function will use the level
//of the node to determine how many spaces it will be indented with
//in the output file. It will then indent the node contents with the
//number of spaces, followed by the length of each token in the node,
//and then followed by the tokens at this node.
void printTokens(struct node_t* root, ofstream &in_file)
{
  //Get an iterator for the set<> to iterate through the set of tokens
  //at the node. Set the iterator to the beginning of the tokens set<>
  //in the node.
  set<string>::iterator set_iterator = root->tokens.begin();

  //Get the number of spaces to indent the output with. Since the
  //levels start at 1, this is the current level minus one, and then
  //multiply this result by 2.
  char space = ' ';
  int num_spaces = (root->level - 1) * 2;
  
  //Get the number of spaces as a string. This insert method will
  //insert the space character num_spaces times to the spaces string,
  //starting at the beginning of the string (position 0).
  string spaces = "";
  spaces.insert(0, num_spaces, space);

  //Indent the line with the (the depth of the node, minus 1, multipled by
  //2) number of spaces, then print the string length of the node, and
  //then print a space because the tokens on this node will follow.
  in_file << spaces << root->token_length << " ";

  //Iterate through the set of tokens at this node. 
  for(set_iterator; set_iterator != root->tokens.end(); ++set_iterator)
  {
    //Write the current item in the set to the output file.
    in_file << (*set_iterator) << " ";
  }  

  //Go to the next line in the output file to process the next set
  //of nodes when printTokens() is called again
  in_file << "\n";
}
