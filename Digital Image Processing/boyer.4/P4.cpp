#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <stdlib.h>
#include "utilities.h"

using namespace std;
using namespace cv;

//Function prototype to calculate gaussian function at a point
//(x, y) for a pixel value.
double calculate_gauss_distance(double, double, int, int, int);

//The name of the window that will display the image.
const char *WINDOW_NAME = "Window";

//The number of arguments expected on the command line when
//invoking the executable for this program.
const int NUM_ARGS = 4;

int main(int argc, char *argv[])
{
  //If the user does not provide an image file as an argument,
  //display program usage and terminate.
  if (argc < 4)
  {
    cout << "Usage: " << argv[0] << " [image] [kernel size] [kernel resolution]\n";
    return -1;
  } 

  //Read the image file provided as an argument.
  Mat img = imread(argv[1], CV_8UC1);

  //Get the kernel size and resolution from the command line.
  int kernel_size = strtol(argv[2], NULL, 10);
  int kernel_resolution = strtol(argv[3], NULL, 10);

  //Create the 2D kernel.
  double kernel[kernel_size][kernel_size];

  //Get the center of the kernel, which is defined as 
  //floor (1/2 * (kernel size minus 1))
  int kernel_center = floor((0.5 * (kernel_size - 1)));

  //The value of PI.
  double const PI = 4.0 * atan(1.0);

  //The kernel resolution is specified as a power of 2.
  kernel_resolution = (int)pow(2, kernel_resolution);

  //If the image file is empty, does not exist, or cannot be read,
  //display an error and terminate the program.
  if (img.empty())
  {
    cout << "Could not open or read the image file " << argv[1] << "\n";
    return -1;
  }

  cout << "Kernel size: " << kernel_size << "\nKernel resolution: "
    << kernel_resolution << "\n";

  double sum = 0;

  for (int r = 0; r < kernel_size; r++)
  {
    for (int c = 0; c < kernel_size; c++)
    {
      //Calculate the gaussian function for the kernel
      //at the point (r, c), where sigma is the kernel resolution.
      //This will weight kernel coeffecients near the boundaries
      //low, and the highest coeffecient will be at the center
      //of the kernel.
      double distance = calculate_gauss_distance(PI, kernel_resolution, r, c,
        kernel_size);

      //Multiply the value obtained from the gaussian function by
      //the kernel resolution; this will ensure that all elements
      //in the kernel add up to the resolution.
      kernel[r][c] = (distance * kernel_resolution);
      
      sum += kernel[r][c];
    }
  } 

  double total = 0;

  //Normalize the kernel values.
  for (int r = 0; r < kernel_size; r++)
  {
    for (int c = 0; c < kernel_size; c++)
    {
      //Divide each element of the kernel by the sum.
      kernel[r][c] = (kernel[r][c] / sum);

      total += (kernel[r][c] * kernel_resolution);
    }
  }

  cout << "The sum of the kernel coeffecients is " << total << "\n";

  //Create a deep copy the input image to apply the kernel to.
  Mat blur_img = img.clone();

  double run_total;

  //Apply kernel to image.
  for (int rows = 0; rows < img.rows; rows++)
  {
    for (int cols = 0; cols < img.cols; cols++)
    {
      run_total = 0.0;
      for (int i = 0; i < kernel_size; i++)
      {
        for (int j = 0; j < kernel_size; j++)
        {
          int y_coord = (rows + kernel_center) - i;
          int x_coord = (cols + kernel_center) - j;

          //If the pixels are boundary pixels, reflect them.
          reflect_pixel(y_coord, 0, img.rows);
          reflect_pixel(x_coord, 0, img.cols);

          //Add the kernel value, multiplied by each coordinate
          //in the kernel neighborhood, to the running total.
          run_total += kernel[j][i] * img.at<uchar>(y_coord, x_coord);
        }
      }

      //Set the pixel value at (rows, cols) to the value obtained
      //above. This will get an average of the neighborhood using the
      //kernel values.
      blur_img.at<uchar>(rows, cols) = run_total;
    }
  }

  //Display the image.
  //display_img(img, WINDOW_NAME);
  display_img(blur_img, "blur");

  waitKey(0);

  return 0;
}

//Sigma will be 2 to the power of the specified resolution. row, col
//are the current (y, x) coordinates to calculate G(x, y).
double calculate_gauss_distance(double PI, double sigma, int row, int col,
  int kernel_size)
{
  //This is the 2D gaussian equation for a point (row, col), which is
  //(1/(2 * PI * sigma squared)), multiplied by 
  //e ^ (- (x^2 + y^2 / 2 * sigma squared)).
  double value = 
    (1 / (2 * PI * pow(sigma, 2))) *
    exp(-(pow(row - kernel_size / 2, 2) + pow(col - kernel_size / 2, 2))
    / (2 * pow(sigma, 2)));

  return value;
}
