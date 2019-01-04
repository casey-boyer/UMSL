#include "tree.h"
#include "node.h"
#include <string>
#include <stdlib.h>
#include <iostream>
#include <fstream>
#include <istream>
#include <sstream>

//This is an integer variable that declares the size of the C-strings
//that will hold input from the file. It is static because only the 
//buildTree() function requires this variable.
static const int BUF_SIZE = 256;

//This is the size of the tree, which is the number of nodes
//in the tree. It will be incremented every time a new node is inserted
//into the tree.
static int tree_size = 0;

//This is the length of the tokens in the rightmost node of the subtree;
//since nodes on the right indicate a greater token length, the rightmost
//node on the tree will have the tokens with the maximum length. This
//value will be used when assigning levels to the nodes of the tree.
static int max_token_length = 0;

//Read the data from the file, redirection from the file, or the keyboard.
//The input stream object (istream) named in is passed by reference from
//the function call, and this will either be a file pointer, keyboard input,
//or input from a file that was redirected at the command line.
//Separate each input string based on whitespace characters '\n', '\t',
//and ' ', which are newline, tab, and space, respectively.
struct node_t* buildTree(istream &in)
{
  //This is the root of the binary search tree. It is initialized
  //to NULL until input from te input stream is read below.
  struct node_t* root = NULL;

  //Check if the input is empty. If the user provided a filename on the
  //command line and the file is empty, this will return NULL. If the user
  //redirected input from an empty file, this will return NULL. If the
  //user is providing keyboard input and enters 'CTRL+D' before entering
  //any input, this will return NULL.
  //The peek() function of the istream object will return the next
  //character in the input sequence WITHOUT extracting it from the
  //input sequence. If there are no characters to be read, the
  //function will return the EOF value and set the eof flag.
  if (in.peek() == fstream::traits_type::eof())
    return NULL;

  //These are the whitespace delimiters.
  char delim_space = ' ';
  char delim_tab = '\t';
  char delim_newline = '\n';

  //The input c-string used to store input read from the file
  //or keyboard
  char input[BUF_SIZE];

  //The input c-string used to store input read from an
  //individual line in the file or keyboard, where the line is
  //retrieved using a string stream
  char input_string_stream[BUF_SIZE];

  //This string will store the input c-string when a line is read,
  //so that the string_stream object (defined below) may be 
  //initialized with it.
  string input_string;

  //These are the individual tokens returned from the string
  //stream object when calling its getline() function.
  string individual_tokens;

  //While the input (whether this is from an input file OR redirected
  //from a file to the keyboard OR directly from the keyboard) has a line
  //(delimited by newline) of characters to process
  while (in.getline(input, BUF_SIZE, delim_newline))
  {
    //Get the entire line of input returned by in.getline()
    input_string = input;

    //Create a stringstream object that will tokenize this entire
    //line of input
    stringstream string_stream(input_string);

    //While the stringstream object has a 'line' (strings delimited
    //by a space) from the input string
    while (string_stream.getline(input_string_stream, BUF_SIZE, delim_space))
    {
      //Create another stringstream object to tokenize the input delimited
      //by spaces to further tokenize it by tabs (if any exist)
      stringstream tab_stream(input_string_stream);

      //While the stringstream object has a 'line' (strings delimited
      //by a tab) from the input string delimited by spaces
      while(tab_stream.getline(input_string_stream, BUF_SIZE, delim_tab))
      {
        //If the end of the line has not yet been reached, this will
        //always execute. Get the individual token, that was separated
        //by newline, space, and tab, from the input_string_stream.
        string individual_tokens = input_string_stream;

        //If the stringstream tab_stream object returned a token, insert
        //it into the BST.
        if (individual_tokens.length() != 0)
        {
          //If the root is null, this is the first token. Assign the root
          //to this token by invoking the insert() function to insert
          //the token into the BST, and assign the root to the returned node.
          //Otherwise, insert the token into the tree using the
          //root node to insert the token in its proper position in the tree.
          if (root == NULL)
            root = insert(root, individual_tokens);
          else
            insert(root, individual_tokens);
        }
      }
    }
  }

  //Return the root, which will have subtree(s) (if the file was not
  //empty or there were more than one line) that point to all nodes in
  //the tree.
  return root;
}

//This function creates a new node, using the string token that will be the
//first token inserted at this node.
struct node_t* create_node(string token)
{ 
  //Initialize an empty node.
  struct node_t* temp_node = new(struct node_t);

  //Assign the token_length of this node based on the length of the string
  //argument.
  temp_node->token_length = token.length();

  //Insert the string argument into the set<> of tokens at this node.
  temp_node->tokens.insert(token);

  //Initialize the left and right children to null.
  temp_node->left = temp_node->right = NULL;

  //Return the newly created node.
  return temp_node;
}

//This function will insert a new node into the BST, with the root
//(and/or subtree) node as an argument, and the token to be inserted.
struct node_t* insert(struct node_t* node, string token)
{
  //If the node is NULL, this is either the root of the tree OR a 
  //new left or right child of a node. Insert the new node into the tree.
  if (node == NULL)
  {
    //Increment the total number of nodes in the tree, because
    //when the node == NULL condition is true, a new node is being
    //added to the left or right subtree of a node.
    tree_size += 1;

    //Check if the current node being added is the rightmost subtree
    //of the tree; if so, this node will have tokens of the greatest length
    //in the tree, so update the max_token_length. If the node being
    //added is the leftmost subtree of the tree, then the max_token_length
    //value will be greater than this node's token length, so do not
    //update the max_token_length.
    if (max_token_length < token.length())
      max_token_length = token.length();

    //Return the newly created node.
    return create_node(token);
  }
  
  //If the length of the token is less than the length of the tokens
  //at the current node, recur down the left subtree.
  //If the length of the token is greater than the length of the tokens
  //at the current node, recur down the right subtree.
  //If the length of the token is equal to the length of the tokens
  //at the current node, insert this token into the current node's
  //set<> of tokens.
  if (token.length() < node->token_length)
    node->left = insert(node->left, token);
  else if (token.length() > node->token_length)
    node->right = insert(node->right, token);
  else if (token.length() == node->token_length)
    node->tokens.insert(token);

  //Return the node
  return node;
}

//This function will traverse the entire tree, after it is created,
//to assign the level to each node in the tree. It accepts the root
//of the tree as an argument.
void getLevels(struct node_t* root)
{
  //Initially set the data to the max token length, to find the node(s)
  //at the bottom of the tree (lowest levels) and recurisvely go up
  //the tree by decreasing this counter.
  int data = max_token_length;

  for (int index = 0; index <= tree_size; index++)
  {
    //This passes the root of the tree, and the length of the token(s)
    //in decreasing order. This way, the level is searched for based on
    //the size of the tokens in a node.
    int level = findLevel(root, data);

    //Decrement the size of the tokens to search for the level of the next
    //node.
    --data;
  }
}

//This is a utility function that will find the level of a specific
//node, given by the root argument, based on the length of the
//tokens at this node.
int findLevel(struct node_t* root, int token_length)
{
  //Find the level of the current node, starting at the root
  //and level 1, and searching for the node with the specified
  //token length. Then, return the level of the node to the calling
  //function.
  int level = assignLevel(root, token_length, 1);
  return level;
}

//This function will recur down the left and right subtrees to identify
//the level of a node until the level with the specified token_length
//is found
int assignLevel(struct node_t* root, int token_length, int level)
{
  //If the root is NULL, then this node does not exist (could be a left
  //or right branch of a node that is at the bottom of the tree).
  //Return 0 to indicate that this level is not present.
  if (root == NULL)
    return 0;

  //If the length of the tokens at this node is equal to the length of 
  //the tokens of the node we are searching for, then the node on this
  //level has been found. Set the level member of this node, and return
  //the level value, indicating that the level of this node was found.
  if (root->token_length == token_length)
  {
    root->level = level;
    return level;
  }

  //RECUR DOWN BOTH THE LEFT AND RIGHT SUBTREES OF THE CURRENT NODE,
  //this way, nodes that are on the same level but different branches
  //may still be assigned the correct level.  

  //Recursively search down the left subtree of the current node
  //by invoking assignLevel() with the left node of the current node,
  //and incrementing level by 1, to indicate this is further down in
  //the tree.
  int down_level = assignLevel(root->left, token_length, level + 1);

  //If 0 was not returned from the root == NULL condition, then return
  //the value of down_level, so that the level of the node may be found
  //by the above conditions.
  if (down_level != 0)
    return down_level;

  //Recursively search down the right subtree of the current node,
  //by invoking assignLevel() with the right node of the current node.
  //and incrementing level by 1, indiciating that this is further down
  //in the tree.
  down_level = assignLevel(root->right, token_length, level + 1);

  //Return the value of down_level to assign the level of the node.
  return down_level;
}
