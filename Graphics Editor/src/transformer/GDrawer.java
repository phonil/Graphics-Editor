package transformer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Stack;
import java.util.Vector;

import shapes.GShape;

public class GDrawer extends GTransformer {
	private Vector<GShape> groupShapes;
	private Color c;
	private Stack<GShape> undoStack;

	public GDrawer(GShape shape, Vector<GShape> groupShapes, Color c, Stack<GShape> undoStack) {
		super(shape);
		this.groupShapes = groupShapes;
		this.c = c;
		this.undoStack = undoStack;
	}

	public void initTransform(int x, int y, Graphics2D graphics2D) {
		this.shape.setShape(x, y, x, y);
	}

	public void keepTransform(int x, int y, Graphics2D graphics2D) {
		this.shape.draw(graphics2D);
		this.shape.resizePoint(x, y);
		this.shape.draw(graphics2D);
	}

	public void continueTransform(int x, int y, Graphics2D graphics2D) {
		this.shape.addPoint(x, y);
	}

	public void finalizeTransform(int x, int y, Graphics2D graphics2D, Vector<GShape> shapes) {
		this.shape.setSelected(true);
		this.shape.anchorDraw(graphics2D);
		this.shape.setShapeColor(c);

		shapes.add(this.shape);
		undoStack.add(this.shape);

		for (GShape shape : this.groupShapes) {
			shape.anchorDraw(graphics2D);
			shape.setSelected(false);
		}
		this.groupShapes.clear();
	}

}
