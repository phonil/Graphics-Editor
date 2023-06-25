package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GDoMenu extends JMenu {

	private GDrawingPanel drawPanel;

	public enum EDoMenuItem {
		eUndo("undo", new JMenuItem("undo")), eRedo("redo", new JMenuItem("redo"));

		private String name;
		private JMenuItem menuItem;

		private EDoMenuItem(String name, JMenuItem menuItem) {
			this.name = name;
			this.menuItem = menuItem;
		}

		public String getName() {
			return name;
		}

		public JMenuItem getMenuItem() {
			return menuItem;
		}
	}

	private EDoMenuItem eSelectedMenuItem;

	public GDoMenu(String title, GDrawingPanel drawPanel) {
		super(title);
		this.drawPanel = drawPanel;

		ActionHandler actionHandler = new ActionHandler();

		for (EDoMenuItem eMenuItem : EDoMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eMenuItem.getName());
			this.add(menuItem);
			menuItem.setActionCommand(eMenuItem.toString());
			menuItem.addActionListener(actionHandler);
		}

	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			eSelectedMenuItem = EDoMenuItem.valueOf(e.getActionCommand());

			if (eSelectedMenuItem == EDoMenuItem.eUndo) {
				drawPanel.undo();
			} else if (eSelectedMenuItem == EDoMenuItem.eRedo) {
				drawPanel.redo();
			}
		}
	}

}
