#ifndef UTILITIES_H
#define UTILITIES_H

#include <iostream>
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <cmath>

using namespace cv;
using namespace std;

//Function to display an image to a window. The function accepts the
//name of the window as the first parameter, and the image to be
//displayed as the second parameter.
void display_img(const char* WINDOW_NAME, Mat img)
{
  //Create the window.
  namedWindow(WINDOW_NAME);

  //Display the image in the created window.
  imshow(WINDOW_NAME, img);
}

//Function to compute the average distance between corresponding
//pixel intensity levels of two images. It accepts to images
//as parameters.
void compute_avg_distance(Mat img_one, Mat img_two)
{
  //The intensities of both images, which are initially 0,
  //will be accumlated as sums.
  int img_one_intensity = 0;
  int img_two_intensity = 0;

  //The size of the image, n = width * height. This is obtained
  //from image one since this function is only ever invoked when both
  //input images are the same size.
  int image_size = img_one.rows * img_one.cols;

  //Iterate through the rows and columns of both images to obtain
  //their pixel intensities at (column, row) as a sum for each
  //pixel
  for (int rows = 0; rows < img_one.rows; rows++)
  {
    for (int cols = 0; cols < img_one.cols; cols++)
    {
      //Add the value of the pixel at (rows, cols) to the intensity
      //sums of the first image and the second image.
      img_one_intensity += img_one.at<uchar>(rows, cols);
      img_two_intensity += img_two.at<uchar>(rows, cols);
    }
  }

  //Get the average intensities of both the first image and the second
  //image. This is obtained by dividing the intensity sum of each image
  //by the size of the image.
  double img_one_avg_intensity = ((double)(img_one_intensity))
    / ((double)(image_size));
  double img_two_avg_intensity = ((double)(img_two_intensity))
    / ((double)(image_size));

  //Get the average distance between the two images in terms of their
  //pixel intensities. This is obtained by subtracting the average
  //pixel intensity of the first image from the average pixel intensity
  //of the second image (absolute value the result).
  double avg_distance = abs(img_one_avg_intensity - img_two_avg_intensity);

  //Report the average distance between corresponding pixel intensity
  //levels of the image.
  cout << "The average distance between corresponding pixel intensity  " 
    << "levels of the two images is: " << avg_distance << "\n";
}

#endif
