package model;

import java.io.*;

/**
 * @author Edmond Wu & Vincent Xie
 */
public abstract class User implements Serializable {
	
	private String username;
	private String password;
	private boolean priv;
	
	/**
	 * User constructor
	 * @param n Username
	 * @param p Password
	 */
	public User(String n, String p) {
		username = n.toLowerCase();
		password = p;
		priv = true;
	}
	
	/**
	 * Gets the username
	 * @return username of the user
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Gets the password
	 * @return password of the user
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Gets privacy setting
	 * @return privacy setting
	 */
	public boolean getPrivate() {
		return priv;
	}
	
	/**
	 * Gets privacy setting
	 * @param p privacy setting
	 */
	public void setPrivate(boolean p) {
		priv = p;
	}
	
	
	/**
	 * Serializes the user data
	 */
	public void serialize() {
		try {
	        FileOutputStream fileOut = new FileOutputStream("data/" + username + ".ser");
	        ObjectOutputStream out = new ObjectOutputStream(fileOut);
	        out.writeObject(this);
	        out.close();
	        fileOut.close();
	    } catch(Exception e) {
	    	System.out.println("Invalid serialization.");
	    }
	}
}
