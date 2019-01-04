#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "utilities.h"

using namespace std;
using namespace cv;

//The subsampling function
void subsample(Mat, Mat&, int);

//Subsample the input image by a factor of 2.
const int SUBSAMPLE_FACTOR = 2;

//The names of the display windows for the input image and the
//subsampled input image
const char *INPUT_IMAGE = "Input image";
const char *SUBSAMPLE_IMAGE = "Subsampled by 2";

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

  //If the number of channels in the input image is not 1, then the
  //input image is not a grayscale image. Convert it to a grayscale
  //image by calling cvtColor with the CV_BGR2GRAY argument.
  if (img.channels() != 1)
    cvtColor(img, grayscale_img, CV_BGR2GRAY);
  else
    grayscale_img = img;

  //The height and width of the subsampled image. Both the height and
  //width of the subsampled image will be divided by the subsample factor,
  //since we are removing alternate rows and columns.
  int height = (grayscale_img.size().height) / SUBSAMPLE_FACTOR;
  int width = (grayscale_img.size().width) / SUBSAMPLE_FACTOR;

  //Create the subsampled image.
  Mat subsample_img = Mat(height, width, CV_8UC1);

  //Subsample the image
  subsample(grayscale_img, subsample_img, SUBSAMPLE_FACTOR);

  //Display the input image and the subsampled input image
  display_img(INPUT_IMAGE, grayscale_img);
  display_img(SUBSAMPLE_IMAGE, subsample_img);

  waitKey(0);

  return 0;
}

void subsample(Mat input, Mat &output, int factor)
{
  //Iterate through the matrix of values for the output image
  for (int rows = 0; rows < output.rows; rows++)
  {
    for (int cols = 0; cols < output.cols; cols++)
    {
      //Since we are subsampling (downsampling) the input image by
      //the specified factor, then we are discarding every other row
      //and column specified by the factor
      output.at<uchar>(rows, cols) = 
        input.at<uchar>(rows * factor, cols * factor);
    }
  }
}
