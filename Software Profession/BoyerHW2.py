#Casey Boyer, 08/29/18, CS4500: Introduction to the Software Profession
#BoyerHW2 Version 1.0
#
#Using Python 3.7.0 and the Python 3.7.0 Shell to compile and test program
#
#***To run the game, run the 'BoyerHW2.py' module, NOT the other modules***
#   (pyramid.py, node.py, constants.py).
#
#***HOWEVER, 'BoyerHW2.py', 'pyramid.py', 'node.py', and 'constants.py'
#   must ALL be in the SAME folder/parent directory or the program will NOT
#   run correctly!***
#
#The purpose of this software is to emulate the 'Pyramid' of numbers game,
#where the user is presented with a Pyramid of 21 values and begins
#at the top of the pyramid, on the value '1'. For each value in the pyramid,
#there is are 'dots' associated with the value, where the number of dots is
#incremented everytime this value is visited. The goal of the game is for
#the user to traverse all values in the pyramid at least once, where the user
#may only go in the up left, up right, down left, or down right direction
#when on a value in the pyramid. To do so, a four-sided die is rolled everytime
#the user arrives at a value, and the die will continue to be rolled until
#all values have been visited. The four-sided die has the values
#'UL' (up left), 'UR' (up right), 'DL' (down left), and 'DR' (down right).
#So, when the user begins at '1', they may go down left to 2 or
#down right to 3, but they may not go up left or up right, since no value
#in these directions exist; similarly, the user cannot go from the value
#'1' to '5' in one dice roll, as this move is not possible within the
#four allowed directions. If the die rolls a direction that does not exist
#relative to a value (i.e., rolling UL when at the value 2), this counts
#as a move, and the number of dots is increased at this value.
#Everytime the user makes a move, the value of the current position is
#recorded to both the screen and the output file.
#When the user finally visits all 21 values, the game ends and the statistics
#(total number of moves, average number of dots per value, and largest number
#of dots at a value) are displayed to the screen and written to the output
#file.
#**The output file that will be either created or overwritten is named
#'HW2boyerOutfile.txt', and will be created in the same folder as BoyerHW2.py**
#
#**EXTERNAL FILES:**
#   IMPORTANT NOTE: constants.py, HW2Functions.py, node.py, and pyramid.py
#   MUST be in the same folder/directory as BoyerHW2.py, or the program
#   may not execute correctly!
#
#   1. constants.py: This contains a simple list of constant definitions, such
#   as the numerical values of the UL, UR, DL, DR directions, the game
#   description, and so on
#
#   2. node.py: This file contains the class definition for the Node class,
#   and the Node object(s) represent the individual values within the pyramid.
#
#   3. pyramid.py: This file contains the class definition for the Pyramid
#   class, which contains a list() of Node objects and performs the dice
#   rolling, traversal throughout the pyramid, updating the dots per
#   node and overall nodes, and calculating statistics.
#
#   4. random.py (in Pyramid.py): This is the random module from the Python
#   standard library. The pyramid class uses this to generate a uniform
#   integer from 0-3 inclusive, where 0-3 represent the UL, UR,
#   DL, DR directions.
#
#   5. signal.py: This is the signal module from the Python standard library.
#   This is used to set a signal handler function for the SIGINT signal
#   (CTRL + C/Keyboard interrupt) if the user wishes to halt execution during
#   the program.
#
#   6. sys.py: This is the system module from the Python standard library.
#   This is used to terminate the program when the program ends, when a
#   keyboard interrupt (SIGINT signal) is received, or when open() fails
#   to create or overwrite the output text file for writing.
#
#Sources used:
#
#https://docs.python.org/3/library/random.html
#   This source is the Python 3 documentation for the random module,
#   and was used for the arguments and return value of the randint()
#   method, which is used when rolling the die for a specific direction.
#
#https://docs.python.org/3/library/signal.html#signal.signal
#   This source is the Python 3 documentation for catching signals,
#   which was used to create a handler function for the SIGINT keyboard
#   interruption signal, if the user desires to halt the program.
#
#https://docs.python.org/3/tutorial/floatingpoint.html
#   This source is the Python 3 documentation for floating point values,
#   which was used to truncate the 'average number of dots' to 3 decimal
#   points with the format() method.
#
#https://www.programiz.com/python-programming/variables-constants-literals#constants
#   This source provided several examples on how to define 'constants' in
#   Python. This source was used to define a separate constants file, where
#   only variable definitions exist and can be used across all modules for HW2.
#
#https://www.programiz.com/python-programming/methods/list/clear
#   This source provides simple documentation about Python's lists' clear()
#   method. This was used to clear all elements in a list without deleting
#   it when the maximum number of dots on a Node was updated.
#
#https://www.pythoncentral.io/pythons-time-sleep-pause-wait-sleep-stop-your-code/
#   This source provides examples and documentation on Python's sleep()
#   method. This source was used in order to pause execution between
#   die rolls(), if the user desired.
#https://www.tutorialspoint.com/python3/python_classes_objects.htm
#   This source describes in moderate detail, with examples, how OOP works
#   in Python. This was used to correctly create the Node and Pyramid classes,
#   and the notation for accessing class attributes as well as initializing or
#   modifying class attributes. This also helped me understand how to create
#   instances of a class and how to access class methods from separate modules.
#
#https://www.geeksforgeeks.org/print-without-newline-python/
#   This source describes how to print output on the same line
#   using the print() method in Python 3. This source was used to print each
#   node to the same line on the screen, since print() by default prints
#   to a new line on the screen each time.
#
#https://docs.python.org/3/library/functions.html#open AND
#https://www.tutorialspoint.com/python3/python_files_io.htm
#   Both sources were used to open and/or create files for writing,
#   and for identifying the exception thrown when open() fails
#
#https://docs.python.org/3/tutorial/errors.html AND
#https://www.tutorialspoint.com/python3/python_exceptions.htm
#   Both sources were used to handle exceptions (used for when the open()
#   method throws an OSError exception)
#
#https://docs.python.org/3/library/sys.html#sys.exit
#   This source is used to terminate program execution with sys.exit(),
#   when the open() method fails to create or overwrite the output text
#   file for writing, when a SIGINT signal is received, or when the
#   program ends.
import signal
import sys
import node
import pyramid
import constants

#This is the handler function for the SIGINT signal (the CTRL+C/Keyboard
#interrupt signal). This handler prints to both the console and output
#file that the signal was received, then closes the output file and
#terminates the program.
def handler(signum, frame):
    print(constants.SIGINT_MESSAGE)
    output_file.write(constants.SIGINT_MESSAGE)
    output_file.close()
    sys.exit()
    
#Open (create or overwrite) the output file for writing ("w").
#If the open() function cannot create the output file or
#overwrite the existing one, then attempt to catch the
#exception thrown, print this to the screen, and terminate the program.
try:
    output_file = open(constants.OUTPUT_FILE_NAME, "w")
except OSError as e:
    print("OSError: File could not be created or overwritten for writing:\n", \
          e)
    sys.exit()
except:
    print("Unexpected error attempting to create output file for writing.")
    sys.exit()

#This method sets the handler for the SIGINT signal to the function
#named handler.
signal.signal(signal.SIGINT, handler)

#Create the pyramid
pyramid = pyramid.Pyramid(constants.MAX_NODES)

#Print the game description to the output file and console,
#and print the pyramid to the output file.
print(constants.GAME_DESCRIPTION)
output_file.write(constants.GAME_DESCRIPTION + "\n")

#Continue rolling the die and traversing the pyramid until all
#nodes in the pyramid have been visited.
while pyramid.visited_nodes < constants.MAX_NODES:
    #During each die roll, print to both the screen and the output
    #text file the number where the game is at the moment, followed by
    #a comma
    output_str = str(pyramid.getCurrentNodeValue())

    #Set the delimiter as a comma so that each
    #number printed to the screen is on the same line
    print(output_str, end = ",")

    #Write the node value followed by a comma to the output file.
    output_file.write(output_str + ",")

    #Roll the die and traverse the pyramid; in the rollDice() method,
    #the total number of dots and total number of moves are incremented
    #regardless if the move results in a new Node, SO the last move
    #before the game ends is recorded.
    pyramid.rollDice(output_file)

#Print the last node to the screen and output file that the pyramid
#ended on, followed by a period.
output_str = str(pyramid.getCurrentNodeValue()) + ".\n"    
print(output_str)
output_file.write(output_str)

#Print to the output file and screen the total number of moves (dots),
#the average number of dots, and the node with the largest number
#of dots
pyramid.displayStats(output_file)

#Close the output file
output_file.close()

#Terminate the program
sys.exit()
