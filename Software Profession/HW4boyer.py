#Casey Boyer, 09/26/18, CS4500: Introduction to the Software Profession
#BoyerHW4 Version 1.0
#
#Using Python 3.7.0 and the Python 3.7.0 Shell to compile and test program
#
#***To run the game, run the 'HW4boyer.py' module***
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
#Then, an input file titled "HWinfile.txt" is read that provides 4
#pairs of integers, where each pair is the levels of a pyramid and
#the number of times to simulate this pyramid. After simulating
#each of the 4 different possible pyramids for the specified number
#of simulations, the maximum number of dots of each simulation and
#the overall average moves of each simulation is calculated and
#printed to the screen and output file.
#**The output file that will be either created or overwritten is named
#'HW4boyerOutfile.txt', and will be created in the same folder as HW4boyer.py**
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
#   4. turtle.py: This is a graphical module that is included with
#   the Python standard library. It used to show the user an example
#   of how the game works, with a pyramid of 6 levels and 21 nodes.
#
#   5. time.py: This is the time module from the Python standard library.
#   This is used to wait two seconds after the graphical example ends
#   before closing the graphical (turtle) window.
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
#
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
#https://stackoverflow.com/questions/10712002/create-an-empty-list-in-python-with-certain-size
#   This source details how to create an empty list, that is, specify a
#   list with size n elements without assigning any values to the list
#   indices. This is used to create an inital empty list that will contain
#   the total number of moves of each game.
#
#https://docs.python.org/3.3/library/turtle.html?highlight=turtle
#   This source is used for turtle graphics.
import random
import signal
import sys
import turtle
import time

#The following is a simple declaration of constant variables
#that will be used repeatedly throughout the program
#and will NOT be modified.

#The name of the input file to read the number of pyramid levels
#and simulations from.
INPUT_FILE_NAME = "HWinfile.txt"

#The total number of pyramids (of a specific size) that will be
#created for each T simulation
TOTAL_PYRAMIDS = 4

#The maximum number of Nodes and levels in the example pyramid
EXAMPLE_NODES = 21
EXAMPLE_LEVELS = 6

#The diameter of the dot drawn by the turtle module at each
#node in the pyramid.
DOT_DIAMETER = 10

#A light blue, cyan color to graphically represent a node with no dots.
NO_DOTS_COLOR = "#48f8f9"

#A dark blue color to graphically represent the current node.
CURRENT_NODE_COLOR = "#2e427f"

#The color black, in hexadecimal notation
COLOR_BLACK = "#000"

#A string of 15 dashes, to separate each vector of statistics.
STATS_SEPARATOR = "---------------"

#The numerical values representing each direction
UP_LEFT = 0
UP_RIGHT = 1
BOTTOM_LEFT = 2
BOTTOM_RIGHT = 3

#The name of the output file
OUTPUT_FILE_NAME = "HW4boyerOutfile.txt"

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

    #These are the x and y coordinates of a Node in the pyramid in the
    #graphical representation.
    def setTurtleCoordinates(self, turtle_x, turtle_y):
        self.turtle_x = turtle_x
        self.turtle_y = turtle_y

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

    #Return the index of the current node
    def getCurrentNode(self):
        return self.current_index

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

    #This method will assign the x,y coordinates of each Node object in the
    #nodes_list member variable, where these coordinates are the actual
    #x,y values of the Node(s) on the graphical/turtle window.
    def assignTurtleCoordinates(self):
        index = 0
        current_level = 1

        #The first node, 1, will be printed at (0, 0) in the window.
        x_coordinate = 0
        y_coordinate = 0
        node_offset = 0 #The node offset is the x coordinate of the leftmost node in the pyramid
        node_spacing = 50 #Spacing between each node is 50 units

        #Assign the turtle coordinates of each node in the list.
        while index < len(self.nodes_list):
            if self.nodes_list[index].level == current_level:
                if self.nodes_list[index].level != 1:
                    #The current y coordinate of the Node object minus 1 (since each level begins
                    #at 1 instead of 0), multipled by 50, plus the offset to get equal spacing
                    #between each node and ensure that the last node in the level is the absolute
                    #value of the first node in the level
                    x_coordinate = ((self.nodes_list[index].y_coordinate - 1)* node_spacing) + node_offset
                    self.nodes_list[index].setTurtleCoordinates(x_coordinate, y_coordinate)
                    #turtle.setx(x_coordinate)
                else:
                    self.nodes_list[index].setTurtleCoordinates(0, 0)
                    #turtle.setx(x_coordinate)
            else: #If the current level is not the level at the current node, new level
                current_level += 1 #Increment the level

                #Move the next node (since this is the left edge) further
                #to the left
                x_coordinate = (current_level - 1) * -25
                
                #Move the next level of nodes further DOWN
                y_coordinate = current_level * -25

                #Set the coordinates of the node
                self.nodes_list[index].setTurtleCoordinates(x_coordinate, y_coordinate)

                #Get the node offset, an offset of where other nodes on this level
                #should appear
                node_offset = (current_level - 1) * -25

            index += 1

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
    #to calculate the maxium number of dots in any game;
    #
    #inserting the total moves from each game and the maximum dots
    #from each game into two separate lists, in order to retrieve the
    #total number of moves of all games, and the
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
        self.maximum_max_index = 0

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

        #Compare the maximum number of dots of the newly finished game
        #to the maximum number of dots of the game with the current
        #maximum number of dots of all games. If the index of the
        #newly finished game has a larger maximum dot count, then
        #update the maximum dot index.
        if self.max_dots_per_game[self.current_game] > \
           self.max_dots_per_game[self.maximum_max_index]:
            self.maximum_max_index = self.current_game

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

    #Return the maximum maximum number of dots in a game
    def getMaximumDots(self):
        return self.max_dots_per_game[self.maximum_max_index]

    #Print the overall average number of moves across T games, the minimum and
    #maximum number of moves in T simulated games, and the maximum maximum dots,
    #average maximum dots, and minimum maximum dots
    def displayStats(self, output_file, levels):
        #Separate from previous output
        print(STATS_SEPARATOR)
        output_file.write(STATS_SEPARATOR + "\n")

        #Print the levels of the pyramid, the number of simulations,
        #the overall average number of moves, and the maximum number
        #of dots in a game in the (L,T,M,D) format
        output_str = "(" + str(levels) + "," + str(self.number_games) + \
                     "," + str(self.getOverallAverageMoves()) + "," + \
                     str(self.getMaximumDots()) + "):"
        print(output_str)
        output_file.write(output_str + "\n")

        #Print the number of levels of this pyramid to the output file and
        #screen
        output_str = "LEVELS: " + str(levels)
        print(output_str)
        output_file.write(output_str + "\n")

        #Print the number of simulations of this pyramid to the output file
        #and screen
        output_str = "# OF SIMULATIONS: " + str(self.number_games)
        print(output_str)
        output_file.write(output_str + "\n")

        #Print the overall average number of moves of T simulations of
        #this pyramid to the output file and screen
        output_str = "OVERALL AVERAGE MOVES: " + str(self.getOverallAverageMoves())
        print(output_str)
        output_file.write(output_str + "\n")

        #Print the maximum number of dots of all T simulations of this
        #pyramid to the output file and screen
        output_str = "MAXIMUM DOTS OVER ALL SIMULATIONS: " + str(self.getMaximumDots())
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

#Calculate the total nodes in the pyramid. This is
#the total number of levels plus one multipled with the total number of
#levels, and then divide this result by 2; ((k)(k + 1))/2.
#Convert the result to an integer value to lose the '.0' floating-point.
def get_total_nodes(levels):
    total_nodes = int((levels * (levels + 1)) / 2)
    return total_nodes

#This method will draw the pyramid of nodes, graphically, using turtle.
def draw_pyramid(pyramid):
    index = 0

    #Assign the "turtle" coordinates of each node in the pyramid's list.
    pyramid.assignTurtleCoordinates()

    #Draw the pyramid of nodes to the screen
    while index < len(pyramid.nodes_list):
        #Draw the node's value (numerical value) to the screen
        #based on the Node's turtle x,y coordinates; offset these
        #coordinates by subtracting the dot diameter from them, so when
        #a dot is drawn on the node (for visited and unvisted nodes),
        #the dot is on the number rather than next to it
        turtle.setx(pyramid.nodes_list[index].turtle_x + -(DOT_DIAMETER))
        turtle.sety(pyramid.nodes_list[index].turtle_y + -(DOT_DIAMETER))
        turtle.write(str(pyramid.nodes_list[index].value), font=("Arial", 16, "normal"))

        #Draw the dot on the node (because, all unvisited nodes have
        #a dot as well)
        turtle.setx(pyramid.nodes_list[index].turtle_x)
        turtle.sety(pyramid.nodes_list[index].turtle_y)
        #Draw the dot with the 'NO_DOTS_COLOR' color to indicate that
        #this node has not dots at it yet
        turtle.dot(DOT_DIAMETER, NO_DOTS_COLOR)
        
        index += 1

#This method will modify the color of the dot at a node
#to signify how many dots are currently present at this node. As the
#number of dots at a node increases, the node becomes more red
#by subtracting from the green and blue values of the rgb vector.
def set_dot_intensity(pyramid):
    #Get the number of dots at this node
    num_dots = pyramid.nodes_list[pyramid.current_index].num_dots

    #Set the position of the turtle to the coordinates of this node
    #so that the dot is drawn on this node
    turtle.setpos(pyramid.nodes_list[pyramid.current_index].turtle_x, \
                  pyramid.nodes_list[pyramid.current_index].turtle_y)

    #Set the intensity of the color at this node, which is the
    #number of dots multiplied by 5 (so that the color changes faster,
    #rather than extremely slow)
    intensity = num_dots * 5

    #The color of the dot at a node is (255, 254-intensity, 97-intensity),
    #which will begin at a yellow color and slowly change to orange and then
    #to red as the number of dots increases
    rgb_red = 255
    rgb_green = 254 - intensity
    rgb_blue = 97 - intensity

    #Check the values of the rgb_red, rgb_green, and rgb_blue to ensure
    #they are greater than 0
    if rgb_green < 0 and rgb_blue < 0:
        #If both rgb_green and rgb_blue are less than 0, set them
        #to 0. This means there are a lot of dots at this node, and will
        #make the dot a very red color.
        rgb_green = 0
        rgb_blue = 0

        #Set the red value in the rgb vector. Divide the number of dots
        #by 6 so that this value is not as drastic (do not want to go from red
        #to suddenly black)
        rgb_red = 255 - int((num_dots / 6))

        #If this value is less than 0, set rgb_red to 0. This will display
        #a black dot.
        if rgb_red < 0:
            rgb_red = 0
    else: #Otherwise, if rgb_green or rgb_blue is less than 0, set the value to 0.
        if rgb_green < 0:
            rgb_green = 0
        if rgb_blue < 0:
            rgb_blue = 0

    #Set the colormode of the turtle module to 255 so it will accept
    #an rgb tuple of integers.
    turtle.colormode(cmode=255)

    #Set the color of the dot as calculated above.
    turtle.color((rgb_red, rgb_green, rgb_blue))
    turtle.dot(DOT_DIAMETER) #Draw a dot at this node.
    
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

#Create the pyramid
pyramid_example = Pyramid(EXAMPLE_NODES, EXAMPLE_LEVELS)

#Print the game description to the output file and console,
#and print the pyramid to the output file.
print(GAME_DESCRIPTION)
output_file.write(GAME_DESCRIPTION + "\n")

#Hide the turtle pen cursor by setting the pen attribute 'shown' to false.
#Speed = 5 is half speed. hideturtle() to speed up the drawing.
#penup() removes the line
#trail left behind when drawing to the screen.
turtle.pen(shown=False, speed=5)
turtle.hideturtle()
turtle.penup()

#Draw the pyramid of nodes
draw_pyramid(pyramid_example)

#Set the cursor position to the first node, 1, which is at 0,0
turtle.setpos(0, 0)
current_pos = turtle.stamp()

#Continue rolling the die and traversing the pyramid until all
#nodes in the pyramid have been visited.
while pyramid_example.visited_nodes < EXAMPLE_NODES:
    #Draw a dot at the current node, and set the yellow to red intensity color
    #at this node based on how many dots it has
    set_dot_intensity(pyramid_example)

    #Remove the old cursor
    turtle.clearstamp(current_pos)
    #Set the color of the stamp
    turtle.color(CURRENT_NODE_COLOR)
    #Update the cursor to the new node
    current_pos = turtle.stamp()

    #Roll the die and traverse the pyramid; in the rollDice() method,
    #the total number of dots and total number of moves are incremented
    #regardless if the move results in a new Node, SO the last move
    #before the game ends is recorded.
    pyramid_example.rollDice(output_file)

#Remove the old cursor
turtle.clearstamp(current_pos)

#Draw the dot to the last node
set_dot_intensity(pyramid_example)

#Write 'THE END!' to the screen so the user understands that this
#pyramid example is over
turtle.setpos(0, -200)
turtle.color(COLOR_BLACK)
turtle.write("THE END!", font=("Arial", 16, "normal"))
time.sleep(2) #Wait 2 seconds before closing the turtle window
turtle.bye() #Close the turtle window

index = 0

pyramids = list() #The list of pyramids of a specific level from the input file
simulations = list() #The list of simulations of a specific value from the input file

#Open the input file for reading;
#the open() function will return a file object, that will
#be referenced by input_file. If the open function does not return a file
#object, then OSError was thrown and the file could not be found.
try:
    input_file = open(INPUT_FILE_NAME, "r")
except OSError as e:
    print("OSError: File could not be found or opened:\n", e)
except:
    print("Unexpected error trying to open input file.")
else:
    for line in input_file:
        #Split the current line based on whitespace (default argument for split), and
        #store the returned list() in the file_lines list.
        file_lines = line.split()
        
        #Get the levels of the pyramid as specified by the input file
        #(the first integer on a line) and use this to create a Pyramid
        #object, and insert this pyramid into the pyramids list.
        pyramid_levels = int(file_lines[0])
        pyramids.insert(index, Pyramid(get_total_nodes(pyramid_levels), \
                                       pyramid_levels))

        #Get the number of simulations of this pyramid as specified
        #by the input file (the second integer on a line) and use this to
        #create a GameStatistics() object, and insert this GameStatistics
        #object into the simulations list.
        simulations.insert(index, GameStatistics(int(file_lines[1])))

        index += 1

num_pyramids = 0
index = 0

#Print the header for the statistics to the screen and output file
output_str = "\n\t\tOVERALL STATISTICS:\n"
print(output_str)
output_file.write(output_str + "\n")

#Perform the number of simulations for each pyramid of a specific
#size as given by the input file
while num_pyramids < TOTAL_PYRAMIDS:
    #While the pyramid(s) at the current index have not completed the number of
    #games specified by the simulations parameter
    while pyramids[index].number_games < simulations[index].number_games:

        #Continue rolling the die and traversing the pyramid until all
        #nodes in the pyramid have been visited.
        while pyramids[index].visited_nodes < pyramids[index].max_nodes:
            pyramids[index].rollDice(output_file)

        #Update the stats across all pyramid games of this size
        simulations[index].addGameStats(pyramids[index].total_moves, \
                                        pyramids[index].total_dots)
        simulations[index].addTotalMoves(pyramids[index].total_moves)

        #Reset the pyramid for a new game
        pyramids[index].reset()

        #Update the current game number
        if pyramids[index].number_games != simulations[index].number_games:
            simulations[index].updateGame()

    #Display the statistics of the T simulations
    simulations[index].displayStats(output_file, pyramids[index].max_levels)
    index += 1
    num_pyramids += 1
    
#Close the output file
output_file.close()

#Terminate the program
sys.exit()
