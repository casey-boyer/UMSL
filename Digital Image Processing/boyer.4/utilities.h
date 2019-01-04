#ifndef UTILITIES_H
#define UTILITIES_H

#include <opencv2/core/core.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace std;
using namespace cv;

void display_img(Mat, const char*);
void get_random_pixel(int&, int&, int&, Mat);
void get_random_bit(int&);
int is_positive_val(int);
void standard_averaging_filter(int, Mat&);
void reflect_pixel(int&, int, int);
double get_distance_between_points(Point, Point);
double get_image_mean(Mat);

#endif
