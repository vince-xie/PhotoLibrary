package model;

import java.util.*;

/**
 * @author Edmond Wu & Vincent Xie
 */
public class Admin extends User {
	
	private ArrayList<User> master_list;
	
	/**
	 * Constructor for an admin user
	 * @param u username
	 * @param p password
	 */
	public Admin(String u, String p) {
		super(u, p);
		master_list = new ArrayList<User>();
	}
	
	/**
	 * Get the user master list
	 * @return admin's user list
	 */
	public ArrayList<User> getUserList() {
		return master_list;
	}
}
