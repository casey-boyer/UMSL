CS4280-001: Casey Boyer, P3

***Using the 'local' option for variables where:**
	-variables outsie of a block are global
	-variables in a given block are scoped in this block
	-Rules as in C (smaller scope hides the outer/global scope variable)

Invocation: ./statSem [filename]
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
