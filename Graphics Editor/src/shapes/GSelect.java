package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GSelect extends GRectangle { // 그룹핑하여 기억할거니까 나중에... / 일단 네모 모양이긴 함 --> GRectangle 상속받음

	public GSelect() {
	}

	public void draw(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		graphics2D.setColor(Color.MAGENTA);
		graphics2D.draw(shape);
	}

}