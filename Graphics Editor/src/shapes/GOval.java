package shapes;

import java.awt.geom.Ellipse2D;

public class GOval extends GShape {
	private int px, py; // 무브하려고 찍은 점

	private int ox, oy;

	public GOval() {
	}

	@Override
	public GShape clone() {
		return new GOval(); // 빈게 하나 만들어진 것이지 / 사실 이건 복제라기보단 빈 것을 만든 것이지
	}

	@Override
	public void setShape(int x1, int y1, int x2, int y2) {
		this.shape = new Ellipse2D.Double(x1, y1, x2 - x1, y2 - y1);
		ox = x1;
		oy = y1;
	}

	@Override
	public void resizePoint(int x2, int y2) {
		Ellipse2D ellipse2D = (Ellipse2D) shape;

//		ellipse2D.setFrame(ellipse2D.getX(), ellipse2D.getY(), x2 - ellipse2D.getX(), y2 - ellipse2D.getY());

		if (ox <= x2 && oy <= y2) {
			ellipse2D.setFrame(ox, oy, x2 - ox, y2 - oy);
		} else if (ox > x2 && oy < y2) {
			ellipse2D.setFrame(x2, oy, ox - x2, y2 - oy);
		} else if (ox < x2 && oy > y2) {
			ellipse2D.setFrame(ox, y2, x2 - ox, oy - y2);
		} else {
			ellipse2D.setFrame(x2, y2, ox - x2, oy - y2);
		}

	}

	@Override
	public void setPoint(int x, int y) {
		this.px = x;
		this.py = y;
	}

	@Override
	public void movePoint(int x, int y) {
		Ellipse2D ellipse2D = (Ellipse2D) shape;
		ellipse2D.setFrame(ellipse2D.getX() + x - px, ellipse2D.getY() + y - py, ellipse2D.getWidth(),
				ellipse2D.getHeight());
		this.px = x;
		this.py = y;

	}

}