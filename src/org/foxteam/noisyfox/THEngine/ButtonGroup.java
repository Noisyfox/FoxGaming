package org.foxteam.noisyfox.THEngine;

import java.util.ArrayList;
import java.util.List;

import org.foxteam.noisyfox.FoxGaming.Core.FGButton;

public class ButtonGroup {

	private List<FGButton> buttons = new ArrayList<FGButton>();
	private int left = 0;
	private int top = 0;
	private int right = 0;
	private int bottom = 0;
	private int center_x = 0;
	private int center_y = 0;
	private boolean isHorizon = true;

	public void addButton(FGButton button) {
		buttons.add(button);
	}

	public void setPlaceRegion(int left, int top, int right, int bottom,
			int center_x, int center_y) {
		if (left > right || top > bottom || center_x < left || center_x > right
				|| center_y < top || center_y > bottom) {
			throw new IllegalArgumentException();
		}

		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.center_x = center_x;
		this.center_y = center_y;
	}

	/**
	 * 设置按钮的排列方向
	 * 
	 * @param horizon
	 */
	public void setDirection(boolean horizon) {
		isHorizon = horizon;
	}

	public void dismissAll() {
		for (FGButton b : buttons) {
			b.dismiss();
		}
	}

	public void performAll(int stage, int depth) {
		for (FGButton b : buttons) {
			b.perform(stage);
			b.setDepth(depth);
		}
	}

	public void control(float k) {
		if (k < 0)
			k = 0;
		if (k > 1)
			k = 1;

		if (k == 1) {
			for (FGButton b : buttons) {
				b.setEnabled(true);
			}
		} else {
			for (FGButton b : buttons) {
				b.setEnabled(false);
			}
		}

		if (isHorizon) {
			int y_top = (int) ((center_y - top) * k + top);
			int y_bottom = (int) (bottom - (bottom - center_y) * k);
			int i = 1;
			int size = buttons.size() + 1;
			for (FGButton b : buttons) {
				b.setPosition((float) left + (float) (right - left)
						/ (float) size * (float) i, i % 2 == 0 ? y_top
						: y_bottom);
				i++;
			}
		} else {
			int x_left = (int) ((center_x - left) * k + left);
			int x_right = (int) (right - (right - center_x) * k);
			int i = 1;
			int size = buttons.size() + 1;
			for (FGButton b : buttons) {
				b.setPosition(i % 2 == 0 ? x_left : x_right, (float) top
						+ (float) (bottom - top) / (float) size * (float) i);
				i++;
			}
		}

	}

}
