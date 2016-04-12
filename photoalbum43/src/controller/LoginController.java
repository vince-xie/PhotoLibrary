package controller;

import app.PhotoAlbum;
import java.io.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.NonAdminUser;

/**
 * @author Edmond Wu & Vincent Xie
 */
public class LoginController extends Controller {
	
	@FXML
	private TextField Username;
	
	@FXML
	private PasswordField Password;
	
	@FXML
	private Button Login;
	
	@FXML
	private Text userwrong;
	
	@FXML
	private Text passwrong;
	
	public void start(Stage mainStage) {
	    Login.setDefaultButton(true);
	}
	
	/**
	 * Logs the user in.
	 * @param e Action event.
	 * @throws IOException 
	 */
	public void login(ActionEvent e) throws IOException{
		String username = Username.getText().toLowerCase();
		String password = Password.getText();
		if(username.equalsIgnoreCase("admin")){
			if (password.equals(PhotoAlbum.admin.getPassword())) {
				segue("/view/Admin.fxml");
				return;
			}
			else {
				passwrong.setStyle("-fx-opacity: 1;");
				Password.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
				return;
			}
		} 
		//search through list
		for(int i = 0; i < PhotoAlbum.admin.getUserList().size(); i++){
			PhotoAlbum.regular_user = (NonAdminUser) PhotoAlbum.admin.getUserList().get(i);
			if(PhotoAlbum.regular_user.getUsername().equals(username)){
				if(PhotoAlbum.regular_user.getPassword().equals(password)){
					Username.setStyle("-fx-text-box-border: white; -fx-focus-color: #008ED6;");
					userwrong.setStyle("-fx-opacity: 0;");
					Password.setStyle("-fx-text-box-border: white; -fx-focus-color: #008ED6;");
					passwrong.setStyle("-fx-opacity: 0;");
					PhotoAlbum.logged_in = (NonAdminUser) PhotoAlbum.admin.getUserList().get(i);
					segue("/view/Albums.fxml");
					return;
				} else {
					passwrong.setStyle("-fx-opacity: 1;");
					Password.setStyle("-fx-text-box-border: red; -fx-focus-color: red;");
					return;
				}
			}
		}
		//if not found
		userwrong.setStyle("-fx-opacity: 1;");
		Username.setStyle("-fx-text-box-border: #e67e22; -fx-focus-color: #e67e22;");
		Password.setStyle("-fx-text-box-border: #e67e22; -fx-focus-color: #e67e22;");
	}
	
	/**
	 * Reset default colors and clears username field.
	 * @param e Mouse event.
	 */
	public void resetUsername(MouseEvent e){
		Username.setStyle("-fx-text-box-border: white; -fx-focus-color: #008ED6;");
		userwrong.setStyle("-fx-opacity: 0;");
	}
	
	/**
	 * Reset default colors and clears password field.
	 * @param e Mouse event.
	 */
	public void resetPassword(MouseEvent e){
		Password.setStyle("-fx-text-box-border: white; -fx-focus-color: #008ED6;");
		Password.clear();
		passwrong.setStyle("-fx-opacity: 0;");
	}
}
