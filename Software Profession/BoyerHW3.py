#Casey Boyer, 09/10/18, CS4500: Introduction to the Software Profession
#BoyerHW3 Version 1.0
#
#Using Python 3.7.0 and the Python 3.7.0 Shell to compile and test program
#
#***To run the game, run the 'BoyerHW3.py' module***
#
#The purpose of this software is to emulate the 'Pyramid' of numbers game,
#where the user is presented with a Pyramid of 3-325 values (2-25 levels)
#and begins at the top of the pyramid, on the value '1'.
#The user will choose how many levels are in the pyramid, from a range of
#2-25 (inclusive), and will also choose how many times an individual
#pyramid game will run, from a range of 10 to 50 (inclusive).
#For each value in the pyramid, there is are 'dots' associated with the value,
#where the number of dots is incremented everytime this value is visited.
#The goal of the game is for the user to traverse all values in the pyramid
#at least once, where the user may only go in the up left, up right,
#down left, or down right direction when on a value in the pyramid.
#To do so, a four-sided die is rolled everytime
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
#When the user finally visits all values in the pyramid, the game ends and
#the statistics (total number of moves, average number of dots per value,
#and largest number of dots at a value) are displayed to the screen and
#written to the output file. This process will continue until the
#amount of simulated games, specified by the user, have been completed.
#Once all games are completed, the statistics for all games are written
#to the screen and output file; the statistics are as follows:
#   1. The overall average number of moves required to finish T simulated games
#   2. The minimum and maximum number of moves in the T simulated games
#   3. From the maximum number of dots in each game, the minimum maximum,
#   the average maximum, and the maximum maxium are provided.
#
#**The output file that will be either created or overwritten is named
#'HW3boyerOutfile.txt', and will be created in the same folder as BoyerHW3.py**
#
#**EXTERNAL FILES:**
#   1. random.py (in Pyramid.py): This is the random module from the Python
#   standard library. The pyramid class uses this to generate a uniform
#   integer from 0-3 inclusive, where 0-3 represent the UL, UR,
#   DL, DR directions.
#
#   2. signal.py: This is the signal module from the Python standard library.
#   This is used to set a signal handler function for the SIGINT signal
#   (CTRL + C/Keyboard interrupt) if the user wishes to halt execution during
#   the program.
#
#   3. sys.py: This is the system module from the Python standard library.
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
#   Python.
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
#
#https://docs.python.org/3/library/stdtypes.html
#   This source was used to convert a value to an int in a specific base.
#   Specifically, this source was used for the int(x, base) method.
#
#https://docs.python.org/3/tutorial/controlflow.html#default-argument-values
#   This source is documentation about control flows in Python 3.7.0.
#   This source was used to understand how to implement default function
#   arguments in Python (which is used in the get_input()
#   and print_node_value() function).
#
#https://www.w3schools.com/python/python_ref_string.asp
#   This source is documentation on various Python string methods. This
#   source was specifically used for the upper() method, which converts
#   a string to uppercase; this method is used in the get_input() method.
#
#https://stackoverflow.com/questions/10712002/create-an-empty-list-in-python-with-certain-size
#   This source details how to create an empty list, that is, specify a
#   list with size n elements without assigning any values to the list
#   indices. This is used to create an inital empty list that will contain
#   the total number of moves of each game.
import random
import signal
import sys

#The following is a simple declaration of constant variables
#that will be used repeatedly throughout the program
#and will NOT be modified.

#This will be used to test if the user input, an integer, is of
#base 10; that is, NOT an octal, hex, binary value, etc.
DECIMAL_BASE = 10

#The minumum and maximum number (inclusive) of how many levels will
#be in the pyramid.
MINIMUM_LEVELS = 2
MAXIMUM_LEVELS = 25

#The minimum or maximum number (inclusive) that represents how many
#times the game will be simulated.
MINIMUM_GAMES = 10
MAXIMUM_GAMES = 50

#These values represent the user's choice, 'yes' or 'no, for
#printing each node value, delimited by a commma, to the screen.
USER_PRINT_TRUE = "Y"
USER_PRINT_FALSE = "N"

#The numerical values representing each direction
UP_LEFT = 0
UP_RIGHT = 1
BOTTOM_LEFT = 2
BOTTOM_RIGHT = 3

#The name of the output file
OUTPUT_FILE_NAME = "HW3boyerOutfile.txt"

#The description of the game
GAME_DESCRIPTION = "In this game, there is a pyramid of 21 values (see output file). " + \
                   "The game will start at the top of the pyramid, where the value is 1. " +\
                   "\nThe ultimate point of the game is to visit all of the values on the pyramid. " + \
                   "\nThis is done by rolling a 4-sided die, with the labels UL (up left), UR " + \
                   "(up right), DL (down left), and DR (down right). \nWhen the die lands on one " + \
                   "of these sides, the user will move from the current value to the value in " + \
                   "this direction.\n So, if the user is at the value 5, and the die rolls an " + \
                   "up left direction, the user will move to the value 2. \nEach value in the " + \
                   "pyramid has a number of dots, where the dots of the value denote how many " + \
                   "times this value was visited. \nSince the user begins at the value 1, the " + \
                   "game will begin with 1 dot at the value 1. \nIf the die rolls a direction " + \
                   "that is not a possible move from the current value, then the user will " + \
                   "stay at the current value, \nand the number of dots on this value will " + \
                   "be incremented. So, if the user is at value 7 and 7 has 2 dots, and " + \
                   "the die rolls an up left direction, \nthe user stays at the value 7 and " + \
                   "the number of dots at this value is updated to 3.\nThe game will end " + \
                   "when all values in the pyramid have been visited.\n"

#The message that will be printed to the screen and written to the output
#file when the a SIGINT signal is received (when the user hits CTRL+C /
#triggers a keyboard interrupt
SIGINT_MESSAGE = "\n\nKeyboard interrupt recieved. Closing the output file " + \
                 "and terminating the program."

#This message will be displayed to the user when gathering the input value
#for the number of pyramid levels.
LEVELS_MESSAGE = "Please provide an integer, between " + \
                 str(MINIMUM_LEVELS) +  " and " + str(MAXIMUM_LEVELS) + \
                 " inclusive, for the number of pyramid levels."

#This message will be displayed to the user when prompting for how many times
#the game should run.
GAMES_MESSAGE = "Please provided an integer, between " + \
                str(MINIMUM_GAMES) + " and " + str(MAXIMUM_GAMES) + \
                " inclusive, for how many times the game will run."

#This message will be displayed to the screen when asking the user
#whether they would like the node values, delimited by a comma, printed
#to the screen as the game runs.
PRINT_MESSAGE = "Would you like to see the sequence of visted nodes " + \
                "printed to the screen and output file? Enter 'Y' for " + \
                "yes, 'N' for no: "

#This message will be displayed to the user when they incorrectly provide
#an input value.
INPUT_ERROR = "Incorrect input."

#This is the Node class definition. A Node object represents a single value
#in the pyramid. The Node definition is separate from the Pyramid definition
#so that each Node may have its own attributes to uniquely identify it,
#such as its location in the pyramid (in x,y coordinates or by index), the
#indices of the Nodes in the UL, UR, DL, DR directions relative to a
#single Node, and the number of dots at a particular Node.
class Node:
    'This is the Node class definition, where a Node is a single value'
    'within the pyramid; an individual Node object has a numerical'
    'value, a number of dots, an index of its location in the Pyramid'
    'list() attribute, indices of the Nodes surrounding it in the UL, UR'
    'DL, DR directions, and whether or not this Node has been visited before.'

    #The initialization of a Node object requires the numerical value of the
    #node, and its index in the list of Node objects (in the Pyramid).
    #The value attribute of the Node is its numerical value.
    #The num_dots attribute of the Node is the number of times the node was
    #'visited' in the pyramid.
    #The is_visited attribute is a Boolean value that indicates whether
    #this Node has been visted before (if the Pyramid has traversed to this
    #Node previously or if this is the first time the pyramid has
    #moved to this Node). This attribute is especially important as it will
    #help indicate when all Nodes in the pyramid have been visited, and
    #thus when the game should end.
    #The index attribute of the Node is the index of the Node in the list of
    #Node objects.
    #The up_left, up_right, bottom_left, bottom_right attributes of the Node
    #are the indices of Nodes in the Nodes object list, where each attribute
    #represents the Node in this direction in relation to this Node. These are
    #initially set to -1.
    def __init__(self, value, index):
        self.value = value
        self.num_dots = 0
        self.is_visited = False
        self.index = index
        self.up_left = -1
        self.up_right = -1
        self.bottom_left = -1
        self.bottom_right = -1

    #The level of a Node is the level of the Node in the pyramid, where
    #the levels begin at 1. So, the Node with the value '1' is on level 1,
    #the Node with the value '3' is on level 2, the Node with the value '12'
    #is on level 5, and so on.
    def setLevel(self, level):
        self.level = level

    #These are the x and y 'coordinates' of a Node in the pyramid, where
    #the x coordinate is the level in the pyramid the Node is on, and the
    #y coordinate is the position of this Node in that level.
    def setCoordinates(self, x_coordinate, y_coordinate):
        self.x_coordinate = x_coordinate
        self.y_coordinate = y_coordinate

    #Return the index of the Node at the specified direction
    def getNodeAtDirection(self, direction):
        if direction == UP_LEFT:
            return self.up_left
        elif direction == UP_RIGHT:
            return self.up_right
        elif direction == BOTTOM_LEFT:
            return self.bottom_left
        elif direction == BOTTOM_RIGHT:
            return self.bottom_right

    #Update the number of dots for this Node.
    def updateDots(self):
        self.num_dots += 1

    #Update the status of whether the Node was visited or not.
    def updateVisited(self):
        self.is_visited = True

#The class for the pyramid game, which represents the pramid of nodes.
#The most important attribute of this class is the nodes_list list() of
#Node objects, which contains the 21 Nodes. For every Node in this list,
#the Pyramid class initializes the level, position in x and y coordinates,
#and indices of the Node(s) to the up left, up right, bottom left, and
#bottom right of a particular Node. The class also keeps track of the total
#number of dots across all Nodes and the largest number of dots out of all Nodes.
#The class also performs the primary game mechanism, rolling the dice and
#traversing through the pyramid according to the die direction.
class Pyramid:
    'This class represents the pyramid of nodes.'

    #To initialize a Pyramid object, the maximum number of nodes must be given.
    #The nodes_list attribute of the Pyramid is the list of Node objects
    #representing an individual Node in the pyramid. The init()
    #method also initializes the level and coordinates of each Node in the
    #Pyramid, which is used to identify the surrounding Nodes when traversing
    #the Pyramid.
    def __init__(self, max_nodes, max_levels):
        #Initialize the maximum number of nodes, the nodes_list for each
        #Node object, the total dots across all Nodes,
        #the total moves across the pyramid (this value is always 1 less than
        #the total number of dots, since starting at Node 1 does not
        #count as a move) the number of visited
        #Nodes, the index of the Node with the most dots, the list of the
        #values of the Nodes with the most dots (if two Nodes have the same
        #number of dots and are the largest number of dots), the current
        #index (position) of the Node in the pyramid, and the number of
        #simulated Pyramid games to be completed.
        self.max_nodes = max_nodes
        self.max_levels = max_levels
        self.nodes_list = list()
        self.total_dots = 0
        self.total_moves = 0
        self.visited_nodes = 0
        self.max_dots_index = 0
        self.current_index = 0
        self.number_games = 0

        #Create a list of integers that will be used for populating the
        #value of each Node object in the nodes_list list.
        values_list = [0] * self.max_nodes
        values_index = 0

        #Populate the nodes_list list with Node objects, initializing
        #each Node with its numerical value in the pyramid and its index
        #in the nodes_list list
        index = 0
        while index < max_nodes:
            self.nodes_list.insert(index, Node((index + 1), index))
            index += 1

        index = 0
        counter = 0
        level = 1

        #Set the total number of dots and visited nodes, since the Pyramid
        #will always start at 1
        self.setInitialDots()

        #Assign the level of each node, as well as its coordinates
        #in the pyramid. The level and coordinates of a node will be used
        #to identify whether a Node exists in any of the 4 directions
        #(UL, UR, DL, DR) relative to it.
        while index < len(self.nodes_list):
            #Reassign the counter value to the index position
            counter = index

            #The current position (y-axis value) in the pyramid at the
            #current level
            position = 1

            #As long as we are out of the for loop, the number of nodes for
            #the next level has not yet been established
            num_nodes_level = 0

            #Assign the level and coordinates of each Node at a given
            #level in a pyramid, until the last Node on a level is reached.
            for nodes in self.nodes_list:

                if counter < self.max_nodes:
                    #Assign the node at this level the current level value
                    self.nodes_list[counter].setLevel(level)

                    #Assign the current node at this level the x and y coordinates
                    self.nodes_list[counter].setCoordinates(level, position)

                    #Increment the number of nodes on this level
                    num_nodes_level += 1

                    #Increment the current position on this level
                    position += 1

                    #Increment the counter to go to the next node
                    counter += 1

                #If the number of nodes on this level is equal to the level,
                #then this level is 'full', go to the next level
                if num_nodes_level == level:
                    level += 1
                    position = 1
                    break

            #Reassign the index to the counter position so when assigning the
            #level to a node, start off from the end of the last level; this
            #prevents assigning the incorrect level to the next node in the list
            index = counter

        #Set the up right, up left, bottom right, and bottom left indices
        #of each Node object in the list.
        self.setDirections()

    #Since the game starts at the Node with value 1, this Node will have a
    #dot when the game begins. So, update the total number of dots at Node
    #1 and across all Nodes in the pyramid, and the number of visited Nodes.
    def setInitialDots(self):
        self.nodes_list[self.current_index].updateDots()
        self.nodes_list[self.current_index].updateVisited()
        self.updateTotalDots()
        self.updateVisitedNodes()

    #This method 'resets' the Pyramid after a game ends and before
    #another begins. This involves resetting the total dots, moves,
    #and visited nodes, and starting the Pyramid at the first Node (1).
    def reset(self):
        #Reset the total number of dots, moves, and visited nodes to 0
        #since the Pyramid is starting at Node 1.
        self.total_dots = 0
        self.total_moves = 0
        self.visited_nodes = 0

        #Reset the index with the maximum number of dots to 0 since
        #no Nodes have been traversed yet, and reset the current index
        #to 0 since the Pyramid will re-begin at Node 1.
        self.max_dots_index = 0
        self.current_index = 0

        #If the Pyramid is being reset, then a game has been completed,
        #so increment the number of games.
        self.number_games += 1

        #For each Node in the Pyramid, mark it as unvisited and
        #reset the dots to 0, since the Pyramid will begin at 1.
        index = 0
        while index < len(self.nodes_list):
            self.nodes_list[index].is_visited = False
            self.nodes_list[index].num_dots = 0
            index += 1

        #Set the total number of dots and visited nodes, since the Pyramid
        #will always start at 1
        self.setInitialDots()

    #Update the number of dots across all Nodes in the Pyramid.
    def updateTotalDots(self):
        self.total_dots += 1

    #Update the number of moves across all Nodes in the Pyramid.
    def updateTotalMoves(self):
        self.total_moves += 1

    #Return the number of visited Nodes in the Pyramid.
    def updateVisitedNodes(self):
        self.visited_nodes += 1

    #Return the total amount of dots across all Nodes.
    def getTotalDots(self):
        return self.total_dots

    #Return the total amount of Nodes visited thus far in the Pyramid.
    def getVisitedNodes(self):
        return self.visited_nodes

    #Return the numerical value of the Node at the current index (position)
    #in the Pyramid
    def getCurrentNodeValue(self):
        return self.nodes_list[self.current_index].value

    #Return the number of dots of the Node at the current index (position)
    #in the Pyramid
    def getCurrentNodeDots(self):
        return self.nodes_list[self.current_index].num_dots

    #For each Node in the Pyramid (nodes_list), the surrounding Nodes
    #in the UL, UR, DL, DR directions need to be identified to correctly
    #traverse the pyramid. This method initializes the index
    #(in the nodes_list list)of each Node in the UL, UR, DL, DR
    #directions of a given Node. This is done by identifying if the Node
    #is on the left or right edge of a level in the Pyramid, or at the top
    #or bottom of the pyramid; a Node on the left edge of the pyramid will
    #have a y coordinate value of 1, a Node on the right edge of the pyramid
    #will have a y coordinate value equal to the Node's level, a Node at the
    #top of the pyramid will be in position (1, 1), and a Node at the bottom
    #of the pyramid will have an x coordinate value equal to the maximum level.
    #If it is not in any of these positions,
    #then the UL, UR, DL, DR Node indices in relation to the current Node
    #are found using the current Node's level (x coordinate) and index in
    #the node_list.
    #If any UL, UR, DL, DR Node indices are -1, this indicates that no
    #such Node in this direction exists.
    def setDirections(self):
        index = 0

        #The first_pos is 1 because the y coordinate of a Node object
        #in the first position at any level of the pyramid is always 1.
        #Also retrieve the maximum number of levels applicable in the
        #Pyramid.
        first_pos = 1
        max_level = self.max_levels

        #The Node to the UL of the current Node will be the current
        #Nodes x and y coordinates minus 1. I.e. The Node 8 at (4, 2)
        #will have a Node in the UL direction at (3, 1).
        #Similarly, the Node to the UR of the current Node will be
        #the current Node's x minus 1.
        up_left_x = -1
        up_left_y = -1
        up_right_x = -1

        #Check if the current Node is a right or left edge Node,
        #and/or at the top of or bottom of the pyramid. Otherwise,
        #use the index and value of the current level of the Node
        #to identify the index of the surrounding Nodes.
        while index < len(self.nodes_list):
            #Get the x and y coordinates of the current Node, as well
            #as the level of the current Node.
            current_node_x = self.nodes_list[index].x_coordinate
            current_node_y = self.nodes_list[index].y_coordinate
            current_node_level = self.nodes_list[index].level

            #Assign the x,y coordinates of the Node to the UL direction
            #and the x coordinate of the Node to the UR direction.
            ul_x = current_node_x + up_left_x
            ul_y = current_node_y + up_left_y
            ur_x = current_node_x + up_right_x

            #If the node is on the left edge of the pyramid, cannot
            #go to the upper left; this Node will be the first Node
            #in a level, or have a y coordinate of 1.
            if current_node_y == first_pos:
                self.nodes_list[index].up_left = -1

            #If the node is on the right edge of the pyramid, cannot go
            #to the upper right; this Node will be the last Node in a level,
            #or have a y coordinate equal to the current level.
            if current_node_y == current_node_level:
                self.nodes_list[index].up_right = -1
                is_edge = True
            else:
                is_edge = False

            #If the node is on the bottom of the pyramid, then its
            #x coordinate is equal to the maximum number of levels in
            #the pyramid. Thus, there are no DL or DR Nodes.
            if current_node_x == max_level:
                self.nodes_list[index].bottom_left = -1
                self.nodes_list[index].bottom_right = -1
                is_bottom = True
            else:
                is_bottom = False

            #If the x and y coordinates of the current node minus 1
            #are greater than 0, an up left value of this node exists.
            if ul_x > 0 and ul_y > 0:
                #The up left node value relative to this node is the
                #index of the current node minus the current node's x coordinate
                #(the level it is on)
                self.nodes_list[index].up_left = \
                    self.nodes_list[(index - current_node_x)].index

            #The up right coordinate is the current level of the node minus 1,
            #and then this value subtracted from the current node's index
            if ur_x > 0 and is_edge == False:
                self.nodes_list[index].up_right = \
                    self.nodes_list[(index - (current_node_level - 1))].index

            #The bottom left coordinate is the current level added to the index
            #of the current node. The bottom right coordinate is the current
            #level plus one, then adding this value to the node of the
            #current index
            if is_bottom == False:
                self.nodes_list[index].bottom_left = \
                    self.nodes_list[(index + current_node_level)].index
                self.nodes_list[index].bottom_right = \
                    self.nodes_list[(index + (current_node_level + 1))].index

            #Increment the index to go to the next node in the list
            index += 1

    #Display the statistics at the end of the game. This method accepts the
    #output file as a parameter, so that the statistics may be written
    #to the screen AND the output file. The statistics include the total
    #number of moves across all Nodes, the average number of dots for each
    #Node, and the Node with the largest amount of dots.
    def displayStats(self, output_file):
        #Indicate to the user in both the output file and to the screen
        #that the statistics will be displayed
        output_str = "\t\tGAME #" + str(self.number_games + 1) + " STATISTICS:"
        print(output_str)
        output_file.write("\n" + output_str + "\n")

        #Print the total number of moves to the output file and
        #screen
        output_str = "\nTotal number of moves: " + str(self.total_moves)
        print(output_str)
        output_file.write(output_str)

        #Get the average number of dots. The average number of dots is the
        #total number of dots across all nodes divided by the number
        #of nodes (21). The format() method with the '.3f' argument
        #returns an integer with 3 decimal points.
        avg_num_dots = format((self.total_dots / self.max_nodes), '.3f')

        #Print the average number of dots to the output file and screen
        output_str = "\nAverage number of dots: " + str(avg_num_dots)
        print(output_str)
        output_file.write(output_str)

        #Print the dot size associated with the Node that has the
        #largest number of dots to the screen and output file.
        output_str = "\nThe largest number of dots on any node: " + \
                     str(self.nodes_list[self.max_dots_index].num_dots) + \
                     "\n"
        print(output_str)
        output_file.write(output_str)

    #This method performs the rolling of the die to traverse the pyramid.
    #The method accepts the output file as a parameter, so that the
    #direction and new Node may be recorded in the output file.
    #The method will roll the die by generating a random number between
    #0-3 inclusive, where 0 represents UL, 1 represents UR, 2 represents DL,
    #and 3 represents DR.
    #Then, the method will invoke the current Node's getNodeAtDirection()
    #method to get the index of the Node in the specified direction. If this
    #Node exists, the current index of the nodes_list is updated to this new
    #Node; if not, then the current index remains the same. In either situation,
    #the number of dots at the new (or same) Node is incremented, as well
    #as the total number of dots in the game. If the new Node has not yet
    #been vistied, the number of nodes visited is incremented.
    #Additionally, the index of the Node with the largest amount of dots is
    #updated if the new Node is greater.
    def rollDice(self, output_file):
        #Get a random integer between 0-3 inclusive to represent
        #the direction to traverse the pyramid in.
        direction = random.randint(UP_LEFT, BOTTOM_RIGHT)

        #Get the index of the Node at the direction rolled by the die.
        temp_index = self.nodes_list[self.current_index].getNodeAtDirection(direction)

        #The total number of dots should always be updated, even if the die
        #roll does not result in a possible move.
        self.updateTotalDots()

        #The total number of moves should be updated EVERY time the die is
        #rolled, even if the move results in staying at the same Node.
        self.updateTotalMoves()

        if temp_index == -1:
            #If the index of the Node at the direction rolled by the die is
            #-1, then this direction at the current node is not possible.
            #Stay at this node, but update the number of dots at this Node.
            self.nodes_list[self.current_index].updateDots()
        else:
            #Otherwise, update the index to reflect the position of the new Node,
            #and update the number of dots at this Node.
            self.current_index = temp_index
            self.nodes_list[self.current_index].updateDots()

            #Compare the number of dots at the new node to the node
            #with the largest number of dots to see if the new node
            #has a larger number of dots.
            self.compareDotSize(self.current_index)
            
            #If the new Node has not been visited before, update the
            #number of visited nodes and mark this Node as visited.
            if self.nodes_list[self.current_index].is_visited == False:
                self.nodes_list[self.current_index].is_visited = True
                self.updateVisitedNodes()

    #When moving to a new node, this method will compare the number of dots
    #at the new node to the number of dots at the node which currently
    #has the largest number of dots. If the new node has a larger number
    #of dots, the index of the node with the largest number of dots is
    #updated to this one.
    def compareDotSize(self, new_index):
        if self.nodes_list[new_index].num_dots > \
           self.nodes_list[self.max_dots_index].num_dots:
            #Update the largest number of dots index to the index of the
            #new node
            self.max_dots_index = new_index

    #Return the node with the largest number of dots
    def getLargestDotSize(self):
        return self.nodes_list[self.max_dots_index].value

#The game statistics class collects data from each simulated game run,
#and provides statistics of all the games once the simulated T games
#have finished. This includes :
    #adding the total moves from each to a sum to get the total moves
    #across T simulated games, and using this value to calculate
    #the average number of overall moves;
    #
    #adding the maximum dots from each to a sum to get the total number
    #of maximum dots across T simulated games, and using this value
    #to calculate the average number of maximum dots;
    #
    #inserting the total moves from each game and the maximum dots
    #from each game into two separate lists, in order to retrieve the
    #maximum and minimum number of moves of all games, and the
    #maximum maximum dots, the minimum maximum dots, and the average
    #maximum dots of all games
class GameStatistics:
    'This is the definition of the GameStatistics class. This class'
    'will collect data from each Pyramid game (the total number of moves'
    'in a game, the mininmum and maximum number of moves in the T simulated'
    'games, and from the maximum number of dots in each game, the maximum'\
    'maximum, average maximum, and minimum maximum.'

    def __init__(self, number_games):
        #The number of games to be simulated
        self.number_games = number_games

        #The current number of simulated games
        self.current_game = 0

        #This list, total_moves_per_game, will hold the total number
        #of moves of each finished game. This will be used for comparison
        #purposes (maximum and minimum number of moves of T games).
        self.total_moves_per_game = [None] * number_games

        #This list, max_dots_per_game, will hold the largest number of dots
        #on any Node from each completed game, so that the maximum minimum,
        #average maximum, and minimum maximum largest dots statistics
        #may be printed at the end of the game.
        self.max_dots_per_game = [None] * number_games

        #The total number of moves across all T simulated games (a sum)
        self.total_moves = 0

        #The total amount of maximum dots across all T simulated games
        #(a sum)
        self.total_dots = 0

        #The average number of moves across T simulated games. This
        #will be calculated when all simulations have finished, and the
        #value is the total number of moves across T simulated games
        #divided by T
        self.overall_average_moves = 0

        #The average number of maximum dots across all T simulated games.
        self.average_maximum_dots = 0

        #The index associated with the game with the least amount of
        #maximum dots, and the index associated with the game with the
        #highest amount of maximum dots.
        self.minimum_max_index = 0
        self.maximum_max_index = 0

        #The index, or the game, with the maximum number of moves across
        #all T simulated games.
        self.max_index = 0

        #The index, or the game, with the minimum number of moves across
        #all T simulated games.
        self.min_index = 0

    #Increment the current_game variable to reflect that a game has been
    #completed.
    def updateGame(self):
        self.current_game += 1

    #For each finished game, add the total number of moves from this game
    #to the list holding the total moves to each game.
    #Then, take the value of the total moves from the game with the maximum
    #number of moves and the game with the minimum number of moves and
    #compare this with the game that currently has the maximum number
    #of moves and the minimum number of moves to see if a larger
    #or smaller number of total moves occurred in the recently added game.
    def addGameStats(self, total_moves, total_dots):
        #Add the total_moves from the completed game to the
        #total_moves_per_game list.
        self.total_moves_per_game[self.current_game] = total_moves

        #Add the maximum dots from the completed game to the
        #max_dots_per_game list.
        self.max_dots_per_game[self.current_game] = total_dots

        #Compare the total number of moves of the newly finished game
        #to the total number of moves of the game with the maximum
        #number of moves. If the newly finished game's total moves is
        #greater, update the max_index.
        if self.total_moves_per_game[self.current_game] > \
           self.total_moves_per_game[self.max_index]:
            self.max_index = self.current_game

        #Compare the maximum number of dots of the newly finished game
        #to the maximum number of dots of the game with the current
        #maximum number of dots of all games. If the index of the
        #newly finished game has a larger maximum dot count, then
        #update the maximum dot index.
        if self.max_dots_per_game[self.current_game] > \
           self.max_dots_per_game[self.maximum_max_index]:
            self.maximum_max_index = self.current_game

        #Compare the total number of moves of the newly finished game
        #to the total number of moves of the game with the minimum
        #number of moves. If the newly finished game's total moves is
        #lesser, update the min_index.
        if self.total_moves_per_game[self.current_game] < \
           self.total_moves_per_game[self.min_index]:
            self.min_index = self.current_game

        #Compare the maximum number of dots of the newly finished game
        #to the game associated with the minimum count of maximum dots. If
        #the newly finished game has a lesser amount of maximum dots,
        #update the minimum maximum dot index.
        if self.max_dots_per_game[self.current_game] < \
           self.max_dots_per_game[self.minimum_max_index]:
            self.minimum_max_index = self.current_game

    #For each finished game, add the total number of moves from this game
    #to the total number of moves of all the games
    def addTotalMoves(self, total_moves):
        self.total_moves += total_moves

    def addTotalDots(self, total_dots):
        self.total_dots += total_dots

    #Calculate the overall average of total moves across all games, and
    #return this
    def getOverallAverageMoves(self):
        #The overall average total moves across T simulated games is
        #the sum of the total moves across T simulated games divided by T
        #The format() method gives the decimal value 3 decimal points.
        self.overall_average_moves = \
                                   format((self.total_moves / self.number_games), '.3f')
        return self.overall_average_moves

    #Calculate the average number of maximum dots across the maximum dots
    #of all games, and return this
    def getAverageMaximumDots(self):
        #The average number of maximum dots across T simulated games is
        #the sum of the maximum dots from each game, divided by T.
        #The format() method gives the decimal value 3 decimal points.
        self.average_maximum_dots = \
                                  format((self.total_dots / self.number_games), '.3f')
        return self.average_maximum_dots

    #Return the total moves of the game with the maximum number of moves
    def getMaximumMoves(self):
        return self.total_moves_per_game[self.max_index]

    #Return the total moves of the game with the minimum number of moves
    def getMinimumMoves(self):
        return self.total_moves_per_game[self.min_index]

    #Return the maximum maximum number of dots in a game
    def getMaximumMaxDots(self):
        return self.max_dots_per_game[self.maximum_max_index]

    #Return the minimum maximum number of dots in a game
    def getMinimumMaxDots(self):
        return self.max_dots_per_game[self.minimum_max_index]

    #Print the overall average number of moves across T games, the minimum and
    #maximum number of moves in T simulated games, and the maximum maximum dots,
    #average maximum dots, and minimum maximum dots
    def displayStats(self, output_file):
        output_str = "\n\t\tOVERALL STATISTICS:\n"
        print(output_str)
        output_file.write(output_str + "\n")
        
        #Get the overall average number of moves it took to finish the T
        #simulated games
        output_str = "The overall average number of moves it took to finish " + \
                     " the " + str(self.number_games) + " simulated games: " + \
                     str(game_statistics.getOverallAverageMoves())

        #Display the overall average number of moves it took to finish the
        #T simulated games
        print(output_str)
        output_file.write(output_str + "\n")
        
        #Get the maximum number of moves as a string
        output_str = "The maximum number of moves in a game is: " + \
                     str(game_statistics.getMaximumMoves())

        #Display the maximum number of moves in a game
        print(output_str)
        output_file.write(output_str + "\n")

        #Get the minimum number of moves as a string
        output_str = "The minimum number of moves in a game is: " + \
                     str(game_statistics.getMinimumMoves())

        #Display the minimum number of moves in a game
        print(output_str)
        output_file.write(output_str + "\n")

        #Get the maximum number of dots out of all games as a string
        output_str = "The maximum number of maximum dots in a game is " + \
                     str(game_statistics.getMaximumMaxDots())

        #Display the maximum number of dots in a game
        print(output_str)
        output_file.write(output_str + "\n")

        #Get the minimum maximum number of dots out of all games as a string
        output_str = "The minimum number of maximum dots in a game is " + \
                     str(game_statistics.getMinimumMaxDots())

        #Display the minimum maximum number of dots in a game
        print(output_str)
        output_file.write(output_str + "\n")

        #Get the average number of maximum dots across T simulated
        #games a string
        output_str = "The average maximum number of dots across " + \
                     str(self.number_games) + " games: " + \
                     str(game_statistics.getAverageMaximumDots())

        #Display the average number of maximum dots across T simulated games
        print(output_str)
        output_file.write(output_str + "\n")
        
#This is the handler function for the SIGINT signal (the CTRL+C/Keyboard
#interrupt signal). This handler prints to both the console and output
#file that the signal was received, then closes the output file and
#terminates the program.
def handler(signum, frame):
    print(SIGINT_MESSAGE)
    output_file.write(SIGINT_MESSAGE)
    output_file.close()
    sys.exit()

#This method will get input from the user and test whether it is
#acceptable. The method accepts a console_message string that will tell
#the user which input values are acceptable, a boolean isInt variable
#that will decide whether the user input is an integer or not, and
#minimum and maximum integer values that are an inclusive range
#to test the user's integer input. The minimum_val and maximum_val arguments
#are 0 by default, because if an integer input is expected, these will
#be given an explicit value in the method call, otherwise a non-integer
#input is expected.
#If isInt is True:
#   Then, no default arguments are used and the method is testing for
#   an integer value from the user. The minimum_val and maximum_val
#   arguments will be provided in the method call, and these will
#   serve as the (inclusive) range of acceptable integer values.
#   The method will test if the
#   user input is a digit, and if so, if the user input is within
#   the minimum and maximum range (as provided by the minimum_val and
#   maximum_val arguments), and if the integer is a base 10 (decimal whole
#   number) value. If the input does not meet these requirements, then an
#   error message is displayed and the user is again prompted for correct
#   input.
#If isInt is False:
#   The method is called with the default arguments, since an integer
#   value is not being received from the user.
#   Then, the method is testing for a 'Y'/'y' or 'N'/'n' value from
#   the user, where 'Y' represents True, and 'N' represents false.
#   The only acceptable values are (non case-sensitive) 'Y' or 'N'.
#   If the user input is 'Y', true is returned, and if it is
#   'N', false is returned; if it is neither, an error message is displayed
#   and the user is again prompted for correct input.
def get_input(console_message, isInt, minimum_val = 0, maximum_val = 0):
    #Initially assume correct user input is false, so the while loop
    #may execute at least once, or until the user enters the
    #correct input (until correct_user_input is true).
    correct_user_input = False

    #Continue looping for input until the user provides a correct
    #input value.
    while correct_user_input == False:
        #Prompt the user for input, and store the user's input in
        #user_input.
        user_input = input(console_message)

        #If the method call is testing for an integer value
        if isInt:
            #If the user input is a digit
            if user_input.isdigit():
                #Test if the user input, converted to decimal (base 10),
                #is equivalent to the original user input. This will fail
                #if the user enters 09, because 9 will be converted to 9,
                #which will not equal 09. Then, prompt again.
                #Else, test if the user input is less than the minimum value
                #or greater than the maximum value. If not, prompt again.
                if (str(int(user_input, DECIMAL_BASE)) == user_input) and \
                   (int(user_input) <= maximum_val and \
                     int(user_input) >= minimum_val):
                    correct_user_input = True
                    user_value = user_input
                else: #If the user input is not a digit, error print to the screen.
                    print(INPUT_ERROR)
            else: #If the user input is not a digit, error print to the screen.
                print(INPUT_ERROR)
        else:
            #If the user entered 'Y' or 'y' or 'N' or 'n', these are valid
            #inputs (corresponding to yes and no, or true or false).
            #Otherwise, prompt the user for correct input again.
            if user_input.upper() == USER_PRINT_TRUE:
                user_value = True
                correct_user_input = True
            elif user_input.upper() == USER_PRINT_FALSE:
                user_value = False
                correct_user_input = True
            else:
                print(INPUT_ERROR)

    #Return the correct input recieved by the user, which is either
    #an integer value, or a True or False value.
    return user_value

#This method will print the value of each node, followed by a comma, and the
#last node, followed by a period, while the game(s) are running.
#The method accepts the value of the node, a delimiter (which is a comma
#if the game is running, or a period if the game has ended and the
#last node has been reached), a boolean print_to_screen variable which
#if True will print the node value to the screen, and a default isEnd
#variable, which if True, the print() method will behave differently
#(it will have no 'end' parameter, because it will be printing with
#a newline character since the end of the game has been reached).
#If print_to_screen is True, the node value will be printed to the screen
#and output file.
def print_node_value(node_value, delimiter, print_to_screen, isEnd = False):
    #If print to screen is true, print the node value, with the proper
    #delimiter (a comma or period) to both the screen and output file.
    if print_to_screen:
        if isEnd:
            #Print the last node to the screen and output file that the pyramid
            #ended on, followed by a period.
            #Delimiter is ".\n"
            output_str = str(node_value) + delimiter
            print(output_str)
            output_file.write(output_str)
        else:
            #During each die roll, print to both the screen and the output
            #text file the number where the game is at the moment, followed by
            #the delimiter which is a comma
            output_str = str(node_value)
            print(output_str, end = delimiter)

            #Write the node value followed by a comma to the output file.
            output_file.write(output_str + delimiter)
    
#Open (create or overwrite) the output file for writing ("w").
#If the open() function cannot create the output file or
#overwrite the existing one, then attempt to catch the
#exception thrown, print this to the screen, and terminate the program.
try:
    output_file = open(OUTPUT_FILE_NAME, "w")
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

#Print the game description to the output file and console,
#and print the pyramid to the output file.
print(GAME_DESCRIPTION)
output_file.write(GAME_DESCRIPTION + "\n")

#Get the total number of levels for the pyramid and the number of games
#to simulate from the user. Also get whether the user wants the value
#of each node displayed to the screen as the game runs.
TOTAL_LEVELS = int(get_input(LEVELS_MESSAGE, True, \
                             MINIMUM_LEVELS, MAXIMUM_LEVELS))
NUMBER_GAMES = int(get_input(GAMES_MESSAGE, True, \
                             MINIMUM_GAMES, MAXIMUM_GAMES))
PRINT_TO_SCREEN = get_input(PRINT_MESSAGE, False)

#Calculate the total nodes in the pyramid. This is
#the total number of levels plus one multipled with the total number of
#levels, and then divide this result by 2; ((k)(k + 1))/2.
#Convert the result to an integer value to lose the '.0' floating-point.
TOTAL_NODES = int((TOTAL_LEVELS * (TOTAL_LEVELS + 1))/2)

#Create the pyramid
pyramid = Pyramid(TOTAL_NODES, TOTAL_LEVELS)

#Create the class that will hold the game statistics
game_statistics = GameStatistics(NUMBER_GAMES)

#Continue simulating Pyramid game(s) until the number of games
#provided have been simulated.
while pyramid.number_games < NUMBER_GAMES:
    #Continue rolling the die and traversing the pyramid until all
    #nodes in the pyramid have been visited.
    while pyramid.visited_nodes < TOTAL_NODES:

        #Print the node value
        print_node_value(pyramid.getCurrentNodeValue(), ",", PRINT_TO_SCREEN)
        
        #Roll the die and traverse the pyramid; in the rollDice() method,
        #the total number of dots and total number of moves are incremented
        #regardless if the move results in a new Node, SO the last move
        #before the game ends is recorded.
        pyramid.rollDice(output_file)

    #Print the last node value
    print_node_value(pyramid.getCurrentNodeValue(), ".\n", \
                     PRINT_TO_SCREEN, True)

    #Print to the output file and screen the total number of moves (dots),
    #the average number of dots, and the node with the largest number
    #of dots
    pyramid.displayStats(output_file)

    #Update the statistics across all pyramid games; provide the
    #total moves and maximum dots of the finished game, and increment
    #the sum representing the total moves across all games and the
    #maximum number of dots across all games.
    game_statistics.addGameStats(pyramid.total_moves, pyramid.total_dots)
    game_statistics.addTotalMoves(pyramid.total_moves)
    game_statistics.addTotalDots(pyramid.total_dots)
    
    #Reset the pyramid for a new game
    pyramid.reset()

    #Update the current game number
    if pyramid.number_games != NUMBER_GAMES:
        game_statistics.updateGame()

#Print the overall average number of moves across T games, the minimum and
#maximum number of moves in T simulated games, and the maximum maximum dots,
#average maximum dots, and minimum maximum dots
game_statistics.displayStats(output_file)

#Close the output file
output_file.close()

#Terminate the program
sys.exit()
