package org.foxteam.noisyfox.THEngine.Performers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.foxteam.noisyfox.FoxGaming.Core.FGGameCore;
import org.foxteam.noisyfox.FoxGaming.Core.FGGamingThread;
import org.foxteam.noisyfox.FoxGaming.Core.FGPerformer;
import org.foxteam.noisyfox.FoxGaming.Core.FGStage;
import org.foxteam.noisyfox.FoxGaming.G2D.FGFrame;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSpriteConvertor;
import org.foxteam.noisyfox.THEngine.THEngineMainActivity;
import org.foxteam.noisyfox.common.huawei.SDKUtil;

import com.imax.vmall.sdk.android.common.adapter.ServiceCallback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Message;
import android.widget.EditText;

public final class WeiboShareProcessor extends FGPerformer {
	Bitmap screenShot;
	private FGFrame ssf = new FGFrame();
	private FGSprite sss = new FGSprite();
	private FGSpriteConvertor sssc = new FGSpriteConvertor();
	private boolean caped = false;
	private float k = 0;
	private boolean ap = false;
	private boolean showConfirm = false;
	static AniState state = AniState.hided;
	Paint p = new Paint();
	private static String shareStr = "";

	private enum AniState {
		showing, shown, hiding, hided;
	}

	@Override
	protected void onDraw() {
		getCanvas().drawRGB(0, 0, 0);
		sss.draw(getCanvas(), 0, 0, sssc);
		if (!caped) {
			int y = (int) (k * FGStage.getCurrentStage().getHeight() / 2);
			getCanvas().drawRect(0, 0, FGStage.getCurrentStage().getWidth(), y,
					p);
			getCanvas().drawRect(0, FGStage.getCurrentStage().getHeight() - y,
					FGStage.getCurrentStage().getWidth(),
					FGStage.getCurrentStage().getHeight(), p);
		}
		if (showConfirm) {

		}
	}

	@Override
	protected void onCreate() {
		p.setColor(Color.WHITE);
		screenShot = FGGamingThread.getScreenshots();
		ssf.loadFromBitmap(screenShot);
		sss.bindFrames(ssf);
		sssc.setAlpha(1);
		freezeAll(true, true);
		state = AniState.showing;
		showConfirm = false;

		try {
			saveMyBitmap(screenShot, Environment.getExternalStorageDirectory()
					.getPath() + "/fox_share.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void onStep() {
		switch (state) {
		case showing:
			if (!caped) {
				if (!ap) {
					k += 0.2;
					if (k >= 1) {
						k = 1;
						ap = true;
					}
				} else {
					k -= 0.2;
					if (k <= 0) {
						k = 0;
						caped = true;
					}
				}
			} else {
				if (sssc.getAlpha() > 0.5) {
					sssc.setAlpha(sssc.getAlpha() - 0.1);// 屏幕变暗
				} else {
					state = AniState.shown;
					shareItHandled();
				}
			}
			break;
		case shown:
			break;
		case hiding:
			if (sssc.getAlpha() < 1) {
				sssc.setAlpha(sssc.getAlpha() + 0.1);
			} else {
				state = AniState.hided;
			}
			break;
		case hided:
			this.dismiss();
			break;
		}
	}

	@Override
	protected void onDestory() {
		freezeAll(false, true);
	}

	/**
	 * 分享
	 * 
	 * @param score
	 */
	public static void shareIt() {
		final EditText e = new EditText(FGGameCore.getMainContext());
		e.setText("我正在玩 \"卡通射击\" ~这是我的截图~赶快去下载一个和我一较高下吧！看看谁能一命通关！");
		new AlertDialog.Builder(FGGameCore.getMainContext())
				.setTitle("分享到新浪微博")
				.setView(e)
				.setPositiveButton("分享", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// 准备开始分享
						if (!SDKUtil.isInited()) {
							SDKUtil.sdkInit(FGGameCore.getMainContext(), null);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						if (!SDKUtil.isInited()) {
							THEngineMainActivity.showToast("哎呀~好像出了点问题呢");
						} else {
							shareStr = e.getText().toString();
							SDKUtil.oauth("sina",
									THEngineMainActivity.RESULT_SHAREHIGHSCORE);
						}
					}
				})
				.setNegativeButton("算了吧",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								state = AniState.hiding;
							}
						}).show();
	}

	public static void shareItHandled() {
		Message message = new Message();
		message.what = THEngineMainActivity.MESSAGE_SHAREWEIBO;
		THEngineMainActivity.getHandler().sendMessage(message);
	}

	public void saveMyBitmap(Bitmap bitmap, String filePath) throws IOException {
		File f = new File(filePath);
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sendShare() {
		state = AniState.hiding;
		THEngineMainActivity.showToast("正在后台为您织微博呢~");
		SDKUtil.statusShare("sina", shareStr, Environment
				.getExternalStorageDirectory().getPath() + "/fox_share.png",
				new ServiceCallback() {
					@Override
					public void onComplete(String arg0) {
						THEngineMainActivity.showToast("分享成功~多谢您的支持~");
					}

					@Override
					public void onError(String arg0) {
						THEngineMainActivity.showToast("哎呀~好像出了点问题呢");
					}
				});
	}

}
