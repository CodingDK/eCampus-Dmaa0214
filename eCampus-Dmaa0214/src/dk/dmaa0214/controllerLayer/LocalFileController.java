package dk.dmaa0214.controllerLayer;

import java.io.File;

public class LocalFileController {
	
	private String langPath;

	public static void main(String[] args) {
		new LocalFileController();
	}

	public LocalFileController() {
		langPath = "D:\\SkyDrive\\UCN\\ITIO";
		final File folder = new File(langPath);
		listFilesForFolder(folder);
	}
	
	public void listFilesForFolder(final File folder) {
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	        	String path = fileEntry.getPath();
	        	path = path.replace(langPath, "");
	            System.out.println(path + ", size: " + fileEntry.getUsableSpace());
	        }
	    }
	}
	
	

}
