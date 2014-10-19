package dk.dmaa0214.guiLayer.extensions;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class JFilePath extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final private ImageIcon iconError = new ImageIcon(getClass().getResource("images/error.png"));
	final private ImageIcon iconOk = new ImageIcon(getClass().getResource("images/ok.png"));
	private Icon icon = null;
	public JFilePath() {
		super();
        Border border = UIManager.getBorder("TextField.border");
        JTextField dummy = new JTextField();
        border.getBorderInsets(dummy);
        super.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
				
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				checkFilePath();
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				checkFilePath();
			}
        });
       
	}
	
	public void setIcon(Icon icon){
        this.icon = icon;
    }
 
    public Icon getIcon(){
        return this.icon;
    }
    
    @Override
    public void setText(String text) {
    	super.setText(text);
    	checkFilePath();
    }
        
    public void checkFilePath() {
    	File f = new File(this.getText());
		if (f.isDirectory()) {
			icon = iconOk;
		} else {
			icon = iconError;
		}
		super.repaint();
    }
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

        if(this.icon!=null){
        	int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            int x = this.getWidth() - iconWidth - 2;
            int y = (this.getHeight() - iconHeight)/2;
        	icon.paintIcon(this, g, x, y);
        }

	}
}
