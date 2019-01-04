#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <iomanip>
#include <time.h>
#include <stdlib.h>
#include <string>
#include <cstddef>
#include <bitset>
#include "utilities.h"

using namespace std;
using namespace cv;

//The name of the window that will display the image.
const char *WINDOW_NAME = "Window";

//The number of images to be generated
const int NUM_IMAGES = 10;

//The size of each byte in a grayscale image
const int BYTE_SIZE = 8;

//The number of pixel intensity values in a grayscale image
const int LEVELS = 256;

int main(int argc, char *argv[])
{
  //If the user does not provide an image file as an argument,
  //display program usage and terminate.
  if (argc <= 2)
  {
    cout << "Usage: " << argv[0] << " [image] n\n";
    return -1;
  } 

  //Seed the random number
  srand(time(NULL));

  //Read the image file provided as an argument.
  Mat img = imread(argv[1], CV_8UC1);

  Mat added_imgs = Mat::zeros(img.rows, img.cols, CV_8UC1);

  //Create a vector of 10 Mat (for each image).
  vector<Mat> noise_imgs (NUM_IMAGES);

  //Get the name of the input image file, and create a vector
  //of 10 strings, where each string is the name of the input image
  //file. This will be used to name each image with noise added to it.
  vector<string> img_names (NUM_IMAGES, argv[1]);

  //Convert the command line argument for the percentage to
  //an int using strtol() and specifying decimal base.
  int percentage = strtol(argv[2], NULL, 10);

  //The size of the image is the image rows multiplied with
  //the image columns.
  int img_size = (img.rows * img.cols);

  //The sum of the pixel intensities of each generated image. Intialize
  //each element to 0. The vector will be of size equal to the image size,
  //since this accounts for each pixel in the image.
  vector<int> pixel_intensity_sum(img_size, 0);

  //This will report the absolute difference between the integrated
  //image and the input image.
  double distance;

  //The randomly selected pixel in an image.
  int random_pixel;
  int random_row;
  int random_column;

  //The randomly selected bit position of a byte in a pixel
  int bit_pos;
  int num_bits; //The number of bits to be flipped in the image(s)

  //If the image file is empty, does not exist, or cannot be read,
  //display an error and terminate the program.
  if (img.empty())
  {
    cout << "Could not open or read the image file " << argv[1] << "\n";
    return -1;
  }

  cout << "Percentage of bits to be flipped is: " << percentage << "%.\n";

  //The number of bits to be flipped in the image is the image size
  //multiplied by 8, and then dividing this result by the percentage.
  num_bits = round((img_size * BYTE_SIZE) * 
    ((double)percentage / (double)100));

  //Get the name of the image, before the image extension type.
  //Ex: this will return the location of the t before .jpg 
  //in 'astronaut.jpg'
  string input_img_name = argv[1];
  size_t found = input_img_name.find_last_of(".");

  //Create the 10 images.
  for (int counter = 0; counter < NUM_IMAGES; counter++)
  {
    //Intialize the image that will have noise added to it. By
    //invoking img.clone(), the Mat at this counter will have
    //a deep copy of the input image; thus, the Mat at this counter
    //will have its own copy of pixels and will not modify the input image.
    noise_imgs.at(counter) = img.clone();

    //The number of the image, i.e. '00' or '07'
    string img_number = "0";
    stringstream str_stream;
    str_stream << counter;

    //Store the name of this image in the img_names vector
    img_number.append(str_stream.str());
    img_names.at(counter).insert(found, img_number);

    for (int i = 0; i <= num_bits; i++)
    {
      //Get the random pixel, as well as the (row, column) location
      //of the randomly selected pixel.
      get_random_pixel(random_pixel, random_row, random_column, 
        noise_imgs.at(counter));

      //Get the 8 bits of the randomly selected pixel.
      bitset<8> pixel_bit(random_pixel);

      //Get the position of the random bit to be flipped.
      get_random_bit(bit_pos);

      //Flip the bit at the position of bit_pos in the pixel.
      pixel_bit.flip(bit_pos);

      //Get the value of the bitset as an int
      int pixel_val = (int)pixel_bit.to_ulong();

      //Assign the pixel at the random location to the value of the
      //pixel with the specified flipped bit
      noise_imgs.at(counter).at<uchar>(random_row, random_column) =
        pixel_val;
    }

    //Save the image to this directory
    imwrite(img_names.at(counter).c_str(), noise_imgs.at(counter));

    //Sum for the pixel intensity values
    int i = 0;

    //Get the sum of all pixel intensity values in each image in the 
    for (int r = 0; r < noise_imgs.at(counter).rows; r++)
    {
      for (int c = 0; c < noise_imgs.at(counter).cols; c++)
      {
        pixel_intensity_sum[i] += noise_imgs.at(counter).at<uchar>(r, c);
        i++;
      }
    }
  }

  //Divide each pixel intensity sum value by the number of images
  for (int counter = 0; counter < img_size; counter++)
  {
    pixel_intensity_sum[counter] /= NUM_IMAGES;
  }

  int i = 0;

  for (int r = 0; r < img.rows; r++)
  {
    for (int c = 0; c < img.cols; c++)
    {
      //Set the pixel at (r, c) in the integrated image to the pixel
      //intensity sum obtained above
      added_imgs.at<uchar>(r, c) = pixel_intensity_sum[i];
      i++;
    }
  }

  //Calculate the absolute difference between the integrated image
  //and the input image. The constant 'NORM_L2' specifies that the
  //distance should be taken as euclidean distance.
  distance = norm(img, added_imgs, NORM_L2);

  cout << "The distance between the integrated image and the input image is "
    << (int)distance << "\n";

  //Display the 10 generated images
  for (int counter = 0; counter < NUM_IMAGES; counter++)
  {
    display_img(noise_imgs.at(counter), img_names.at(counter).c_str());
  }

  //Display the image.
  display_img(img, WINDOW_NAME);
  display_img(added_imgs, "Integrated image");

  waitKey(0);

  return 0;
}
