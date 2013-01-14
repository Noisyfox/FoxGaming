/*
 * Color.h
 *
 *  Created on: 2013-1-14
 *      Author: Noisyfox
 */

#ifndef COLOR_H_
#define COLOR_H_
#ifdef __cplusplus
extern "C" {
#endif

const int BLACK = -16777216;
const int DKGRAY = -12303292;
const int GRAY = -7829368;
const int LTGRAY = -3355444;
const int WHITE = -1;
const int RED = -65536;
const int GREEN = -16711936;
const int BLUE = -16776961;
const int YELLOW = -256;
const int CYAN = -16711681;
const int MAGENTA = -65281;
const int TRANSPARENT = 0;

int argb(int a, int r, int g, int b);
int rgb(int r, int g, int b);
int alpha(int color);
int red(int color);
int green(int color);
int blue(int color);
void RGBToHSV(int red, int green, int blue, float hsv[]);
void colorToHSV(int color, float hsv[]);
int HSVToColor(float hsv[]);
int aHSVToColor(int alpha, float hsv[]);

#ifdef __cplusplus
}
#endif
#endif /* COLOR_H_ */
