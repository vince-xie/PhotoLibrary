package model;

import java.io.*;
import java.util.*;

/**
 * @author Edmond Wu & Vincent Xie
 */
public class Album implements Serializable {
	
	private String name;
	private ArrayList<Photo> photos;
	
	/**
	 * Constructor with album name
	 * @param s name of the photo album
	 */
	public Album(String s) {
		name = s;
		photos = new ArrayList<Photo>();
	}
	
	/**
	 * Returns the name of the album
	 * @return name of album
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Get a list of the photos in the album
	 * @return ArrayList of photos
	 */
	public ArrayList<Photo> getPhotos() {
		return photos;
	}
	
	/**
	 * Sets the photos in the album
	 * @param ArrayList of photos
	 */
	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}
	
	/**
	 * Changes the album name
	 * @param n New name of the album
	 */
	public void changeName(String n) {
		name = n;
	}
}
