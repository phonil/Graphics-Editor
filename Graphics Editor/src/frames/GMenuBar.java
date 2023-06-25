package frames;

import javax.swing.JMenuBar;

public class GMenuBar extends JMenuBar {
	private static final long serialVersionUID = 1L;

	private GFileMenu fileMenu;
	private GDoMenu doMenu;
	private GCutPasteMenu cutPastMenu;

	public GMenuBar(GDrawingPanel drawingPanel) {
		fileMenu = new GFileMenu("File", drawingPanel);
		doMenu = new GDoMenu("undo/redo", drawingPanel);
		cutPastMenu = new GCutPasteMenu("cut/paste", drawingPanel);
		this.add(fileMenu);
		this.add(doMenu);
		this.add(cutPastMenu);

	}

}
