package frames;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GFileMenu extends JMenu {

	private static final long serialVersionUID = 1L;

	private JMenuItem itemNew;
	private JFileChooser fileChooser;
//

	private JPanel drawPanel;

	public enum EMenuItem {
		eCapture("capture", new JMenuItem("capture")), eSave("save", new JMenuItem("save")),
		eOpen("open", new JMenuItem("open"));

		private String name;
		private JMenuItem menuItem;

		private EMenuItem(String name, JMenuItem menuItem) {
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

	private EMenuItem eSelectedMenuItem;

	public GFileMenu(String title, JPanel drawPanel) {
		super(title);
		this.drawPanel = drawPanel;

		ActionHandler actionHandler = new ActionHandler();

		for (EMenuItem eMenuItem : EMenuItem.values()) {
			JMenuItem menuItem = new JMenuItem(eMenuItem.getName());
			this.add(menuItem);
			menuItem.setActionCommand(eMenuItem.toString());
			menuItem.addActionListener(actionHandler);
		}

		fileChooser = new JFileChooser();

		// �⺻ Path�� ��� ���� (����ȭ��)
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop"));

		// ���͸��� Ȯ����
		FileNameExtensionFilter filter = new FileNameExtensionFilter("png ����", "png");
		fileChooser.addChoosableFileFilter(filter);

	}

	private void saveFile() {
		int answer = JOptionPane.showConfirmDialog(null, "�����Ͻðڽ��ϱ�?", "Save File?", JOptionPane.YES_NO_CANCEL_OPTION);
		if (answer == JOptionPane.YES_OPTION) { // Yes�� ������ ���
			// ���Ͽ��� ���̾�α� �� ���
			int result = fileChooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				// Save
				File file = fileChooser.getSelectedFile();
				String fileName = file.getName();
				if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
					fileName += ".png";
					file = new File(file.getParentFile(), fileName);
				}
				try {
					// �׸��� ������ ũ�⸸ŭ BufferedImage�� ����
					BufferedImage image = new BufferedImage(drawPanel.getWidth(), drawPanel.getHeight(),
							BufferedImage.TYPE_INT_ARGB);
					Graphics2D Graphics2d = image.createGraphics();

					// �׸��� ������ ������ ������
					drawPanel.paint(Graphics2d);
					Graphics2d.dispose();

					// ������ �̹����� ���Ϸ� ����
					ImageIO.write(image, "png", file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void capture() {
		int answer = JOptionPane.showConfirmDialog(null, "��ü ȭ���� ĸ���Ͻðڽ��ϱ�?", "Save File?",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (answer == JOptionPane.YES_OPTION) { // Yes�� ������ ���
			// ���Ͽ��� ���̾�α� �� ���
			int result = fileChooser.showSaveDialog(null);
			if (result == JFileChooser.APPROVE_OPTION) {
				// Screen Save
				File file = fileChooser.getSelectedFile();
				String fileName = file.getName();
				if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
					fileName += ".png";
					file = new File(file.getParentFile(), fileName);
				}
				try {
					Robot robot = new Robot();
					Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
					BufferedImage screenImage = robot.createScreenCapture(screenRect);
					ImageIO.write(screenImage, "png", file);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

//	private void openFile() {
//		int result = fileChooser.showOpenDialog(null);
//		if (result == JFileChooser.APPROVE_OPTION) { // Click Open
//			// ������ ������ ��� ��ȯ
//			File selectedFile = fileChooser.getSelectedFile();
//			try {
//				BufferedImage openedImage = ImageIO.read(selectedFile);
//				if (openedImage != null) {
//					if (drawPanel instanceof GDrawingPanel) {
//						GDrawingPanel drawPanelInstance = (GDrawingPanel) drawPanel;
//						drawPanelInstance.setBufferedImage(openedImage);
//					}
//					drawPanel.repaint();
//				} else {
//					JOptionPane.showMessageDialog(drawPanel, "�ùٸ��� ���� �̹��� �����Դϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//				JOptionPane.showMessageDialog(drawPanel, "������ ���� ���� ������ �߻��߽��ϴ�.", "Error", JOptionPane.ERROR_MESSAGE);
//			}
//		} else { // Click Cancel
//			System.out.println("Cancel");
//		}
//	}

	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			eSelectedMenuItem = EMenuItem.valueOf(e.getActionCommand());

			if (eSelectedMenuItem == EMenuItem.eCapture) {
				capture();
			} else if (eSelectedMenuItem == EMenuItem.eSave) {
				saveFile();
			} else if (eSelectedMenuItem == EMenuItem.eOpen) {
//				openFile();
			}
		}
	}

}
