package controller;

import java.util.ArrayList;

import app.PhotoAlbum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import model.Admin;
import model.NonAdminUser;
import model.User;


/**
 * @author Edmond Wu & Vincent Xie
 */
public class UsersController extends Controller {
	
	@FXML
	private CheckBox privacy;
	
	@FXML
	private ListView<String> list;
	
	@FXML
	private Button view;
	
	private ObservableList<String> obsList;     

	public void start(Stage mainStage) {
		if(!PhotoAlbum.logged_in.getPrivate()){
			privacy.setSelected(true);
		}
		obsList = FXCollections.observableArrayList();
		ArrayList<User> users = PhotoAlbum.admin.getUserList();
		for (int i = 0; i < PhotoAlbum.admin.getUserList().size(); i++) {
			User user = users.get(i);
			if(!user.equals(PhotoAlbum.logged_in) && user.getPrivate() == false && !(user instanceof Admin)){
				obsList.add(PhotoAlbum.admin.getUserList().get(i).getUsername());
			}
		}
		list.setItems(obsList); 
		list.getSelectionModel().select(0);
	}
	
	/**
	 * Privacy box checked
	 * @param e
	 */
	public void checked(ActionEvent e){
		User user = PhotoAlbum.logged_in;
		if(user.getPrivate() == true){
			user.setPrivate(false);
		} else {
			user.setPrivate(true);
		}
		user.serialize();
	}
	
	/**
	 * Goes back to logged in user's page.
	 * @param e
	 */
	public void back(ActionEvent e){
		PhotoAlbum.regular_user = PhotoAlbum.logged_in;
		segue("/view/Albums.fxml");
	}
	
	/**
	 * Views albums for selected user
	 * @param e
	 */
	public void viewAlbums(ActionEvent e){
		String username = list.getSelectionModel().getSelectedItem();
		for(User u: PhotoAlbum.admin.getUserList()){
			if(u.getUsername().equals(username)){
				if(u instanceof NonAdminUser){
					PhotoAlbum.regular_user = (NonAdminUser)u;
				}
			}
		}
		segue("/view/Albums.fxml");
	}
}
