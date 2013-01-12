/**
 * FileName:     FGPathBezier3.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-9-8 下午11:02:09
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-9-8      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.G2D;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @ClassName: FGPathBezier3
 * @Description: 由三次贝塞尔曲线构成的路径
 * @author: Noisyfox
 * @date: 2012-9-8 下午11:02:09
 * 
 */
public final class FGPathBezier3 {
	float scale = 0.6f;// 控制点收缩系数
	float stepLength = 0.5f;// 最小距离间隔
	float precision = 0.05f;// 近似计算误差

	ArrayList<Node> nodes = new ArrayList<Node>();
	boolean computed = false;
	ArrayList<FGPointF> pathPoints = new ArrayList<FGPointF>();
	boolean started = false;

	public void reset() {
		nodes.clear();
		pathPoints.clear();
		computed = false;
	}

	public void startPath(float x, float y) {
		if (!nodes.isEmpty() || started) {
			throw new IllegalStateException();
		}
		started = true;
		addNode(x, y);
	}

	public void addNode(float x, float y) {
		if (computed || !started) {
			throw new IllegalStateException();
		}
		Node thisNode = new Node();
		thisNode.me = new FGPointF(x, y);
		nodes.add(thisNode);
	}

	public void endPath(float x, float y, boolean compute) {
		if (nodes.isEmpty() || !started) {
			throw new IllegalStateException();
		}
		addNode(x, y);

		started = false;

		if (compute) {
			compute();
		}
	}

	public void setPrecision(float stepLength, float precision, float scale) {
		if (stepLength <= 0 || precision <= 0 || precision >= stepLength
				|| scale <= 0) {
			throw new IllegalArgumentException();
		}
		this.stepLength = stepLength;
		this.precision = precision;
		this.scale = scale;
	}

	public void compute() {
		if (nodes.size() < 3) {
			throw new IllegalStateException("节点数量不得小于3个！");
		}

		if (started) {
			throw new IllegalStateException();
		}

		computed = false;
		pathPoints.clear();

		// 先计算控制点
		// 生成中点
		FGPointF[] middlePoint = new FGPointF[nodes.size() - 1];
		for (int i = 0; i < nodes.size() - 1; i++) {
			FGPointF thisPoint = nodes.get(i).me;
			FGPointF nextPoint = nodes.get(i + 1).me;
			middlePoint[i] = new FGPointF();
			middlePoint[i].x = (thisPoint.x + nextPoint.x) / 2.0f;
			middlePoint[i].y = (thisPoint.y + nextPoint.y) / 2.0f;
		}
		// 平移中点
		FGPointF[] ctrlPoints = new FGPointF[(nodes.size() - 2) * 2];
		for (int i = 0; i < ctrlPoints.length; i++) {
			ctrlPoints[i] = new FGPointF();
		}
		for (int i = 1; i < nodes.size() - 1; i++) {
			Node thisNode = nodes.get(i);
			// 中点的中点
			FGPointF m2Point = new FGPointF();
			m2Point.x = (middlePoint[i - 1].x + middlePoint[i].x) / 2.0f;
			m2Point.y = (middlePoint[i - 1].y + middlePoint[i].y) / 2.0f;
			float offsetX = thisNode.me.x - m2Point.x;
			float offsetY = thisNode.me.y - m2Point.y;
			ctrlPoints[(i - 1) * 2].x = middlePoint[i - 1].x + offsetX;
			ctrlPoints[(i - 1) * 2].y = middlePoint[i - 1].y + offsetY;
			ctrlPoints[(i - 1) * 2 + 1].x = middlePoint[i].x + offsetX;
			ctrlPoints[(i - 1) * 2 + 1].y = middlePoint[i].y + offsetY;
			// 收缩
			ctrlPoints[(i - 1) * 2].x = (ctrlPoints[(i - 1) * 2].x - thisNode.me.x)
					* scale + thisNode.me.x;
			ctrlPoints[(i - 1) * 2].y = (ctrlPoints[(i - 1) * 2].y - thisNode.me.y)
					* scale + thisNode.me.y;
			ctrlPoints[(i - 1) * 2 + 1].x = (ctrlPoints[(i - 1) * 2 + 1].x - thisNode.me.x)
					* scale + thisNode.me.x;
			ctrlPoints[(i - 1) * 2 + 1].y = (ctrlPoints[(i - 1) * 2 + 1].y - thisNode.me.y)
					* scale + thisNode.me.y;

			thisNode.lControlPoint = ctrlPoints[(i - 1) * 2];
			thisNode.rControlPoint = ctrlPoints[(i - 1) * 2 + 1];
		}

		// 开始计算每一段的点
		FGPointF lastPoint = new FGPointF(nodes.get(0).me.x, nodes.get(0).me.y);
		FGPointF nowPoint = new FGPointF(lastPoint.x, lastPoint.y);
		pathPoints.add(lastPoint);
		float stepLengthMax = stepLength + precision;
		float stepLengthMin = stepLength - precision;
		float missingLength = 0;
		for (int i = 1; i <= nodes.size() - 1; i++) {
			boolean fin = false;
			float t = 0;
			Node lNode = nodes.get(i - 1);
			Node rNode = nodes.get(i);
			missingLength = nowPoint.distance(lNode.me);
			while (!fin) {
				bezierFunc(lNode, rNode, t, nowPoint);
				float dst = lastPoint.distance(nowPoint) + missingLength;
				if (dst < stepLengthMin) {
					t += 0.00001f;
					if (t > 1) {
						fin = true;
					}
				} else if (dst > stepLengthMax) {
					t -= 0.000001f;
					if (t < 0) {
						throw new RuntimeException();
					}
				} else {
					pathPoints.add(lastPoint);
					lastPoint = nowPoint;
					nowPoint = new FGPointF(lastPoint.x, lastPoint.y);
					missingLength = 0;
				}
			}
		}

		computed = true;
	}

	public void draw(Canvas c) {
		Paint p = new Paint();
		p.setColor(Color.RED);
		for (int i = 0; i < pathPoints.size() - 1; i++) {
			FGPointF p1 = pathPoints.get(i);
			FGPointF p2 = pathPoints.get(i + 1);
			c.drawLine(p1.x, p1.y, p2.x, p2.y, p);
		}
	}

	// 2或3次贝塞尔曲线求点
	private void bezierFunc(Node leftNode, Node rightNode, float t, FGPointF dst) {
		if (leftNode.rControlPoint != null && rightNode.lControlPoint != null) {
			bezier3Func(leftNode, rightNode, t, dst);
		} else {
			bezier2Func(leftNode, rightNode, t, dst);
		}
	}

	// 2次贝塞尔曲线求点
	private void bezier2Func(Node leftNode, Node rightNode, float t,
			FGPointF dst) {
		FGPointF lP = leftNode.me;
		FGPointF rP = rightNode.me;
		FGPointF cP = null;
		if (leftNode.rControlPoint == null) {
			if (rightNode.lControlPoint == null) {
				throw new IllegalArgumentException();
			} else {
				cP = rightNode.lControlPoint;
			}
		} else {
			cP = leftNode.rControlPoint;
		}

		dst.x = (1 - t) * (1 - t) * lP.x + 2 * t * (1 - t) * cP.x + t * t
				* rP.x;
		dst.y = (1 - t) * (1 - t) * lP.y + 2 * t * (1 - t) * cP.y + t * t
				* rP.y;
	}

	// 3次贝塞尔曲线求点
	private void bezier3Func(Node leftNode, Node rightNode, float t,
			FGPointF dst) {
		FGPointF lP = leftNode.me;
		FGPointF rP = rightNode.me;
		FGPointF cPl = leftNode.rControlPoint;
		FGPointF cPr = rightNode.lControlPoint;

		dst.x = (1 - t) * (1 - t) * (1 - t) * lP.x + 3 * (1 - t) * (1 - t) * t
				* cPl.x + 3 * (1 - t) * t * t * cPr.x + t * t * t * rP.x;
		dst.y = (1 - t) * (1 - t) * (1 - t) * lP.y + 3 * (1 - t) * (1 - t) * t
				* cPl.y + 3 * (1 - t) * t * t * cPr.y + t * t * t * rP.y;
	}

	private class Node {
		FGPointF lControlPoint;
		FGPointF rControlPoint;
		FGPointF me;
	}

	// 获取路径的长度
	public float getLength() {
		if (!computed) {
			throw new IllegalStateException();
		}

		float length = 0;
		int size = pathPoints.size();
		for (int i = 0; i < size - 1; i++) {
			length += pathPoints.get(i).distance(pathPoints.get(i + 1));
		}
		return length;
	}

	public int getMaxFlag() {
		if (!computed) {
			throw new IllegalStateException();
		}

		return pathPoints.size() - 1;
	}

	public int nextPositionFlag(int currentFlag, float dLength) {
		if (!computed) {
			throw new IllegalStateException();
		}

		int flag = currentFlag;
		float length = 0;
		if (dLength >= 0) {
			while (true) {
				if (flag >= pathPoints.size() - 1) {
					return pathPoints.size() - 1;
				}
				float d = pathPoints.get(flag).distance(
						pathPoints.get(flag + 1));
				if (dLength - length > d) {
					flag++;
					length += d;
				} else {
					if (dLength - length < 0) {
						return flag;
					} else {
						if (dLength - length < d / 2.0f) {
							return flag - 1;
						}
						return flag;
					}
				}
			}
		} else {
			while (true) {
				dLength = -dLength;
				if (flag <= 0) {
					return 0;
				}
				float d = pathPoints.get(flag).distance(
						pathPoints.get(flag - 1));
				if (dLength - length > d) {
					flag--;
					length += d;
				} else {
					if (dLength - length < 0) {
						return flag;
					} else {
						if (dLength - length < d / 2.0f) {
							return flag;
						}
						return flag - 1;
					}
				}
			}
		}
	}

	public float getX(int positionFlag) {
		if (!computed) {
			throw new IllegalStateException();
		}

		return pathPoints.get(positionFlag).x;
	}

	public float getY(int positionFlag) {
		if (!computed) {
			throw new IllegalStateException();
		}

		return pathPoints.get(positionFlag).y;
	}

}
