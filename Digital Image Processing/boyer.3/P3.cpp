#include "utilities.h"
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>

using namespace cv;
using namespace std;

//The name of the window that will display the grayscale image
//with histogram equalization applied.
const char *WINDOW_NAME = "Histogram Equalized Grayscale";

//The name of the window that will display the color image with
//histogram equalization applied.
const char *WINDOW_NAME_COLOR = "Histogram Equalized Color";

//The name of the window that will display the input image.
const char *INPUT_IMG = "Input Image";

const int B_CHANNEL_INDEX = 0;
const int G_CHANNEL_INDEX = 1;
const int R_CHANNEL_INDEX = 2;

const int NUM_CHANNELS = 3;

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

  Mat img_equalized_color;

  //Boolean variable that will be false if the input image is NOT a color
  //image (that is, a grayscale image), or true if the input image IS a 
  //color image.
  int input_img_is_color = false;

  //Check if the number of channels of the input image is greater than 1.
  //If so, the input image is a color image, and histogram equalization
  //must be applied to all three of its channels.
  if (img.channels() != 1)
  {
    input_img_is_color = true;

    //Get the Blue, Green, and Red channels of the image.
    Mat img_channels[3];

    //This will return each color channels (B, G, R) from the input image
    //as a Mat and store each Mat in the img_channels array.
    split(img, img_channels);

    //A 2D array, where each row corresponds to the histogram of the B,
    //G, and R channels of the input image, respectively.
    int hist_arr_colors[3][256] = {0};

    double norm_hist_arr_colors[3][256] = {0.0};

    double cdf_colors[3][256] = {0.0};

    //Compute the histograms of the B, G, and R channels.
    compute_histogram(hist_arr_colors[B_CHANNEL_INDEX], 
      img_channels[B_CHANNEL_INDEX]);
    compute_histogram(hist_arr_colors[G_CHANNEL_INDEX], 
      img_channels[G_CHANNEL_INDEX]);
    compute_histogram(hist_arr_colors[R_CHANNEL_INDEX], 
      img_channels[R_CHANNEL_INDEX]);

    //Compute the normalized histogram of the B, G, and R channels.
    compute_normalized_histogram(hist_arr_colors[B_CHANNEL_INDEX], 
      norm_hist_arr_colors[B_CHANNEL_INDEX], 
      img_channels[B_CHANNEL_INDEX]);
    compute_normalized_histogram(hist_arr_colors[G_CHANNEL_INDEX],
      norm_hist_arr_colors[G_CHANNEL_INDEX], 
      img_channels[G_CHANNEL_INDEX]);
    compute_normalized_histogram(hist_arr_colors[R_CHANNEL_INDEX],
      norm_hist_arr_colors[R_CHANNEL_INDEX], 
      img_channels[R_CHANNEL_INDEX]);

    //Compute the cumulative distribution function (CDF) of the B, G, and
    //R channels.
    compute_equalized_histogram(norm_hist_arr_colors[B_CHANNEL_INDEX],
      cdf_colors[B_CHANNEL_INDEX], img_channels[B_CHANNEL_INDEX]);
    compute_equalized_histogram(norm_hist_arr_colors[G_CHANNEL_INDEX],
      cdf_colors[G_CHANNEL_INDEX], img_channels[G_CHANNEL_INDEX]);
    compute_equalized_histogram(norm_hist_arr_colors[R_CHANNEL_INDEX],
      cdf_colors[R_CHANNEL_INDEX], img_channels[R_CHANNEL_INDEX]);

    //Apply histogram equalization to each channel in the image (the
    //blue, green, and red channel(s).)
    histogram_equalize(cdf_colors[B_CHANNEL_INDEX], 
      img_channels[B_CHANNEL_INDEX]);
    histogram_equalize(cdf_colors[G_CHANNEL_INDEX],
      img_channels[G_CHANNEL_INDEX]);
    histogram_equalize(cdf_colors[R_CHANNEL_INDEX],
      img_channels[R_CHANNEL_INDEX]);

    merge(img_channels, NUM_CHANNELS, img_equalized_color);
  }

  //Get a grayscale version of the input image.
  Mat grayscale_img;
  get_grayscale_img(img, grayscale_img);

  //Create the histogram array, and initially initalize all elements to 0.
  int hist_arr[256] = {0};
  double norm_hist[256] = {0.0};
  double cumulative_dist_function[256] = {0.0};

  //Compute the histogram of the grayscale image.
  compute_histogram(hist_arr, grayscale_img);

  //Compute the normalized hisogram (the PDF) of the grayscale image.
  compute_normalized_histogram(hist_arr, norm_hist, grayscale_img);

  //Compute the CDF of the grayscale image.
  compute_equalized_histogram(norm_hist, cumulative_dist_function,
    grayscale_img);

  //Apply histogram equalization to the grayscale image, using 
  //the obtained CDF of the image.
  histogram_equalize(cumulative_dist_function, grayscale_img);
  
  //Display the grayscale image.
  display_img(WINDOW_NAME, grayscale_img);

  //If the input was a color image, display the result of applying
  //histogram equalization to each of the channels of the image.
  if (input_img_is_color)
    display_img(WINDOW_NAME_COLOR, img_equalized_color);

  //Display the input image
  display_img(INPUT_IMG, img);

  waitKey(0); 

  return 0;
}
