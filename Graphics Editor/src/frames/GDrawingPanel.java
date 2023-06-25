package frames;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JPanel;

import main.GConstants.EAnchors;
import main.GConstants.EShape;
import main.GConstants.EUserAction;
import shapes.GShape;
import transformer.GDrawer;
import transformer.GMover;
import transformer.GResizer;
import transformer.GRotator;
import transformer.GSelector;
import transformer.GTransformer;

public class GDrawingPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private EUserAction eUserAction;

	private enum EDrawingEvent {
		eStart, eMoving, eCon, eEnd
	}

	private enum EDrawingState {
		eIdle, eTransforming
	}

	private EDrawingState eDrawingState;
	private Vector<GShape> shapes;
	private GShape currentShape;

	private Vector<GShape> groupShapes;

	private Cursor cursor;

	// 현재 색
	private Color currentColor;

	private int r, g, b;

	// undo / redo
	private Stack<GShape> undoStack;
	private Stack<GShape> redoStack;

	// cut / paste
	private ArrayList<GShape> clipboard;

	// association
	private GToolBar toolBar;
	private GColorToolBar colorToolBar;

	public void setToolBar(GToolBar toolBar, GColorToolBar colorToolBar) {
		this.toolBar = toolBar;
		this.colorToolBar = colorToolBar;
	}

	private GTransformer transformer;

	public GDrawingPanel() {
		this.eDrawingState = EDrawingState.eIdle;
		this.shapes = new Vector<GShape>();
		this.currentShape = null;
		// group
		this.groupShapes = new Vector<GShape>();

		// color
		this.currentColor = Color.BLACK;

		// undo / redo
		this.undoStack = new Stack<>();
		this.redoStack = new Stack<>();

		this.setBackground(Color.WHITE);

		MouseEventHandler mouseEventHandler = new MouseEventHandler();
		this.addMouseListener(mouseEventHandler);
		this.addMouseMotionListener(mouseEventHandler);
	}

	public void paint(Graphics graphics) {
		super.paint(graphics);
		graphics.setColor(Color.white);
		graphics.setXORMode(currentColor);

		for (GShape shape : shapes) {
			Color a = new Color(255 - shape.getShapeColor().getRed(), 255 - shape.getShapeColor().getGreen(),
					255 - shape.getShapeColor().getBlue());

			graphics.setXORMode(a);
			shape.draw((Graphics2D) graphics);
		}
	}

	public GShape onShape(Point point) { // 어느 도형 위에 있는지 / 있으면 그 도형 반환
		for (GShape shape : shapes) {
			if (shape.onShape(point)) {
				return shape;
			}
		}
		return null;
	}

	public void initTransforming(int x, int y) { // 어떤 transformer를 쓸 지를 여기서 판단하는 것 / 우선순위가 제일 높은 것이 toolbar가 눌렸는지에 대한
		Graphics2D graphics2D = (Graphics2D) this.getGraphics();
		graphics2D.setXORMode(this.getBackground());

		int r = 255 - colorToolBar.getESelectedColor().getColor().getRed();
		int g = 255 - colorToolBar.getESelectedColor().getColor().getGreen();
		int b = 255 - colorToolBar.getESelectedColor().getColor().getBlue();
		Color c = new Color(r, g, b);

		if (this.toolBar.getESelectedShape() == EShape.eSelect) { // toolbar x
			// selection - move / resize / rotate
			EAnchors eSelectedAnchor = this.onShape(x, y); // 어떤 앵커 잡혔는지 반환 / MM이면 내 도형
			if (eSelectedAnchor == null) { // 빈공간 --> 그룹핑
				this.clearSelection();
				this.currentShape = this.toolBar.getESelectedShape().getGShape().clone();
				this.transformer = new GSelector(this.currentShape, c, this.shapes);
				this.transformer.initTransform(x, y, graphics2D);

			} else { // 빈공간 x / 도형이든 앵커든
				switch (eSelectedAnchor) {
				case MM: // 도형
					this.transformer = new GMover(this.currentShape, this.shapes, this.groupShapes);
					this.transformer.initTransform(x, y, graphics2D);
					break;
				case RR:
					// Rotator
					this.transformer = new GRotator(this.currentShape, this.shapes, this.groupShapes);
					this.transformer.initTransform(x, y, graphics2D);
					break;
				default:
					// Resizer
					this.transformer = new GResizer(this.currentShape, this.groupShapes, eSelectedAnchor);
					this.transformer.initTransform(x, y, graphics2D);
					break;
				}
			}

		} else { // toolbar o

			this.currentShape = this.toolBar.getESelectedShape().getGShape().clone();
			this.transformer = new GDrawer(this.currentShape, this.groupShapes, c, this.undoStack);
			this.transformer.initTransform(x, y, graphics2D);

		}
	}

	public void keepTransforming(int x, int y) {
		int r = 255 - colorToolBar.getESelectedColor().getColor().getRed();
		int g = 255 - colorToolBar.getESelectedColor().getColor().getGreen();
		int b = 255 - colorToolBar.getESelectedColor().getColor().getBlue();
		Color c = new Color(r, g, b);

		Graphics2D graphics2D = (Graphics2D) this.getGraphics();

		graphics2D.setXORMode(c);
		this.transformer.keepTransform(x, y, graphics2D);

	}

	public void continueTransforming(int x, int y) {
		Graphics2D graphics2D = (Graphics2D) this.getGraphics();
		graphics2D.setXORMode(this.getBackground());
		this.transformer.continueTransform(x, y, graphics2D);
	}

	public void finalizeTransforming(int x, int y) {
		Graphics2D graphics2D = (Graphics2D) this.getGraphics();

		this.transformer.finalizeTransform(x, y, graphics2D, this.shapes);

		for (GShape gShape : this.shapes) {
			if (gShape.getSelected())
				this.groupShapes.add(gShape);
		}

		this.currentShape = null;
		this.toolBar.resetESelectedShape();
		this.colorToolBar.resetESelectedColor();

	}

	private void changeCursors(int x, int y) {
		EAnchors eAnchor = this.onShape(x, y);
		if (eAnchor == null) {
			cursor = new Cursor(Cursor.DEFAULT_CURSOR);
			this.setCursor(cursor);
		} else {
			switch (eAnchor) {
			case MM:
				cursor = new Cursor(Cursor.CROSSHAIR_CURSOR);
				break;
			case RR:
				// rotate
				cursor = new Cursor(Cursor.HAND_CURSOR);
				break;
			case NW:
				cursor = new Cursor(Cursor.NW_RESIZE_CURSOR);
				break;
			case NN:
				cursor = new Cursor(Cursor.N_RESIZE_CURSOR);
				break;
			case NE:
				cursor = new Cursor(Cursor.NE_RESIZE_CURSOR);
				break;
			case EE:
				cursor = new Cursor(Cursor.E_RESIZE_CURSOR);
				break;
			case SE:
				cursor = new Cursor(Cursor.SE_RESIZE_CURSOR);
				break;
			case SS:
				cursor = new Cursor(Cursor.S_RESIZE_CURSOR);
				break;
			case SW:
				cursor = new Cursor(Cursor.SW_RESIZE_CURSOR);
				break;
			case WW:
				cursor = new Cursor(Cursor.W_RESIZE_CURSOR);
				break;
			default:
				cursor = new Cursor(Cursor.DEFAULT_CURSOR);
				break;
			}
			this.setCursor(cursor);
		}

	}

	private EAnchors onShape(int x, int y) {
		for (GShape gShape : this.shapes) {
			EAnchors eAnchor = gShape.onShape(x, y);
			if (eAnchor != null) { // 앵커 혹은 도형(MM)
				this.currentShape = gShape;
				return eAnchor;
			}
		}
		return null;
	}

	private void clearSelection() {
		Graphics2D graphics2D = (Graphics2D) this.getGraphics();
		for (GShape shape : this.shapes) {
			if (shape.getSelected()) {
				shape.anchorDraw(graphics2D);
				shape.setSelected(false);
			}
		}
		this.groupShapes.clear();
	}

	public void undo() {
		if (!undoStack.isEmpty()) {
			GShape shape = undoStack.pop();
			redoStack.push(shape);
			this.shapes.remove(shape);
			if (this.groupShapes.contains(shape)) {
				this.groupShapes.remove(shape);
				shape.setSelected(false);
			}

			repaint();

		}
	}

	public void redo() {
		if (!redoStack.isEmpty()) {
			GShape shape = redoStack.pop();
			undoStack.push(shape);
			this.shapes.add(shape);
			if (this.groupShapes.contains(shape)) {
				this.groupShapes.add(shape);
				shape.setSelected(true);
			}

			repaint();
		}
	}

	public void cut() {
		clipboard = new ArrayList<>();
		for (GShape shape : this.groupShapes) {
			clipboard.add(shape);
			shape.setSelected(false);
			this.shapes.remove(shape);
		}

		this.groupShapes.clear();
		repaint();
	}

	public void paste() {
		if (clipboard != null) {
			Graphics2D graphics2D = (Graphics2D) this.getGraphics();
			for (GShape shape : clipboard) {
				this.shapes.add(shape);
			}
			clipboard.clear();
			repaint();
		}
	}

	private class MouseEventHandler implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1) {
				mouse1Clicked(e);
			} else if (e.getClickCount() == 2) {
				mouse2Clicked(e);
			}
		}

		private void mouse2Clicked(MouseEvent e) {
			if (toolBar.getESelectedShape().getEUserAction() == EUserAction.eNPoint) {
				if (eDrawingState == EDrawingState.eTransforming) {
					finalizeTransforming(e.getX(), e.getY());
					eDrawingState = EDrawingState.eIdle;
				}
			}

		}

		private void mouse1Clicked(MouseEvent e) {
			if (toolBar.getESelectedShape().getEUserAction() == EUserAction.eNPoint) {
				if (eDrawingState == EDrawingState.eIdle) {
					initTransforming(e.getX(), e.getY());
					eDrawingState = EDrawingState.eTransforming;

				} else if (eDrawingState == EDrawingState.eTransforming) {
					continueTransforming(e.getX(), e.getY());
				}
			}

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			if (eDrawingState == EDrawingState.eTransforming) {
				keepTransforming(e.getX(), e.getY());
			} else if (eDrawingState == EDrawingState.eIdle) {
				changeCursors(e.getX(), e.getY());
			}

		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (toolBar.getESelectedShape().getEUserAction() == EUserAction.e2Point) {
				if (eDrawingState == EDrawingState.eIdle) {
					initTransforming(e.getX(), e.getY());
					eDrawingState = EDrawingState.eTransforming;
				}
			}

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (toolBar.getESelectedShape().getEUserAction() == EUserAction.e2Point) {
				if (eDrawingState == EDrawingState.eTransforming) {
					keepTransforming(e.getX(), e.getY());
				}

			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (toolBar.getESelectedShape().getEUserAction() == EUserAction.e2Point) {
				if (eDrawingState == EDrawingState.eTransforming) {
					finalizeTransforming(e.getX(), e.getY());
					eDrawingState = EDrawingState.eIdle;
				}
			}

		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
}
