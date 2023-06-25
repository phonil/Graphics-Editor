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

		// 기본 Path의 경로 설정 (바탕화면)
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home") + "//" + "Desktop"));

		// 필터링될 확장자
		FileNameExtensionFilter filter = new FileNameExtensionFilter("png 파일", "png");
		fileChooser.addChoosableFileFilter(filter);

	}

	private void saveFile() {
		int answer = JOptionPane.showConfirmDialog(null, "저장하시겠습니까?", "Save File?", JOptionPane.YES_NO_CANCEL_OPTION);
		if (answer == JOptionPane.YES_OPTION) { // Yes를 눌렀을 경우
			// 파일오픈 다이얼로그 를 띄움
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
					// 그림판 영역의 크기만큼 BufferedImage를 생성
					BufferedImage image = new BufferedImage(drawPanel.getWidth(), drawPanel.getHeight(),
							BufferedImage.TYPE_INT_ARGB);
					Graphics2D Graphics2d = image.createGraphics();

					// 그림판 영역의 내용을 렌더링
					drawPanel.paint(Graphics2d);
					Graphics2d.dispose();

					// 생성된 이미지를 파일로 저장
					ImageIO.write(image, "png", file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void capture() {
		int answer = JOptionPane.showConfirmDialog(null, "전체 화면을 캡쳐하시겠습니까?", "Save File?",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (answer == JOptionPane.YES_OPTION) { // Yes를 눌렀을 경우
			// 파일오픈 다이얼로그 를 띄움
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
//			// 선택한 파일의 경로 반환
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
//					JOptionPane.showMessageDialog(drawPanel, "올바르지 않은 이미지 파일입니다.", "Error", JOptionPane.ERROR_MESSAGE);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//				JOptionPane.showMessageDialog(drawPanel, "파일을 여는 도중 오류가 발생했습니다.", "Error", JOptionPane.ERROR_MESSAGE);
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
