package dk.dmaa0214.modelLayer;

import java.util.ArrayList;

public class SPFolderCont {

	private static SPFolderCont instance = null;
	private ArrayList<SPFolder> files;
	
	private SPFolderCont() {
		files = new ArrayList<SPFolder>();
	}
	
	public static SPFolderCont getInstance() {
		if(instance == null) {
			instance = new SPFolderCont();
		}
		return instance;
	}

	/**
	 * @return the files
	 */
	public ArrayList<SPFolder> getFiles() {
		return files;
	}
	
	public void addFile(SPFolder file) {
		files.add(file);
	}

	public void remove(SPFolder f) {
		files.remove(f);
	}
	
	public boolean hasFolderFile(SPFolder spFoler) {
		int i = 0;
		boolean found = false;
		while(i < files.size() && !found) {
			if(files.get(i).getParent().equals(spFoler)) {
				found = true;
			}
			i++;
		}
		return true;
	}

}
