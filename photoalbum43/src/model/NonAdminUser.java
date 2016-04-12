package model;

import java.util.*;

/**
 * @author Edmond Wu & Vincent Xie
 */
public class NonAdminUser extends User {
	
	private ArrayList<Album> albums;
	
	/**
	 * Constructor with username and password
	 * @param u username
	 * @param p password
	 */
	public NonAdminUser(String u, String p) {
		super(u, p);
		albums = new ArrayList<Album>();
	}
	
	/**
	 * Gets the user's album list
	 * @return album list of the user
	 */
	public ArrayList<Album> getAlbums() {
		return albums;
	}
}
