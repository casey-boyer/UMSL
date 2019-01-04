#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <time.h> /*For getting a random number*/
#include <stdlib.h> /*For getting a random number*/

using namespace std;
using namespace cv;

//Function to display an image to the window.
void display_img(Mat img, const char *WINDOW_NAME)
{
  //Create the window.
  namedWindow(WINDOW_NAME);

  //Display the image in the window.
  imshow(WINDOW_NAME, img);
}

//Function to get a random pixel from an image.
void get_random_pixel(int &pixel, int &random_row, 
  int &random_column, Mat img)
{
  //Get a random row and column within the image's size;
  //that is, within the range 0 to image's rows and columns.
  random_row = rand() % (img.rows + 0);
  random_column = rand() % (img.cols + 0);

  //Set the pixel value to the pixel at the randomly
  //generated row and column.
  pixel = img.at<uchar>(random_row, random_column);
}

//Randomly get the position of a bit in the range 0-7.
void get_random_bit(int &bit_pos)
{
  //Get a random bit position in the range of 0-7.
  bit_pos = rand() % (7 + 0);
}

//Function to test whether a given value is positive (greater than 0)
//or negative.
int is_positive_val(int val)
{
  if (val <= 0)
    return -1;
  else
    return 1;
}

//Apply a smoothing spatial filter (box kernel) to the Mat img,
//and create the box filter with the given kernel size.
void standard_averaging_filter(int kernel_size, Mat &img)
{
  //Create the 2d kernel.
  int kernel[kernel_size][kernel_size];

  //Get the half width and half height of the kernel.
  int kernel_half_width = floor((kernel_size - 1) / 2);
  int kernel_half_height = floor((kernel_size - 1) / 2);

  double img_avg = 0.0; //The average pixel intensity value of a block
  int pixel_sum = 0; //The sum of the pixel intensity values at a block

  //Initialize all coeffecients of the kernel to 1.
  for (int r = 0; r < kernel_size; r++)
  {
    for (int c = 0; c < kernel_size; c++)
    {
      kernel[r][c] = 1;
    }
  }

  for (int rows = 0; rows < img.rows; rows++)
  {
    for (int cols = 0; cols < img.cols; cols++)
    {
      //Apply the kernel to all columns in this row.
      for (int j = 0; j < kernel_size; j++)
      {
        for (int i = 0; i < kernel_size; i++)
        {
          int x_coord = (cols + (i - kernel_half_width));
          int y_coord = (rows + (j - kernel_half_height));

          //Handle boundary values. If the calculated x or y coordinate
          //is negative, then set the x or y coordinate to its
          //negation - 1. If the calculated x or y coordinate is
          //greater than the image width or height, then set the
          //x or y coordinate to 2 * the image width (or height)
          //and subtract this from the x or y coordinate minus 1.
          if (x_coord < 0)
            x_coord = -(x_coord) - 1;
          else if (x_coord >= img.cols)
            x_coord = (2 * img.cols) - x_coord - 1;

          if (y_coord < 0)
            y_coord = -(y_coord) - 1;
          else if (y_coord >= img.rows)
            y_coord = (2 * img.rows) - y_coord - 1;

          //Get an average of the pixel intensity values within
          //this block, and get the sum of the pixels at this block.
          img_avg += img.at<uchar>(y_coord, x_coord);
          pixel_sum++;
        }
      }

      //Apply the average of the neighboring pixels to this pixel.
      img_avg = (img_avg / pixel_sum);
      img.at<uchar>(rows, cols) = (int)(img_avg);

      //Reset the sum and average before processing the next block.
      pixel_sum = 0;
      img_avg = 0.0;
    }
  }
}

//Reflect a pixel.
void reflect_pixel(int &coordinate, int min, int max)
{
  //If the pixel value is negative, then set its value to
  //its negation minus 1. Otherwise, if the pixel value
  //is beyond or at the boundary, set its pixel value to
  //2 * the max, and subtract the pixel value and 1 from this
  //result.
  if (coordinate < 0)
    coordinate = -(coordinate) - 1;
  else if (coordinate >= max)
    coordinate = (2 * max) - coordinate - 1;
}

//Get the euclidean distance between two points.
double get_distance_between_points(Point point_one, Point point_two)
{
  //Subtract the x coordinates of each point, and square the result.
  double x_dist = pow((point_one.x - point_two.x), 2);

  //Subtract the y coordinates of each point, and square the result.
  double y_dist = pow((point_one.y - point_two.y), 2);

  //Get the distance be taking the square root of the sum
  //of the above results.
  double distance = sqrt(x_dist + y_dist);

  return distance;
}

//Get the mean pixel intensity value of an image.
double get_image_mean(Mat img)
{
  double mean = 0.0;

  for (int rows = 0; rows < img.rows; rows++)
  {
    for (int cols = 0; cols < img.cols; cols++)
    {
      //Add each pixel intensity value to the mean.
      mean += img.at<uchar>(rows, cols);
    }
  }

  //Set the mean to the sum of all pixel intensity values
  //divided by the image's size.
  mean = (mean / (double)(img.cols * img.rows));

  return mean;
}
