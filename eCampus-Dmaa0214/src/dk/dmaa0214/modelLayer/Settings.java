package dk.dmaa0214.modelLayer;

import java.io.Serializable;

public class Settings implements Serializable{
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	
	public Settings(String username, String password){
		this.username = username;
		this.password = password;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
