#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include "utilities.h"

using namespace std;
using namespace cv;

//Prototype for the function callback that will handle trackbar events
void on_trackbar(int, void*);

//The name of the window that will display the image.
const char *WINDOW_NAME = "sm_filter";

//The name of the trackbar.
const char *TRACKBAR_NAME = "Kernel size";

//The maximum kernel size is 7.
const int MAX_KERNEL_SIZE = 7;

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

  //Store the trackbar value.
  int trackbar_pos;

  //Create the window.
  namedWindow(WINDOW_NAME);

  //Create a trackbar in the window with the specified trackbar name,
  //and set the maximum value of the trackbar to 7. Use the int variable
  //trackbar_pos to get the current value of the trackbar. Set the
  //callback function for the trackbar to the defined function
  //on_trackbar().
  createTrackbar(TRACKBAR_NAME, WINDOW_NAME, &trackbar_pos, 
    MAX_KERNEL_SIZE, on_trackbar, &img);

  //Display the image in the window.
  imshow(WINDOW_NAME, img);

  waitKey(0);

  return 0;
}

//The callback function to handle trackbar events. It accepts the trackbar
//value (value of n in the trackbar position) and the input image.
void on_trackbar(int trackbar_pos, void *user_data)
{
  //Only apply the smoothing spatial filter if the trackbar
  //value is odd and greater than 0.
  if ((trackbar_pos == 0) || (trackbar_pos % 2 == 0))
  {
    cout << "Please select an odd kernel size between 1-7.\n";
  }
  else
  {
    //Get the input image
    Mat *img = (Mat*) user_data;

    //Make a deep copy of the input image to apply
    //the smoothing spatial filter to
    Mat img_blur = (*img).clone();

    //Apply the smoothing spatial filter to the image
    standard_averaging_filter(trackbar_pos, img_blur);

    //Display the result.
    imshow(WINDOW_NAME, img_blur);
  }
}
