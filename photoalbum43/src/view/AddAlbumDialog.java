//Edmond Wu & Vincent Xie

package view;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class AddAlbumDialog extends Dialog<ButtonType> {

    private ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
    private ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    private TextField albumText;

    /**
     * Creates an add user dialog box.
     */
    public AddAlbumDialog() {
    	
    	DialogPane dialogPane = this.getDialogPane();
    	dialogPane.getStylesheets().add(
    	   getClass().getResource("Dialog.css").toExternalForm());
    	
        setTitle("Add an album");
        setHeaderText(null);

        GridPane dPane = new GridPane();
        Text album = new Text("Album name: ");
        albumText = new TextField();
        albumText.setPromptText("Required");

        dPane.setHgap(7D);
        dPane.setVgap(8D);

        GridPane.setConstraints(album, 0, 0);
        GridPane.setConstraints(albumText, 1, 0);
        
        dPane.getChildren().addAll(album, albumText);
        getDialogPane().getButtonTypes().addAll(ok, cancel);
        getDialogPane().setContent(dPane);
    }
    
    /**
     * Extracts album name from dialog box.
     * @return album name
     */
    public String getAlbumName() {
    	return albumText.getText();
    }
}