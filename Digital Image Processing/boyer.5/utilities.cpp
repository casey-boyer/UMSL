#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include "utilities.h"

using namespace std;
using namespace cv;

const int NUM_PLANES = 2; //For the real and imaginary parts

//Create a named window and display the image, img, in the window.
void display_img(Mat img, const char *WINDOW_NAME)
{
  namedWindow(WINDOW_NAME);
  imshow(WINDOW_NAME, img); 
}

//Create the spectrum of the fourier transformed image to see
//where noise is present in the image.
void create_power_spectrum(Mat complex_img, Mat &magnitude_img, Mat planes[])
{
  //Split the fourier transformed image into its
  //real and imaginary parts
  split(complex_img, planes);

  //Compute the magnitude of the fourier transformed image
  magnitude(planes[0], planes[1], planes[0]);

  //Set the magnitude image to the real part of the magnitude
  magnitude_img = planes[0];

  //Switch to the logarithmic scale
  magnitude_img += Scalar::all(1);
  log(magnitude_img, magnitude_img);

  //Crop the spectrum and rearrange the quadrants of the fourier
  //image so that the origin is at the image center.
  crop_rearrange(magnitude_img);

  //Transform the spectrum image with float values between
  //0-1 so it is viewable.
  normalize(magnitude_img, magnitude_img, 0, 1, CV_MINMAX);
}

//Remove noise from the fourier transformed image. Given the ROI,
//set the real and imaginary parts of the image to 0 within this ROI.
void filter_frequency(Mat &magnitude, Mat &real_img, Mat &imaginary_img,
  Rect ROI)
{
  //Get the starting and ending values of the ROI.
  int start_y = ROI.y;
  int end_y = ROI.y + ROI.height;
  int start_x = ROI.x;
  int end_x = ROI.x + ROI.width;

  cout << "Removing frequencies at the points from (" << start_x << ", " 
    << start_y << ") to (" << end_x << ", " << end_y << ")\n";

  for (int rows = start_y; rows < end_y; rows++)
  {
    for (int cols = start_x; cols < end_x; cols++)
    {
      //Set the real and imaginary values of the fourier transformed
      //image to 0 within the ROI to remove the noise.
      real_img.at<float>(rows, cols) = 0;
      imaginary_img.at<float>(rows, cols) = 0;
    }
  }
}

//Rearrange the quadrants of the fourier transformed image so
//that the origin is at the center, and crop the spectrum if it has
//an odd number of rows or columns.
void crop_rearrange(Mat &magnitude_img)
{
  magnitude_img = magnitude_img(Rect(0, 0, magnitude_img.cols & -2,
    magnitude_img.rows & -2));

  int cx = magnitude_img.cols / 2;
  int cy = magnitude_img.rows / 2;

  Mat q0(magnitude_img, Rect(0, 0, cx, cy)); //Top-left
  Mat q1(magnitude_img, Rect(cx, 0, cx, cy)); //Top-right
  Mat q2(magnitude_img, Rect(0, cy, cx, cy)); //Bottom-left
  Mat q3(magnitude_img, Rect(cx, cy, cx, cy)); //Bottom-right

  //Swap the top-left and bottom right quadrants.
  Mat tmp;
  q0.copyTo(tmp);
  q3.copyTo(q0);
  tmp.copyTo(q3);

  //Swap the top-right and bottom left quadrants.
  q1.copyTo(tmp);
  q2.copyTo(q1);
  tmp.copyTo(q2);
}
