#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>
#include <iostream>
#include <unistd.h>
#include <stdlib.h>
#include "utilities.h"

using namespace std;
using namespace cv;

//The name of the window that will display the input image.
const char *WINDOW_NAME = "Image with zoom";

//The minimum zoom factor the user may select
const int MIN_ZOOM_FACTOR = 1;

//Number of arguments expected on the command line
const int NUM_ARGS = 13;

int main(int argc, char *argv[])
{
  //This is the error that will print upon recieving erroroneous values
  //on the command line.
  string usage = "Usage: ";
  usage.append(argv[0]);
  usage.append(" -z X -R X -C X -H X -W X [input_image] [output_image]");

  //This is the error that will print if one of the command line arguments
  //is negative.
  string negative_error = "must be positive";

  //The current command line argument
  int opt;

  //The zoom factor, center (rows, cols), height and width of the zoom factor
  //as specified on the command line
  int zoom_factor, zoom_center_rows, zoom_center_columns, zoom_height,
    zoom_width;
  int z_flag = 0; //If the -z argument is provided
  int r_flag = 0; //If the -R argument is provided
  int c_flag = 0; //If the -C argument is provided
  int h_flag = 0; //If the -H argument is provided
  int w_flag = 0; //If the -W argument is provided

  //Check if all command line arguments were provided.
  while ((opt = getopt(argc, argv, "z:R:C:H:W:")) != -1)
  {
    switch (opt)
    {
      case 'z': //-z option
        //Store the zoom_factor
        zoom_factor = atoi(optarg);

        //If negative, print an error and terminate
        if (is_positive_val(zoom_factor) == -1)
        {
          cout << "The zoom factor " << negative_error << ": " <<
            zoom_factor << "\n";
          return -1;
        }

        z_flag = 1; //-z option is present
        break;
      case 'R':
        //Store the zoom center rows
        zoom_center_rows = atoi(optarg);

        //If negative, print an error and terminate
        if (is_positive_val(zoom_center_rows) == -1)
        {
          cout << "The center " << negative_error << ": " 
            << zoom_center_rows << "\n";
          return -1;
        }

        r_flag = 1; //-R option is present
        break;
      case 'C':
        //Store the zoom center columns
        zoom_center_columns = atoi(optarg);

        //If negative, print an error and terminate
        if (is_positive_val(zoom_center_columns) == -1)
        {
          cout << "The center " << negative_error << ": " 
            << zoom_center_columns << "\n";
          return -1;
        }

        c_flag = 1; //-C option is present
        break;
      case 'H':
        //Store the zoom height
        zoom_height = atoi(optarg);

        //If negative, print an error and terminate
        if (is_positive_val(zoom_height) == -1)
        {
          cout << "The height " << negative_error << ": " <<
            zoom_height << "\n";
          return -1;
        }

        h_flag = 1; //-H option is present
        break;
      case 'W':
        //Store the zoom width
        zoom_width = atoi(optarg);

        //If negative, print an error and terminate
        if (is_positive_val(zoom_width) == -1)
        {
          cout << "The height " << negative_error << ": " <<
            zoom_height << "\n";
          return -1;
        }

        w_flag = 1; //-W flag present
        break;
      default: 
        //If an above option is provided with no argument, or
        //an option not specified is provided
        cout << usage << "\n";
        exit(EXIT_FAILURE);
    }
  }

  //If the user provided no command line arguments
  if ((opt == -1) && (argc == 1))
  {
    cout << usage << "\n";
    return -1;
  }

  //If the user did not provide all of the command line arguments
  if ( (z_flag == 0) || (r_flag == 0) || (c_flag == 0) || (h_flag == 0)
    || (w_flag == 0))
  {
    cout << "The zoom factor, center coordinates of the zoom window, and "
      << "the width and height of the zoom window are required.\n";
   return -1;
  }

  if (argc < NUM_ARGS)
  {
    cout << usage << "\n";
    return -1;
  }

  //Read the image file provided as an argument.
  Mat img = imread(argv[optind], CV_8UC1);

  //Create a binary image with the dimensions of the zoom width and
  //height.
  Mat zoom_img(zoom_height, zoom_width, CV_8UC1);

  //Get the size of the input image.
  int img_size = (img.rows * img.cols);

  //Get the name of the output image (which the zoomed image will be
  //written to).
  string output_image_name = argv[optind + 1];

  //If the image file is empty, does not exist, or cannot be read,
  //display an error and terminate the program.
  if (img.empty())
  {
    cout << "Could not open or read the image file " << argv[1] << "\n";
    return -1;
  }

  //The begin (rows, cols) coordinate of the area to be zoomed and
  //the end (rows, cols) coordinate of the area to be zoomed.
  int zoom_begin_rows = zoom_center_rows - (zoom_height / 2);
  int zoom_begin_cols = zoom_center_columns - (zoom_width / 2);
  int zoom_end_rows = zoom_center_rows + (zoom_height / 2);
  int zoom_end_cols = zoom_center_columns + (zoom_width / 2);

  //Check if the given parameters are within the bounds of the image.
  if (zoom_factor > max(img.rows, img.cols))
  {
    cout << "Zoom factor cannot be larger than the image.\n";
    return -1;
  }

  //Check if the zoom height or zoom width is beyond the image
  //boundaries.
  if ((zoom_height > img.rows) || (zoom_width > img.cols))
  {
    cout << "The width and height of the zoom factor cannot be larger than "
      << "the image's width and height.\n";
    return -1;
  }

  //Check if the begin area of the zoom is greater than 0.
  if ((zoom_begin_rows < 0) || (zoom_begin_cols < 0))
  {
    cout << "The center of the zoom and width and height of the zoom must "
      << "be within the image's boundaries.\n";
    cout << "zoom_begin_rows: " << zoom_begin_rows << ", zoom_begin_cols = "
      << zoom_begin_cols << "\n";
    return -1;
  }

  //Check if the end area of the zoom is less than the image
  //boundaries.
  if ((zoom_end_rows > img.rows) || (zoom_end_cols > img.cols))
  {
    cout << "The center of the zoom and width and height of the zoom cannot "
      << "exceed the image's boundaries.\n";
    return -1;
  }

  //The beginning (rows, cols) coordinate of the ROI to be zoomed and
  //the end (rows, cols) coordinate of the ROI to be zoomed.
  int start_row = zoom_center_rows - ((zoom_height / 2) / zoom_factor);
  int start_column = zoom_center_columns - ((zoom_width / 2) / zoom_factor);
  int max_row = zoom_center_rows + ((zoom_height / 2) / zoom_factor);
  int max_column = zoom_center_columns + ((zoom_width / 2) / zoom_factor);

  //To go through the entire block and replicate pixels as
  //necessary, then divide the zoom factor by 2. This will be used
  //when getting the (x, y) index of the element in the block
  //where the pixel will be replicated.
  int range = (zoom_factor / 2);
  int zoom_r = range;
  int zoom_c = range;

  for (int rows = start_row; rows < max_row; rows++)
  {
    for (int cols = start_column; cols < max_column; cols++)
    {
      //This pixel at (rows, cols) needs to be placed in the
      //zoom factor block n times, where n is the zoom factor
      for (int i = 0; i < zoom_factor; i++)
      {
        for (int j = 0; j < zoom_factor; j++)
        {
          //int zoom_y = (zoom_r + (i - range));
          //int zoom_x = (zoom_c + (j - range));

          zoom_img.at<uchar>(zoom_r + (i - range), zoom_c + (j - range)) =
            img.at<uchar>(rows, cols);
        }
      }
      zoom_c += zoom_factor;
    }
    zoom_c = range;
    zoom_r += zoom_factor;
  }


  //Copy back into image
  zoom_r = start_row;
  zoom_c = start_column;

  for (int rows = 0; rows < zoom_img.rows; rows++)
  {
    for (int cols = 0; cols < zoom_img.cols; cols++)
    {
      //If the (zoom_r, zoom_c) coordinate is within the
      //image boundaries, copy the pixel value of the zoom image
      //at (rows, cols) to the pixel value in the input image
      //at (zoom_r, zoom_c).
      if ((zoom_r < img.rows) && (zoom_c < img.cols))
      {
        img.at<uchar>(zoom_r, zoom_c) = zoom_img.at<uchar>(rows, cols);
        zoom_c++;
      }
    }
    zoom_c = start_column; //Reset the zoom column
    zoom_r++; //Increment the zoom row
  }

  //Save and write the output image to the directory.
  imwrite(output_image_name.c_str(), img);

  //Display the image.
  display_img(img, WINDOW_NAME);
  
  waitKey(0);

  return 0;
}
