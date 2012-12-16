/**
 * FileName:     GlobalResources.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-9-10 下午8:19:35
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-9-10      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.THEngine;

import javax.microedition.khronos.opengles.GL10;

import org.foxteam.noisyfox.FoxGaming.Core.FGEGLHelper;
import org.foxteam.noisyfox.FoxGaming.Core.FGGameCore;
import org.foxteam.noisyfox.FoxGaming.G2D.FGFrame;
import org.foxteam.noisyfox.FoxGaming.G2D.FGGraphicFont;
import org.foxteam.noisyfox.FoxGaming.G2D.FGPathBezier3;
import org.foxteam.noisyfox.FoxGaming.G2D.FGSprite;
import org.foxteam.noisyfox.FoxGaming.G2D.Particle.FGParticleType;

import android.graphics.Typeface;

/**
 * @ClassName: GlobalResources
 * @Description: 全局资源
 * @author: Noisyfox
 * @date: 2012-9-10 下午8:19:35
 * 
 */
public class GlobalResources {

	public static FGPathBezier3 PATHBEZIER3_ENEMY_DOOL_BLUE = null;
	public static FGGraphicFont GRAPHICFONT_SCORE = null;

	// 载入所有的图像资源
	public static FGFrame FRAMES_BULLET_ENEMY_1 = null;
	public static FGFrame FRAMES_BULLET_ENEMY_2 = null;
	public static FGFrame FRAMES_BULLET_ENEMY_3 = null;
	public static FGFrame FRAMES_BULLET_PLAYER_MISSILE_GUIDED = null;
	public static FGFrame FRAMES_BULLET_PLAYER_MISSILE_MANUAL = null;
	public static FGFrame FRAMES_BULLET_PLAYER_NORMAL = null;
	public static FGFrame FRAMES_BUTTON_GAMESTART = null;
	public static FGFrame FRAMES_BUTTON_HIGHSCORE = null;
	public static FGFrame FRAMES_BUTTON_RETURNMAINMENU = null;
	public static FGFrame FRAMES_BUTTON_RESUMEGAME = null;
	public static FGFrame FRAMES_BUTTON_RESTART = null;
	public static FGFrame FRAMES_BUTTON_NEXTSTAGE = null;
	public static FGFrame FRAMES_BUTTON_SINAWEIBO = null;
	public static FGFrame FRAMES_ENEMY_BOX_SCORE = null;
	public static FGFrame FRAMES_ENEMY_BUTTERFLY = null;
	public static FGFrame FRAMES_ENEMY_DOLL_BLUE = null;
	public static FGFrame FRAMES_ENEMY_DUCK = null;
	public static FGFrame FRAMES_ENEMY_FLY = null;
	public static FGFrame FRAMES_ENEMY_PIG = null;
	public static FGFrame FRAMES_EXPLOSION_BOSS = null;
	public static FGFrame FRAMES_EXPLOSION_BULLET_PLAYER_NORMAL = null;
	public static FGFrame FRAMES_EXPLOSION_FLASHTEXT_SCORE_500 = null;
	public static FGFrame FRAMES_EXPLOSION_MISSILE_MEDIUM = null;
	public static FGFrame FRAMES_EXPLOSION_MISSILE_SMALL = null;
	public static FGFrame FRAMES_EXPLOSION_NORMAL = null;
	public static FGFrame FRAMES_FLASHTEXT_GAME_OVER = null;
	public static FGFrame FRAMES_FLASHTEXT_STAGE_CLEAR = null;
	public static FGFrame FRAMES_PARTICLE_MISSILESMOKE = null;
	public static FGFrame FRAMES_PARTICLE_FIREWORKS_PATH = null;
	public static FGFrame FRAMES_PARTICLE_FIREWORKS_BOOM = null;
	public static FGFrame FRAMES_PLAYER = null;
	public static FGFrame FRAMES_POWERUP_MISSILE = null;
	public static FGFrame FRAMES_POWERUP_SCORE = null;
	public static FGFrame FRAMES_PLAYER_ICON = null;
	public static FGFrame FRAMES_TOUCH_SCREEN_TO_CONTINUE = null;
	public static FGFrame FRAMES_HIGHSCORE_BACKGROUND = null;
	public static FGFrame FRAMES_BACKGROUND_MAINMENU = null;
	public static FGFrame FRAMES_BACKGROUND_GAMECLEAR = null;
	public static FGFrame FRAMES_BACKGROUND_TESTSTAGE = null;
	// 通用的粒子类型
	public static FGParticleType PARTICLE_TYPE_MILLSILSMOKE = null;
	public static FGParticleType PARTICLE_TYPE_FIREWORKS_PATH = null;
	public static FGParticleType PARTICLE_TYPE_FIREWORKS_BOOM = null;

	// 字体
	public static Typeface FONT_VINERITC = null;

	public static void loadResources() {

		GL10 gl = FGEGLHelper.getBindedGL();

		// 初始化 敌机 玩偶怪 的移动路径
		PATHBEZIER3_ENEMY_DOOL_BLUE = new FGPathBezier3();
		PATHBEZIER3_ENEMY_DOOL_BLUE.startPath(0, 50);
		PATHBEZIER3_ENEMY_DOOL_BLUE.addNode(50, 0);
		PATHBEZIER3_ENEMY_DOOL_BLUE.endPath(320, 0, true);

		// 初始化分数字体
		GRAPHICFONT_SCORE = new FGGraphicFont();
		GRAPHICFONT_SCORE.mapFont(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.ascii_score,
				"0123456789/.", false);
		GRAPHICFONT_SCORE.setCharacterSpacing(-4);
		GRAPHICFONT_SCORE.setAlignment(FGGraphicFont.ALIGN_RIGHT);

		// 初始化所有图片
		FRAMES_BULLET_ENEMY_1 = new FGFrame();
		FRAMES_BULLET_ENEMY_1.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.bullet_enemy_1, false);
		FRAMES_BULLET_ENEMY_2 = new FGFrame();
		FRAMES_BULLET_ENEMY_2.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.bullet_enemy_2, false);
		FRAMES_BULLET_ENEMY_3 = new FGFrame();
		FRAMES_BULLET_ENEMY_3.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.bullet_enemy_3, false);
		FRAMES_BULLET_PLAYER_MISSILE_GUIDED = new FGFrame();
		FRAMES_BULLET_PLAYER_MISSILE_GUIDED
				.loadFromBitmap(
						gl,
						org.foxteam.noisyfox.THEngine.R.drawable.bullet_player_missile_guided,
						false);
		FRAMES_BULLET_PLAYER_MISSILE_MANUAL = new FGFrame();
		FRAMES_BULLET_PLAYER_MISSILE_MANUAL
				.loadFromBitmap(
						gl,
						org.foxteam.noisyfox.THEngine.R.drawable.bullet_player_missile_manual,
						false);
		FRAMES_BULLET_PLAYER_NORMAL = new FGFrame();
		FRAMES_BULLET_PLAYER_NORMAL.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.bullet_player_normal,
				false);
		FRAMES_BUTTON_GAMESTART = new FGFrame();
		FRAMES_BUTTON_GAMESTART.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.button_gamestart, 3,
				1, false);
		FRAMES_BUTTON_HIGHSCORE = new FGFrame();
		FRAMES_BUTTON_HIGHSCORE.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.button_highscore, 3,
				1, false);
		FRAMES_BUTTON_RETURNMAINMENU = new FGFrame();
		FRAMES_BUTTON_RETURNMAINMENU.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.button_returnmainmenu,
				3, 1, false);
		FRAMES_BUTTON_RESUMEGAME = new FGFrame();
		FRAMES_BUTTON_RESUMEGAME.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.button_resumegame, 3,
				1, false);
		FRAMES_BUTTON_RESTART = new FGFrame();
		FRAMES_BUTTON_RESTART.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.button_restart, 3, 1,
				false);
		FRAMES_BUTTON_NEXTSTAGE = new FGFrame();
		FRAMES_BUTTON_NEXTSTAGE.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.button_nextstage, 3,
				1, false);
		FRAMES_BUTTON_SINAWEIBO = new FGFrame();
		FRAMES_BUTTON_SINAWEIBO.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.button_weibo, 3, 1,
				false);
		FRAMES_ENEMY_BOX_SCORE = new FGFrame();
		FRAMES_ENEMY_BOX_SCORE
				.loadFromBitmap(
						gl,
						org.foxteam.noisyfox.THEngine.R.drawable.enemy_box_score,
						false);
		FRAMES_ENEMY_BUTTERFLY = new FGFrame();
		FRAMES_ENEMY_BUTTERFLY.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.enemy_butterfly, 4, 1,
				false);
		FRAMES_ENEMY_DOLL_BLUE = new FGFrame();
		FRAMES_ENEMY_DOLL_BLUE.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.enemy_doll_blue, 12,
				1, false);
		FRAMES_ENEMY_DUCK = new FGFrame();
		FRAMES_ENEMY_DUCK.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.enemy_duck, 10, 1,
				false);
		FRAMES_ENEMY_FLY = new FGFrame();
		FRAMES_ENEMY_FLY.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.enemy_fly, 10, 1,
				false);
		FRAMES_ENEMY_PIG = new FGFrame();
		FRAMES_ENEMY_PIG.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.enemy_pig, 10, 1,
				false);
		FRAMES_EXPLOSION_BOSS = new FGFrame();
		FRAMES_EXPLOSION_BOSS.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.explosion_boss, 5, 1,
				false);
		FRAMES_EXPLOSION_BULLET_PLAYER_NORMAL = new FGFrame();
		FRAMES_EXPLOSION_BULLET_PLAYER_NORMAL
				.loadFromBitmap(
						gl,
						org.foxteam.noisyfox.THEngine.R.drawable.explosion_bullet_player_normal,
						10, 1, false);
		FRAMES_EXPLOSION_FLASHTEXT_SCORE_500 = new FGFrame();
		FRAMES_EXPLOSION_FLASHTEXT_SCORE_500
				.loadFromBitmap(
						gl,
						org.foxteam.noisyfox.THEngine.R.drawable.explosion_flashtext_score_500,
						2, 1, false);
		FRAMES_EXPLOSION_MISSILE_MEDIUM = new FGFrame();
		FRAMES_EXPLOSION_MISSILE_MEDIUM
				.loadFromBitmap(
						gl,
						org.foxteam.noisyfox.THEngine.R.drawable.explosion_missile_medium,
						5, 1, false);
		FRAMES_EXPLOSION_MISSILE_SMALL = new FGFrame();
		FRAMES_EXPLOSION_MISSILE_SMALL
				.loadFromBitmap(
						gl,
						org.foxteam.noisyfox.THEngine.R.drawable.explosion_missile_small,
						5, 1, false);
		FRAMES_EXPLOSION_NORMAL = new FGFrame();
		FRAMES_EXPLOSION_NORMAL.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.explosion_normal, 7,
				1, false);
		FRAMES_FLASHTEXT_GAME_OVER = new FGFrame();
		FRAMES_FLASHTEXT_GAME_OVER.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.flashtext_game_over,
				1, 1, false);
		FRAMES_FLASHTEXT_STAGE_CLEAR = new FGFrame();
		FRAMES_FLASHTEXT_STAGE_CLEAR.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.flashtext_stage_clear,
				1, 1, false);
		FRAMES_PARTICLE_MISSILESMOKE = new FGFrame();
		FRAMES_PARTICLE_MISSILESMOKE.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.particle_missilesmoke,
				1, 1, false);
		FRAMES_PARTICLE_FIREWORKS_PATH = new FGFrame();
		FRAMES_PARTICLE_FIREWORKS_PATH
				.loadFromBitmap(
						gl,
						org.foxteam.noisyfox.THEngine.R.drawable.particle_fireworks_path,
						1, 1, false);
		FRAMES_PARTICLE_FIREWORKS_BOOM = new FGFrame();
		FRAMES_PARTICLE_FIREWORKS_BOOM
				.loadFromBitmap(
						gl,
						org.foxteam.noisyfox.THEngine.R.drawable.particle_fireworks_boom,
						1, 1, false);
		FRAMES_PLAYER = new FGFrame();
		FRAMES_PLAYER.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.player, 1, 1, false);
		FRAMES_PLAYER_ICON = new FGFrame();
		FRAMES_PLAYER_ICON.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.player_icon, 1, 1,
				false);
		FRAMES_POWERUP_MISSILE = new FGFrame();
		FRAMES_POWERUP_MISSILE.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.powerup_missile, 2, 1,
				false);
		FRAMES_POWERUP_SCORE = new FGFrame();
		FRAMES_POWERUP_SCORE.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.powerup_score, 1, 1,
				false);
		FRAMES_TOUCH_SCREEN_TO_CONTINUE = new FGFrame();
		FRAMES_TOUCH_SCREEN_TO_CONTINUE
				.loadFromBitmap(
						gl,
						org.foxteam.noisyfox.THEngine.R.drawable.touch_screen_to_continue,
						true);
		FRAMES_HIGHSCORE_BACKGROUND = new FGFrame();
		FRAMES_HIGHSCORE_BACKGROUND.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.highscore_background,
				true);
		FRAMES_BACKGROUND_MAINMENU = new FGFrame();
		FRAMES_BACKGROUND_MAINMENU.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.background_menu, true);
		FRAMES_BACKGROUND_GAMECLEAR = new FGFrame();
		FRAMES_BACKGROUND_GAMECLEAR.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.background_gameclear,
				true);
		FRAMES_BACKGROUND_TESTSTAGE = new FGFrame();
		FRAMES_BACKGROUND_TESTSTAGE.loadFromBitmap(gl,
				org.foxteam.noisyfox.THEngine.R.drawable.background_teststage,
				true);

		// 初始化粒子
		PARTICLE_TYPE_MILLSILSMOKE = new FGParticleType();
		FGSprite _particleSprite = new FGSprite();
		_particleSprite
				.bindFrames(GlobalResources.FRAMES_PARTICLE_MISSILESMOKE);
		_particleSprite.setOffset(3, 3);
		PARTICLE_TYPE_MILLSILSMOKE.setSprite(_particleSprite);
		PARTICLE_TYPE_MILLSILSMOKE.setLifeTime(10, 15);
		PARTICLE_TYPE_MILLSILSMOKE.setSize(0.8, 1.3, -0.03, 0.01);
		PARTICLE_TYPE_MILLSILSMOKE.setOrientation(0, 359.9f, 0, 1, false);
		PARTICLE_TYPE_MILLSILSMOKE.setAlpha(1.0, 0.0);

		PARTICLE_TYPE_FIREWORKS_PATH = new FGParticleType();
		_particleSprite = new FGSprite();
		_particleSprite
				.bindFrames(GlobalResources.FRAMES_PARTICLE_FIREWORKS_PATH);
		_particleSprite.setOffset(1, 1);
		PARTICLE_TYPE_FIREWORKS_PATH.setSprite(_particleSprite);
		PARTICLE_TYPE_FIREWORKS_PATH.setLifeTime(10, 15);
		PARTICLE_TYPE_FIREWORKS_PATH.setSize(3, 4, -0.3, 0.01);
		PARTICLE_TYPE_FIREWORKS_PATH.setAlpha(1.0, 0.0);
		PARTICLE_TYPE_FIREWORKS_PATH.setGravity(0.6, 270);
		PARTICLE_TYPE_FIREWORKS_PATH.setColorHSV(31, 1, 0.64, 60, 1, 1);

		PARTICLE_TYPE_FIREWORKS_BOOM = new FGParticleType();
		_particleSprite = new FGSprite();
		_particleSprite
				.bindFrames(GlobalResources.FRAMES_PARTICLE_FIREWORKS_BOOM);
		_particleSprite.setOffset(5, 0);
		PARTICLE_TYPE_FIREWORKS_BOOM.setSprite(_particleSprite);
		PARTICLE_TYPE_FIREWORKS_BOOM.setLifeTime(25, 35);
		PARTICLE_TYPE_FIREWORKS_BOOM.setSize(1, 2, 0, 0.01);
		PARTICLE_TYPE_FIREWORKS_BOOM.setAlpha(1.0, 0.0);
		PARTICLE_TYPE_FIREWORKS_BOOM.setGravity(0.6, 270);
		PARTICLE_TYPE_FIREWORKS_BOOM.setColorHSV(31, 1, 0.64, 60, 1, 1);
		PARTICLE_TYPE_FIREWORKS_BOOM.setDirection(0, 359.9, 0, 0);
		PARTICLE_TYPE_FIREWORKS_BOOM.setOrientation(0, 0, 0, 0, true);
		PARTICLE_TYPE_FIREWORKS_BOOM.setSpeed(8, 13, 0, 0);

		// 加载字体
		FONT_VINERITC = Typeface.createFromAsset(FGGameCore.getMainContext()
				.getAssets(), "VINERITC.TTF");
	}
}
