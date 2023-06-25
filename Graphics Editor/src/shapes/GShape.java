package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import main.GConstants.EAnchors;

abstract public class GShape implements Serializable, Cloneable { // �̷��� �ϸ�, �����Ϸ��� �ڵ����� read / write �Լ��a ����� ������
	protected Shape shape; // ������, �̸��� ������ ���� / ��ü �ƴ�
	private boolean bSelected;
	private GAnchors gAnchors;

	protected Color shapeColor;

	public GShape() { // GShape�� ��ǥ�� ������ �ʿ䰡 ����
		// ���� ���� �� �����ڿ� �̹� �־����� ���� ���� ���� �ʿ� ����
		this.bSelected = false;
		this.gAnchors = new GAnchors();
	}

	abstract public GShape clone(); // shape�� ����Ű�, �𸣰����� �ؿ� ����� �˾Ƽ� �ض�..

	public Color getShapeColor() {
		return this.shapeColor;
	}

	public void setShapeColor(Color shapeColor) {
		this.shapeColor = shapeColor;
	}

	public Shape getShape() {
		return this.shape; // ��¥ �׸��� �갡 ������ �ִ�..?

	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	// onShape�� draw�� ��� ������ �����ϹǷ�
	public boolean onShape(Point p) { // ������ �� ���� �ִ��Ŀ� ���� �Լ�
		return shape.contains(p);
	}

	public void draw(Graphics2D graphics2D) {
		graphics2D.draw(shape);
//		if (this.bSelected) {
//			this.gAnchors.draw(graphics2D, this.shape.getBounds());
//		}
		this.anchorDraw(graphics2D);
	}

	public void anchorDraw(Graphics2D graphics2D) {
		if (this.bSelected) {
			this.gAnchors.draw(graphics2D, this.shape.getBounds());
		}
	}

	public boolean getSelected() {
		return this.bSelected;
	}

	public void setSelected(boolean bSelected) {
		this.bSelected = bSelected;
	}

	public EAnchors onShape(int x, int y) { // ���õ� �� �� /
		if (this.bSelected) { // ��Ŀ�� �ִ���.. ��Ŀ ���� �ִ��� ?
			EAnchors eAnchor = this.gAnchors.onShape(x, y);
			if (eAnchor != null) {
				return eAnchor;
			}
		}
		if (this.shape instanceof Line2D) {
			Line2D line2D = (Line2D) shape;
			if (line2D.ptSegDist(new Point(x, y)) < 5)
				return EAnchors.MM;

		} else if (this.shape.contains(x, y)) { // ��Ŀ ���� ���� ������ �� ���� �ִ��� ?
			return EAnchors.MM;
		}
		return null;
	}

	public Point2D.Double getShapeCenter() {
		Rectangle2D bounds = shape.getBounds2D();
		double centerX = bounds.getCenterX();
		double centerY = bounds.getCenterY();
		return new Point2D.Double(centerX, centerY);
	}

	public abstract void setShape(int x1, int y1, int x2, int y2);

	public abstract void setPoint(int x, int y);

	public abstract void resizePoint(int x, int y);

	public abstract void movePoint(int x, int y);

	public void addPoint(int x, int y) {// abstract �Ⱥ��� / only Polygon�̹Ƿ�, �̰��� �����ϴ� ����� ��ü�ض�! ��� ��
		// abstract�� ��� �� �����ض�! ��� ��
	}

}