#include "testScanner.h"
#include "scanner.h"
#include "token.h"
#include <iostream>
#include <istream>

int current_index; //Refers to the extern int current_index in token.h
int line_index; //Refers to the extern int line_index in token.h
static int comment_flag;

int testScanner (istream &in)
{
  string input_line; //For each line of input
  Token token; //When the scanner() returns a token

  //Intialize the map for operators and keywords
  populate_operator_map();
  populate_keyword_map();

  //Set the line_index to 1; when testScanner is called, it will begin
  //reading from the input stream, which is the first line.
  line_index = 1; 

  //While there are still lines from the file, read each line and store
  //the line as a string in input_line.
  while (getline(in, input_line))
  {
    //Everytime there is a new line, start at the beginning state and column
    //of the FSA state table.
    current_index = 0;

    //Filter the input by removing comments and whitespace.
    if (filter(input_line) == -1)
      return 1;

    //Initialize the index of the current character within a token;
    //this is used when identifying tokens in scanner().
    token_index = 0;

    //If the line is not empty, invoke the scanner to get each token
    //from the line.
    if (input_line.length() > 0)
    {
      //Continue invoking scanner() until each token in the line has
      //been identified. Then, display the token to the output.
      while (scanner(input_line, token) == 0)
        display_token(token);
    }

    //Update the current line number (index) in the file.
    line_index++;
  }

  //If all lines of the file have been read, print the EOF token.
  if (in.eof())
  {
    //Get the line number of the EOF token. If the line index is greater
    //than 1, then this is the last line of the file. If the line index
    //is 1, then the EOFtk is on the first line of the file.
    if (line_index > 1)
      line_index--;

    //Initialize and display the EOF token.
    token.line_number = line_index;
    token.ID = EOFtk;
    token.desc = "EOF";
    display_token(token);
  }
  
  return 0;
}

//Remove comments and whitespace from the input line.
int filter(string &input_string)
{
  //If the current_index is greater than the input string length,
  //then return 0; otherwise, this would cause a segmentation fault
  //by going out of bounds of the input string.
  if (current_index >= input_string.length())
    return 0;

  //This string will contain the input line stripped of whitespace
  //and comments.
  string filtered_string;

  //The current character and previous character in the input line, as
  //well as a character representing a space in the input line.
  char current_ch;
  const char SPACE = ' ';
  char prev_ch = ' ';

  //Iterate through each character in the input line.
  for (int counter = current_index; counter < input_string.length(); counter++)
  {
    //Get the current character in the input line.
    current_ch = input_string.at(counter);

    //Get the previous character in the input line. If this is not the
    //beginning of the input line, the previous character is at the current
    //loop counter index minus 1 in the string.
    if (counter > 0)
      prev_ch = input_string.at(counter - 1);

    //If the current character is a comment delimiter: '#'
    if (current_ch == COMMENT_DELIM)
    {
      //Add a space to the filtered string so that any characters
      //in the comment(s) in this line will be ignored
      filtered_string.push_back(SPACE);
      
      //Flip the comment flag on.
      comment_flag = !comment_flag;
    }
    else if (!comment_flag)
    {
      //If the current character is a space, then check if the
      //previous character is NOT a space; if so, add the current character
      //in the string to the filitered string so that the whitespace is
      //ignored. If not, do nothing so that the filtered string does not
      //contain any whitespace.
      if (isspace(current_ch))
      {
        if (!isspace(prev_ch))
          filtered_string.push_back(current_ch);
      }
      else if (is_valid_ch(current_ch) == -1)
      {
        //If the current character is not in the alphabet, print an error
        //to the screen.
        cout << "Scanner error: \'" << current_ch <<
          "\' is not a valid character.\n";
        return -1;
      }
      else 
      {
        //If the current character is not a space and is in the alphabet,
        //add it to the filtered string.
        filtered_string.push_back(current_ch);
      }
    }

    current_index++; //Increment the current index of the character in the line
  }

  //Reassign the input string to the filtered string so that no whitespace or
  //comments are present.
  input_string.assign(filtered_string);

  return current_index;
}

//Check if the character within a line is part of the alphabet,
//which is defined as all characters (uppercase and lowercase), digits,
//and the specified operators.
int is_valid_ch(char ch)
{
  //If the input is not an operator, digit, or alphabetic character,
  //return -1; else, return 0
  if (!is_operator(ch) && !isdigit(ch) && !isalpha(ch))
    return -1;
  else
    return 0;
}
