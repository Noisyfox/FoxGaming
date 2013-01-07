package org.foxteam.noisyfox.THEngine.Section.BasicElements;

import java.util.HashMap;
import java.util.Stack;

public final class BulletPool {

	private static HashMap<Class<?>, Stack<Bullet>> mBulletPool = new HashMap<Class<?>, Stack<Bullet>>();

	public static Bullet obtainBullet(Class<?> bulletType) {
		if (Bullet.class.isAssignableFrom(bulletType)) {
			if (mBulletPool.containsKey(bulletType)) {
				Stack<Bullet> bs = mBulletPool.get(bulletType);
				if (bs.isEmpty()) {
					try {
						return (Bullet) bulletType.newInstance();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					return bs.pop();
				}
			} else {
				try {
					Bullet b = (Bullet) bulletType.newInstance();
					return b;
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			throw new IllegalArgumentException();
		}
		return null;
	}

	public static void recycleBullet(Bullet bullet) {
		bullet.recycleBullet();
		Class<?> bulletType = bullet.getClass();
		Stack<Bullet> bs;
		if (mBulletPool.containsKey(bulletType)) {
			bs = mBulletPool.get(bulletType);
		} else {
			bs = new Stack<Bullet>();
			mBulletPool.put(bulletType, bs);
		}
		bs.push(bullet);
	}
}
