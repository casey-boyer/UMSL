CS4280-001: Casey Boyer, P4

***Using the 'local' option for storage allocation of variables where:**
	-variables outsie of a block are global
	-variables in a given block are scoped in this block
	-Rules as in C (smaller scope hides the outer/global scope variable)

Invocation: ./comp [filename]
	where
          -[filename] has either the explicit '.fs18' extension or is implicit
	  -[filename] is an existing file
	  -[filename] is a file's contents redirected from the keyboard
	  -[filename] is the keyboard itself (cin)

As from P1, the scanner was implemented using an FSA table and driver.

Producing output:
  To print the preorder traversal of the tree, simply uncomment the
  line 'preorder(root, root->level)' in main.cpp. This will invoke
  the preorder() function that is defined in testTree.cpp.

Testing:
  -All testing was done using the test files provided for P4.

  -P4_test1.fs18 will print the literal 11

  -P4_test2.fs18 will print 11 after being assigned to a variable

  -P4_test3.fs18 will get input from the user, and write it to the output.

  -P4_test4.fs18 will evaluate an expression using +- and /* operators
    and print the value to the screen.

  -P4_test5.fs18 will print the negation of the value 5, after it is
    negated 3 times.

  -P4_test6.fs18 will print 1 four times, each within a new block.

  -P4_test7.fs18 will read input into a variable from the user, and print
    the value to the screen if it is greater than 0.

  -P4_test8.fs18 will read input from the user, and then print the value
    to the screen only if the negation of the input is nonzero.

  -P4_test9.fs18 will read input from the user, and will print it to the
    screen if and only if the input is between 1..10 (inclusive).

  -P4_test10.fs18 will read input from the user, and will print the
    input value -2 repeatedly until the value is 1 or 2 based on the
    input being odd or even.

  -P4_test11.fs18 will read input from the user, and enter a nested loop
    that will print the input from -1...0 and then the outer loop will
    print the input from -1...0.

  -P4_local.fs18 will read in 4 inputs from the user, in different
   scopes with the same identifier names, and then print them in
   reverse.

Notes:
  -All variables have an initial value of 0. 
    An assign statement will modify the variable value.
    A read statement will also modify the variable value.

Testing (for local variables from P3):
  -For a program that attempts to access a variable that has not been
   defined, an error is printed and the program terminates.

  -For a program that attempts to define an identifier with the same
   name as an identifier in the same scope, an error is printed and
   the program terminates.

  -For a program that defines multiple identifiers with the same name,
   all in different scopes, there is no error.

  -The scoping of variables is assigned every time a <block> production
   is encountered in the parse tree. So, there may be multiple nested
   blocks that define variables with the same name as previous blocks.
   This also means that the <stat> production that follows the 
   <if> and <loop> productions can be a <block>, and variables will be
   scoped in this block.

  -Any variables defined in the <vars> production of <program> are global.
   Any variables defined in the subsequent <block> production(s) are local
   to that block.
