package controller;

import java.io.*;

import app.PhotoAlbum;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.User;

/**
 * @author Edmond Wu & Vincent Xie
 */
public abstract class Controller {
	
	public void start(Stage mainStage) {                
	    
	}
	
	/**
	 * Logs a user out.
	 * @throws IOException
	 */
	public void logout() throws IOException{
		ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
		ButtonType cancel = new ButtonType("Cancel", ButtonData.NO);
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.getDialogPane().getButtonTypes().add(ok);
		dialog.getDialogPane().getButtonTypes().add(cancel);
		dialog.setHeaderText("Confirm.");
		dialog.setContentText("Are you sure you want to logout?");
		dialog.showAndWait().ifPresent(response -> {
			if (response == ok) {
				for (User u : PhotoAlbum.admin.getUserList()) {
					u.serialize();
				}
				segue("/view/Login.fxml");
			}
		});
	}
	
	/**
	 * Changes screen
	 * @param fxml path to fxml file
	 */
	public void segue(String fxml){
		try{
			FXMLLoader loader = new FXMLLoader(); 
			loader.setLocation(getClass().getResource(fxml));
			Pane root;
			root = (Pane)loader.load();
			Controller login = loader.getController();
			login.start(PhotoAlbum.stage);

			Scene scene = new Scene(root, 800, 600);
			PhotoAlbum.stage.setScene(scene);
			PhotoAlbum.stage.setResizable(false);
			PhotoAlbum.stage.show(); 
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Hides button and disables it
	 * @param button
	 */
	public void hideButton(Button button){
		button.setOpacity(0);
		button.setDisable(true);
	}
	
	/**
	 * Shows button and enables it
	 * @param button
	 */
	public void showButton(Button button){
		button.setOpacity(1);
		button.setDisable(false);
	}
}
