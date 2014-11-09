package dk.dmaa0214.guiLayer.extensions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.KeyStroke;

public class KeyListener {

	public static void addEscapeListener(final JDialog dialog) {
		ActionListener escListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		};
		dialog.getRootPane().registerKeyboardAction(escListener,
				KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
	
}

