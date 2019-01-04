#include "scanner.h"
#include "token.h"
#include <string>
#include <cctype> //For isdigit, isspace, islower, isalpha functions
#include <cstddef>
#include <iostream>
#include <stdlib.h> //For the exit() function

using namespace std;

//From token.h
int token_index;

int current_index; //Refers to the extern int current_index in token.h
int line_index; //Refers to the extern int line_index in token.h
vector <string> file_string; //Refers to the extern vector <string> file_string
static int comment_flag;

//Function that will read the entire input file and store
//each line in a (global) vector, file_string.
void read_file(istream &in)
{
  //Initialize the maps for operator and keyword tokens.
  populate_operator_map();
  populate_keyword_map();

  //Current line of input in the file.
  string input_line;
  current_index = 0;
  Token token;

  int counter = 0;

  //Read each line of input from the input file, until EOF is
  //reached. Store each line in the file_string vector.
  while (getline(in, input_line))
  {
    //This represents the current index in the line when removing
    //extra whitespace or comments in the filter() function.
    current_index = 0;

    //Remove comments and extra whitespace.
    filter(input_line);

    //Store the line in the file_string vector.
    if (input_line.length() > 0)
    {
      file_string.push_back(input_line);
      counter++;
    }
  }
}

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
    //beginning of the input line, the previous character is at the
    //current loop counter index minus 1 in the string.
    if (counter > 0)
      prev_ch = input_string.at(counter - 1);

    //If the current character is a comment delimiter: '#'
    if (current_ch == COMMENT_DELIM)
    {
      //Add a space to the filtered string so that any characters
      //in the comment(s) in this line will be ignored.
      filtered_string.push_back(SPACE);

      //Flip the comment flag on.
      comment_flag = !comment_flag;
    }
    else if (!comment_flag)
    {
      //If the current character is a space, then check if the previous
      //character is NOT a space; if so, add the current character
      //in the string to the filtered string so that the whitespace is
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
        cout << "Error: \'" << current_ch <<
          "\' is not a valid character.\n";
        exit(EXIT_FAILURE);
      }
      else
      {
        //If the current character is not a space and is in the
        //alphabet, add it to the filtered string.
        filtered_string.push_back(current_ch);
      }
    }

    current_index++; //Increment the current index of the char in the line
  }

  string whitespaces = " \t\f\v\n\r";

  //Find the index of the last character in the string that is
  //not a whitespace. This will be the starting index + 1 for stripping
  //the string of trailing whitespaces. This is necessary because
  //spaces are inserted into the string in place of comments, and must
  //be stripped from the string at the end.
  size_t trailing_whitespace_index = 
    filtered_string.find_last_not_of(whitespaces);

  //If the position of the first character that does not match
  //the specified whitespaces is found, then remove the trailing
  //whitespaces from the string. Otherwise, if no such characters
  //are found, then there are no trailing whitespaces.
  if (trailing_whitespace_index != string::npos)
  {
    filtered_string.erase(trailing_whitespace_index + 1);
  }
  else
  {
    //Check if the entire line is all spaces. This may occur when
    //there is a comment that spans multiple lines, or a line that
    //contains one or more comments, because comments are replaced by
    //space characters. Initially set the all_spaces_flag to true.
    bool all_spaces_flag = true;

    //Iterate through the filtered string. If there is any character
    //that is not a space, set the all_spaces_flag to false and break.
    //This indicates that there are other characters on the line outside
    //of comments that need to be processed.
    for (int i = 0; i < filtered_string.length(); i++)
    {
      if (filtered_string.at(i) != SPACE)
      {
        all_spaces_flag = false;
        break;
      }
    }

    //If the all_spaces_flag is true, then the entire filtered_string
    //is comments; reassign the filtered_string an empty value, so it
    //will not be considered for processing.
    if (all_spaces_flag)
      filtered_string.assign("");
  }

  //Reassign the input string to the filtered string so that no 
  //whitespace or comments are present.
  input_string.assign(filtered_string);

  return current_index;
}

int is_valid_ch(char ch)
{
  //If the input is not an operator, digit, or alphabetic character,
  //return -1; else, return 0
  if (!is_operator(ch) && !isdigit(ch) & !isalpha(ch))
    return -1;
  else
    return 0;
}

//Get the current line from the file.
string get_string()
{
  return file_string[line_index];
}

/*Make sure scanner terminates the program when an error is encountered!*/
int scanner(Token &token)
{
  string input_string = get_string();

  if (token_index == input_string.length())
  {
    line_index++;
    token_index = 0;
   
    if (line_index < file_string.size())
      input_string = file_string[line_index];
    else
    {
      token.desc = "EOF";
      token.ID = EOFtk;
      token.line_number = (line_index - 1);
      return 1;
    }
  }

  //Set the line number for the current token.
  token.line_number = (line_index + 1);

  //Begin at state 0 in the state transition table when identifying
  //the token.
  int current_state = 0;

  //These variables will refer to the next state and column of the 2D
  //array representing the state transition table.
  int next_state;
  int next_col;

  //This is the description of the token.
  string token_desc;

  //The next character in the token.
  char next_char;

  //Represents a space character. This will be useful when identifying
  //a token that ends on a newline, and using the space column in the
  //state transition table when encountering a newline to determine
  //the token.
  const char SPACE = ' ';

  //While the current character of the line is less than the length
  //of the input string, continuing reading the line and attempting to
  //identify tokens.
  while (token_index <= input_string.length())
  {
    //If the token_index is less than the input string, then
    //set the next character of the token so it may be used in the FSA
    //table; otherwise, set the next character as a space so it may
    //be identified as a final state in the FSA table.
    if (token_index < input_string.length())
      next_char = input_string.at(token_index);
    else
      next_char = SPACE;

    //Get the column of the token in the FSA table, and use this value
    //with the current_state to look up the state in the FSA table
    next_col = get_column(next_char);
    next_state = FSA_TABLE[current_state][next_col];

    //If the FSA table returns a negative integer, then this is an error
    //state; print the corresponding error and return to testScanner().
    if (next_state < 0)
    {
      //Reset the state to 0.
      current_state = 0;

      //Print the corresponding error message for this state.
      error_output(next_state, input_string);

      //Print where on the line the error occurred.
      cout << input_string.substr(0, token_index + 1) << "\n";
      cout << string(token_index, SPACE) << "^\n";

      token_index++; //Increment where the character is on the line

      exit(EXIT_FAILURE);
    }
    else if (next_state > FINAL_STATES)
    {
      //If the FSA table returned a final state, then match the returned
      //value to one of the available final states.
      
      //Set the description of the token to the current token_desc, since
      //this is a final state and the token will be identified now.
      token.desc = token_desc;

      //Identify the final state and assign the token ID and desc accordingly.
      switch (next_state)
      {
        //If the state is the final state for an identifier token,
        //compare the token to the keywords string array to test if it is
        //a keyword; if so, assign the token ID KEYWORDtk; if not,
        //assign the token ID to IDtk.
        case IDENTIFIER_FINAL_STATE:
          if (is_keyword(token) != -1)
          {
            token.ID = KEYWORDtk;
            //token.desc.append(" " + token_desc);
            token.desc.assign(token_desc);
          }
          else
          {
            token.ID = IDtk;
            token.desc.assign("IDtk " + token_desc);
          }
          break;
        case INTEGER_FINAL_STATE:
          //If the state is the final state for an integer token,
          //compare the token, then assign the token ID to INTtk and
          //assign the desc to INTtk and the value of the integer.
          token.ID = INTtk;
          token.desc.assign("INTtk " + token_desc);
          break;
        case OPERATOR_FINAL_STATE:
          //If the final state is the final state for an operator
          //token, then assign the ID to OPtk. Then, get the corresponding
          //operator (using the operator_map in get_operator()) and 
          //assign the operator token and value to the desc of the token.
          token.ID = OPtk;
          get_operator(token);
          token.desc.assign(token_desc);
          break;
      }

      return 0;
    }

    //If the next_state was not an error or final state,
    //assign the current state to the next state (so that the current
    //state will be reflected in the next loop iteration).
    current_state = next_state;

    token_index++; //Update the index of the input string

    //If the next character is not a space, add this character from
    //the input string to the token description.
    if (!isspace(next_char))
      token_desc.push_back(next_char);
  }

  return -1;
}

//Get the column in the FSA table that corresponds to the current
//character.
int get_column(char ch)
{
  if (isalpha(ch))
  {
    //If the character is an alphabetic character, determine if it
    //is uppercase or lowercase.
    if (islower(ch))
      return 0; //If lowercase, return 0 for column 0.
    else
      return 1; //If uppercase, return 1 for column 1.
  }
  else if (isdigit(ch))
    return 2; //If the character is a digit, return 2 for column 2.
  else if (isspace(ch))
    return 3; //If the character is a space, return 3 for column 3.
  else if (is_operator(ch))
    return 5; //If the character is an operator, return 5 for column 5.
  else
    return -1; //The character does not match the accepted categories.
}

//Print the error for a given error state.
void error_output(int state, string input_string)
{
  cout << "Scanner error: Line " << line_index << ": ";
  
  //Print the error.
  if (state == ERROR_STATE_UPPERCASE)
  {
    //If the token is an identifier and began with uppercase letters
    cout << "All tokens with alphabetical characters must begin with a "
      << "lowercase letter.\n";
  }
  else if (state == ERROR_STATE_INTEGER)
  {
    //If the token is an identifier and began with digits
    cout << "All integer tokens (tokens that begin with a digit) must "
      << " contain only digits.\n";
  }
}
