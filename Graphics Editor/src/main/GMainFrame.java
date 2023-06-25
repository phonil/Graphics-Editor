package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import frames.GColorToolBar;
import frames.GDrawingPanel;
import frames.GMenuBar;
import frames.GToolBar;

public class GMainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private GMenuBar menuBar;
	private GToolBar toolBar;
	private GColorToolBar colorToolBar;
	private GDrawingPanel drawingPanel;

	public GMainFrame() {
		// attrubutes
		this.setSize(GConstants.CMainFrame.w, GConstants.CMainFrame.h);

		Dimension frameSize = this.getSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// components

		BorderLayout layout = new BorderLayout();
		this.setLayout(layout);

		this.toolBar = new GToolBar();
		this.add(this.toolBar, BorderLayout.NORTH);

		this.colorToolBar = new GColorToolBar();
		this.add(this.colorToolBar, BorderLayout.EAST);

		this.drawingPanel = new GDrawingPanel();
		this.add(drawingPanel, BorderLayout.CENTER);

		this.menuBar = new GMenuBar(drawingPanel);
		this.setJMenuBar(menuBar);

		// association
		this.drawingPanel.setToolBar(toolBar, colorToolBar);
	}
}
