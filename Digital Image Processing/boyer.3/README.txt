Casey Boyer, CS4420: Digital Image Processing
Assignment #3, Due 10/31/18

The functions for computing a histogram, normalized histogram (PDF),
CDF, thresholding, and so on are defined in utilities.cpp, and the
function prototypes are defined in utilities.h.

Invocation:
  'make' to make the executables for each problem

  'make P1' to make the executable for problem #1, and so on

  Execution:
    ./P1 [image]

    ./P2 [image]

    ./P3 [image]

    ./P4 [image] [image] OR ./P4 [image] -f filename
	-where the first image is the input image, and the second image is the
	reference image for histogram matching

	-where the first image is the input image, and the filename contains
	 a file of 256 integers, one value per line.

   ./P5 [image]
	-the input image will be displayed, and you may select the ROI
	  using the left mouse button (the down click is the starting point,
	  and releasing the button is the end point)
	-then, the input image with histogram equalization applied to that
	  area will be displayed

   ./P6 [image] x y width height OR ./P6 [image]
	-the command line arguments are the starting point (x, y) of
	  the ROI and the width and height of the ROI
	-if the ROI is not specified, then the histogram equalized image
	 is subtracted from the entire input image.

Additional information:
  -For problem #1, if a color image is specified, the intensity value
    of the blue, green, and red channel(s) at that location is
    provided in the order B, G, R.

  -Problems #5 and #6 both accept color image as input, and perform
    the operations on each channel (B, G, R) of the color image.

  -For problem #5, to specify the ROI, the image provided in the executable
    invocation will be displayed, and (using the mouse), hold down
    the left mouse button to set the starting point, and release
    the left mouse button when the desired end point is reached.
    Then, histogram equalization will be applied to this region
    and the result will be displayed.

  -For problem #6, to specify the ROI, provide the starting x, starting y, and
    height and width of the ROI as follows:
  './P6 astronaut.jpg 20 40 300 200' for a ROI starting at (20, 40) and
    ending at (320, 240).
  -Additionally, on problem #6, if a color image is specified, 
    histogram equalization and subtraction is applied to each of 
    the channels (blue, green, and red) and then merged for the result.
