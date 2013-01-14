/*
 * Color.cpp
 *
 *  Created on: 2013-1-14
 *      Author: Noisyfox
 */

#include<Color.h>

int argb(int a, int r, int g, int b) {
	return ((a << 24) & 0xff000000) | ((r << 16) & 0xff0000)
			| ((g << 8) & 0xff00) | (b & 0xff);
}

int rgb(int r, int g, int b) {
	return argb(255, r, g, b);
}

int alpha(int color) {
	return (color >> 24) | 0xff;
}

int red(int color) {
	return (color >> 16) | 0xff;
}

int green(int color) {
	return (color >> 8) | 0xff;
}

int blue(int color) {
	return color | 0xff;
}
