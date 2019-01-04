#ifndef UTILITIES_H
#define UTILITIES_H
#include <opencv2/core/core.hpp>
#include <opencv2/opencv.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace std;
using namespace cv;

void display_img(Mat, const char*);
void create_power_spectrum(Mat, Mat&, Mat[]);
void filter_frequency(Mat&, Mat&, Mat&, Rect);
void crop_rearrange(Mat&);

#endif
