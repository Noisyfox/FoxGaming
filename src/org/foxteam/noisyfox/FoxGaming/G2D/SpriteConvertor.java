/**
 * FileName:     SpriteConvertor.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-27 上午10:43:57
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-27      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.G2D;

/**
 * @ClassName: SpriteConvertor
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-27 上午10:43:57
 * 
 */
public class SpriteConvertor extends Convertor{
	protected double alpha = 1;
	
	public final void setAlpha(double alpha) {
		if (alpha > 1) {
			alpha = 1;
		} else if (alpha < 0) {
			alpha = 0;
		}
		this.alpha = alpha;
	}

	public final double getAlpha() {
		return alpha;
	}

}