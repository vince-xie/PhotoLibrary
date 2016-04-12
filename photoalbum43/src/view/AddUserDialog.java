//Edmond Wu & Vincent Xie

package view;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class AddUserDialog extends Dialog<ButtonType> {

    private ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
    private ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    private TextField userText;
    private TextField passText;

    /**
     * Creates an add user dialog box.
     */
    public AddUserDialog() {
    	DialogPane dialogPane = this.getDialogPane();
    	dialogPane.getStylesheets().add(
    	   getClass().getResource("Dialog.css").toExternalForm());
    	
        setTitle("Add a user");
        setHeaderText(null);

        GridPane dPane = new GridPane();
        Text user = new Text("Username: ");
        Text pass = new Text("Password: ");
        userText = new TextField();
        passText = new PasswordField();
        userText.setPromptText("Required");
        passText.setPromptText("Required");

        dPane.setHgap(7D);
        dPane.setVgap(8D);

        GridPane.setConstraints(user, 0, 0);
        GridPane.setConstraints(pass, 0, 1);
        GridPane.setConstraints(userText, 1, 0);
        GridPane.setConstraints(passText, 1, 1);

        dPane.getChildren().addAll(user, pass, userText, passText);
        getDialogPane().getButtonTypes().addAll(ok, cancel);
        getDialogPane().setContent(dPane);
    }
    
    /**
     * Extracts username from dialog box.
     * @return username
     */
    public String getUsername() {
    	return userText.getText();
    }
    
    /**
     * Extracts password from dialog box.
     * @return password
     */
    public String getPassword() {
    	return passText.getText();
    }
}