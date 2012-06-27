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

	int x = 0;
	int y = 0;

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
		this.x = x;
		this.y = y;
	}

	// 判断一个点是否在一个夹角内
	private boolean inAngle(int x, int y, int vertexX, int vertexY, int p1X,
			int p1Y, int p2X, int p2Y) {
		x -= vertexX;
		y -= vertexY;
		p1X -= vertexX;
		p1Y -= vertexY;
		p2X -= vertexX;
		p2Y -= vertexY;

		int v1 = x * p1Y - y * p1X;
		int v2 = x * p2Y - y * p2X;

		if (v1 * v2 <= 0)
			return true;
		return false;
	}

	public final boolean isCollisionWith(GraphicCollision target) {
		// 优先进行点的判断
		// 点与点
		for (Point p1 : points) {
			for (Point p2 : target.points) {
				if (p1.getX(x) == p2.getX(target.x)
						&& p1.getY(y) == p2.getY(target.y)) {
					return true;
				}
			}
		}
		// 点与圆面
		for (Circle c : target.circles) {
			for (Point p1 : points) {
				if (c.filled()) {
					int d2 = (p1.getX(x) - c.getX(target.x))
							* (p1.getX(x) - c.getX(target.x))
							+ (p1.getY(y) - c.getY(target.y))
							* (p1.getY(y) - c.getY(target.y));
					if (d2 <= (c.getR() * c.getR())) {
						return true;
					}
				}
			}
		}
		// 点与多边形
		for (Polygon pol : target.polygons) {
			for (Point p : points) {
				if (pol.filled() && !pol.isLine) {
					int nVertex = pol.getVertexNumber();
					boolean collision = true;
					for (int i = 0; i < nVertex; i++) {
						if (!inAngle(p.getX(x), p.getY(y), pol.getVertex(i)
								.getX(target.x), pol.getVertex(i)
								.getY(target.y),
								pol.getVertex(i - 1).getX(target.x), pol
										.getVertex(i - 1).getY(target.y), pol
										.getVertex(i + 1).getX(target.x), pol
										.getVertex(i + 1).getY(target.y))) {
							collision = false;
							break;
						}
					}
					if (collision) {
						return true;
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
			while(vertexIndex < 0)vertexIndex += num_vertexs;
			while(vertexIndex >= num_vertexs)vertexIndex -= num_vertexs;
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
		int x;
		int y;

		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return getX(0);
		}

		public int getX(int positionX) {
			return x + positionX;
		}

		public int getY() {
			return getY(0);
		}

		public int getY(int positionY) {
			return y + positionY;
		}
	}
}
