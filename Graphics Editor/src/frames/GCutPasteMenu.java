package frames;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GCutPasteMenu extends JMenu {
	private static final long serialVersionUID = 1L;

	private GDrawingPanel drawPanel;

	public enum ECutPasteMenuItem {
		eCut("cut", new JMenuItem("cut")), ePaste("paste", new JMenuItem("paste"));

		private String name;
		private JMenuItem menuItem;

		private ECutPasteMenuItem(String name, JMenuItem menuItem) {
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

	private ECutPasteMenuItem eSelectedMenuItem;

	public GCutPasteMenu(String title, GDrawingPanel drawPanel) {
		super(title);
		this.drawPanel = drawPanel;

		ActionHandler actionHandler = new ActionHandler();

		for (ECutPasteMenuItem eMenuItem : ECutPasteMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eMenuItem.getName());
			this.add(menuItem);
			menuItem.setActionCommand(eMenuItem.toString());
			menuItem.addActionListener(actionHandler);
		}

	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			eSelectedMenuItem = ECutPasteMenuItem.valueOf(e.getActionCommand());

			if (eSelectedMenuItem == ECutPasteMenuItem.eCut) {
				drawPanel.cut();
			} else if (eSelectedMenuItem == ECutPasteMenuItem.ePaste) {
				drawPanel.paste();
			}
		}
	}

}
