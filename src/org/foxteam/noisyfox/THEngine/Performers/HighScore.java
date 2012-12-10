package org.foxteam.noisyfox.THEngine.Performers;

import org.foxteam.noisyfox.FoxGaming.Core.FGButton;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGFrame;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;
import org.foxteam.noisyfox.THEngine.GlobalResources;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;

public final class HighScore extends FGPerformer {

	int highscoreTextColor = Color.rgb(60, 33, 11);
	Paint highscoreTextPaint = new Paint();
	float textSize_Title = 72;// 单位px
	float textSize_Detail = 30;// 单位px
	Canvas tmpCanvas = null;
	Bitmap tmpBitmap = null;
	FGButton share2Weibo = new Button_WeiboShare();
	double hk = 0.05;
	double inX = 0;
	AniState state = AniState.hided;
	int width;
	int height;
	int edgeHeight;
	int edgeWidth;

	static Rect srcRect = new Rect();
	static Rect dstRect = new Rect();
	static FGFrame cachedFrame = new FGFrame();
	static FGSprite cachedSprite = new FGSprite();
	static FGSpriteConvertor aniConvertor = new FGSpriteConvertor();

	static float edgePercentHeight = 35f / 380f;// 图案上下边缘各占的比例
	static float edgePercentWidth = 35f / 300f;// 图案左右边缘各占的比例

	private enum AniState {
		showing, shown, hiding, hided;
	}

	public HighScore() {
		highscoreTextPaint.setTypeface(GlobalResources.FONT_VINERITC);
		highscoreTextPaint.setColor(highscoreTextColor);
	}

	@Override
	protected void onDraw() {
		if (state == AniState.hided)
			return;

		cachedSprite.draw(getCanvas(), (int) inX, cachedSprite.getOffsetY(),
				aniConvertor);
	}

	@Override
	protected void onCreate() {

		// 根据屏幕尺寸计算高分榜大小
		width = FGStage.getCurrentStage().getWidth();
		height = (int) (FGStage.getCurrentStage().getHeight() * 0.8);
		edgeHeight = (int) (height * edgePercentHeight);
		edgeWidth = (int) (width * edgePercentWidth);
		float k = width / 480f;

		// 准备临时画布
		tmpBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		tmpCanvas = new android.graphics.Canvas(tmpBitmap);
		tmpCanvas.setDrawFilter(new PaintFlagsDrawFilter(0,
				Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
		tmpCanvas.drawARGB(0, 255, 255, 255);
		Bitmap backgroundBitmap = GlobalResources.FRAMES_HIGHSCORE_BACKGROUND
				.getFrame(0);

		srcRect.set(0, 0, backgroundBitmap.getWidth(),
				backgroundBitmap.getHeight());
		dstRect.set(0, 0, width, height);

		tmpCanvas.drawBitmap(backgroundBitmap, srcRect, dstRect, null);

		// 绘制标题
		highscoreTextPaint.setTextAlign(Align.CENTER);
		highscoreTextPaint.setTextSize(textSize_Title * k);
		tmpCanvas.drawText("HighScore", width / 2, edgeHeight + textSize_Title
				* k / 2f, highscoreTextPaint);

		// 绘制分数
		highscoreTextPaint.setTextSize(textSize_Detail * k);
		int y_start = (int) (edgeHeight + textSize_Title * k + 6 + textSize_Detail
				* k / 2);
		int y_end = (int) (height - edgeHeight
				- GlobalResources.FRAMES_BUTTON_SINAWEIBO.getHeight()
				- textSize_Detail * k / 2 - 6);
		int x = 0;
		float dy = (y_end - y_start) / 10;
		// 绘制左边一列：姓名
		highscoreTextPaint.setTextAlign(Align.LEFT);
		x = edgeWidth + 3;
		tmpCanvas.drawText("NAME", x, y_start, highscoreTextPaint);
		for (int i = 1; i <= 10; i++) {
			tmpCanvas.drawText("anonym", x, y_start + dy * i,
					highscoreTextPaint);
		}
		// 绘制右边一列：分数
		highscoreTextPaint.setTextAlign(Align.RIGHT);
		x = width - edgeWidth - 3;
		tmpCanvas.drawText("SCORE", x, y_start, highscoreTextPaint);
		for (int i = 1; i <= 10; i++) {
			tmpCanvas.drawText("0", x, y_start + dy * i, highscoreTextPaint);
		}

		cachedFrame.loadFromBitmap(tmpBitmap);
		cachedSprite.bindFrames(cachedFrame);
		cachedSprite.setOffset(0, cachedSprite.getHeight() / 2);
		aniConvertor.setScale(1, hk);

	}

	@Override
	protected void onStep() {
		switch (state) {
		case showing:
			if (inX >= 0) {
				inX = 0;

				hk += 0.1;
				if (hk >= 1) {
					hk = 1;
					state = AniState.shown;

					share2Weibo.perform(FGStage.getCurrentStage()
							.getStageIndex());
					share2Weibo.setDepth(depth - 1);
					share2Weibo.setPosition(
							width - edgeWidth - share2Weibo.getWidth() / 2 - 3,
							height - edgeHeight - share2Weibo.getHeight() / 2
									- 3);
				}
				aniConvertor.setScale(1, hk);
			} else {
				inX += 30;
				if (inX >= 0) {
					inX = 0;
				}
			}
			break;
		case shown:
			break;
		case hiding:
			if (hk > 0.05) {
				hk -= 0.1;
				if (hk < 0.05)
					hk = 0.05;
			} else {
				hk = 0.05;

				if (inX > -FGStage.getCurrentStage().getWidth()) {
					inX -= 30;
				} else {
					state = AniState.hided;
				}
			}
			aniConvertor.setScale(1, hk);

			break;
		case hided:
			this.dismiss();
			break;
		}
	}

	public void show() {
		if (state != AniState.hided)
			return;
		state = AniState.showing;

		perform(FGStage.getCurrentStage().getStageIndex());
		inX = -FGStage.getCurrentStage().getWidth();
		hk = 0.05;

	}

	public void hide() {
		if (state != AniState.shown)
			return;

		share2Weibo.dismiss();
		state = AniState.hiding;
	}

	@Override
	protected void onDestory() {
		share2Weibo.dismiss();
		state = AniState.hided;
	}

}
