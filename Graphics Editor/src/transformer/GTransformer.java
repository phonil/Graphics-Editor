package transformer;

import java.awt.Graphics2D;
import java.util.Vector;

import shapes.GShape;

abstract public class GTransformer { // transformer - 좌표. 즉, 내부 속성을 변경시켜주는 아이
	protected GShape shape;

	public GTransformer(GShape shape) {
		this.shape = shape;
	}

	abstract public void initTransform(int x, int y, Graphics2D graphics2D);

	abstract public void keepTransform(int x, int y, Graphics2D graphics2D);

	abstract public void finalizeTransform(int x, int y, Graphics2D graphics2D, Vector<GShape> shapes);

	public void continueTransform(int x, int y, Graphics2D graphics2D) { // 2p drawing에서는 필요 없으므로..!
	}

}
