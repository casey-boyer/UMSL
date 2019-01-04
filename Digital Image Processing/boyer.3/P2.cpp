#include "utilities.h"
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

//The name of the window that will display the thresholded image.
const char *WINDOW_NAME = "Thresholded image";

//The number of channels in the color image.
const int NUM_CHANNELS = 3;

//The index of the blue channel, green channel, and red channel
//in a color image.
const int BLUE_CHANNEL_INDEX = 0;
const int GREEN_CHANNEL_INDEX = 1;
const int RED_CHANNEL_INDEX = 2;

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

  //Check if the input image is a color image or grayscale image
  bool is_grayscale = is_grayscale_img(img);

  //Declare the result image
  Mat result_img;

  if (!is_grayscale)
  {
    //If the input image is a color image, apply the thresholding
    //function to each of the image's channels.

    //Create an array of 3 Mat objects, 1 for each channel.
    Mat img_channels[NUM_CHANNELS];

    //Get the three channels of the input image, and place each
    //channel in the img_channels array.
    split(img, img_channels);

    //Get the mean of each channel
    double mean_blue_channel = 
      get_img_mean(img_channels[BLUE_CHANNEL_INDEX]);
    double mean_green_channel = 
      get_img_mean(img_channels[GREEN_CHANNEL_INDEX]);
    double mean_red_channel = 
      get_img_mean(img_channels[RED_CHANNEL_INDEX]);

    //Display the mean of each channel to the console.
    cout << "The mean of the blue channel is " << mean_blue_channel;
    cout << "The mean of the green channel is " << mean_green_channel;
    cout << "The mean of the red channel is " << mean_red_channel;

    //Threshold each channel according to its mean
    threshold_img(mean_blue_channel, img_channels[BLUE_CHANNEL_INDEX]);
    threshold_img(mean_green_channel, img_channels[GREEN_CHANNEL_INDEX]);
    threshold_img(mean_red_channel, img_channels[RED_CHANNEL_INDEX]);

    //Merge the thresholded channels into the result image
    merge(img_channels, NUM_CHANNELS, result_img);
  }
  else
  {
    //Initialize the result image
    get_grayscale_img(img, result_img);

    //Get the mean of the grayscale image
    double mean = get_img_mean(img);

    //Display the mean to the console.
    cout << "The mean is " << mean << ".\n";

    //Threshold the grayscale image according to its mean
    threshold_img(mean, result_img);
  }

  //Display the resulting thresholded image.
  display_img(WINDOW_NAME, result_img);

  waitKey(0); 

  return 0;
}
