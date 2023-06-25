package shapes;

import java.awt.Rectangle;

public class GRectangle extends GShape {
	private int ox, oy;
	private int px, py; // �����Ϸ��� ���� ��

	public GRectangle() {

	}

	@Override
	public GShape clone() {
		return new GRectangle(); // ��� �ϳ� ������� ������ / ��� �̰� ������⺸�� �� ���� ���� ������
	}

	@Override
	public void setShape(int x1, int y1, int x2, int y2) {
		this.shape = new Rectangle(x1, y1, x2 - x1, y2 - y1); // Shape Ŭ������ contains �Լ� / ������ ���� �ֳ�.. �̷� �޼ҵ尡 ����
		ox = x1;
		oy = y1;
	}

	@Override
	public void resizePoint(int x2, int y2) {
		Rectangle rectangle = (Rectangle) shape;

//		int x = (int) Math.min(rectangle.getX(), x2);
//		int y = (int) Math.min(rectangle.getY(), y2);
//		int x1 = (int) Math.max(rectangle.getX(), x2);
//		int y1 = (int) Math.max(rectangle.getY(), y2);
//
//		rectangle.setFrame(x, y, x1 - x, y1 - y);

		if (ox <= x2 && oy <= y2) {
			rectangle.setFrame(ox, oy, x2 - ox, y2 - oy);
		} else if (ox > x2 && oy < y2) {
			rectangle.setFrame(x2, oy, ox - x2, y2 - oy);
		} else if (ox < x2 && oy > y2) {
			rectangle.setFrame(ox, y2, x2 - ox, oy - y2);
		} else {
			rectangle.setFrame(x2, y2, ox - x2, oy - y2);
		}

	}

	@Override
	public void setPoint(int x, int y) {
		this.px = x;
		this.py = y;
	}

	@Override
	public void movePoint(int x, int y) {
		Rectangle rectangle = (Rectangle) shape;
		rectangle.setLocation(rectangle.x + x - px, rectangle.y + y - py); // rec.x + dx , rec.y + dy
		this.px = x;
		this.py = y;
	}

}