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

/**
 * @ClassName: GraphicCollision
 * @Description: TODO
 * @author: Noisyfox
 * @date: 2012-6-27 上午11:12:21
 * 
 */
public class GraphicCollision {

	List<Polygon> polygons = new ArrayList<Polygon>();
	List<Circle> circles = new ArrayList<Circle>();
	List<Point> points = new ArrayList<Point>();

	int baseX = 0;
	int baseY = 0;

	public GraphicCollision() {

	}

	public final void addCircle(int x, int y, int r) {
		addCircle(x, y, r, true);
	}

	public final void addCircle(int x, int y, int r, boolean fill) {
		Circle c = new Circle(x, y, r, fill);
		circles.add(c);
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

		Polygon p = new Polygon(v, fill);
		polygons.add(p);
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
		baseX = x;
		baseY = y;
	}

	public final boolean isCollisionWith(GraphicCollision target) {
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
					if (pointInCircle(p1, c)) {
						return true;
					}
				}
			}
		}
		for (Circle c : circles) {
			for (Point p1 : target.points) {
				if (c.filled()) {
					if (pointInCircle(p1, c)) {
						return true;
					}
				}
			}
		}
		// 点与多边形
		for (Polygon pol : target.polygons) {
			for (Point p : points) {
				if (pol.filled()) {
					if (pointInPolygon(p, pol)) {
						return true;
					}
				}
			}
		}
		for (Polygon pol : polygons) {
			for (Point p : target.points) {
				if (pol.filled()) {
					if (pointInPolygon(p, pol)) {
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
					if (circleVSline(c, pol.getVertex(0), pol.getVertex(1),
							true)) {
						return true;
					}
					continue;
				}
				// 圆与多边形
				// 有任何一边与圆相交
				for (int i = 0; i < pol.getEdgeNumber(); i++) {
					if (circleVSline(c, pol.getVertex(i), pol.getVertex(i + 1),
							true)) {
						return true;
					}
				}
				// 没有边相交，则判断是否有包含关系
				if (pointInCircle(pol.getVertex(0), c)) {
					if (c.filled()) {
						return true;
					}
				} else if (pol.filled()) {
					if (pointInPolygon(c, pol)) {
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
					if (lineVSline(pol1.getVertex(0), pol1.getVertex(1),
							pol2.getVertex(0), pol2.getVertex(1))) {
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
					boolean b1 = pointInPolygon(pol1.getVertex(0), pol2);
					boolean b2 = pointInPolygon(pol1.getVertex(1), pol2);
					if ((b1 && (!b2)) || ((!b1) && b2)) {
						return true;
					}
					continue;
				}
				if (!pol1.isLine() && pol2.isLine()) {
					boolean b1 = pointInPolygon(pol2.getVertex(0), pol1);
					boolean b2 = pointInPolygon(pol2.getVertex(1), pol1);
					if ((b1 && (!b2)) || ((!b1) && b2)) {
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
							if (pointInPolygon(pol1.getVertex(i), pol2)) {
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
							if (pointInPolygon(pol2.getVertex(i), pol1)) {
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

	// ********************************************************************************************
	// 私有成员
	//
	// 判断一个点是否在一个圆内
	private boolean pointInCircle(Point p, Circle c) {
		int d2 = p.squareDistance(c);
		if (d2 <= (c.getR() * c.getR())) {
			return true;
		}
		return false;
	}

	// 判断一个点是否在一个夹角内
	private boolean pointInAngle(Point point, Point vertex, Point p1, Point p2) {
		int x = point.getX() - vertex.getX();
		int y = point.getY() - vertex.getY();
		int p1X = p1.getX() - vertex.getX();
		int p1Y = p1.getY() - vertex.getY();
		int p2X = p2.getX() - vertex.getX();
		int p2Y = p2.getY() - vertex.getY();

		int v1 = x * p1Y - y * p1X;
		int v2 = x * p2Y - y * p2X;

		if (v1 * v2 <= 0)
			return true;
		return false;
	}

	// 判断两条线段是否相交
	private boolean lineVSline(Point l1P1, Point l1P2, Point l2P1, Point l2P2) {
		return pointInAngle(l1P2, l1P1, l2P1, l2P2)
				&& pointInAngle(l2P2, l2P1, l1P1, l1P2);
	}

	// 判断点是否在多边形内
	private boolean pointInPolygon(Point p, Polygon pol) {
		if (!pol.isLine()) {
			int nVertex = pol.getVertexNumber();
			boolean collision = true;
			for (int i = 0; i < nVertex; i++) {
				if (!pointInAngle(p, pol.getVertex(i), pol.getVertex(i - 1),
						pol.getVertex(i + 1))) {
					collision = false;
					break;
				}
			}
			if (collision) {
				return true;
			}
		}
		return false;
	}

	// 数量积
	private int dotProduct(Point vertex, Point p1, Point p2) {
		int x1, y1;
		int x2, y2;

		x1 = p1.getX() - vertex.getX();
		y1 = p1.getY() - vertex.getY();
		x2 = p2.getX() - vertex.getX();
		y2 = p2.getY() - vertex.getY();
		return x1 * x2 + y1 * y2;
	}

	// 点到直线距离平方
	private int squareDistanceFromPointToLine(Point point, Point p1, Point p2) {
		int dp = dotProduct(p1, point, p2);
		int dp2 = dp * dp;
		int p1p22 = p1.squareDistance(p2);
		int projectionSquareLength = dp2 / p1p22;
		return point.squareDistance(p1) - projectionSquareLength;
	}

	// 判断圆与直线（线段）有无交点
	private boolean circleVSline(Circle c, Point p1, Point p2, boolean segment) {
		if (segment) {// 线段
			boolean hasIn = false;
			boolean hasOut = false;
			if (pointInCircle(p1, c)) {
				hasIn = true;
			} else {
				hasOut = true;
			}
			if (pointInCircle(p2, c)) {
				hasIn = true;
			} else {
				hasOut = true;
			}
			// 先判断两个端点是不是都在圆内
			if (hasIn && !hasOut) {
				// 都在圆内，无交点
				return false;
			}
			// 判断圆心是不是在线段两侧
			if (dotProduct(p1, c, p2) * dotProduct(p2, c, p1) <= 0) {
				// 在线段两侧，则只需要验证两个端点即可
				if (hasOut && !hasIn) {
					return false;
				}
				return true;
			}
		}
		// 直线或者圆心在线段上方，只需判断圆心到直线距离即可
		int squareDistance = squareDistanceFromPointToLine(c, p1, p2);
		return squareDistance <= c.getR() * c.getR();
	}

	private class Polygon {
		Point[] vertex;
		int num_edges = 0;
		int num_vertexs = 0;
		boolean isLine = false;
		boolean fill = false;

		public Polygon(Point[] vertex, boolean fill) {
			if (vertex.length <= 1) {
				throw new IllegalArgumentException();
			}

			// 判断有无点重合
			for (int i = 0; i < vertex.length; i++) {
				for (int j = i + 1; j < vertex.length; j++) {
					if (vertex[i].getX() == vertex[j].getX()
							&& vertex[i].getY() == vertex[j].getY()) {
						throw new IllegalArgumentException();
					}
				}
			}

			this.vertex = vertex;
			num_vertexs = vertex.length;
			if (num_vertexs == 2) {
				num_edges = 1;
				isLine = true;
			} else {
				num_edges = num_vertexs;
			}
			this.fill = fill;
		}

		public final Point getVertex(int vertexIndex) {
			while (vertexIndex < 0)
				vertexIndex += num_vertexs;
			while (vertexIndex >= num_vertexs)
				vertexIndex -= num_vertexs;
			return vertex[vertexIndex];
		}

		public final int getEdgeNumber() {
			return num_edges;
		}

		public final int getVertexNumber() {
			return num_vertexs;
		}

		public final boolean filled() {
			return fill;
		}

		public final boolean isLine() {
			return isLine;
		}

	}

	private class Circle extends Point {
		int r;

		boolean fill = false;

		public Circle(int x, int y, int r, boolean fill) {
			super(x, y);
			if (r <= 0) {
				throw new IllegalArgumentException();
			}
			this.r = r;

			this.fill = fill;
		}

		public int getR() {
			return r;
		}

		public final boolean filled() {
			return fill;
		}

	}

	private class Point {
		int x = 0;
		int y = 0;

		public Point(int x, int y) {
			setPosition(x, y);
		}

		public void setPosition(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void move(int dx, int dy) {
			x += dx;
			y += dy;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		// 得到两点距离的平方
		public int squareDistance(Point p2) {
			return (x - p2.getX()) * (x - p2.getX()) + (y - p2.getY())
					* (y - p2.getY());
		}

	}
}
