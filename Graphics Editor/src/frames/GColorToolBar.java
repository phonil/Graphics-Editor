package frames;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

public class GColorToolBar extends JToolBar {

	public enum EColor {
		eBlack("BLACK", Color.BLACK), eRed("RED", Color.RED), eBlue("BLUE", Color.BLUE), eGreen("GREEN", Color.GREEN);

		private String name;
		private Color color;

		private EColor(String name, Color color) {
			this.name = name;
			this.color = color;
		}

		public String getName() {
			return this.name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Color getColor() {
			return this.color;
		}

		public void setColor(Color color) {
			this.color = color;
		}
	}

	private ButtonGroup buttonGroup;
	private EColor eSelectedColor;

	public EColor getESelectedColor() {
		return this.eSelectedColor;
	}

	public void resetESelectedColor() {
		this.buttonGroup.clearSelection();

		this.eSelectedColor = EColor.eBlack;

	}

	public GColorToolBar() {
		super();

		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(layout);

		ActionHandler actionHandler = new ActionHandler();
		buttonGroup = new ButtonGroup();

		for (EColor eColor : EColor.values()) {
			if (eColor != EColor.eBlack) {
				JRadioButton buttonShape = new JRadioButton(eColor.getName());
				this.add(buttonShape);
				buttonShape.setActionCommand(eColor.toString());
				buttonShape.addActionListener(actionHandler);
				buttonShape.setBorderPainted(false);
				buttonGroup.add(buttonShape);
			}
		}
		resetESelectedColor();
	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			eSelectedColor = EColor.valueOf(e.getActionCommand());
		}
	}
}
