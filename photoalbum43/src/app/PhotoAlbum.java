package app;


import java.io.*;
import java.util.*;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Admin;
import model.Album;
import model.NonAdminUser;
import model.Photo;
import model.User;

/**
 * @author Edmond Wu & Vincent Xie
 */
public class PhotoAlbum extends Application {
	
	public static Admin admin = new Admin("admin", "password");
	public static NonAdminUser logged_in;
	public static NonAdminUser regular_user;
	public static Album album;
	public static Stage stage;
	public static ArrayList<Photo> search;
	public static Photo photo;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		updateUserList();
		stage = primaryStage;
		FXMLLoader loader = new FXMLLoader();   
	    loader.setLocation(getClass().getResource("/view/Login.fxml"));
	    AnchorPane root = (AnchorPane)loader.load();
	    LoginController login = loader.getController();
	    login.start(primaryStage);
	    
	    Scene scene = new Scene(root, 800, 600);
	    primaryStage.setScene(scene);
	    primaryStage.setResizable(false);
	    primaryStage.show(); 
	    
	    search = new ArrayList<Photo>();
	}
	
	/**
	 * Retrieves a user object from a file name
	 * @param file_name Name of the user file
	 * @return User object with its relevant data
	 */
	public User deSerialize(String file_name) {
		User u = null;
	    try {
	    	FileInputStream fileIn = new FileInputStream("data/" + file_name);
	        ObjectInputStream in = new ObjectInputStream(fileIn);
	        u = (User) in.readObject();
	        in.close();
	        fileIn.close();
	    } catch(Exception e) {
	         System.out.println("Invalid deserialization.");
	         return null;
	    } 
	    return u;
	}
	
	/**
	 * Updates the user list from pre-existing serialized files
	 */
	public void updateUserList() {
		File dir = new File("data");
		File[] directoryListing = dir.listFiles();
		if (directoryListing != null) {
			for (File child : directoryListing) {
				String file_name = child.getName();
				if (file_name.toLowerCase().contains(".ser")) {
					User u = deSerialize(file_name);
					PhotoAlbum.admin.getUserList().add(u);
				}
		    }
		} else {
		    System.out.println("Empty or invalid directory");
		}
	}
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}