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

abstract public class GShape implements Serializable, Cloneable { // 이렇게 하면, 컴파일러가 자동으로 read / write 함수륾 만드는 것이지
	protected Shape shape; // 포인터, 이름만 가지고 있음 / 객체 아님
	private boolean bSelected;
	private GAnchors gAnchors;

	protected Color shapeColor;

	public GShape() { // GShape에 좌표를 저장할 필요가 없음
		// 도형 만들 때 생성자에 이미 넣었으니 여기 굳이 넣을 필요 없음
		this.bSelected = false;
		this.gAnchors = new GAnchors();
	}

	abstract public GShape clone(); // shape을 만들거고, 모르겠으니 밑에 놈들이 알아서 해라..

	public Color getShapeColor() {
		return this.shapeColor;
	}

	public void setShapeColor(Color shapeColor) {
		this.shapeColor = shapeColor;
	}

	public Shape getShape() {
		return this.shape; // 진짜 그림은 얘가 가지고 있는..?

	}

	public void setShape(Shape shape) {
		this.shape = shape;
	}

	// onShape과 draw는 모든 도형이 동일하므로
	public boolean onShape(Point p) { // 도형이 점 위에 있느냐에 대한 함수
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

	public EAnchors onShape(int x, int y) { // 선택된 놈 중 /
		if (this.bSelected) { // 앵커가 있느냐.. 앵커 위에 있느냐 ?
			EAnchors eAnchor = this.gAnchors.onShape(x, y);
			if (eAnchor != null) {
				return eAnchor;
			}
		}
		if (this.shape instanceof Line2D) {
			Line2D line2D = (Line2D) shape;
			if (line2D.ptSegDist(new Point(x, y)) < 5)
				return EAnchors.MM;

		} else if (this.shape.contains(x, y)) { // 앵커 위에 있지 않으면 내 위에 있느냐 ?
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

	public void addPoint(int x, int y) {// abstract 안붙임 / only Polygon이므로, 이것을 구현하는 사람만 대체해라! 라는 뜻
		// abstract는 모두 다 구현해라! 라는 뜻
	}

}