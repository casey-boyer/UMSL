#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include "utilities.h"

using namespace std;
using namespace cv;

//Prototypes for flip, flop functions.
void flip(const Mat&, Mat&);
void flop(const Mat&, Mat&);

//The name of the window to display the input image.
const char *DISPLAY = "Input Image";

//The name of the window to display the flipped input image.
const char *FLIP = "Flipped Image";

//The name of the window to display the flopped input image.
const char *FLOP = "Flopped Image";

int main(int argc, char *argv[])
{
  if (argc == 1)
  {
    //If the user only provided the program name (no input image),
    //terminate the program.
    cout << "Usage: " << argv[0] << " [image]\n";
    return -1;
  }

  //Get the input image from the user.
  Mat img = imread(argv[1]);

  //If img.empty() returns true, then the image is invalid or does
  //not exist.
  if (img.empty())
  {
    cout << "Unable to open picture file " << argv[1] << "\n";
    return -1;
  }

  //If the input image is a color image, convert it to grayscale.
  //If the number of channels is 3, it is an RGB image.
  //Declare the image that will be used as a grayscale version of the
  //input image.
  Mat grayscale_img;

  if (img.channels() != 1)
    cvtColor(img, grayscale_img, CV_BGR2GRAY);
  else
    grayscale_img = img;

  //Get the width and height of the input image.
  int img_width = img.size().width;
  int img_height = img.size().height;

  //Create the flipped image and flopped image.
  Mat flip_img = Mat(img_height, img_width, CV_8UC1);
  Mat flop_img = Mat(img_height, img_width, CV_8UC1);

  //Get the 'flipped' input image.
  flip(grayscale_img, flip_img);

  //Get the 'flopped' input image.
  flop(grayscale_img, flop_img);

  //Display the flipped image.
  display_img(FLIP, flip_img);

  //Display the flopped image.
  display_img(FLOP, flop_img);

  //Display the original image.
  display_img(DISPLAY, grayscale_img);

  waitKey(0);

  return 0;
}

//Function to flip an image. The output image will be the input image
//rotated upside down about the horizontal axis.
void flip(const Mat &input, Mat &output)
{
  //Get the height of the input image.
  int height = input.size().height;

  for (int rows = 0; rows < input.rows; rows++)
  {
    for (int columns = 0; columns < input.cols; columns++)
    {
      //For each (x, y) pixel of the input image, the corresponding
      //location in the output image will be (x, height - 1 - y), in order
      //to swap the first and last rows.
      int y_coordinate = (height - 1 - rows);
 
      //Set the pixel of the output image at (x, height - 1 - y) to the
      //pixel value at (x, y) of the input image.
      output.at<uchar>(y_coordinate, columns) = input.at<uchar>(rows, columns);
    }
  }
}

//Function to flop an image. The output image will be the right-to-left
//mirror version of the input image about the vertical axis.
void flop(const Mat &input, Mat &output)
{
  //Get the width of the input image.
  int width = input.size().width;

  for (int rows = 0; rows < input.rows; rows++)
  {
    for (int columns = 0; columns < input.cols; columns++)
    {
      //For each (x, y) pixel in the input image, the corresponding location
      //in the output image will be (width - 1 - x, y), in order to swap
      //the first and last columns.
      int x_coordinate = (width - 1 - columns);

      //Set the pixel of the output image at (width - 1 - x, y) to the pixel
      //value at (x, y) of the input image.
      output.at<uchar>(rows, x_coordinate) = input.at<uchar>(rows, columns);
    }
  }
}
