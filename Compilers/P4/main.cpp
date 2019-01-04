#include <iostream> //For cout and cin; input and output
#include <fstream> //Stream class for file operations
#include <string> //For appending the filename extension if not present
#include <cstring> //For storing the file name
#include "token.h"
#include "node.h"
#include "scanner.h"
#include "parser.h"
#include "testTree.h"
#include "semantics.h"

using namespace std;

ofstream target_file;

int main(int argc, char* argv[])
{
  //The file stream pointing to the input file.
  ifstream input_file;

  //The name of the input file, including the extension 'fs18'.
  char * input_file_name;

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
    //extension is present.
    size_t index = file_name.find(".fs18");
    string input_file_str = file_name;
    string output_file_str = file_name;

    //If the input file does not include the .fs18 extension, then
    //append the .fs18 extension to the file name.
    if (index == string::npos)
    {
      //file_name.append(".fs18");
      input_file_str.append(".fs18");
      output_file_str.append(".asm");
    }
    else
    {
      output_file_str = file_name.substr(0, index);
      output_file_str.append(".asm");
    }

    //Intialize the c-string, input_file_name, to the length
    //of the file_name string plus one.
    input_file_name = new char[input_file_str.length() + 1];
    char *output_file_name = new char[output_file_str.length() + 1];

    //Convert the string, file_name, to a c-string and copy its
    //contents to the input_file_name c-string.
    strcpy(input_file_name, input_file_str.c_str());
    strcpy(output_file_name, output_file_str.c_str());

    //Open the input file for input (ifstream::in flag).
    input_file.open(input_file_name, ifstream::in);

    //Check if the input file file opened successfully.
    if (!input_file.is_open())
    {
      cout << "System error: Could not open the input file " << file_name 
        << ". Terminating program.\n";
      return -1;
    }
    else
    {
      //Read the file contents and store each line of the
      //file in the vector<string> file_string.
      target_file.open(output_file_name, ofstream::out);
      //target_file(output_file_name);
      read_file(input_file);
    }
  }
  else if (argc == 1)
  {
    //Scan the tokens from redirected input (or directly from keyboard typing),
    //and store each line in the vecotr<string> file_string.
    target_file.open("out.asm", ofstream::out);
    read_file(cin);
  }

  //Close the input file
  input_file.close();

  node_t* root = parser();

  //To print a preorder traversal of the parse tree, uncomment
  //the line below.
  //preorder(root, root->level);
  semantics(root);

  target_file.close();

  return 0;
}
