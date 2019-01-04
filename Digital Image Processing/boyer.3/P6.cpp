#include "utilities.h"
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

//The name of the window that will display the image.
const char *INPUT_IMG = "Input Image";
const char *HIST_EQ_IMG = "Histogram Equalization";
const char *SUBTRACT_IMG = "Subtracted Histogram Equalization";

//Number of pixel intensity levels will be in the range {0, 1, 2, ..., 255}.
const int LEVELS = 256;

//The number of channels in a color image.
const int NUM_CHANNELS = 3;

//The index of each channel in a color image.
const int B_CHANNEL_INDEX = 0;
const int G_CHANNEL_INDEX = 1;
const int R_CHANNEL_INDEX = 2;

//If the user specifies an ROI on the command line, then the total number
//of arguments on the command line needs to be 6:
//P6 [image_file] x y width height
const int ROI_ARGS_NUM = 6;

int main(int argc, char *argv[])
{
  int ROI_x;
  int ROI_y;
  int ROI_width;
  int ROI_height;
  Rect rect_roi;
  bool ROI_specified = false;

  //If the user does not provide an input image, print the program usage
  //and exit.
  if (argc == 1)
  {
    cout << "Usage: " << argv[0] << " [image]\n";
    return -1;
  }
  else if (argc == ROI_ARGS_NUM)
  {
    //If the user provides a ROI on the command line, parse the command
    //line arguments
    char * str_end;

    ROI_x = strtol(argv[2], NULL, 10);
    ROI_y = strtol(argv[3], NULL, 10);
    ROI_width = strtol(argv[4], NULL, 10);
    ROI_height = strtol(argv[5], NULL, 10);
    ROI_specified = true;

    cout << "ROI is (" << ROI_x << ", " << ROI_y << "), with width " <<
      ROI_width << " and height " << ROI_height << "\n";

    rect_roi = Rect(ROI_x, ROI_y, ROI_width, ROI_height);

    //If strtol() returned a zero value (0L), then it was unable to convert
    //the command line argument to a long integer. Print an error message
    //and terminate the program.
  }
  else if (argc > ROI_ARGS_NUM)
  {
    cout << "Usage: " << argv[0] << " [image] x y width height\n" << argc;
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


  //Ensure that the specified ROI is within the image bounds
  //and is non-negative.
  if ((ROI_specified) &&
    (verify_ROI(rect_roi.x, rect_roi.y, rect_roi.width, 
      rect_roi.height, img) == -1))
  {
    return -1;
  }

  //Get the grayscale image equivalent of the input image.
  Mat grayscale_img;
  get_grayscale_img(img, grayscale_img);

  //Create a result image.
  Mat result_img;

  //Test if the input image is a grayscale
  //image.
  bool is_grayscale = is_grayscale_img(img);

  //If the input image is a color image, get the PDF and CDF of each
  //channel, then apply histogram equalization to each channel.
  //Then, subtract the histogram equalization of each channel from the
  //corresponding channel of the input image. Merge this result to get
  //the input image subtracted from its histogram equalized image.
  if (!is_grayscale)
  {
    //Apply histogram equalization to these channels
    Mat img_channels[NUM_CHANNELS];

    //Apply the result of subtracting the histogram equalized channels
    //from the channels of the input image to each of these
    //channels
    Mat img_channels_result[NUM_CHANNELS];

    //Store the channels of the input image
    Mat input_img_channels[NUM_CHANNELS];

    //Get the image matrix of the B, G, and R channels.
    split(img, img_channels);

    //Get the image matrix of the B, G, and R channels again; these Matrices
    //will be used to store the result of subtracting the histogram
    //equalized channels.
    split(img, img_channels_result);

    //Get the B, G, and R channels of the input image to represent the input
    //image.
    split(img, input_img_channels);

    //Create a 1D array of 256 elements for each channel for the histogram
    //of each channel.
    int channels_hist[NUM_CHANNELS][LEVELS] ={0};

    //Create a 1D array of 256 (double) elements for each channel for the
    //normalized histogram of each channel.
    double channels_norm_hist[NUM_CHANNELS][LEVELS] = {0.0};

    //Create a 1D array of 256 (double) elements for each channel for the
    //cumulative distribution function of each channel.
    double channels_cdf[NUM_CHANNELS][LEVELS] = {0.0};

    //Compute the histogram of each channel.
    compute_histogram(channels_hist[B_CHANNEL_INDEX],
      img_channels[B_CHANNEL_INDEX]);
    compute_histogram(channels_hist[G_CHANNEL_INDEX],
      img_channels[G_CHANNEL_INDEX]);
    compute_histogram(channels_hist[R_CHANNEL_INDEX],
      img_channels[R_CHANNEL_INDEX]);

    //Compute the normalized histogram of each channel.
    compute_normalized_histogram(channels_hist[B_CHANNEL_INDEX],
      channels_norm_hist[B_CHANNEL_INDEX], img_channels[B_CHANNEL_INDEX]);
    compute_normalized_histogram(channels_hist[G_CHANNEL_INDEX],
      channels_norm_hist[G_CHANNEL_INDEX], img_channels[G_CHANNEL_INDEX]);
    compute_normalized_histogram(channels_hist[R_CHANNEL_INDEX],
      channels_norm_hist[R_CHANNEL_INDEX], img_channels[R_CHANNEL_INDEX]);

    //Compute the CDF of each channel.
    compute_equalized_histogram(channels_norm_hist[B_CHANNEL_INDEX],
      channels_cdf[B_CHANNEL_INDEX], img_channels[B_CHANNEL_INDEX]);
    compute_equalized_histogram(channels_norm_hist[G_CHANNEL_INDEX],
      channels_cdf[G_CHANNEL_INDEX], img_channels[G_CHANNEL_INDEX]);
    compute_equalized_histogram(channels_norm_hist[R_CHANNEL_INDEX],
      channels_cdf[R_CHANNEL_INDEX], img_channels[R_CHANNEL_INDEX]);

    //Apply histogram equalization to each of the channels.
    histogram_equalize(channels_cdf[B_CHANNEL_INDEX], 
      img_channels[B_CHANNEL_INDEX]);
    histogram_equalize(channels_cdf[G_CHANNEL_INDEX], 
      img_channels[G_CHANNEL_INDEX]);
    histogram_equalize(channels_cdf[R_CHANNEL_INDEX], 
      img_channels[R_CHANNEL_INDEX]);

    //Subtract the histogram equalized channel(s) from the image's original
    //channels on the entire image or specified ROI.
    if (!ROI_specified)
    {
      histogram_equalized_subtract(input_img_channels[B_CHANNEL_INDEX], 
        img_channels[B_CHANNEL_INDEX], img_channels_result[B_CHANNEL_INDEX]);
      histogram_equalized_subtract(input_img_channels[G_CHANNEL_INDEX],
        img_channels[G_CHANNEL_INDEX], img_channels_result[G_CHANNEL_INDEX]);
      histogram_equalized_subtract(input_img_channels[R_CHANNEL_INDEX],
        img_channels[R_CHANNEL_INDEX], img_channels_result[R_CHANNEL_INDEX]);
    }
    else
    {
      histogram_equalized_subtract(input_img_channels[B_CHANNEL_INDEX],
        img_channels[B_CHANNEL_INDEX], img_channels_result[B_CHANNEL_INDEX],
        ROI_x, ROI_y, ROI_width, ROI_height);
      histogram_equalized_subtract(input_img_channels[G_CHANNEL_INDEX],
        img_channels[G_CHANNEL_INDEX], img_channels_result[G_CHANNEL_INDEX],
        ROI_x, ROI_y, ROI_width, ROI_height);
      histogram_equalized_subtract(input_img_channels[R_CHANNEL_INDEX],
        img_channels[R_CHANNEL_INDEX], img_channels_result[R_CHANNEL_INDEX],
        ROI_x, ROI_y, ROI_width, ROI_height);
    }

    //Create an image to merge the subtracted results.
    Mat dest_img;

    //Merge the results of subtracting histogram equalization to the
    //Mat dest_img.
    merge(img_channels_result, NUM_CHANNELS, dest_img);

    //Display the result.
    display_img(SUBTRACT_IMG, dest_img);
    display_img(INPUT_IMG, img);

    waitKey(0);
  }
  else
  {
    //Get the grayscale equivalent for the result image.
    get_grayscale_img(img, result_img);

    //Create and intialize to 0 the histogram, normalized histogram, and
    //CDF of the image.
    int hist_arr[LEVELS] = {0};
    double norm_hist[LEVELS] = {0.0};
    double cdf[LEVELS] = {0.0};

    //Get the histogram of the grayscale image.
    compute_histogram(hist_arr, grayscale_img);

    //Get the normalized histogram (PDF) of the grayscale image.
    compute_normalized_histogram(hist_arr, norm_hist, grayscale_img);

    //Get the cumulative distribution function (CDF) of the grayscale
    //image.
    compute_equalized_histogram(norm_hist, cdf, grayscale_img);

    //Equalize the grayscale image.
    histogram_equalize(cdf, grayscale_img);

    //Apply the result of the subtracting histogram equalization to the
    //entire image or the specified ROI.
    if (!ROI_specified)
      histogram_equalized_subtract(img, grayscale_img, result_img);
    else
    {
      histogram_equalized_subtract(img, grayscale_img, result_img,
        ROI_x, ROI_y, ROI_width, ROI_height);
    }

    //Display the image.
    display_img(INPUT_IMG, img);

    //Display the histogram equalized image.
    display_img(HIST_EQ_IMG, grayscale_img);

    //Display the resulting image obtained from subtracting the histogram
    //equalized image from the input image.
    display_img(SUBTRACT_IMG, result_img);

    waitKey(0); 
  }

  return 0;
}
