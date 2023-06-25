package transformer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

import shapes.GShape;

public class GSelector extends GTransformer {
	private Vector<GShape> shapes;
	private Color c;
	private int px, py;

	public GSelector(GShape shape, Color c, Vector<GShape> shapes) {
		super(shape);
		this.shapes = shapes;
		this.c = c;
	}

	@Override
	public void initTransform(int x, int y, Graphics2D graphics2d) {
		this.shape.setShape(x, y, x, y);

	}

	@Override
	public void keepTransform(int x, int y, Graphics2D graphics2D) {
		this.shape.draw(graphics2D);
		this.shape.resizePoint(x, y);
		this.shape.draw(graphics2D);
	}

	@Override
	public void finalizeTransform(int x, int y, Graphics2D graphics2D, Vector<GShape> shapes) {
//		graphics2D.setXORMode(Color.white);
		graphics2D.setXORMode(c);
		this.shape.draw(graphics2D);
		for (GShape shape : shapes) {
			if (this.shape.getShape().intersects(shape.getShape().getBounds())) {
				shape.setSelected(true);
				shape.anchorDraw(graphics2D);
			}
		}
	}

}
