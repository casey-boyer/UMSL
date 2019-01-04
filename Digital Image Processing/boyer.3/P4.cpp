#include "utilities.h"
#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <fstream>

using namespace cv;
using namespace std;

//The name of the window that will display the image.
const char *WINDOW_NAME = "Histogram Matching";

//The number of pixel intensity values, in the range {0, 1, 2, ..., 255}.
const int NUM_LEVELS = 256;

int main(int argc, char *argv[])
{
  Mat img; //The input image
  Mat ref_img; //The reference image (if provided instead of a file)

  //The reference file (if provided instead of a reference image)
  ifstream ref_file;

  bool is_file = false; //True if the reference is a file
  bool is_image = false; //True if the reference is another image

  //If the user does not provide an input image, print the program usage
  //and exit.
  if ((argc == 2) || (argc == 1))
  {
    cout << "Usage: " << argv[0] << " [image] OR -f file\n";
    return -1;
  }
  else if (argc == 3) //./P4 [input image] [reference image]
  {
    //Check if the reference image is a valid image.
    ref_img = imread(argv[2]);

    if (ref_img.empty())
    {
      cout << "Unable to open image file " << argv[2] << "\n";
      return -1;
    }

    //Convert the reference image to grayscale.
    cvtColor(ref_img, ref_img, CV_BGR2GRAY);

    //Obtain the histogram, normalized histogram, and CDF of the
    //reference image.
    is_image = true;
  }
  else if (argc == 4) //./P4 [input image] -f filename
  {
    //Check if the file exists.
    ref_file.open(argv[3]);

    //If the file is invalid or could not be found, print an error
    //and terminate the program.
    if (!ref_file.is_open())
    {
      cout << "Could not open file " << argv[3] << "\n";
      return -1;
    }

    //Get the histogram from the file, and compute the normalized histogram
    //and CDF based on this reference histogram
    is_file = true;
  }
  else
  {
    cout << "Too many command line arguments: " << argc << "\n";
    cout << "Usage: ./P4 [input image] [reference image] OR " 
      << "./P4 [input image] -f filename\n";
    return -1;
  }

  //Get the input image.
  img = imread(argv[1]);

  cvtColor(img, img, CV_BGR2GRAY);

  //If empty() returns true, then the images are invalid or do not exist.
  //Print an error and exit.
  if (img.empty())
  {
    cout << "Unable to open the picture file: " << argv[1] << "\n";
    return -1;
  }

  //Compute both the histogram, normalized histogram, and CDF of the
  //input image and reference image (or reference file)
  int input_hist[NUM_LEVELS] = {0};
  int ref_hist[NUM_LEVELS] = {0};
  double input_norm_hist[NUM_LEVELS] = {0.0};
  double ref_norm_hist[NUM_LEVELS] = {0.0};
  double input_cdf[NUM_LEVELS] = {0.0};
  double ref_cdf[NUM_LEVELS] = {0.0};

  //Compute the histogram of the input image
  compute_histogram(input_hist, img);

  //Compute the normalized histogram of the input image
  compute_normalized_histogram(input_hist, input_norm_hist, img);

  //Compute the CDF of the input image
  compute_equalized_histogram(input_norm_hist, input_cdf, img);

  if (is_image)
  {
    //If the argument provided as a reference on the command line
    //is an image, compute the histogram, normalized histogram, and
    //CDF of the reference image.
    compute_histogram(ref_hist, ref_img);
    compute_normalized_histogram(ref_hist, ref_norm_hist, ref_img);
    compute_equalized_histogram(ref_norm_hist, ref_cdf, ref_img);
  }
  else if (is_file)
  {
    //If the argument provided as a reference on the command line
    //is a file, obtain the histogram as an int[] array of 256 elements,
    //and then compute the normalized histogram and CDF based on this
    //histogram. The size of the reference image is obtained by summing
    //together all elements of the histogram.

    //String that will hold each line of input from the
    //file
    string hist_line;

    //A counter that will range from 0-255
    int level = 0;

    //The sum of all the elements from the input file
    int sum = 0;

    //Get the histogram from the input file, reading line by
    //line until EOF is reached or the level counter is less than
    //256.
    while ((getline(ref_file, hist_line)) || (level < NUM_LEVELS))
    {
      //Store the string read by the input in a input string stream.
      istringstream iss(hist_line);

      int value;

      //If the value could not be read by the input string stream
      //and converted to an integer, print an error message and 
      //terminate the program.
      if (!(iss >> value))
      {
        cout << "Unable to read the values in the file: \n" << hist_line 
          << " is not a valid value for the reference histogram.\n";
        return -1;
      }
      else if (value <= -1)
      {
        //If the value obtained from the line is negative, print
        //an error message and terminate the program.
        cout << "Reference histogram must have non-negative values.\n";
        return -1;
      }
      else
      {
        //Store the value retrieved from the line
        //into the reference histogram.
        ref_hist[level] = value;

        //Increment the running sum to get the total number
        //of pixels in the reference image.
        sum += value;
      }

      //Increment the level counter.
      level++;
    }

    //Ensure that the number of lines read from the input file was at least
    //256; if not, print an error and terminate the program.
    if (level < NUM_LEVELS)
    {
      cout << "Please provide 256 integers in the file for the reference" <<
        " histogram.\n";
      return -1;
    }

    //Compute the normalized histogram and the CDF of the reference
    //image based on the histogram obtained from the file and the
    //number of pixels obtained from the histogram file (the variable sum).
    compute_normalized_histogram(ref_hist, ref_norm_hist, img, sum);
    compute_equalized_histogram(ref_norm_hist, ref_cdf, img);
  }

  //Loop from l = 0 to l = 255.
  //In this loop, set l_prime = 255;
  //Then, a do-while loop will:
  //	Find l_prime such that cdf_ref[l_prime] is nearly equal
  //	to cdf[l]
  //	-Repeatedly decrement l_prime while l_prime >= 0 and
  //	cdf_ref[l_prime] >= cdf(L)
  //	-f(l) = l_prime is our output function for the lookup table;
  //	since l is fixed during each iteration of the do-while loop,
  //	f(l) will equal l_prime such that cdf_ref[l_prime] >= cdf(L)
  int l_prime;
  int output_function[NUM_LEVELS] = {0};

  for (int l = 0; l < NUM_LEVELS; l++)
  {
    //Initialize l_prime prior to performing the operations in
    //the do-while loop.
    l_prime = 255;

    do
    {
      //Store the value of l_prime at the location f[l]
      output_function[l] = l_prime;

      //Decrement l_prime for each while-loop iteration, until
      //l_prime is less than 0 OR the reference CDF at l_prime
      //is less than the input image CDF at the value l
      l_prime -= 1;

    } while ( (l_prime > 0) && (ref_cdf[l_prime] > input_cdf[l]) );
  }

  //If the input was a file, clost the file.
  if (is_file)
    ref_file.close();

  //Apply the output function at each pixel value to the
  //current pixel so that the histogram of the input image
  //closely resembles that of the reference image
  for (int rows = 0; rows < img.rows; rows++)
  {
    for (int cols = 0; cols < img.cols; cols++)
    {
      img.at<uchar>(rows, cols) = 
        output_function[img.at<uchar>(rows, cols)];
    }
  }

  //Display the image.
  display_img(WINDOW_NAME, img);

  waitKey(0); 

  return 0;
}
