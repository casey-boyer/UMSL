#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "utilities.h"

using namespace std;
using namespace cv;

//The BGR matrix: [0.11 0.59 0.30]
const double B_CHANNEL = 0.11;
const double G_CHANNEL = 0.59;
const double R_CHANNEL = 0.30;

//The name of the window that will display the
//input image.
const char *INPUT_WINDOW = "Input image";


//The name of the window that will display the grayscale
//image obtained using the BGR vector.
const char *GRAYSCALE_VEC = "Grayscale image using BGR vector";

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

  //Create an empty image, with dimensions equal to the input image, with one
  //channel (grayscale). This image will be used to convert the input
  //image to grayscale using the BGR vector.
  Mat grayscale_img = Mat(img.size().height, img.size().width, CV_8UC1);

  for (int rows = 0; rows < img.rows; rows++)
  {
    for (int cols = 0; cols < img.cols; cols++)
    {
      //OpenCV stores RGB values as BGR, so 0 is the blue channel,
      //1 is the green channel, and 2 is the red channel
      Vec3b pixel_intensity = img.at<Vec3b>(rows, cols);

      //Multiply the BGR vector by the matrix [0.11 0.59 0.30]
      uchar blue = pixel_intensity.val[0] * B_CHANNEL;
      uchar green = pixel_intensity.val[1] * G_CHANNEL;
      uchar red = pixel_intensity.val[2] * R_CHANNEL;

      //Sum the values of this multiplication to complete the operation,
      //and set the value of this operation as the pixel at the corresponding
      //point in the grayscale image
      grayscale_img.at<uchar>(rows, cols) = blue + green + red;
    }
  }

  //Convert the input image to grayscale using the cvtColor function.
  Mat grayscale_img_cvt;
  cvtColor(img, grayscale_img_cvt, CV_BGR2GRAY);

  //Compute the average distance between pixels of the grayscale image
  //obtained by multiplying the input image channels by the BGR vector,
  //and the grayscale image obtained by using the cvtColor function.
  compute_avg_distance(grayscale_img, grayscale_img_cvt);

  //Display the input image, and the image converted to grayscale using
  //the above vector.
  display_img(INPUT_WINDOW, img);
  display_img(GRAYSCALE_VEC, grayscale_img);

  waitKey(0);

  return 0;
}
