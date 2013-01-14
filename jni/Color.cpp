/*
 * Color.cpp
 *
 *  Created on: 2013-1-14
 *      Author: Noisyfox
 */

#include<math.h>

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

void RGBToHSV(int red, int green, int blue, float hsv[]) {
	double R, G, B, Max, Min, del_R, del_G, del_B, del_Max;
	R = red / 255.0;       //Where RGB values = 0 ÷ 255
	G = green / 255.0;
	B = blue / 255.0;

	Min = fmin(R, fmin(G, B));    //Min. value of RGB
	Max = fmax(R, fmax(G, B));    //Max. value of RGB
	del_Max = Max - Min;        //Delta RGB value

	hsv[2] = (Max + Min) / 2.0;

	if (del_Max == 0)           //This is a gray, no chroma...
			{
		hsv[0] = 0;                  //HSL results = 0 ÷ 1
		hsv[1] = 0;
	} else                        //Chromatic data...
	{
		if (hsv[2] < 0.5)
			hsv[1] = del_Max / (Max + Min);
		else
			hsv[1] = del_Max / (2 - Max - Min);

		del_R = (((Max - R) / 6.0) + (del_Max / 2.0)) / del_Max;
		del_G = (((Max - G) / 6.0) + (del_Max / 2.0)) / del_Max;
		del_B = (((Max - B) / 6.0) + (del_Max / 2.0)) / del_Max;

		if (R == Max)
			hsv[0] = del_B - del_G;
		else if (G == Max)
			hsv[0] = (1.0 / 3.0) + del_R - del_B;
		else if (B == Max)
			hsv[0] = (2.0 / 3.0) + del_G - del_R;

		if (hsv[0] < 0)
			hsv[0] += 1;
		if (hsv[0] > 1)
			hsv[0] -= 1;
	}
	hsv[0] *= 360;
}

void colorToHSV(int color, float hsv[]) {
	RGBToHSV(red(color), green(color), blue(color), hsv);
}

double Hue2RGB(double v1, double v2, double vH);

int HSVToColor(float hsv[]) {
	return aHSVToColor(255, hsv);
}

int aHSVToColor(int alpha, float hsv[]) {
	double R, G, B;
	double var_1, var_2;
	if (hsv[1] == 0)                       //HSL values = 0 ÷ 1
			{
		R = hsv[2] * 255.0;                   //RGB results = 0 ÷ 255
		G = hsv[2] * 255.0;
		B = hsv[2] * 255.0;
	} else {
		if (hsv[2] < 0.5)
			var_2 = hsv[2] * (1 + hsv[1]);
		else
			var_2 = (hsv[2] + hsv[1]) - (hsv[1] * hsv[2]);

		var_1 = 2.0 * hsv[2] - var_2;

		R = 255.0 * Hue2RGB(var_1, var_2, hsv[0] + (1.0 / 3.0));
		G = 255.0 * Hue2RGB(var_1, var_2, hsv[0]);
		B = 255.0 * Hue2RGB(var_1, var_2, hsv[0] - (1.0 / 3.0));
	}

	return argb(alpha, R, G, B);
}

double Hue2RGB(double v1, double v2, double vH) {
	if (vH < 0)
		vH += 1;
	if (vH > 1)
		vH -= 1;
	if (6.0 * vH < 1)
		return v1 + (v2 - v1) * 6.0 * vH;
	if (2.0 * vH < 1)
		return v2;
	if (3.0 * vH < 2)
		return v1 + (v2 - v1) * ((2.0 / 3.0) - vH) * 6.0;
	return (v1);
}
