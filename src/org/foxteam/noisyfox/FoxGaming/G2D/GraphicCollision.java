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
	List<Polygon> polygons_tmp = new ArrayList<Polygon>();
	List<Circle> circles_tmp = new ArrayList<Circle>();
	List<Point> points_tmp = new ArrayList<Point>();
	Rect reducedArea_tmp = new Rect(0, 0, 0, 0);

	int baseX = 0;
	int baseY = 0;

	public GraphicCollision() {
		p.setColor(Color.RED);
		p.setStyle(Paint.Style.STROKE);
		p.setAlpha(100);
		// Matrix a = new Matrix();
		// a.m
	}

	public void clear() {
		polygons.clear();
		circles.clear();
		points.clear();
		reducedArea.setEmpty();

		polygons_tmp.clear();
		circles_tmp.clear();
		points_tmp.clear();
		reducedArea_tmp.setEmpty();
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

		Circle c_tmp = new Circle(x, y, r, fill);
		circles_tmp.add(c_tmp);

		reducedArea_tmp.left = reducedArea.left;
		reducedArea_tmp.top = reducedArea.top;
		reducedArea_tmp.right = reducedArea.right;
		reducedArea_tmp.bottom = reducedArea.bottom;
	}

	public final void addTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
		addTriangle(x1, y1, x2, y2, x3, y3, true);
	}

	public final void addTriangle(int x1, int y1, int x2, int y2, int x3,
			int y3, boolean fill) {
		int[][] vertex = { { x1, y1 }, { x2, y2 }, { x3, y3 } };
		addPolygon(vertex, fill);
	}

	public final void addRectangle(int left, int top, int width, int height) {
		addRectangle(left, top, width, height, true);
	}

	public final void addRectangle(int left, int top, int width, int height,
			boolean fill) {
		int[][] vertex = { { left, top }, { left, top + height },
				{ left + width, top + height }, { left + width, top } };
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

		Point p_tmp = new Point(x, y);
		points_tmp.add(p_tmp);

		reducedArea_tmp.left = reducedArea.left;
		reducedArea_tmp.top = reducedArea.top;
		reducedArea_tmp.right = reducedArea.right;
		reducedArea_tmp.bottom = reducedArea.bottom;
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

		Point[] v_tmp = new Point[vertex.length];

		for (int i = 0; i < vertex.length; i++) {
			Point v = vertex[i];
			reducedArea.left = Math.min(v.x, reducedArea.left);
			reducedArea.top = Math.min(v.y, reducedArea.top);
			reducedArea.right = Math.max(v.x, reducedArea.right);
			reducedArea.bottom = Math.max(v.y, reducedArea.bottom);

			v_tmp[i] = new Point(v.getX(), v.getY());
		}

		Polygon p_tmp = new Polygon(v_tmp, fill);
		polygons_tmp.add(p_tmp);

		reducedArea_tmp.left = reducedArea.left;
		reducedArea_tmp.top = reducedArea.top;
		reducedArea_tmp.right = reducedArea.right;
		reducedArea_tmp.bottom = reducedArea.bottom;
	}

	public final void setPosition(int x, int y) {
		int dx = x - baseX;
		int dy = y - baseY;
		for (Point p : points_tmp) {
			p.move(dx, dy);
		}
		for (Circle c : circles_tmp) {
			c.move(dx, dy);
		}
		for (Polygon pol : polygons_tmp) {
			for (Point p : pol.vertex) {
				p.move(dx, dy);
			}
		}

		reducedArea_tmp.offset(dx, dy);

		baseX = x;
		baseY = y;
	}

	private final void mapPoint(Point src, Point dst, Convertor convertor) {
		float srcP[] = { src.getX(), src.getY() };
		float dstP[] = { dst.getX(), dst.getY() };
		convertor.getConvertMatrix().mapPoints(dstP, srcP);
		dst.x = (int) dstP[0];
		dst.y = (int) dstP[1];
	}

	public final void applyConvertor(Convertor convertor) {
		reducedArea_tmp.setEmpty();
		for (int i = 0; i < points.size(); i++) {
			mapPoint(points.get(i), points_tmp.get(i), convertor);

			reducedArea_tmp.left = Math.min(points_tmp.get(i).x,
					reducedArea_tmp.left);
			reducedArea_tmp.top = Math.min(points_tmp.get(i).y,
					reducedArea_tmp.top);
			reducedArea_tmp.right = Math.max(points_tmp.get(i).x,
					reducedArea_tmp.right);
			reducedArea_tmp.bottom = Math.max(points_tmp.get(i).y,
					reducedArea_tmp.bottom);
		}

		for (int i = 0; i < circles.size(); i++) {
			mapPoint(circles.get(i), circles_tmp.get(i), convertor);

			reducedArea_tmp.left = Math.min(
					points_tmp.get(i).x - circles_tmp.get(i).r,
					reducedArea_tmp.left);
			reducedArea_tmp.top = Math.min(
					points_tmp.get(i).y - circles_tmp.get(i).r,
					reducedArea_tmp.top);
			reducedArea_tmp.right = Math.max(
					points_tmp.get(i).x + circles_tmp.get(i).r,
					reducedArea_tmp.right);
			reducedArea_tmp.bottom = Math.max(
					points_tmp.get(i).y + circles_tmp.get(i).r,
					reducedArea_tmp.bottom);
		}

		for (int i = 0; i < polygons.size(); i++) {
			for (int j = 0; j < polygons.get(i).num_vertexs; j++) {
				mapPoint(polygons.get(i).vertex[j],
						polygons_tmp.get(i).vertex[j], convertor);

				reducedArea_tmp.left = Math.min(
						polygons_tmp.get(i).vertex[j].x, reducedArea_tmp.left);
				reducedArea_tmp.top = Math.min(polygons_tmp.get(i).vertex[j].y,
						reducedArea_tmp.top);
				reducedArea_tmp.right = Math.max(
						polygons_tmp.get(i).vertex[j].x, reducedArea_tmp.right);
				reducedArea_tmp.bottom = Math
						.max(polygons_tmp.get(i).vertex[j].y,
								reducedArea_tmp.bottom);
			}
		}

		// convertor.getConvertMatrix().mapRect(reducedArea_tmp, reducedArea);

		int x = baseX;
		int y = baseY;

		setPosition(x, y);

		baseX = 0;
		baseY = 0;
	}

	public final boolean isCollisionWith(GraphicCollision target) {
		// 粗略判断范围
		if (!MathsHelper.rectVSrect(reducedArea_tmp, target.reducedArea_tmp)) {
			return false;
		}

		// 优先进行点的判断
		// 点与点
		for (Point p1 : points_tmp) {
			for (Point p2 : target.points_tmp) {
				if (p1.getX() == p2.getX() && p1.getY() == p2.getY()) {
					return true;
				}
			}
		}
		// 点与圆面
		for (Circle c : target.circles_tmp) {
			for (Point p1 : points_tmp) {
				if (c.filled()) {
					if (MathsHelper.pointInCircle(p1, c)) {
						return true;
					}
				}
			}
		}
		for (Circle c : circles_tmp) {
			for (Point p1 : target.points_tmp) {
				if (c.filled()) {
					if (MathsHelper.pointInCircle(p1, c)) {
						return true;
					}
				}
			}
		}
		// 点与多边形
		for (Polygon pol : target.polygons_tmp) {
			for (Point p : points_tmp) {
				if (pol.filled()) {
					if (MathsHelper.pointInPolygon(p, pol)) {
						return true;
					}
				}
			}
		}
		for (Polygon pol : polygons_tmp) {
			for (Point p : target.points_tmp) {
				if (pol.filled()) {
					if (MathsHelper.pointInPolygon(p, pol)) {
						return true;
					}
				}
			}
		}

		// 接下来判断圆
		// 圆与圆
		for (Circle c1 : circles_tmp) {
			for (Circle c2 : target.circles_tmp) {
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
		for (Polygon pol : target.polygons_tmp) {
			for (Circle c : circles_tmp) {
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

		for (Polygon pol : polygons_tmp) {
			for (Circle c : target.circles_tmp) {
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
		for (Polygon pol1 : target.polygons_tmp) {
			for (Polygon pol2 : polygons_tmp) {
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
		for (Polygon pol1 : target.polygons_tmp) {
			for (Polygon pol2 : polygons_tmp) {
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
		for (Polygon pol1 : target.polygons_tmp) {
			for (Polygon pol2 : polygons_tmp) {
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

		for (Point p : points_tmp) {
			p.draw(c);
		}
		for (Circle ci : circles_tmp) {
			ci.draw(c);
		}
		for (Polygon pol : polygons_tmp) {
			pol.draw(c);
		}
		c.drawRect(reducedArea_tmp, p);
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
