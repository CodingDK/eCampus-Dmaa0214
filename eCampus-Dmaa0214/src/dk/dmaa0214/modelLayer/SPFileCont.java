package dk.dmaa0214.modelLayer;

import java.util.ArrayList;

public class SPFileCont {

	private static SPFileCont instance = null;
	private ArrayList<SPFile> files;
	
	private SPFileCont() {
		files = new ArrayList<SPFile>();
	}
	
	public static SPFileCont getInstance() {
		if(instance == null) {
			instance = new SPFileCont();
		}
		return instance;
	}

	/**
	 * @return the files
	 */
	public ArrayList<SPFile> getFiles() {
		return files;
	}
	
	public void addFile(SPFile file) {
		files.add(file);
	}

	public void remove(SPFile f) {
		files.remove(f);
	}

}
