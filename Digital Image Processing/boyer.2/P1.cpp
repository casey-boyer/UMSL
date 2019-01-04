#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "utilities.h"

using namespace std;
using namespace cv;

int main(int argc, char *argv[])
{
  //If the user does not provide two input images, print the program
  //usage and exit.
  if ( (argc == 1) || (argc == 2))
  {
    cout << "Usage: " << argv[0] << " [image] [image]\n";
    return -1;
  }

  //Get the input images.
  Mat img_one = imread(argv[1]);
  Mat img_two = imread(argv[2]);

  //If empty() returns true, then the images are invalid or do
  //not exist. Print an error and exit.
  if (img_one.empty() || img_two.empty())
  {
    cout << "Unable to open picture files: " << argv[1] << " " <<
      argv[2] << "\n";
    return -1;
  }

  //Get the dimensions of both input images.
  int img_one_height = img_one.size().height;
  int img_two_height = img_two.size().height;
  int img_one_width = img_one.size().width;
  int img_two_width = img_two.size().width;

  //If the dimensions of the input images are not equal, then print an error
  //message and terminate the program.
  if ((img_one_height != img_two_height) ||
    (img_one_width != img_two_width))
  {
    cout << "The input images must be the same size.\n";
    return -1;
  }

  //If the input image is a color image, convert it to grasycale.
  Mat grayscale_img_one;
  Mat grayscale_img_two;

  //If the number of channels in the input image is not 1, then the
  //input image is not a grayscale image. Convert it to a grayscale
  //image by calling cvtColor with the CV_BGR2GRAY argument.
  if ( (img_one.channels() != 1) && (img_two.channels() != 1))
  {
    cvtColor(img_one, grayscale_img_one, CV_BGR2GRAY);
    cvtColor(img_two, grayscale_img_two, CV_BGR2GRAY);
  }
  else if (img_one.channels() != 1)
    cvtColor(img_one, grayscale_img_one, CV_BGR2GRAY);
  else if (img_two.channels() != 1)
    cvtColor(img_two, grayscale_img_two, CV_BGR2GRAY);
  else
  {
    grayscale_img_one = img_one;
    grayscale_img_two = img_two;
  }

  //Compute the average distance between the two images.
  compute_avg_distance(grayscale_img_one, grayscale_img_two); 

  return 0;
}
