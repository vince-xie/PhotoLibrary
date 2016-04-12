package model;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

import app.PhotoAlbum;

/**
* @author Edmond Wu & Vincent Xie
*/
public class Photo implements Serializable {
	
	private LocalDateTime date;
	private File file_name;
	private HashMap<String, ArrayList<String>> tags;
	private ArrayList<User> likers;
	private String caption;
	
	
	/**
	 * Constructor with file name
	 * @param f name of the photo file
	 */
	public Photo(File f) {
		file_name = f;
		tags = new HashMap<String, ArrayList<String>>();
		likers = new ArrayList<User>();
		caption = "";
	}
	
	/**
	 * Constructor with additional parameters
	 * @param f name of the photo file
	 * @param d date that the photo was last modified
	 * @param c caption
	 */
	public Photo(File f, LocalDateTime d, String c) {
		this(f);
		date = d;
		caption = c;
	}
	
	/**
	 * Gets the date-time of the photo (when last modified)
	 * @return the date-time of the picture
	 */
	public LocalDateTime getDate() {
		return date;
	}
	
	/**
	 * Gets the file name of the photo (when last modified)
	 * @return the file name of the picture
	 */
	public File getFile() {
		return file_name;
	}
	
	/**
	 * Gets the HashMap of tags of the photo (when last modified)
	 * @return tags HashMap
	 */
	public HashMap<String, ArrayList<String>> getTags() {
		return tags;
	}
	
	/**
	 * Gets the display form of the tags
	 * @return all the tags of the photo in display form
	 */
	public String getTagDisplay() {
		String display = "";
		for (String key : tags.keySet()) {
			String key_display = key + ": ";
			for (int i = 0; i < tags.get(key).size(); i++) {
				String val = tags.get(key).get(i);
				key_display += val + ", ";
			}
			key_display = key_display.substring(0, key_display.length() - 2);
			display += key_display + "; ";
		}
		if (display.length() > 0) {
			display = display.substring(0, display.length() - 2);
		}
		return display;
	}
	
	/**
	 * Gets the caption of the photo (when last modified)
	 * @return the caption of the picture
	 */
	public String getCaption() {
		return caption;
	}
	
	/**
	 * Returns the number of likes on a photo
	 * @return the number of likes
	 */
	public int getLikes() {
		for(int i = 0; i < likers.size(); i++){
			if(!PhotoAlbum.admin.getUserList().contains(likers.get(i))){
				likers.remove(i);
				i--;
			}
		}
		return likers.size();
	}
	
	/**
	 * Gets if the user has liked the image already
	 */
	public boolean liked(){
		return likers.contains(PhotoAlbum.logged_in);
	}
	
	/**
	 * Adds current user to likers.
	 */
	public void addUserToLikers(){
		likers.add(PhotoAlbum.logged_in);
	}
	
	/**
	 * Removes current user from likers.
	 */
	public void removeUserFromLikers(){
		while(likers.remove(PhotoAlbum.logged_in));
	}
	
	/**
	 * Sets caption.
	 * @param caption new caption
	 */
	public void setCaption(String caption){
		this.caption = caption;
	}
	
	/**
	 * Prints out the key-value pairs in the tags HashMap
	 */
	public void printTags() {
		for (String key : tags.keySet()) {
			for (int i = 0; i < tags.get(key).size(); i++) {
				System.out.println(key + " - " + tags.get(key).get(i));
			}
		}
	}
}
