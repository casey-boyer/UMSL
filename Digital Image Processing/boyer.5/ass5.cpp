#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include "utilities.h"

using namespace std;
using namespace cv;

//For the real and imaginary parts of the image.
const int NUM_PLANES = 2;

//The name of the image to remove noise from
const char *INPUT_IMAGE = "noisy_backyard.jpg";

//The name of the windows to be displayed
const char *OUTPUT_WINDOW = "Input image with noise removal";
const char *SPECTRUM_BEFORE_WINDOW = "Spectrum before noise removal";
const char *SPECTRUM_AFTER_WINDOW = "Spectrum after noise removal";

//This is the event handler for the mouse callback. This was
//used to find the ROIs where noise needed to be removed.
void mouse_event_handler(int, int, int, int, void*);

//This was used to find an ROI to find where noise needed to be removed.
Point start_point;
Point end_point;

int main(int argc, char *argv[])
{
  Mat input_img = imread(INPUT_IMAGE, CV_LOAD_IMAGE_GRAYSCALE);
  
  if (input_img.empty())
  {
    cout << "Unable to open or locate picture file " << INPUT_IMAGE << "\n";
    return -1;
  }

  Mat output_img;
  Mat padded;
  Mat complex_img;
  
  int m = getOptimalDFTSize(input_img.rows);
  int n = getOptimalDFTSize(input_img.cols);

  //Expand the input image to the optimal size, and add 0 to the
  //border values.
  copyMakeBorder(input_img, padded, 0, m - input_img.rows, 0,
    n - input_img.cols, BORDER_CONSTANT, Scalar::all(0));

  //For the real and imaginary parts of the image.
  Mat planes[] = {Mat_<float>(padded), Mat::zeros(padded.size(), CV_32F)};

  merge(planes, NUM_PLANES, complex_img);
  
  dft(complex_img, complex_img);

  //Compute the magnitude of the spectrum.
  Mat magnitude_img;
  create_power_spectrum(complex_img, magnitude_img, planes);

  //Display the spectrum before noise removal.
  display_img(magnitude_img, SPECTRUM_BEFORE_WINDOW);

  //These are the ROIs, obtained from the spectrum, where noise
  //needs to be removed.
  Rect rightmost_top = Rect(Point(721, 315), Point(740, 328));
  Rect middle_top = Rect(Point(640, 314), Point(653, 326));
  Rect leftmost_top = Rect(Point(555, 317), Point(569, 324));
  Rect leftmost_vertical_middle = Rect(Point(542, 359), Point(568, 363));
  Rect leftmost_vertical_bottom = Rect(Point(540, 396), Point(566, 410));
  Rect middle_bottom = Rect(Point(628, 399), Point(647, 409));
  Rect rightmost_vertical_bottom = Rect(Point(711, 398), Point(730, 405));
  Rect rightmost_vertical_middle = Rect(Point(714, 358), Point(744, 363));

  //Remove the noise from the above ROIs in the image.
  filter_frequency(magnitude_img, planes[0], planes[1], rightmost_top);
  filter_frequency(magnitude_img, planes[0], planes[1], middle_top);
  filter_frequency(magnitude_img, planes[0], planes[1], leftmost_top);
  filter_frequency(magnitude_img, planes[0], planes[1], 
    leftmost_vertical_middle);
  filter_frequency(magnitude_img, planes[0], planes[1], 
    leftmost_vertical_bottom);
  filter_frequency(magnitude_img, planes[0], planes[1], middle_bottom);
  filter_frequency(magnitude_img, planes[0], planes[1], 
    rightmost_vertical_bottom);
  filter_frequency(magnitude_img, planes[0], planes[1], 
    rightmost_vertical_middle);

  //Merge the planes (the real and imaginary parts) back into
  //the complex image.
  merge(planes, NUM_PLANES, complex_img);

  //Compute the inverse DFT of the image with the modified real and
  //imaginary parts, and obtain the real part of the result.
  idft(complex_img, complex_img, DFT_REAL_OUTPUT);

  //Normalize the image values between 0-1 so it is viewable.
  normalize(complex_img, complex_img, 0, 1, CV_MINMAX);

  //Display the spectrum after noise removal, and display the image
  //with noise removal.
  display_img(magnitude_img, SPECTRUM_AFTER_WINDOW);
  display_img(complex_img, OUTPUT_WINDOW);
  waitKey(0);

  return 0;  
}

/*This was used to gather the noise that was displayed on the
 * spectrum.*/
void mouse_event_handler(int event, int x, int y, int flags, void *img)
{
  if (event == EVENT_LBUTTONDOWN)
  {
    start_point.x = x;
    start_point.y = y;
    cout << "Start point: (" << start_point.x << ", " << start_point.y << ")\n";
  }
  else if (event == EVENT_LBUTTONUP)
  {
    end_point.x = x;
    end_point.y = y;
    cout << "End point: (" << end_point.x << ", " << end_point.y << ")\n";
  }
}
