package controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import app.PhotoAlbum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.NonAdminUser;
import view.AddUserDialog;


/**
 * @author Edmond Wu & Vincent Xie
 */
public class AdminController extends Controller {
	
	@FXML
	private ImageView imageView;
	
	@FXML
	private ListView<String> list;
	
	@FXML
	private Text username;
	
	@FXML
	private Text password;
	
	private ObservableList<String> obsList;     

	public void start(Stage mainStage) {
		File file = new File("src/assets/Admin.png");
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);	
		obsList = FXCollections.observableArrayList();
		for (int i = 0; i < PhotoAlbum.admin.getUserList().size(); i++) {
			obsList.add(i, PhotoAlbum.admin.getUserList().get(i).getUsername());
		}
		list.setItems(obsList); 
		list.getSelectionModel().select(0);
		showInfo();
		list.setOnMouseClicked((e) -> showInfo());
	}
	
	/**
	 * Adds to the list of users.
	 * @param e
	 */
	public void add(ActionEvent e) {
		AddUserDialog dialog = new AddUserDialog();
		Optional<ButtonType> result = dialog.showAndWait();
		
		String ok = ButtonType.OK.getText();
		String click = result.get().getText();
		
		if (click.equals(ok)) {
			String username = dialog.getUsername().toLowerCase(), pass = dialog.getPassword();
			if (username.isEmpty() || pass.isEmpty()) {
				Alert error = new Alert(AlertType.INFORMATION);
				error.setHeaderText("Error!");
				error.setContentText("Username and Password are required!");
				error.show();
				return;
			}
			if (username.equalsIgnoreCase("Admin")) {
				Alert error = new Alert(AlertType.INFORMATION);
				error.setHeaderText("Error!");
				error.setContentText("Cannot create a user with username \"" + username + "\"!");
				error.show();
				return;
			}
			NonAdminUser user = new NonAdminUser(username, pass);
			for(int i = 0; i < PhotoAlbum.admin.getUserList().size(); i++){
				if(username.equals(PhotoAlbum.admin.getUserList().get(i).getUsername())){
					Alert error = new Alert(AlertType.INFORMATION);
					error.setHeaderText("Error!");
					error.setContentText("Username already in use.");
					error.show();
					return;
				} else if (username.compareToIgnoreCase(PhotoAlbum.admin.getUserList().get(i).getUsername()) == 0){
					PhotoAlbum.admin.getUserList().add(i, user);
					obsList.add(i, username);
					list.getSelectionModel().select(i);
					showInfo();
					return;
				} else if(username.compareToIgnoreCase(PhotoAlbum.admin.getUserList().get(i).getUsername()) < 0){
					PhotoAlbum.admin.getUserList().add(i, user);
					obsList.add(i, username);
					list.getSelectionModel().select(i);
					showInfo();
					return;
				}
			}
			PhotoAlbum.admin.getUserList().add(user);
			obsList.add(username);
			list.getSelectionModel().select(obsList.size() - 1);
			showInfo();
			PhotoAlbum.regular_user = user;
			PhotoAlbum.regular_user.serialize();
		}
	}

	/**
	 * Removes from the list of users.
	 * @param e
	 */
	public void delete(ActionEvent e){
		int index = list.getSelectionModel().getSelectedIndex();
		if(index >= 0){
			String user = PhotoAlbum.admin.getUserList().get(index).getUsername();
			ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
			ButtonType cancel = new ButtonType("Cancel", ButtonData.NO);
			Dialog<ButtonType> dialog = new Dialog<ButtonType>();
			dialog.getDialogPane().getButtonTypes().add(ok);
			dialog.getDialogPane().getButtonTypes().add(cancel);
			dialog.setHeaderText("Confirm.");
			dialog.setContentText("Are you sure you would like to delete user " + user + "?");
			dialog.showAndWait().ifPresent(response -> {
				if (response == ok) {
					obsList.remove(index);
					PhotoAlbum.admin.getUserList().remove(index);
					showInfo();
					try {
						Files.delete(Paths.get("data/" + user + ".ser"));
					} catch (Exception e1) {
						System.out.println("Invalid serialized file to be deleted.");
					}
				}
			});
		}
	}
	
	/**
	 * Shows account info for selected account.
	 */
	private void showInfo() {                
		int index = list.getSelectionModel().getSelectedIndex();
		if(index >= 0){
			username.setText("Username: " + PhotoAlbum.admin.getUserList().get(index).getUsername());
			password.setText("Password: " + PhotoAlbum.admin.getUserList().get(index).getPassword());
		} else {
			username.setText("Username: ");
			password.setText("Password: ");
		}
	}
	
}
