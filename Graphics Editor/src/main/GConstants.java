package main;

import javax.swing.ImageIcon;

import shapes.GLine;
import shapes.GOval;
import shapes.GPolygon;
import shapes.GRectangle;
import shapes.GSelect;
import shapes.GShape;

public class GConstants {

	public class CMainFrame {
		static final int x = 200;
		static final int y = 100;
		static final int w = 600;
		static final int h = 400;
	}

	public enum EUserAction {
		e2Point, eNPoint
	}

	public enum EAnchors {
		NW, NN, NE, EE, SE, SS, SW, WW, RR, MM
	}

	public enum EShape {
		eSelect(new ImageIcon("src/imgs/icon-pen.png"), "Select", new GSelect(), EUserAction.e2Point),
		eRectangle(new ImageIcon("src/imgs/icon-rectangle.png"), "Rectangle", new GRectangle(), EUserAction.e2Point),
		eOval(new ImageIcon("src/imgs/icon-circle.png"), "Oval", new GOval(), EUserAction.e2Point),
		eLine(new ImageIcon("src/imgs/icon-line.png"), "Line", new GLine(), EUserAction.e2Point),
		ePolygon(new ImageIcon("src/imgs/icon-polygon.png"), "Polygon", new GPolygon(), EUserAction.eNPoint);

		private String name;
		private GShape gShape;
		private EUserAction eUserAction;

		private ImageIcon img;

		private EShape(ImageIcon img, String name, GShape gShape, EUserAction eUserAction) {
			this.img = img;
			this.name = name;
			this.gShape = gShape;
			this.eUserAction = eUserAction;
		}

		public ImageIcon getImg() {
			return this.img;
		}

		public void setImg(String img) {
			this.img = new ImageIcon(img);
		}

		public String getName() {
			return this.name;
		}

		public GShape getGShape() {
			return this.gShape;
		}

		public EUserAction getEUserAction() {
			return this.eUserAction;
		}

	}

}
