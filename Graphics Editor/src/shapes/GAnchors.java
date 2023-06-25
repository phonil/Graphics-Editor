package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;

import main.GConstants.EAnchors;

public class GAnchors { // 이 안에 n개의 oval(ellipse)가 들어가지

	static final int width = 10;
	static final int height = 10;
	private Ellipse2D[] anchors;

	public GAnchors() {
		anchors = new Ellipse2D[EAnchors.values().length - 1]; // mm 제외하기 위해 -1
		for (int i = 0; i < anchors.length; i++) {
			anchors[i] = new Ellipse2D.Float(0, 0, width, height);
		}
	}

	public void setPosition(Rectangle r) {
		int x = r.x - width / 2;
		int y = r.y - height / 2;
		anchors[EAnchors.NW.ordinal()].setFrame(x, y, width, height);
		anchors[EAnchors.NN.ordinal()].setFrame(x + r.width / 2, y, width, height);
		anchors[EAnchors.NE.ordinal()].setFrame(x + r.width, y, width, height);
		anchors[EAnchors.EE.ordinal()].setFrame(x + r.width, y + r.height / 2, width, height);
		anchors[EAnchors.SE.ordinal()].setFrame(x + r.width, y + r.height, width, height);
		anchors[EAnchors.SS.ordinal()].setFrame(x + r.width / 2, y + r.height, width, height);
		anchors[EAnchors.SW.ordinal()].setFrame(x, y + r.height, width, height);
		anchors[EAnchors.WW.ordinal()].setFrame(x, y + r.height / 2, width, height);
		anchors[EAnchors.RR.ordinal()].setFrame(x + r.width / 2, y - 30, width, height);
	}

	public void draw(Graphics2D graphics2D, Rectangle rectangle) {
		// set Position
		setPosition(rectangle);
		graphics2D.setXORMode(Color.white); // 잔상처리
		for (Ellipse2D anchor : anchors) {
			graphics2D.fill(anchor);
			graphics2D.draw(anchor);
		}
	}

	public EAnchors onShape(int x, int y) { // 앵커 위에 있느냐 ? / 어떤 앵커 위에 있느냐
		for (int i = 0; i < anchors.length; i++) {
			if (anchors[i].contains(x, y)) {
				return EAnchors.values()[i];
			}
		}

		return null;
	}
}
