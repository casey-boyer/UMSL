#include <iostream> //For cout and cin; input and output
#include <fstream> //Stream class for file operations
#include <string> //For appending the filename extension if not present
#include <cstring> //For storing the file name
#include "token.h"
#include "testScanner.h"
#include "scanner.h"

using namespace std;

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

    //If the input file does not include the .fs18 extension, display
    //a "System" error and terminate the program.
    if (index == string::npos)
    {
      cout << "System error: File " << file_name << 
        " not found. Terminating program.\n";
      return -1;
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
      cout << "System error: Could not open the input file " << file_name 
        << ". Terminating program.\n";
      return -1;
    }
    else
    {
      //Begin scanning the tokens in the file.
      testScanner(input_file);
    }
  }
  else if (argc == 1)
  {
    //Scan the tokens from redirected input (or directly from keyboard typing).
    testScanner(cin);
  }

  //Close the input file
  input_file.close();

  return 0;
}
