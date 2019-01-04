CS4280-001: Casey Boyer, P1

****PROGRAM IMPLEMENTED USING FSA TABLE AND DRIVER, DETAILS BELOW****

Invocation: scanner [filename].fs18

Reading output:
  For each token from the .fs18 input file, the line number of the token,
   followed by the token type (identifier, operator, keyword), followed
   by the name of the token type (IDtk, THENtk), followed by the token value
   (xyz, 1, then) will be printed.

Testing:
  The following test files are provided:

  -P1_test1.fs18 contains only one token, an identifier: 'x'
 
  -P1_test2.fs18 contains a list of identifiers and numbers:
     'x xy xyz 1 12 23' separated by spaces.

  -P1_test3.fs18 contains a list of tokens, some of which are separated
    by spaces and newlines, and others which are not separated by whitespace.
    This tests whether the scanner can parse tokens that are not
    separated, i.e. 'x4>' will be identified as an identifier token,
    then an integer token, and then a greater than token.

  -P1_test4.fs18 contains a list of tokens, some separated by whitespace
    and others not separated by whitespace, as well as comments, which
    may be comments on one line or comments on multiple lines. This
    tests whether the scanner can still parse tokens with comments
    in between tokens or comments spanning multiple lines.
    **ADDITIONALLY, this file also contains invalid characters such as
    '?', '!', and the scanner reads these characters and sends an error
    to the output and then terminates.

  -P1_test5.fs18 contains a list of tokens and comments with no whitespace.
    This tests whether the scanner can parse comments and tokens with
    no whitespace separation.

  -P1_test6.fs18 is empty, and should return EOFtk.

Information on scanner implemntation:
   I used the third option suggested, which is an FSA table + driver.

  -In main.cpp, after verifying the file exists and can be opened, the
    main() function calls testScanner(), which in turn calls scanner()
    for each line in the input file. testScanner() does not return
    until the EOF is reached.

   The FSA table is defined in the scanner.h file.
   The driver function, testScanner(), is implemented in testScanner.cpp.
     -The filter() function removes comments and extra whitespace and
      constructs a string of characters to pass to the scanner() in
      scanner.cpp. 

     -The filter() function is implemented in testScanner.cpp,
      and testScanner() first calls the filter function, gets the
      stripped string, and then passes this string to scanner().

     -The scanner() will then identify the token using the state transition
      table, and if the table does not return an error state, the scanner()
      will return the token to testScanner(). Then, testScanner() will
      display the line number, token name, and token description of
      the token.
     
     -For each line of input from the file (or redirected file or keyboard),
      the testScanner() will do the above until the EOF is reached.

