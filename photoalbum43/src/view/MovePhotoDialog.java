//Edmond Wu & Vincent Xie

package view;

import app.PhotoAlbum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.Album;

public class MovePhotoDialog extends Dialog<ButtonType> {

    private ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
    private ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    private ComboBox albumText;

    /**
     * Creates an add user dialog box.
     */
    public MovePhotoDialog() {
    	
    	DialogPane dialogPane = this.getDialogPane();
    	dialogPane.getStylesheets().add(
    	   getClass().getResource("Dialog.css").toExternalForm());
    	
        setTitle("Add an album");
        setHeaderText(null);

        GridPane dPane = new GridPane();
        Text album = new Text("Album: ");
        ObservableList<String> options = FXCollections.observableArrayList();
        for(Album a: PhotoAlbum.regular_user.getAlbums()){
        	if(!a.equals(PhotoAlbum.album)){
            	options.add(a.getName());
        	}
        }
        albumText = new ComboBox<String>(options);

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
    	return (String)albumText.getValue();
    }
}