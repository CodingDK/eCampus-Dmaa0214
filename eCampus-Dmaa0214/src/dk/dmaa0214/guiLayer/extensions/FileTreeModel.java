package dk.dmaa0214.guiLayer.extensions;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import dk.dmaa0214.modelLayer.SPFolder;

public class FileTreeModel implements TreeModel{
	private SPFolder root;
	
	public FileTreeModel(SPFolder root){
		this.root = root;
	}

	
	public void addTreeModelListener(TreeModelListener arg0) {}

	
	public Object getChild(Object parent, int index) {
		Object o = null;
		
		if(parent.getClass().getName().contains("SPFolder")){
			if(((SPFolder) parent).getChildNodes().size() < index){
				return null;
			}else{
				o = ((SPFolder) parent).getChildNodes().get(index);
			}
		} else {
			o = null;
		}
		
		return o;
	}

	
	public int getChildCount(Object parent) {
		int count = 0;
		
		if(parent.getClass().getName().contains("SPFolder")){
			count = ((SPFolder) parent).getChildNodes().size();
		}
		
		return count;
	}

	
	public int getIndexOfChild(Object parent, Object child) {
		int res = -1;
		if(parent.getClass().getName().contains("SPFolder")){
			res = ((SPFolder) parent).getChildNodes().indexOf(child);
		}
		
		return res;
	}

	
	public Object getRoot() {
		return root;
	}

	
	public boolean isLeaf(Object node) {
		
		boolean res = false;
		
		if(!node.getClass().getName().contains("SPFolder")){
			res = true;
		}
		
		return res;
	}

	
	public void removeTreeModelListener(TreeModelListener arg0) {}

	
	public void valueForPathChanged(TreePath arg0, Object arg1) {}
	
}
