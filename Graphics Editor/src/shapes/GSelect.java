package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GSelect extends GRectangle { // �׷����Ͽ� ����ҰŴϱ� ���߿�... / �ϴ� �׸� ����̱� �� --> GRectangle ��ӹ���

	public GSelect() {
	}

	public void draw(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setColor(Color.MAGENTA);
		graphics2D.draw(shape);
	}

}