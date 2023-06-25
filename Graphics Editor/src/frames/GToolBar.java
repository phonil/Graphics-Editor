package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JToolBar;

import main.GConstants.EShape;

public class GToolBar extends JToolBar {

	private static final long serialVersionUID = 1L;

	private ButtonGroup buttonGroup;
	private EShape eSelectedShape;

	public EShape getESelectedShape() {
		return this.eSelectedShape;
	}

	public void resetESelectedShape() {
		this.buttonGroup.clearSelection();
		this.eSelectedShape = EShape.eSelect;

		Enumeration<AbstractButton> a = this.buttonGroup.getElements();

		while (a.hasMoreElements())
			a.nextElement().setBorderPainted(false);
	}

	public GToolBar() {
		super();

		ActionHandler actionHandler = new ActionHandler();
		buttonGroup = new ButtonGroup();

		for (EShape eShape : EShape.values()) {
			if (eShape != EShape.eSelect) {
				JRadioButton buttonShape = new JRadioButton(eShape.getName(), eShape.getImg());
				this.add(buttonShape);
				buttonShape.setActionCommand(eShape.toString());
				buttonShape.addActionListener(actionHandler);
				buttonShape.setBorderPainted(false);
				buttonGroup.add(buttonShape);
			}

		}
		resetESelectedShape();

	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			eSelectedShape = EShape.valueOf(e.getActionCommand());
			JRadioButton buttonShape = (JRadioButton) e.getSource();
			buttonShape.setBorderPainted(true);
		}
	}
}
