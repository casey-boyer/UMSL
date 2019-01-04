#include "utilities.h"
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <string>

using namespace cv;
using namespace std;

//The name of the window that will display the image.
const char *WINDOW_NAME = "Image";

//The event handler that will handle mouse events from the user.
void mouse_event_handler(int, int, int, int, void*);

int main(int argc, char *argv[])
{
  //If the user does not provide an input image, print the program usage
  //and exit.
  if (argc == 1)
  {
    cout << "Usage: " << argv[0] << " [image]\n";
    return -1;
  }

  //Get the input image.
  Mat img = imread(argv[1]);

  //If empty() returns true, then the images are invalid or do not exist.
  //Print an error and exit.
  if (img.empty())
  {
    cout << "Unable to open the picture file: " << argv[1] << "\n";
    return -1;
  }

  //Create the named window.
  namedWindow(WINDOW_NAME);

  //Set the mouse handler for the created window, using the 
  //mouse_event_handler function, and passing a reference to the Mat img
  //as the userdata parameter so that the input image may be modified
  //in real time as the user clicks.
  setMouseCallback(WINDOW_NAME, mouse_event_handler, &img);

  //Display the image.
  imshow(WINDOW_NAME, img);

  waitKey(0);

  return 0;
}

void mouse_event_handler(int event, int x, int y, int flags, void *data)
{
  //Get the input image.
  Mat *img = (Mat*) data;

  //Only respond to events with left mouse clicks.
  if (event == EVENT_LBUTTONDOWN)
  {
    if (is_grayscale_img((*img)))
    {
      //If the image provided is a grayscale image, get
      //the intensity of the pixel at the location the user clicked.
      int pixel_value = (*img).at<uchar>(y, x);

      //Get the intensity value as a string.
      string text = get_string_text(pixel_value);
      
      //Get the location where the mouse click occurred.
      Point location = Point(x, y);

      //Write the text to the image at the location the user clicked.
      write_text((*img), text, location);

      cout << "Pixel intensity is: " << text << "\n";

      //Show the image with the text added to it.
      imshow(WINDOW_NAME, (*img));
    }
    else
    {
      //If the input image is a color image, get the value of the
      //intensity of each channel at the location the user clicked.
      Vec3b pixel_intensities = (*img).at<Vec3b>(y, x);

      //Get the value of the blue channel intensity, the green
      //channel intensity, and the red channel intensity.
      int blue_pixel_value = pixel_intensities.val[0];
      int green_pixel_value = pixel_intensities.val[1];
      int red_pixel_value = pixel_intensities.val[2];

      //Create the text to be written to the screen as a string
      //B, G, R
      string text = get_string_text(blue_pixel_value);
      text.append(", ");
      text.append(get_string_text(green_pixel_value));
      text.append(", ");
      text.append(get_string_text(red_pixel_value));

      cout << "Pixel intensity is: " << text << "\n";

      //Get the location where the user clicked.
      Point location = Point(x, y);

      //Write the text to the image at the location the user clicked.
      write_text((*img), text, location);

      //Show the image with the text added to it.
      imshow(WINDOW_NAME, (*img));
    }
  }
}
