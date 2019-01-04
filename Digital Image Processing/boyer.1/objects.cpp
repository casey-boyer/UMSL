#include <iostream>
#include <stdlib.h> //For srand and rand
#include <time.h> //To intialize a random seed
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

using namespace std;
using namespace cv;

//Manipulate the pixel values on the image for a rectangle, square, or
//circle.
void pixel_manip(Point, Point, Point, Mat&, bool = false);

//Get a random location in (x, y) coordinates for a rectangle
//or square
Point get_random_location(int, int);

//Get the euclidean distance between two Point objects
int get_distance(Point, Point);

//The width and height of the image, which is 640 x 480 pixels
//is 200 x 160 pixels
const int RECT_WIDTH = 200;
const int RECT_HEIGHT = 160;

//The dimensions (width and height) of any square drawn to the screen,
//which is 100 x 100 pixels
const int SQUARE_DIMEN = 100;

//The diameter of any circle drawn to the screen, which is 70
const int CIRCLE_DIAMETER = 70;

//The integer values for the colors white and black
const int WHITE = 255;
const int BLACK = 0;

//This is an argument passed to the circle() and rectangle() functions,
//and the negative value indicates that the circle will be filled when it is drawn
//to the screen.
const int FILLED = -1;

//The name of the window that will display the image
const char * DISPLAY = "Display";

int main(int argc, char *argv[])
{
  //Initialize a random seed
  srand(time(NULL));

  //Create a 640 x 480 pixel image, where CV_8UC1 is
  //8 unsigned bits per item where each pixel has 1 of these
  //to form 1 channel. This will create a black image by default.
  Mat img(HEIGHT, WIDTH, CV_8UC1);

  //Get the random location of the square.
  Point sqr_tl = get_random_location(SQUARE_DIMEN, SQUARE_DIMEN);
  Point sqr_br = Point (sqr_tl.x + SQUARE_DIMEN,
    sqr_tl.y + SQUARE_DIMEN);

  //Get the center of the square
  int sqr_center_x = ((SQUARE_DIMEN / 2) + sqr_tl.x);
  int sqr_center_y = ((SQUARE_DIMEN / 2) + sqr_tl.y);
  Point sqr_center = Point(sqr_center_x, sqr_center_y);

  //Draw the square to the screen
  rectangle(img, sqr_tl, sqr_br, Scalar(BLACK), FILLED, 8);

  //Pixel at center of square is black
  img.at<uchar>(sqr_center.y, sqr_center.x) = BLACK;

  //Get the random location of the rectangle. The top-let vertex of the
  //rectangle is given by get_random_location(), and the bottom-right
  //vertex of the rectangle is obtained by adding the rectangle width to
  //the top-left x coordinate, and adding the rectangle height to the
  //top-left y coordinate.
  Point rect_tl = get_random_location(RECT_WIDTH, RECT_HEIGHT);
  Point rect_br = Point(rect_tl.x + RECT_WIDTH, rect_tl.y + RECT_HEIGHT);

  //Draw the rectangle to the screen
  rectangle(img, rect_tl, rect_br, Scalar(BLACK), FILLED, 8);

  //Get the center point of the rectangle
  int rect_center_x = ((RECT_WIDTH / 2) + rect_tl.x);
  int rect_center_y = ((RECT_HEIGHT / 2) + rect_tl.y);
  Point rect_center = Point(rect_center_x, rect_center_y);

  //Make the pixel at the center of the rectangle black;
  //row-major order, so need to use (y, x) instead of (x, y)
  img.at<uchar>(rect_center.y, rect_center.x) = BLACK;

  //Get the top-left and bottom-right vertices of the circle. The top-left
  //vertex is a randomly generated location, using the circle's diameter,
  //and the bottom-right vertex is the coordinates of the top-left vertex
  //plus the circle's diameter.
  Point circle_tl = get_random_location(CIRCLE_DIAMETER, CIRCLE_DIAMETER);
  Point circle_br = Point(circle_tl.x + CIRCLE_DIAMETER,
    circle_tl.y + CIRCLE_DIAMETER);

  //Get the center of the circle. The x coordinate of the center is the
  //circle's diameter divided by 2, and this result added with the top left
  //x coordinate of the circle; the y coordinate of the center is the circle's
  //diameter divided by 2, and this result added with the top left y coordinate
  //of the circle
  int circ_center_x = ((CIRCLE_DIAMETER / 2) + circle_tl.x);
  int circ_center_y = ((CIRCLE_DIAMETER / 2) + circle_tl.y);
  Point circ_center = Point(circ_center_x, circ_center_y);

  //Draw the circle to the screen; the radius is the diameter divided
  //by 2
  circle(img, circle_tl, (CIRCLE_DIAMETER / 2), Scalar(BLACK), FILLED, 8);

  //Make the pixel at the center of the circle black
  img.at<uchar>(circ_center.y, circ_center.x) = BLACK;

  //Modify the pixels of the rectangle, square, and circle such that
  //each pixel is 255 (white) minus the distance of that pixel from the
  //center of the shape. This creates a gradient-like effect
  pixel_manip(rect_center, rect_tl, rect_br, img);
  pixel_manip(sqr_center, sqr_tl, sqr_br, img);
  pixel_manip(circ_center, circle_tl, circle_br, img, true);

  //Create the window where the image 'img' will be displayed,
  //with the window name "Display".
  namedWindow(DISPLAY);

  //Show the img image in the window named "Display".
  imshow(DISPLAY, img);

  //Wait for any keyboard input in the display window.
  waitKey(0);

  //Upon getting keyboard input, destroy the created window.
  destroyWindow(DISPLAY);

  return 0;
}

//Manipulate each pixel of the shape such that the pixel value (color)
//is (white) 255 minus the euclidean distance of that pixel from the center.
//This function accepts the center (x, y) of the shape, the top-left (x, y)
//vertex of the shape, and the bottom right (x, y) vertex of the shape. It
//accepts by reference the Mat (image) that the shape has been draw on, so
//that modifications to the image's pixels are retained. The bool argument
//isCircle is false by default, and if false, treats the shapes as a rectangle;
//if true, it will treat the shape's pixels being manipulated as a circle.
void pixel_manip(Point center, Point tl, Point br, Mat &img, bool isCircle)
{
  //Loop counter variables to traverse the matrix of the image
  int rows, columns;

  //Traverse each row of the image matrix. Start at the top-left y coordinate
  //of the image, and continue traversal until the bottom-right y coordinate
  //of the image is reached.
  for (rows = tl.y; rows < br.y; rows++)
  {
    //For each row, traverse the column of the image matrix. Start at the
    //top-left x coordinate of the image, and continue traversal until
    //the bottom-right x coordinate is reached.
    for (columns = tl.x; columns < br.x; columns++)
    {
	  if ((rows != center.y) || (columns != center.x))
      {
        //Calculate the distance from the current Point (given by columns, rows)
        //to the center of the shape.
        int distance = get_distance(Point(columns, rows), center);

        //If the bool value 'isCircle' is true, then we must ensure that the
        //euclidean distance between the current point and the center is less
        //than the circle's radius. This is because the radius of the circle is
        //the distance from the center of the circle to any other point in
        //the circle. If the distance is greater, then this pixel is
        //not within the circle, so DO NOT modify it. Otherwise, if the distance
        //is less, this is within the circle and can be modified.
        if (isCircle)
        {
          if (distance < (CIRCLE_DIAMETER / 2))
            img.at<uchar>(rows, columns) = WHITE - distance;
        }
        else //Else, modify the pixel color-value in the rectangle.
          img.at<uchar>(rows, columns) = WHITE - distance;
      }
    }
  }
}

//This function will generate a random location (Point object) in x, y
//coordinates on the image. It accepts the width and height of the shape
//as arguments.
Point get_random_location(int width, int height)
{
  //Variables to hold the value of the random x,y coordinates.
  int random_x, random_y;

  //By default, the randomly generated location will have a 'true' out
  //of bounds value; this will be used to test if the full shape, when drawn,
  //will be out of the image boundaries (outside 640 x 480 pixels).
  bool out_of_bounds = true;

  do
  {
    //Get a random x coordinate in the image in the range
    //of 1 to the image width, 640.
    random_x = rand() % WIDTH + 1;

    //Get a random y coordinate in the image in the range of 1
    //to the image height, 480.
    random_y = rand() % HEIGHT + 1;
	
	//If the sum of the randomly generated x coordinate and the width of the
    //shape is greater than 640, then the total shape will be out of bounds;
    //similarly, if the sum of the randomly generated y coordinate and the
    //height of the shape is greater than 480, then the total shape will
    //be out of bounds. In either case, set the out_of_bounds bool variable to
    //true to iterate through the do-while loop until acceptable x,y is found.
    if (random_x + width > WIDTH)
      out_of_bounds = true;
    else if (random_y + height > HEIGHT)
      out_of_bounds = true;
    else
      out_of_bounds = false;

  } while (out_of_bounds);

  //Return the random location that the shape will start on
  return Point(random_x, random_y);
}

//This function calculates the euclidean distance between a specific location
//on a shape and the center of a shape. It returns the distance
//to the calling statement.
int get_distance(Point location, Point center)
{
  //Get the difference of the x, y coordinates of the center of
  //the shape and the given location.
  int x_diff = location.x - center.x;
  int y_diff = location.y - center.y;

  //The euclidean distance between the two points is the sum of the
  //x_diff and y_diff squared, and then taking the square root of this sum.
  int distance = (int)sqrt((pow(x_diff, 2)) + (pow(y_diff, 2)));

  //Return the distance between the two points.
  return distance;
}

