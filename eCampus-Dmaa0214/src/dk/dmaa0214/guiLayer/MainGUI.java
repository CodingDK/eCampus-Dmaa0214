package dk.dmaa0214.guiLayer;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

public class MainGUI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			File file = File.createTempFile("icon", ".txt");   
			FileSystemView view = FileSystemView.getFileSystemView();      
			Icon icon = view.getSystemIcon(file);      
			file.delete();
			UIManager.put("Tree.leafIcon", icon);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
	    } catch (Throwable e) {
	            e.printStackTrace();
	    }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGUI window = new MainGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 488, 412);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new SPGUI());
		frame.setTitle("eCampus Gateway V0.04");
	}

}
