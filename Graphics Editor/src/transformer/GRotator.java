package transformer;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Vector;

import shapes.GShape;

public class GRotator extends GTransformer {
	private Vector<GShape> shapes;
	private Vector<GShape> groupShapes;

	private AffineTransform affineTransform;
	private int px, py;
	private double angle;

	public GRotator(GShape shape, Vector<GShape> shapes, Vector<GShape> groupShapes) {
		super(shape);
		this.affineTransform = new AffineTransform();
		this.shapes = shapes;
		this.groupShapes = groupShapes;
	}

	@Override
	public void initTransform(int x, int y, Graphics2D graphics2D) {
		px = x;
		py = y;
	}

	@Override
	public void keepTransform(int x, int y, Graphics2D graphics2D) {
		double newX = x - px;
		double newY = y - py;
		double newDist = Math.sqrt(newX * newX + newY * newY);

		double rotationSpeedFactor = 0.005;

		double newAngle = Math.atan2(newY, newX) * rotationSpeedFactor;

		for (GShape shape : this.groupShapes) {
			graphics2D.setXORMode(shape.getShapeColor());
			shape.draw(graphics2D);
		}

		angle += newAngle;

		for (GShape shape : this.groupShapes) {
			Point2D shapeCenter = shape.getShapeCenter();
			this.affineTransform.setToRotation(angle, shapeCenter.getX(), shapeCenter.getY());
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