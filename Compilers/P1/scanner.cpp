#include "scanner.h"
#include "token.h"
#include <string>
#include <cctype> //For isdigit, isspace, islower, isalpha functions
#include <iostream>

using namespace std;

//From token.h
int token_index;

int scanner(string &input_string, Token &token)
{
  //Set the line number for the current token.
  token.line_number = line_index;

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

      return -1;
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
            token.desc.append(" " + token_desc);
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
          token.desc.append(" " + token_desc);
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

  //If no final state is reached and no error state was returned, an
  //error occurred; return -1
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
  cout << "Scanner error in line " << line_index << ": ";
  
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
