#include <iostream> //For cout and cin; input and output
#include <fstream> //Stream class for file operations
#include <string> //For appending the filename extension if not present
#include <cstring> //For storing the file name
#include "node.h"
#include "tree.h"
#include "traversals.h"

using namespace std;

int main(int argc, char* argv[])
{
  //The file stream pointing to the input file.
  ifstream input_file;

  //The name of the input file, including the extension 'fs18'.
  char * input_file_name;

  string output_file_name;

  //The root of the binary search tree.
  struct node_t* root = NULL;

  //Test if the argument count provided on the command line is equal
  //to 2. If so, a file name (the input file to be read from)
  //was given. If argc is greater than 2, then too many file arguments have
  //been provided. If argc is equal to 1, then the contents of an input file
  //were provided. 
  if (argc > 2)
  {
    cout << "ERROR: Too many arguments.\nUsage: P0[file]\n";
    return -1;
  }
  else if (argc == 2)
  {
    //Get the name of the input file as a string.
    string file_name = argv[1];

    //See if the input file name contains the '.fs18' extension. If
    //find() returns npos, then no matches were found, and the file
    //name does not have the .fs18 extension. If find() does not return
    //npos, then the position of the first character of the first
    //match of .fs18 in the file name was returned, so the .fs18 file name
    //extension is present and does not need to be added.
    size_t index = file_name.find(".fs18");

    //Assign the output file name to the name of the input file,
    //without the .fs18 extension.
    if (index == string::npos)
    {
      //Since the input file did not include the 'fs18' extension,
      //simply assign the output_file_name to the file_name string.
      output_file_name = file_name;
      file_name.append(".fs18");
    }
    else
    {
      //If the input file DID include the 'fs18' extension, then
      //assign the output_file name only the name of the file, without
      //this extension. This is done by getting a substring of the
      //full file name (file_name) starting at the beginning of the
      //string to the index where the '.' occurs.
      output_file_name = file_name.substr(0, index);
    }

    //Intialize the c-string, input_file_name, to the length
    //of the file_name string plus one.
    input_file_name = new char[file_name.length() + 1];

    //Convert the string, file_name, to a c-string and copy its
    //contents to the input_file_name c-string.
    strcpy(input_file_name, file_name.c_str());

    //Open the input file for input (ifstream::in flag).
    input_file.open(input_file_name, ifstream::in);

    //Check if the input file file opened successfully.
    if (!input_file.is_open())
    {
      cout << "ERROR: Could not open the input file " << file_name 
        << ". Terminating program.\n";
      return -1;
    }
    else
    {
      //If so, build the tree with the contents of the input file.
      root = buildTree(input_file);
    }
  }
  else if (argc == 1)
  {
    cout << "Argument count is 1. Read from the keyboard.\n";

    //If the input is being redirected from a file or the keyboard, the
    //output file name for the preorder, inorder, and postorder
    //traversals will be 'out'.
    output_file_name = "out";
    root = buildTree(cin);
  }

  //Close the input file
  input_file.close();

  //If the root is NULL after invoking buildTree(), then the user
  //provided an empty file OR did not provide any keyboard input.
  //Print an error to the console.
  if (root == NULL)
  {
    cout << "ERROR: Empty input file.\n";
  }
  else
  {
    //Print inorder, preorder, and postorder
    //traversals of the binary search tree.
    getLevels(root);
    
    //Traverse preorder, inorder, and postorder traversals
    //of the BST.
    traversals(root, output_file_name);
  }

  return 0;
}
