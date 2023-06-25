package transformer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import shapes.GShape;

public class GMover extends GTransformer {
	private Vector<GShape> shapes;
	private Vector<GShape> groupShapes;

	private AffineTransform affineTransform;
	private int x0, y0, tx, ty;
	private int px, py;

	public GMover(GShape shape, Vector<GShape> shapes, Vector<GShape> groupShapes) {
		super(shape);
		this.affineTransform = new AffineTransform();
		this.groupShapes = groupShapes;
	}

	@Override
	public void initTransform(int x, int y, Graphics2D graphics2D) {
		px = x;
		py = y;
		// drawer에서 그리고, select true 함
	}

	@Override
	public void keepTransform(int x, int y, Graphics2D graphics2D) {
		for (GShape shape : this.groupShapes) {
			graphics2D.setXORMode(shape.getShapeColor());
			shape.draw(graphics2D);
		}

		for (GShape shape : this.groupShapes) {
			this.affineTransform.setToTranslation(x - px, y - py);
			Shape transformedShape = this.affineTransform.createTransformedShape(shape.getShape());
			shape.setShape(transformedShape);
			graphics2D.setXORMode(shape.getShapeColor());
			shape.draw(graphics2D);
		}

		px = x;
		py = y;

	}

	@Override
	public void finalizeTransform(int x, int y, Graphics2D graphics2D, Vector<GShape> shapes) {
		for (GShape shape : shapes) {
			if (shape.getSelected()) {
				shape.anchorDraw(graphics2D);
				shape.setSelected(false);
				this.groupShapes.remove(shape);
			}
		}

		if (!this.shape.getSelected()) {
			this.shape.setSelected(true);
			this.shape.anchorDraw(graphics2D);
		}
	}

}
