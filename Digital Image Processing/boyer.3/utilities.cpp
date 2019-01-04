#include "utilities.h"
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <math.h> //For round() function
#include <stdlib.h> //For abs() function
#include <string>

using namespace cv;
using namespace std;

const int BLACK = 0;
const int WHITE = 255;

//Display an image in a window, given the window name and
//the image to be displayed.
void display_img(const char* WINDOW_NAME, Mat img)
{
  //Create the window.
  namedWindow(WINDOW_NAME);

  //Display the image in the created window.
  imshow(WINDOW_NAME, img);
}

//Get the mean of pixel intensity values in an image and return
//the mean.
double get_img_mean(Mat img)
{
  int sum; //Running sum of pixel intensity values

  //Total number of pixels in the image is the image's height (number
  //of rows) and the image's width (number of columns)
  int total_pixels = (img.rows * img.cols);

  for (int rows = 0; rows < img.rows; rows++)
  {
    for (int cols = 0; cols < img.cols; cols++)
    {
      //Calculate the running sum of all pixel intensity values
      //in the image.
      sum += img.at<uchar>(rows, cols);
    }
  }

  //The mean is the running sum of pixel intensity values divided
  //by the total number of pixels in an image.
  double mean = (double)(sum) / (double)(total_pixels);

  return mean;
}

//Get the grayscale version of the Mat img, and store the
//result in the Mat grayscale_img.
void get_grayscale_img(Mat &img, Mat &grayscale_img)
{
  //If the number of chnnales in the Mat img is not one, then
  //it is a color image; convert it to a grayscale image.
  //Otherwise, initialize grayscale_img with img.
  if (img.channels() != 1)
    cvtColor(img, grayscale_img, CV_BGR2GRAY);
  else
    grayscale_img = img;
}

//Test if the input Mat img is a grayscale image or not; if so,
//return true, and if not, return false.
bool is_grayscale_img(Mat &img)
{
  //3 2D-arrays, or 3 Mat objects, for each channel of the
  //color image.
  Mat img_channels[3];

  //Split the input image into the three Mat objects.
  split(img, img_channels);

  for (int rows = 0; rows < img.rows; rows++)
  {
    for (int cols = 0; cols < img.cols; cols++)
    {
      //Get the pixel value of the blue channel, green channel, and
      //red channel at each (y, x) location.
      int b_pixel = img_channels[0].at<uchar>(rows, cols);
      int g_pixel = img_channels[1].at<uchar>(rows, cols);
      int r_pixel = img_channels[2].at<uchar>(rows, cols);

      //If, at any occurrence while iterating through the image,
      //all three pixel values are not equal, then the input image is a 
      //color image, so return FALSE.
      if ( (b_pixel != g_pixel) || (b_pixel != r_pixel) ||
        (g_pixel != r_pixel))
        return false;
    }
  }

  //Otherwise, if all three intensity values (B, G, R) at each pixel in the
  //image are equal, then the input image is a grayscale image, so
  //return true.
  return true;
}

//Return a string representation of the int argument.
string get_string_text(int pixel_value)
{
  //The string str to be returned.
  string str;

  //stringstream object to insert the pixel_value.
  stringstream str_stream;

  str_stream << pixel_value;

  //Assign the value of the stringstream object to the
  //str string.
  str.assign(str_stream.str());

  return str;
}

//Threshold the Mat img based on its mean pixel intensity value;
//if a pixel in the image is less than the mean, set that pixel
//intensity value to black (0); otherwise, set that pixel
//intensity value to white (255).
void threshold_img(double mean, Mat &img)
{
  for (int rows = 0; rows < img.rows; rows++)
  {
    for (int cols = 0; cols < img.cols; cols++)
    {
      //Get the pixel intensity value at (y, x).
      int pixel_value = img.at<uchar>(rows, cols);

      //If the pixel intensity value is less than the mean
      //pixel intensity value, set the pixel at location (y, x)
      //to black (0). Otherwise, set the pixel at location (y, x)
      //to white (255).
      if (pixel_value < (int)mean)
        img.at<uchar>(rows, cols) = BLACK;
      else
        img.at<uchar>(rows, cols) = WHITE;
    }
  }
}

void write_text(Mat &img, string str, Point &location)
{
  int font_face = FONT_HERSHEY_COMPLEX_SMALL;
  double scale_factor = 0.8;
  int font_thickness = 1;

  Size text_size= getTextSize(str, font_face, scale_factor, font_thickness, 0);
  
  Point rect_begin = Point(location.x, location.y);
  Point rect_end = Point(location.x + text_size.width, 
    location.y - text_size.height);

  rectangle(img, rect_begin, rect_end, CV_RGB(0, 0, 0), CV_FILLED);

  putText(img, str, location, font_face, scale_factor, Scalar(255, 255, 255),
    font_thickness, 1);
}

//Verify the ROI provided on the command line. The arguments
//include the ROI starting (x, y) point and the width and height
//of the ROI, and the Mat that the ROI will be applied to.
int verify_ROI(int x, int y, int width, int height, Mat img)
{
  //-If the starting point (x, y) is past the
  // image boundary, return -1.
  //
  //-If the ending point (width and height)
  // is past the image boundary, return -1.
  //
  //-If all values are 0, return -1 as this is
  // not a ROI.
  //
  //-If any of the values are negative, return -1.
  //
  //-If the starting x coordinate plus the specified width
  // is greater than the image width, return -1.
  //
  //-If the starting y coordinate plus the specified height
  // is greater than the image height, return -1.
  if ((x > img.cols) || (y > img.rows))
  {
    cout << "The starting point is past the image boundaries.\n";
    return -1;
  }
  else if ((height > img.size().height) ||
    (width > img.size().width))
  {
    cout << "The specified ROI is greater than the image boundaries.\n";
    return -1;
  }
  else if ((x == 0) && (y == 0) && 
    (height == 0) && (width == 0))
  {
    cout << "The ROI cannot be zero or non-integer values.\n";
    return -1;
  }
  else if ((x <= -1) || (y <= -1) ||
    (width <= -1) || (height <= -1))
  {
    cout << "The ROI may not contain negative values.\n";
    return -1;
  }
  else if ((x + width) > img.size().width)
  {
    cout << "The starting x coordinate and the ROI width may not be " << 
      "greater than the image width.\n";
    return -1;
  }
  else if ( (y + height) > img.size().height)
  {
    cout << "The starting y coordinate and the ROI height may not be " <<
      "greater than the image height.\n";
    return -1;
  }
  else
    return 1;
}

//Compute the histogram of a given image, and store the result
//in the provided int[] array.
void compute_histogram(int (&hist_arr)[256], Mat &img)
{
  //For each pixel in the grayscale image, record this level in each bin
  //(which is in the range 0, 1, 2, ..., 256).
  for (int rows = 0; rows < img.rows; rows++)
  {
    for (int cols = 0; cols < img.cols; cols++)
    {
      hist_arr[(int)img.at<uchar>(rows, cols)] += 1;
    }
  }
}

//Compute the normalized histogram of an image, given the histogram
//of the image and the normalized histogram double [] array to store
//the values in. The parameter img_size has a default value of -1;
//if the img_size is -1, then obtain the image size from the Mat
//argument; otherwise, set the image size to the value provided. This
//is to add functionality for reading from a file.
void compute_normalized_histogram(int (&hist_arr)[256], 
  double (&norm_hist)[256], Mat &img, int img_size)
{
  int num_pixels;

  //If the default argument was provided for img_size, then obtain
  //the number of pixels in the image.
  if (img_size == -1)
  {
    //Get the total number of pixels in the input image, which is the
    //height * the width of the input image.
    num_pixels = (img.size().height) * (img.size().width);
  }
  else
    num_pixels = img_size;

  for (int levels = 0; levels < 256; levels++)
  {
    //Compute the normalized histogram by dividing each bin of the
    //image histogram by the image size.
    norm_hist[levels] = (((double)hist_arr[levels]) / ((double)(num_pixels)));
  }
}

//Compute the CDF of an image, given its normalized histogram and the
//CDF double [] array to store the values in.
void compute_equalized_histogram(double (&norm_hist)[256],
  double (&cdf)[256], Mat &img)
{
  //Keep track of the running sum of the normalized histogram.
  double running_sum = 0.0;

  for (int levels = 0; levels < 256; levels++)
  {
    //Add the value of the normalized histogram at the index levels
    //to the running sum.
    running_sum += norm_hist[levels];

    //Assign the value at the index levels of the CDF to the
    //running_sum value.
    cdf[levels] = running_sum;
  }
}

//Function to apply histogram equalization to an image. The CDF
//of the image is provided, as well as the image to apply histogram
//equalization to. The Rect object is a default argument; if
//its components are nonnegative, then a specifc ROI has been
//provided, and histogram equalization will only be applied to this
//area in the image. Otherwise, histogram equalizatio will be
//applied to the entire image.
void histogram_equalize(double (&cdf)[256], Mat &img, Rect rect)
{
  int levels = 255; //Number of intensity levels
  int cols; //The starting index for the inner-for loop
  int rows; //The starting index for the outer-for loop
  int cols_limit; //The limit for the inner-for loop
  int rows_limit; //The limit for the outer-for loop

  if ( (rect.x != -1) && (rect.y != -1))
  {
    //If a Rect object was provided, then histogram
    //equalization must be applied to only the area
    //encompassed by the rectangle in the image. Intiailize
    //the starting (x,y) to (rect.x, rect.y), and the end
    //value to the (rect.x + width, rect.y + height), to
    //apply histogram equalization to the entire ROI.
    rows = rect.y;
    rows_limit = rect.y + rect.height;
    cols = rect.x;
    cols_limit = rect.x + rect.width;
  }
  else
  {
    //Otherwise, apply histogram equalization to the entire image.
    rows = 0;
    cols = 0;
    rows_limit = img.rows;
    cols_limit = img.cols;
  }

  for (int r = rows; r < rows_limit; r++)
  {
    for (int c = cols; c < cols_limit; c++)
    {
      //At each pixel (r, c) in the image, set the intensity value
      //to 255 * the CDF value at (r, c).
      img.at<uchar>(r, c) = 
        round(levels * cdf[img.at<uchar>(r, c)]);
    }
  }
}

//Subtract a histogram equalized image from the orignal image.
//The parameters are the input image, the input image with histogram
//equalization applied, and the destination image (the image that will
//reflect the input image subtracted from the histogram equalized image).
void histogram_equalized_subtract(Mat &img, Mat &equalized_img,
  Mat &dest_img, int ROI_x, int ROI_y, int ROI_width,
  int ROI_height)
{
  int col_limit;
  int row_limit;
  int col_start;
  int row_start;

  //If an ROI was not specified, then subtract the entire histogram
  //equalized image from the input image.
  if ( (ROI_width == 0) && (ROI_height == 0) && (ROI_x == 0) && (ROI_y == 0)) 
  {
    col_limit = img.cols;
    row_limit = img.rows;
    col_start = 0;
    row_start = 0;
  }
  else
  {
    //Otherwise, subtract the histogram equalized image from the
    //input image at only the ROI, which is (ROI_x, ROI_y) to
    //(ROI_x + ROI_width, ROI_y + ROI_height).
    col_limit = ROI_x + ROI_width;
    row_limit = ROI_y + ROI_height;
    col_start = ROI_x;
    row_start = ROI_y;
  }

  for (int cols = col_start; cols < col_limit; cols++)
  {
    for (int rows = row_start; rows < row_limit; rows++)
    {
      //Get the value to apply to the result image by subtracting
      //the coordinate at (rows, cols) in the histogram equalized image
      //from the coordinate at (rows, cols) in the input image
      int value = 
        abs(equalized_img.at<uchar>(rows, cols) - img.at<uchar>(rows, cols));

      //Set the pixel intensity value at (rows, cols) in the destination
      //image to the obtained above value.
      dest_img.at<uchar>(rows, cols) = value;
    }
  }
}
