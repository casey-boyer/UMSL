#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <cmath> //For sqrt, pow functions
#include <algorithm> //For max_element function
#include "utilities.h"

using namespace std;
using namespace cv;

//The dimensions of the histogram to be plotted, 256 x 256.
const int HIST_DIMENSIONS = 256;

//These values will be used as indices in the histogram array.
//The SMALL_INDICES value is (256/2), and will contain the intensities
//of the darker colors in the image (0-128). 
//The LARGE_INDICES value will contain the intensities of the 
//brighter colors in the image (129-256).
const int SMALL_INDICES = 128;
const int LARGE_INDICES = 256;

//The integer representation of the color white.
const int WHITE = 255;

//The name of the display window to display the histogram.
const char *HIST_WINDOW = "Histogram";

int main(int argc, char *argv[])
{
  //If the user does not provide an input image, print the program
  //usage and exit.
  if (argc == 1)
  {
    cout << "Usage: " << argv[0] << " [image]\n";
    return -1;
  }

  //Get the input image.
  Mat img = imread(argv[1]);

  //If img.empty() returns true, then the image is invalid or does
  //not exist. Print an error and exit.
  if (img.empty())
  {
    cout << "Unable to open picture file " << argv[1] << "\n";
    return -1;
  }

  //If the input image is a color image, convert it to grasycale.
  Mat grayscale_img;

  //The image that the histogram will be plotted on. This creates a 
  //256x246 image with one channel (grayscale). Since zeros is used,
  //this will be a 256x256 matrix of 0s, or a black image.
  Mat hist_img = Mat::zeros(HIST_DIMENSIONS, HIST_DIMENSIONS, CV_8UC1);

  //If the number of channels in the input image is not 1, then the
  //input image is not a grayscale image. Convert it to a grayscale
  //image by calling cvtColor with the CV_BGR2GRAY argument.
  if (img.channels() != 1)
    cvtColor(img, grayscale_img, CV_BGR2GRAY);
  else
    grayscale_img = img;

  //Declare and initialize the histogram of the grayscale image, which will
  //contain the set of indices {0, 1, 2, ..., 255} for each gray level. At
  //each index, the number of pixels with this value is recorded.
  int histogram[256] = {0};
  
  int norm_histogram[256] = {0};
  int intensity_sum = 0;
  int dark_intensities = 0;
  int light_intensities = 0;
  int num_pixels = grayscale_img.rows * grayscale_img.cols;

  for (int rows = 0; rows < grayscale_img.rows; rows++)
  {
    for (int cols = 0; cols < grayscale_img.cols; cols++)
    {
      histogram[grayscale_img.at<uchar>(rows, cols)] += 1;
      intensity_sum += grayscale_img.at<uchar>(rows, cols);
    }
  }

  //Calculate the average intensity (mean) of the grayscale levels in
  //the image. This is done by taking the sum of intensities and dividing
  //it by the number of pixels in the image (rows multiplied with columns)
  int avg_intensity = (int)(ceil((intensity_sum / num_pixels)));

  cout << "Average grayscale value of pixels: " << avg_intensity << "\n";

  //The variance, or standard deviation of the grayscale level at each
  //intensity
  double variance[256] = {0.0};

  //The standard deviation of all grayscale levels of the image
  double standard_deviation = 0.0;

  for (int intensity = 0; intensity < 256; intensity++)
  {
    //Subtract the mean (average intensity) from each intensity level
    //and then square this result.
    variance[intensity] = pow(((double)(intensity)) - 
      ((double)(avg_intensity)), 2.0);

    //Get a sum of the total variances for each intensity
    //(grayscale level) in the image.
    standard_deviation += variance[intensity];
  }

  //Since the total variance of an image is the standard deviation squared,
  //take the square root of the standard deviation by dividing it by the
  //max intensity level.
  standard_deviation = sqrt(standard_deviation / 256);

  cout << "Standard deviation is " << standard_deviation << "\n";

  //The histogram for an image that is too dark will have large values
  //for the bins with small indices; for an image that is too bright,
  //the histogram will have large values for the bins with large indices.
  //This is counting the small indices in the range of 0-128, and the
  //large indices in the range of 129-255.
  for (int counter = 0; counter <= SMALL_INDICES; counter++)
    dark_intensities += histogram[counter];

  for (int counter = SMALL_INDICES + 1; counter < LARGE_INDICES; counter++)
    light_intensities += histogram[counter];

  if (dark_intensities > light_intensities)
  {
    cout << "The brightness of the image is low; that is, the image is dark."
      << " The image intensities are concentrated in the range of 0-128.\n";
  }
  else if (light_intensities > dark_intensities)
  {
    cout << "The brightness of the image is high; that is, the image is bright."
      << " The image intensities are concentrated in the range of 129-255.\n";
  }
  else
  {
    cout << "The image has equal values for brightness and darkness, " <<
      "and thus the image has equally dark and bright pixels.\n";
  }


  //If the standard deviation is greater than 50%, then there is a lot
  //of variance of the image from the mean; this indicates that the 
  //histogram values are spread throughout the range 0-255, and the
  //image has high contrast.
  if (standard_deviation > 50)
  {
    cout << "The image has high contrast, as the standard deviation indicates "
      << "that the intensity values have high variance from the mean.\n";
  }
  else 
  {
    cout << "The image has low contrast, as the standard deviation indicates  "
      << "that the intensity values have low variance from the mean.\n";
  }

  //Get the largest intensity in the image. The max_element function
  //returns an int* (dereferenced below) and the input parameters
  //are the initial position and final position of the histogram array
  //to compare values. HIST_DIMENSIONS is used as the final position
  //since the max_element() function searches in the ranges [first, last).
  int max_intensity = *max_element(histogram, histogram + HIST_DIMENSIONS);

  //Calculate the normalized histogram of the histogram[] array between
  //0 and 255 so that the histogram may be plotted in 256 x 256
  //dimensions. Using n as the highest occuring intensity value
  //in the image, the normalized histogram from values 0 - 255
  //is obtained by dividing each element of the histogram array
  //by this max intensity. Then, this result is multiplied with 255
  //to account for all intensities of the image, 0 - 255.
  for (int counter = 0; counter < HIST_DIMENSIONS; counter++)
  {
    norm_histogram[counter] = ((double)histogram[counter] / max_intensity) 
      * WHITE;
  }

  //Draw the histogram.
  for (int counter = 0; counter < HIST_DIMENSIONS; counter++)
  {
    //For each intensity value, draw a vertical line (so the x-coordinate
    //in both Point() objects is the same) from 255 - the normalized
    //histogram value to 255. Scalar(WHITE) will draw a white line.
    line(hist_img, Point(counter, WHITE), 
      Point(counter, WHITE - norm_histogram[counter]), Scalar(WHITE));
  }

  display_img(HIST_WINDOW, hist_img);
  waitKey(0);

  return 0;
}
