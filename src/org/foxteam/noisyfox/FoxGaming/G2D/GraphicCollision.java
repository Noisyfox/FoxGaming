/**
 * FileName:     GraphicCollision.java
 * @Description: TODO
 * All rights Reserved, Designed By Noisyfox
 * Copyright:    Copyright(C) 2012
 * Company       FoxTeam.
 * @author:      Noisyfox
 * @version      V1.0
 * Createdate:   2012-6-27 上午11:12:21
 *
 * Modification  History:
 * Date         Author        Version        Discription
 * -----------------------------------------------------------------------------------
 * 2012-6-27      Noisyfox        1.0             1.0
 * Why & What is modified:
 */
package org.foxteam.noisyfox.FoxGaming.G2D;

import java.util.ArrayList;
import java.util.List;

import org.foxteam.noisyfox.FoxGaming.Core.MathsHelper;
import org.foxteam.noisyfox.FoxGaming.Core.MyDebug;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * @ClassName: GraphicCollision
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-27 上午11:12:21
 * 
 */
public class GraphicCollision {

	private static Paint p = new Paint();

	List<Polygon> polygons = new ArrayList<Polygon>();
	List<Circle> circles = new ArrayList<Circle>();
	List<Point> points = new ArrayList<Point>();
	Rect reducedArea = new Rect(0, 0, 0, 0);

	int baseX = 0;
	int baseY = 0;

	public GraphicCollision() {
		p.setColor(Color.RED);
		p.setStyle(Paint.Style.STROKE);
		p.setAlpha(100);
	}

	public final void addCircle(int x, int y, int r) {
		addCircle(x, y, r, true);
	}

	public final void addCircle(int x, int y, int r, boolean fill) {
		Circle c = new Circle(x, y, r, fill);
		circles.add(c);

		reducedArea.left = Math.min(x - r, reducedArea.left);
		reducedArea.top = Math.min(y - r, reducedArea.top);
		reducedArea.right = Math.max(x + r, reducedArea.right);
		reducedArea.bottom = Math.max(y + r, reducedArea.bottom);
	}

	public final void addTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		addTriangle(x1, y1, x2, y2, x3, y3, true);
	}

	public final void addTriangle(int x1, int y1, int x2, int y2, int x3,
			int y3, boolean fill) {
		int[][] vertex = { { x1, y1 }, { x2, y2 }, { x3, y3 } };
		addPolygon(vertex, fill);
	}

	public final void addLine(int x1, int y1, int x2, int y2) {
		int[][] vertex = { { x1, y1 }, { x2, y2 } };
		addPolygon(vertex, false);
	}

	// 点只能用于与点以及实心圆、多边形进行碰撞检测，自动忽略线段与轮廓
	public final void addPoint(int x, int y) {
		Point p = new Point(x, y);
		points.add(p);

		reducedArea.left = Math.min(x, reducedArea.left);
		reducedArea.top = Math.min(y, reducedArea.top);
		reducedArea.right = Math.max(x, reducedArea.right);
		reducedArea.bottom = Math.max(y, reducedArea.bottom);
	}

	public final void addPolygon(int[][] vertex, boolean fill) {
		for (int[] point : vertex) {
			if (point.length != 2) {
				throw new IllegalArgumentException();
			}
		}

		Point[] v = new Point[vertex.length];
		for (int i = 0; i < vertex.length; i++) {
			v[i] = new Point(vertex[i][0], vertex[i][1]);
		}

		addPolygon(v, fill);
	}

	public final void addPolygon(Point[] vertex, boolean fill) {
		Polygon p = new Polygon(vertex, fill);
		polygons.add(p);

		for (Point v : p.vertex) {
			reducedArea.left = Math.min(v.x, reducedArea.left);
			reducedArea.top = Math.min(v.y, reducedArea.top);
			reducedArea.right = Math.max(v.x, reducedArea.right);
			reducedArea.bottom = Math.max(v.y, reducedArea.bottom);
		}
	}

	public final void setPosition(int x, int y) {
		int dx = x - baseX;
		int dy = y - baseY;
		for (Point p : points) {
			p.move(dx, dy);
		}
		for (Circle c : circles) {
			c.move(dx, dy);
		}
		for (Polygon pol : polygons) {
			for (Point p : pol.vertex) {
				p.move(dx, dy);
			}
		}

		reducedArea.offset(dx, dy);

		baseX = x;
		baseY = y;
	}

	public final boolean isCollisionWith(GraphicCollision target) {
		// 粗略判断范围
		if (!MathsHelper.rectVSrect(reducedArea, target.reducedArea)) {
			return false;
		}

		// 优先进行点的判断
		// 点与点
		for (Point p1 : points) {
			for (Point p2 : target.points) {
				if (p1.getX() == p2.getX() && p1.getY() == p2.getY()) {
					return true;
				}
			}
		}
		// 点与圆面
		for (Circle c : target.circles) {
			for (Point p1 : points) {
				if (c.filled()) {
					if (MathsHelper.pointInCircle(p1, c)) {
						return true;
					}
				}
			}
		}
		for (Circle c : circles) {
			for (Point p1 : target.points) {
				if (c.filled()) {
					if (MathsHelper.pointInCircle(p1, c)) {
						return true;
					}
				}
			}
		}
		// 点与多边形
		for (Polygon pol : target.polygons) {
			for (Point p : points) {
				if (pol.filled()) {
					if (MathsHelper.pointInPolygon(p, pol)) {
						return true;
					}
				}
			}
		}
		for (Polygon pol : polygons) {
			for (Point p : target.points) {
				if (pol.filled()) {
					if (MathsHelper.pointInPolygon(p, pol)) {
						return true;
					}
				}
			}
		}

		// 接下来判断圆
		// 圆与圆
		for (Circle c1 : circles) {
			for (Circle c2 : target.circles) {
				int d2 = c1.squareDistance(c2);
				int r2 = (c1.getR() + c2.getR()) * (c1.getR() + c2.getR());
				int r22 = (c1.getR() - c2.getR()) * (c1.getR() - c2.getR());
				// 两圆相交、相切
				if (d2 <= r2 && d2 >= r22) {
					return true;
				}

				// 两圆内含
				if (c1.getR() < c2.getR() && c2.filled() && d2 < r22) {
					return true;
				}
				if (c1.getR() > c2.getR() && c1.filled() && d2 < r22) {
					return true;
				}
			}
		}
		// 圆与多边形
		for (Polygon pol : target.polygons) {
			for (Circle c : circles) {
				if (pol.isLine()) {
					// 圆与线段
					if (MathsHelper.circleVSline(c, pol.getVertex(0),
							pol.getVertex(1), true)) {
						return true;
					}
					continue;
				}
				// 圆与多边形
				// 有任何一边与圆相交
				for (int i = 0; i < pol.getEdgeNumber(); i++) {
					if (MathsHelper.circleVSline(c, pol.getVertex(i),
							pol.getVertex(i + 1), true)) {
						return true;
					}
				}
				// 没有边相交，则判断是否有包含关系
				if (MathsHelper.pointInCircle(pol.getVertex(0), c)) {
					if (c.filled()) {
						return true;
					}
				} else if (pol.filled()) {
					if (MathsHelper.pointInPolygon(c, pol)) {
						return true;
					}
				}
			}
		}

		// 最后是多边形
		// 两条直线
		for (Polygon pol1 : target.polygons) {
			for (Polygon pol2 : polygons) {
				if (pol1.isLine() && pol2.isLine()) {
					if (MathsHelper.lineVSline(pol1.getVertex(0),
							pol1.getVertex(1), pol2.getVertex(0),
							pol2.getVertex(1))) {
						return true;
					}
					continue;
				}
			}
		}
		// 直线和多边形
		for (Polygon pol1 : target.polygons) {
			for (Polygon pol2 : polygons) {
				if (pol1.isLine() && !pol2.isLine()) {
					boolean b1 = MathsHelper.pointInPolygon(pol1.getVertex(0),
							pol2);
					boolean b2 = MathsHelper.pointInPolygon(pol1.getVertex(1),
							pol2);
					if ((b1 && (!b2)) || ((!b1) && b2)) {
						return true;
					}
					if (b1 && b2 && pol2.filled()) {
						return true;
					}
					continue;
				}
				if (!pol1.isLine() && pol2.isLine()) {
					boolean b1 = MathsHelper.pointInPolygon(pol2.getVertex(0),
							pol1);
					boolean b2 = MathsHelper.pointInPolygon(pol2.getVertex(1),
							pol1);
					if ((b1 && (!b2)) || ((!b1) && b2)) {
						return true;
					}
					if (b1 && b2 && pol1.filled()) {
						return true;
					}
					continue;
				}
			}
		}
		// 多边形和多边形
		for (Polygon pol1 : target.polygons) {
			for (Polygon pol2 : polygons) {
				if (!pol1.isLine() && !pol2.isLine()) {
					{
						boolean hasIn = false;
						boolean hasOut = false;
						int nVertex = pol1.getVertexNumber();
						for (int i = 0; i < nVertex; i++) {
							if (MathsHelper.pointInPolygon(pol1.getVertex(i),
									pol2)) {
								hasIn = true;
							} else {
								hasOut = true;
							}
						}
						if (hasIn && hasOut) {
							return true;
						} else if (hasIn && pol2.filled()) {
							return true;
						}
					}
					{
						boolean hasIn = false;
						boolean hasOut = false;
						int nVertex = pol2.getVertexNumber();
						for (int i = 0; i < nVertex; i++) {
							if (MathsHelper.pointInPolygon(pol2.getVertex(i),
									pol1)) {
								hasIn = true;
							} else {
								hasOut = true;
							}
						}
						if (hasIn && hasOut) {
							return true;
						} else if (hasIn && pol1.filled()) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void draw(Canvas c) {
		if (!MyDebug.isDebugMode()) {
			return;
		}
		
		for (Point p : points) {
			p.draw(c);
		}
		for (Circle ci : circles) {
			ci.draw(c);
		}
		for (Polygon pol : polygons) {
			pol.draw(c);
		}
		c.drawRect(reducedArea, p);
	}

	// 静态函数，判断多个target之间是否发生碰撞
	public final static boolean testCollision(GraphicCollision... targets) {
		for (int i = 0; i < targets.length - 1; i++) {
			for (int j = i; j < targets.length; j++) {
				if (targets[i].isCollisionWith(targets[j])) {
					return true;
				}
			}
		}
		return false;
	}

}
