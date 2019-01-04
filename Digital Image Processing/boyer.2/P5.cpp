#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <bitset>
#include <string>
#include "utilities.h"

using namespace std;
using namespace cv;

void bit_plane_slice(Mat, Mat&, int);

const char *PLANE_7 = "BIT PLANE 7";
const char *PLANE_6 = "BIT PLANE 6";
const char *PLANE_5 = "BIT PLANE 5";
const char *PLANE_4 = "BIT PLANE 4";
const char *PLANE_3 = "BIT PLANE 3";
const char *PLANE_2 = "BIT PLANE 2";
const char *PLANE_1 = "BIT PLANE 1";
const char *PLANE_0 = "BIT PLANE 0";

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
      
  int width = grayscale_img.size().width;
  int height = grayscale_img.size().height;

  //Bit plane 7 is the 7th bit (position 7)
  //Bit plane 6 is the 6th bit (position 6)
  //Bit plane 5 is the 5th bit (position 5)
  //Bit plane 4 is the 4th bit (position 4)
  //Bit plane 3 is the 3rd bit (position 3)
  //Bit plane 2 is the 2nd bit (position 2)
  //Bit plane 1 is the 1st bit (position 1)
  //Bit plane 0 is the 0 bit (position 0)
  Mat bit_plane_7 = Mat(height, width, CV_8UC1);
  Mat bit_plane_6 = Mat(height, width, CV_8UC1);
  Mat bit_plane_5 = Mat(height, width, CV_8UC1);
  Mat bit_plane_4 = Mat(height, width, CV_8UC1);
  Mat bit_plane_3 = Mat(height, width, CV_8UC1);
  Mat bit_plane_2 = Mat(height, width, CV_8UC1);
  Mat bit_plane_1 = Mat(height, width, CV_8UC1);
  Mat bit_plane_0 = Mat(height, width, CV_8UC1);

  //Create each bit plane. Since the image is an 8-bit grayscale
  //image, there will be 8 bit planes (0-7).
  bit_plane_slice(grayscale_img, bit_plane_7, 7);
  bit_plane_slice(grayscale_img, bit_plane_6, 6);
  bit_plane_slice(grayscale_img, bit_plane_5, 5);
  bit_plane_slice(grayscale_img, bit_plane_4, 4);
  bit_plane_slice(grayscale_img, bit_plane_3, 3);
  bit_plane_slice(grayscale_img, bit_plane_2, 2);
  bit_plane_slice(grayscale_img, bit_plane_1, 1);
  bit_plane_slice(grayscale_img, bit_plane_0, 0);

  //Display each bit plane.
  display_img(PLANE_7, bit_plane_7);
  display_img(PLANE_6, bit_plane_6);
  display_img(PLANE_5, bit_plane_5);
  display_img(PLANE_4, bit_plane_4);
  display_img(PLANE_3, bit_plane_3);
  display_img(PLANE_2, bit_plane_2);
  display_img(PLANE_1, bit_plane_1);
  display_img(PLANE_0, bit_plane_0);

  waitKey(0);

  return 0;
}

void bit_plane_slice(Mat input, Mat &output, int bit_plane)
{
  for (int rows = 0; rows < input.rows; rows++)
  {
    for (int cols = 0; cols < input.cols; cols++)
    {
      //Get the binary 8-bit representation of the value of the pixel
      //at point (cols, rows)
      bitset<8> bin_pixel(input.at<uchar>(rows, cols));

      //Check if the bit corresponding to the bit plane in this
      //pixel is set. If so, make the pixel at this point white.
      //If not, make the pixel at this point black.
      if (bin_pixel[bit_plane] == 1)
        output.at<uchar>(rows, cols) = 255;
      else
        output.at<uchar>(rows, cols) = 0;
    }
  }
}
