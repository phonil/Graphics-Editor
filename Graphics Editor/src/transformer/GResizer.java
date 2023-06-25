package transformer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Vector;

import main.GConstants.EAnchors;
import shapes.GShape;

public class GResizer extends GTransformer {
	private Vector<GShape> groupShapes;

	private AffineTransform affineTransform;
	private EAnchors eSelectedAnchor;

	private int x0, y0;
	private double sx, sy;

	public GResizer(GShape shape, Vector<GShape> groupShapes, EAnchors eSelectedAnchor) {
		super(shape);
		this.affineTransform = new AffineTransform();
		this.groupShapes = groupShapes;
		this.eSelectedAnchor = eSelectedAnchor;
	}

	@Override
	public void initTransform(int x, int y, Graphics2D graphics2d) {
		this.shape.setPoint(x, y);
		x0 = (int) this.shape.getShape().getBounds().getX();
		y0 = (int) this.shape.getShape().getBounds().getY();
	}

	@Override
	public void keepTransform(int x, int y, Graphics2D graphics2D) {
		for (GShape shape : this.groupShapes) {
			graphics2D.setXORMode(shape.getShapeColor());
			shape.draw(graphics2D);
		}

		for (GShape shape : this.groupShapes) {
			// x - x0 == width
			sx = (double) ((x - x0) / (shape.getShape().getBounds().getWidth()));
			// y - y0 == heigth
			sy = (double) ((y - y0) / (shape.getShape().getBounds().getHeight()));

			int x1 = (int) shape.getShape().getBounds().getX();
			int y1 = (int) shape.getShape().getBounds().getY();

			if (this.eSelectedAnchor == EAnchors.EE)
				this.affineTransform.setToScale(sx, 1);
			else if (this.eSelectedAnchor == EAnchors.SS)
				this.affineTransform.setToScale(1, sy);
			else if (this.eSelectedAnchor == EAnchors.WW) {
				this.affineTransform.setToScale(sx, 1);
			} else
				this.affineTransform.setToScale(sx, sy);

//			this.affineTransform.setToScale(sx, sy);
			Shape transformedShape = this.affineTransform.createTransformedShape(shape.getShape());
			shape.setShape(transformedShape);

			int x2 = (int) shape.getShape().getBounds().getX();
			int y2 = (int) shape.getShape().getBounds().getY();

			this.affineTransform.setToTranslation(x1 - x2, y1 - y2);
			transformedShape = this.affineTransform.createTransformedShape(shape.getShape());
			shape.setShape(transformedShape);
			graphics2D.setXORMode(shape.getShapeColor());
			shape.draw(graphics2D);
		}

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
