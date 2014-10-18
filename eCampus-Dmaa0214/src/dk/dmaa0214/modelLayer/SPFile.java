package dk.dmaa0214.modelLayer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SPFile {
	private String name;
	private String type;
	private String beforePath;
	private String path;
	private Long changedTime;
	private String addedBy;
	private SPFolder parent;
	
	public SPFile(String beforePath, String name, String path, String addedBy, String changedTime, SPFolder parent){
		this.beforePath = beforePath;
		this.name = name;
		setPathAndType(path);
		this.addedBy = addedBy;
		setChangedTime(changedTime);
		this.parent = parent;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @return the changedTime
	 */
	public Long getChangedTime() {
		return changedTime;
	}

	/**
	 * @return the addedBy
	 */
	public String getAddedBy() {
		return addedBy;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @param path the path to set
	 */
	public void setPathAndType(String path) {
		this.path = path;
		if (path != null && path.lastIndexOf(".") != -1) {
			this.type = path.substring(path.lastIndexOf("."));
		} else {
			this.type = "";
		}
	}
	
	/**
	 * @return the beforePath
	 */
	public String getBeforePath() {
		return beforePath;
	}

	/**
	 * @param beforePath the beforePath to set
	 */
	public void setBeforePath(String beforePath) {
		this.beforePath = beforePath;
	}

	/**
	 * @param changedTime the changedTime to set
	 */
	public void setChangedTime(String string_date) {
		try {
			
			SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			Date d = f.parse(string_date);
			long l = d.getTime();
			this.changedTime = l;
			//System.out.println("date : "+f.format(d));
			//System.out.println("date: " + l + "=" + getChangedTime());
	    } catch (ParseException nfe) {
	    	System.out.println("ParseException: " + nfe.getMessage());
	    }
	}

	/**
	 * @param addedBy the addedBy to set
	 */
	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}

	public String getShortPath(){
		return path.substring(beforePath.length());
	}
	
	public String toString() {
		return name;
	}
	
	/**
	 * @return the parent
	 */
	public SPFolder getParent() {
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(SPFolder parent) {
		this.parent = parent;
	}
}
