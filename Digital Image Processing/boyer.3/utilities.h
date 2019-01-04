#ifndef UTILITIES_H
#define UTILITIES_h

#include <iostream>
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <stdlib.h>

using namespace cv;
using namespace std;

//Function to display an image to a window. The function accepts the name
//of the window as the first parameter, and the image to be displayed as
//the second parameter.
void display_img(const char*, Mat);

//Get the mean value of an image's pixel intensities.
double get_img_mean(Mat);

//Change the second parameter to the grayscale image version
//of the first image.
void get_grayscale_img(Mat&, Mat&);

//Test if the Mat argument is a grayscale image.
bool is_grayscale_img(Mat&);

//Return the string equivalent of a number.
string get_string_text(int);

//Threshold the input image (Mat) using the value provided in the first
//argument.
void threshold_img(double, Mat&);

//Write the string (text) at the specified Point to the image.
void write_text(Mat&, string, Point&);

//Verify if a ROI specified on the command line is a valid region
//in the image; arguments are the (x, y) coordinate and width and height
int verify_ROI(int, int, int, int, Mat);

//Compute the histogram of an image
void compute_histogram(int (&)[256], Mat&);

//Compute the normalized histogram of an image, given its 
//histogram.
void compute_normalized_histogram(int (&)[256], double (&)[256], Mat&,
  int = -1);

//Compute the cumulative distribution function (CDF) of an image,
//given the image's normalized histogram.
void compute_equalized_histogram(double (&)[256], double (&)[256], Mat&);

//Apply histogram equalization to the entire image 
//(when Rect = Rect(-1, -1, -1, -1)) OR to a specific region in the image
//(when Rect is initialized with non-negative values), given the
//image's cumulative distribution function and the image itself.
void histogram_equalize(double (&)[256], Mat&, 
  Rect = Rect(-1, -1, -1, -1));

//Subtract the an image with histogram equalization applied from
//the same image (before histogram equalization) and store the result
//in a new image; apply this to the entire image OR to a specific area
//given the (x, y) and width and height of the region
void histogram_equalized_subtract(Mat&, Mat&, Mat&, int = 0, int = 0,
  int = 0, int = 0);

#endif
