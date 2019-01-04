#include "utilities.h"
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

//The name of the window that will display the image for the
//user to select an ROI.
const char *WINDOW_NAME = "Select the ROI";

//The name of the window that will display the result of applying
//histogram equalization to only the ROI.
const char *RESULT = "Result";

//The number of pixel intensity values, in the range {0, 1, 2,..., 255}.
const int NUM_LEVELS = 256;

//The number of channels for a color image.
const int NUM_CHANNELS = 3;

//The index of the blue channel, green channel, and red channel
//in a color image.
const int B_CHANNEL_INDEX = 0;
const int G_CHANNEL_INDEX = 1;
const int R_CHANNEL_INDEX = 2;

//The start and end position of the ROI.
Point start_position;
Point end_position;

//Function prototype for the mouse event handler, which will process
//the user's mouse click.
void mouse_event_handler(int, int, int, int, void*);

int main(int argc, char *argv[])
{
  //If the user does not provide an input image, print the program usage
  //and exit.
  if (argc == 1)
  {
    cout << "Usage: " << argv[0] << " [image]\n";
    return -1;
  }

  //Get the input image.
  Mat img = imread(argv[1]);

  //If empty() returns true, then the images are invalid or do not exist.
  //Print an error and exit.
  if (img.empty())
  {
    cout << "Unable to open the picture file: " << argv[1] << "\n";
    return -1;
  }

  //Create a named window.
  namedWindow(WINDOW_NAME);

  //Set the mouse handler for the specified window with the name
  //of the char* WINDOW_NAME, the MouseCallback function, and userdata
  //as null.
  setMouseCallback(WINDOW_NAME, mouse_event_handler, NULL);

  //Display the window so the user can select an ROI.
  imshow(WINDOW_NAME, img);

  waitKey(0); 

  cout << "ROI is a rectangle from (" << start_position.x << ", "
    << start_position.y << ") to (" << end_position.x << ", "
    << end_position.y << ").\n";

  //Create the result image.
  Mat ROI_equalized_image;
  get_grayscale_img(img, ROI_equalized_image);

  //Get the width and height of the selected ROI.
  int rect_width = abs(start_position.x - end_position.x);
  int rect_height = abs(start_position.y - end_position.y);

  //Create the ROI as a Rect object.
  Rect rect = Rect(start_position, end_position);

  
  if (!is_grayscale_img(img))
  {
    //If the input image is not a grayscale image, apply histogram
    //equalization to all three channels in the specified ROI
    //and merge the result.

    //Split the input image into three channels for the blue,
    //green, and red channels.
    Mat img_channels[NUM_CHANNELS];
    split(img, img_channels);

    //Intiailize the histogram, normalized histogram, and CDF of
    //all three color channels to 0.
    int hist_arr_channels[NUM_CHANNELS][NUM_LEVELS] = {0};
    double norm_hist_channels[NUM_CHANNELS][NUM_LEVELS] = {0.0};
    double cdf_channels[NUM_CHANNELS][NUM_LEVELS] = {0.0};

    //Compute the histogram of each color channel.
    compute_histogram(hist_arr_channels[B_CHANNEL_INDEX],
      img_channels[B_CHANNEL_INDEX]);
    compute_histogram(hist_arr_channels[G_CHANNEL_INDEX],
      img_channels[G_CHANNEL_INDEX]);
    compute_histogram(hist_arr_channels[R_CHANNEL_INDEX],
      img_channels[R_CHANNEL_INDEX]);

    //Compute the normalized histogram of each color channel.
    compute_normalized_histogram(hist_arr_channels[B_CHANNEL_INDEX],
      norm_hist_channels[B_CHANNEL_INDEX], img_channels[B_CHANNEL_INDEX]);
    compute_normalized_histogram(hist_arr_channels[G_CHANNEL_INDEX],
      norm_hist_channels[G_CHANNEL_INDEX], img_channels[G_CHANNEL_INDEX]);
    compute_normalized_histogram(hist_arr_channels[R_CHANNEL_INDEX],
      norm_hist_channels[R_CHANNEL_INDEX], img_channels[R_CHANNEL_INDEX]);

    //Compute the CDF of each color channel.
    compute_equalized_histogram(norm_hist_channels[B_CHANNEL_INDEX],
      cdf_channels[B_CHANNEL_INDEX], img_channels[B_CHANNEL_INDEX]);
    compute_equalized_histogram(norm_hist_channels[G_CHANNEL_INDEX],
      cdf_channels[G_CHANNEL_INDEX], img_channels[G_CHANNEL_INDEX]);
    compute_equalized_histogram(norm_hist_channels[R_CHANNEL_INDEX],
      cdf_channels[R_CHANNEL_INDEX], img_channels[R_CHANNEL_INDEX]);

    //Apply histogram equalization to each channel in the specified ROI.
    histogram_equalize(cdf_channels[B_CHANNEL_INDEX], 
      img_channels[B_CHANNEL_INDEX], rect);
    histogram_equalize(cdf_channels[G_CHANNEL_INDEX],
      img_channels[G_CHANNEL_INDEX], rect);
    histogram_equalize(cdf_channels[R_CHANNEL_INDEX],
      img_channels[R_CHANNEL_INDEX], rect);

    //Merge the result of apply histogram equalization to each channel
    //at the specified ROI to the Mat ROI_equalized_image.
    merge(img_channels, NUM_CHANNELS, ROI_equalized_image);
  }
  else
  {
    //Initialize the histogram, normalized histogram, and CDF arrays
    //to 0.
    int hist_arr[NUM_LEVELS] = {0};
    double norm_hist_arr[NUM_LEVELS] = {0.0};
    double cdf[NUM_LEVELS] = {0.0};

    //Compute the histogram of the image.
    compute_histogram(hist_arr, img);

    //Compute the normalized histogram of the image.
    compute_normalized_histogram(hist_arr, norm_hist_arr, img);

    //Compute the CDF of the image.
    compute_equalized_histogram(norm_hist_arr, cdf, img);

    //Apply histogram equalization to the specified ROI of the image.
    histogram_equalize(cdf, ROI_equalized_image, rect);
  }

  //Display the result of apply histogram equalization to the specified ROI.
  display_img(RESULT, ROI_equalized_image);
  waitKey(0);

  return 0;
}

//The mouse event handle for the user mouse click.
void mouse_event_handler(int event, int x, int y, int flags, void *data)
{
  if (event == EVENT_LBUTTONDOWN)
  {
    //If the event was a left mouse button down click, then
    //set the start position to the coordinates of this click.
    cout << "Mouse down; x = " << x << ", y = " << y << "\n";
    //Get the starting position
    start_position.x = x;
    start_position.y = y;
  }
  else if (event == EVENT_LBUTTONUP)
  {
    cout << "Mouse up; x = " << x << ", y = " << y << "\n";
    //If the event was a left mouse button release, then set the 
    //end position to the coordinates of where the left mouse button
    //was released.
    end_position.x = x;
    end_position.y = y;

    //Destroy the window after the user releases the mouse button, since
    //the ROI has been obtained.
    destroyWindow(WINDOW_NAME);
  }
}
