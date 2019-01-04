#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include "utilities.h"

using namespace std;
using namespace cv;

//The function to calculate the pastel borders.
int calculate_pastel(int, int, double);

//Callback function to handle trackbar events
void on_trackbar(int, void*);

//The name of the window that will display the image.
const char *WINDOW_NAME = "Pastel Border Blur";

//The name of the trackbar.
const char *TRACKBAR_NAME = "Fade:";

//The pixel value of white.
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

  //Store the value of the trackbar.
  int n;

  //If the image file is empty, does not exist, or cannot be read,
  //display an error and terminate the program.
  if (img.empty())
  {
    cout << "Could not open or read the image file " << argv[1] << "\n";
    return -1;
  }

  //The maximum value of the trackbar is the value of img.cols
  //if the image columns are greater than the image rows, or
  //the value of img.rows if the image rows are greater than
  //the image columns.
  int n_max = max(img.cols, img.rows);

  //Create the window.
  namedWindow(WINDOW_NAME);

  //Create the trackbar in the above window, with the specified
  //trackbar name. n will be used to maintain a reference
  //to the current trackbar value. n_max is the maximum trackbar
  //value. The callback function is on_trackbar, which will provide
  //the input image when called.
  createTrackbar(TRACKBAR_NAME, WINDOW_NAME, &n, n_max, on_trackbar,
    &img);

  //Display the image.
  imshow(WINDOW_NAME, img);

  waitKey(0);

  return 0;
}

//The callback function that will handle trackbar events.
void on_trackbar(int n, void *user_data)
{
  //Get the input image.
  Mat *img = (Mat*)user_data;

  //Make a deep copy of the input image to apply
  //the blurred borders to.
  Mat blur_img = img->clone();

  //The range to apply the blur to the borders to.
  //The top_border_fade and left_border fade represent the
  //top row and left column of the image, respectively. 
  //The bottom_border_fade and right_border_fade represent
  //the bottom row of the image and the right row of the
  //image, respectively. 
  int top_border_fade = n;
  int bottom_border_fade = blur_img.rows - n;
  int left_border_fade = n;
  int right_border_fade = blur_img.cols - n;

  //Apply the blur to the image.
  for (int rows = 0; rows < blur_img.rows; rows++)
  {
    for (int cols = 0; cols < blur_img.cols; cols++)
    {
      double distance;

      //Make the borders of the image white.
      //If the pixel is at the right boundary, or is at the
      //left boundary, set the pixel value to white.
      if ( ((cols + 1) == blur_img.cols) || (cols == 0))
        blur_img.at<uchar>(rows, cols) = WHITE;

      //If the pixel is at the bottom boundary, or top 
      //boundary, set the pixel value to white.
      if (((rows + 1) == blur_img.rows) || (rows == 0))
        blur_img.at<uchar>(rows, cols) = WHITE;

      //If the column value is within the left_border_fade range,
      //then apply the blur to this pixel.
      if (cols < left_border_fade)
      {
        //The distance is between the points (cols, rows) and (0, rows)
        distance = 
          get_distance_between_points(Point(cols, rows), Point(0, rows));

        blur_img.at<uchar>(rows, cols) = 
          calculate_pastel(n, blur_img.at<uchar>(rows, cols), distance);
      }
      else if (cols > right_border_fade)
      {
        //If the column value is within the right_border_fade range,
        //apply the blur to this pixel.
        //The distance is between the points (cols, rows) 
        //and (img.cols, rows)
        distance = 
          get_distance_between_points(Point(cols, rows), 
          Point(blur_img.cols, rows));

        blur_img.at<uchar>(rows, cols) =
          calculate_pastel(n, blur_img.at<uchar>(rows, cols), distance);
      }

      //If the row value is within the top_border_fade range, then
      //apply the blur to this pixel.
      if (rows < top_border_fade)
      {
        //The distance is between the points (cols, 0) and (cols, rows)
        distance =
          get_distance_between_points(Point(cols, 0), Point(cols, rows));

        blur_img.at<uchar>(rows, cols) =
          calculate_pastel(n, blur_img.at<uchar>(rows, cols), distance);
      }
      else if (rows > bottom_border_fade)
      {
        //If the row value is within the bottom_border_fade range,
        //then apply the blur to this pixel.
        //The distance is between the points (cols, rows) and
        //(cols, img.rows)
        distance =
          get_distance_between_points(Point(cols, rows),
          Point(cols, blur_img.rows));

        blur_img.at<uchar>(rows, cols) =
          calculate_pastel(n, blur_img.at<uchar>(rows, cols), distance);
      }
    }
  }

  //Display the image with blurred borders.
  imshow(WINDOW_NAME, blur_img);
}

//Calculate the pixel value of the pixel to be blurred.
int calculate_pastel(int n, int pixel_val, double distance)
{
  double pastel = (double)(pixel_val) + 
    ((((double)n - distance) / (double)n) * double((255 - pixel_val)));
  return (int)pastel;
}
