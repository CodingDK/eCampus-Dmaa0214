package dk.dmaa0214.guiLayer.extensions;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

import dk.dmaa0214.modelLayer.SPFile;

public class FileTreeCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FileTreeCellRenderer() {
		
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree,
		    Object value, boolean selected, boolean expanded,
		    boolean leaf, int row, boolean hasFocus) {
		        super.getTreeCellRendererComponent(tree, value, selected,expanded, leaf, row, hasFocus);
		        
		        if(value instanceof SPFile) {
		        	SPFile f = (SPFile) value;
		        	String type = f.getType();
		        	if(!type.isEmpty()) {
		        		File file;
						try {
							file = File.createTempFile("icon", type);
							FileSystemView view = FileSystemView.getFileSystemView();
			        		
			        		Icon icon = view.getSystemIcon(file);      
			        		file.delete();
			        		setIcon(icon);
						} catch (IOException e) {
							e.printStackTrace();
						}   
		        		
		        	}
		        }
		        return this;
		}

}
