#ifndef SCANNER_H
#define SCANNER_h

#include "token.h"

using namespace std;

//The FSA table is 4 rows and 6 columns
const int STATES = 4;
const int COLUMNS = 6;

//All possible states in the FSA table
const int STATE_ZERO = 0;
const int STATE_ONE = 1;
const int STATE_TWO = 2;
const int STATE_THREE = 3;
const int FINAL_STATES = 1000;
const int IDENTIFIER_FINAL_STATE = 1001;
const int INTEGER_FINAL_STATE = 1003;
const int OPERATOR_FINAL_STATE = 1004;
const int EOF_FINAL_STATE = 1005;
const int ERROR_STATE_UPPERCASE = -1;
const int ERROR_STATE_INTEGER = -2;

//The FSA table. Visually:
//---------------------------------------------------------------
//| Lowercase | Uppercase | Digit   | Space   | EOF   | Operator |
//---------------------------------------------------------------
//| STATE 2   |   ERROR   | STATE 3 | STATE 0 | EOFTK | STATE 1  |
//---------------------------------------------------------------
//| OPtk      |  OPtk     |  OPtk   | OPtk    | OPtk  | OPtk     |
//---------------------------------------------------------------
//| STATE 2   | STATE 2   | STATE 2 | IDtk    | IDtk  | IDtk     |
//---------------------------------------------------------------
//| ERROR INT | ERROR INT | STATE 3 | INTtk   | INTtk | INTtk    |
//---------------------------------------------------------------
const int FSA_TABLE[STATES][COLUMNS] = 
  {
    {STATE_TWO, ERROR_STATE_UPPERCASE, STATE_THREE, STATE_ZERO, 
      EOF_FINAL_STATE, STATE_ONE},

    {OPERATOR_FINAL_STATE, OPERATOR_FINAL_STATE, OPERATOR_FINAL_STATE,
      OPERATOR_FINAL_STATE, OPERATOR_FINAL_STATE, OPERATOR_FINAL_STATE},

    {STATE_TWO, STATE_TWO, STATE_TWO, IDENTIFIER_FINAL_STATE, 
      IDENTIFIER_FINAL_STATE, IDENTIFIER_FINAL_STATE},

    {ERROR_STATE_INTEGER, ERROR_STATE_INTEGER, STATE_THREE, 
      INTEGER_FINAL_STATE, INTEGER_FINAL_STATE, INTEGER_FINAL_STATE}
  };


int scanner(string &, Token &);
int get_column(char);
void error_output(int, string);

#endif
