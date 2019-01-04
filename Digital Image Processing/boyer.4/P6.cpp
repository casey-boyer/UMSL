#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <fstream>
#include <string>
#include <cstddef>
#include "utilities.h"

using namespace std;
using namespace cv;

//The name of the window that will display the binary image.
const char *WINDOW_NAME = "BINARY";

//The names of the windows that will display the dilated
//and eroded binary images.
const char *DILATE_NAME = "DILATE";
const char *ERODE_NAME = "ERODE";

const int WHITE = 255;

int main(int argc, char *argv[])
{
  //If the user does not provide an image file as an argument,
  //display program usage and terminate.
  if (argc == 1)
  {
    cout << "Usage: " << argv[0] << " [image]\n";
    return -1;
  } 

  //Read the image file provided as an argument.
  Mat img = imread(argv[1], CV_8UC1);

  //If the image file is empty, does not exist, or cannot be read,
  //display an error and terminate the program.
  if (img.empty())
  {
    cout << "Could not open or read the image file " << argv[1] << "\n";
    return -1;
  }

  //Get the mean pixel intensity value of the input image.
  double img_mean = get_image_mean(img);

  //Create a binary image with the input image's width and height
  //for the binary image to be thresholded with the input
  //image's mean, and the dilated and eroded images.
  Mat binary_img = Mat::zeros(img.rows, img.cols, CV_8UC1);
  Mat dilate_img = Mat::zeros(img.rows, img.cols, CV_8UC1);
  Mat erode_img = Mat::zeros(img.rows, img.cols, CV_8UC1);

  //Intialize all values of the dilation and erosion binary images.
  for (int rows = 0; rows < dilate_img.rows; rows++)
  {
    for (int cols = 0; cols < dilate_img.cols; cols++)
    {
      //Initialize each pixel of the dilated image to 0, or OFF.
      dilate_img.at<uchar>(rows, cols) = 0;

      //Initialize each pixel of the eroded image to 1 (255), or ON.
      erode_img.at<uchar>(rows, cols) = WHITE;
    }
  }

  //Threshold the binary image with the input image's mean
  threshold(img, binary_img, img_mean, WHITE, THRESH_BINARY);

  //The width, height, half width, and half height of
  //the structuring element.
  int se_width, se_height, se_half_width, se_half_height;

  //Open the structuring element file.
  ifstream se_file("se.txt");
  string input_line; //For reading each line of the file
  stringstream str_stream; //For storing the values of each line in the file

  //Get the width and height of the structuring element
  if (se_file.is_open())
  {
    //The width and height is on the first line of the file.
    getline(se_file, input_line);

    //Find the space separating the width and height.
    size_t space_separator = input_line.find_first_of(" ");

    if (space_separator != string::npos)
    {
      str_stream << input_line.substr(0, space_separator);

      //Get the width of the structuring element.
      str_stream >> se_width;
      str_stream.clear();
      str_stream.str("");

      //Get the height of the structuring element.
      str_stream << input_line.substr(space_separator, 
        input_line.length());
      str_stream >> se_height;

      //Clear the stringstream contents
      str_stream.clear();
      str_stream.str("");
    }
    else
    {
      cout << "Please specify a width and height for the " << 
        "structuring element.\n";
      return -1;
    }
  }

  //Check that the structuring element width is at least 1, and less
  //than the image's columns.
  if ((se_width > img.cols) || (se_width < 1))
  {
    cout << "The structuring element width must be a positive integer "
      << "within the boundaries of the image.\n";
    return -1;
  }
 
  //Check that the structuring element height is at least 1, and less
  //than the image's rows. 
  if ((se_height > img.rows) || (se_height < 1))
  {
    cout << "The structuring element height must be a postive integer "
      << "within the boundaries of the image.\n";
    return -1;
  }

  //Calculate the half width and half height of
  //the structuring element.
  se_half_width = floor(se_width / 2);
  se_half_height = floor(se_height / 2);

  //Create the structuring element array.
  int se[se_height][se_width];

  int r_index = 0;
  int c_index = 0;

  //Intialize the values of the structuring element.
  while (getline(se_file, input_line))
  {
    //Store the input line in the stringstream.
    str_stream << input_line;

    //Initialize each row of the structuring element
    if (r_index < se_height)
    {
      //Intialize each column of this row of the structuring element
      while (c_index < se_width)
      {
        //Store the value of the structuring element
        str_stream >> se[r_index][c_index];

        //Check if the value in the file is a '0' or '1'; if not, print
        //an error and the invalid value, then terminate.
        if ((se[r_index][c_index] != 0) && (se[r_index][c_index] != 1))
        {
          cout << "Invalid structuring element value: " <<
            se[r_index][c_index] << "\n";
          return -1;
        }

        //Increment the column index.
        c_index++;
      }

      //Clear the string stream contents.
      str_stream.clear();
      str_stream.str("");

      //Reset the column index, and go to the next row.
      c_index = 0;
      r_index++;
    }
    else
      break;
  }

  //Dilation
  for (int rows = 0; rows < binary_img.rows; rows++)
  {
    for (int cols = 0; cols < binary_img.cols; cols++)
    {
      //For each pixel in the input binary image, intiailize the
      //output value to 0 (OFF).
      int any = 0;

      for (int r = 0; r < se_height; r++)
      {
        for (int c = 0; c < se_width; c++)
        {
          //Go through each element of the structuring element.
          int x_coord = cols - c + se_half_width;
          int y_coord = rows - r + se_half_height;
          int pixel_val = binary_img.at<uchar>(y_coord, x_coord);
          
          //If any ON (1) value in the structuring element overlaps
          //an ON (255) pixel in the input image, then set
          //the pixel in the output to ON (255). Otherwise,
          //set the output pixel to OFF (0).
          if ((se[r][c] == 1) && (pixel_val == WHITE))
            any = WHITE;
        }
      }

      //Set the output pixel to the ON or OFF value retrieved
      //from above.
      dilate_img.at<uchar>(rows, cols) = any;
    }
  }

  //Erosion
  for (int rows = 0; rows < binary_img.rows; rows++)
  {
    for (int cols = 0; cols < binary_img.cols; cols++)
    {
      //For each pixel in the input binary image,
      //intialize the output value in the eroded image to
      //ON (255).
      int all = WHITE;

      for (int r = 0; r < se_height; r++)
      {
        for (int c = 0; c < se_width; c++)
        {
          //Go through each element of the structuring element.
          int x_coord = cols + c - se_half_width;
          int y_coord = rows + r - se_half_height;
          int pixel_val = binary_img.at<uchar>(y_coord, x_coord);

          //If any ON value in the structuring element overlaps
          //an OFF (0) pixel in the input binary image, then
          //set the output pixel in the eroded image to 
          //OFF (0), otherwise leave it as ON (255).
          if ((se[r][c] == 1) && (pixel_val != WHITE))
            all = 0;
        }
      }

      //Set the pixel value in the eroded image to ON or OFF
      //based on the value of all set above.
      erode_img.at<uchar>(rows, cols) = all;
    }
  }

  //Display the binary image, the dilated image, and
  //the eroded image.
  display_img(binary_img, WINDOW_NAME);
  display_img(dilate_img, DILATE_NAME);
  display_img(erode_img, ERODE_NAME);

  waitKey(0);

  return 0;
}
