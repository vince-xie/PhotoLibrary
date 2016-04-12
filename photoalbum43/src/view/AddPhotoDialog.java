//Edmond Wu & Vincent Xie

package view;

import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class AddPhotoDialog extends Dialog<ButtonType> {

    private ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
    private ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    private Button browse = new Button("Browse...");
    private TextField caption;
    private FileChooser browser;
    public static File file;

    /**
     * Creates an add user dialog box.
     */
    public AddPhotoDialog() {
    	file = null;
    	DialogPane dialogPane = this.getDialogPane();
    	dialogPane.getStylesheets().add(getClass().getResource("Dialog.css").toExternalForm());
    	
        setTitle("Add a photo");
        setHeaderText(null);
        
        GridPane dPane = new GridPane();
        dPane.setPrefWidth(500);
        Text photo = new Text("Photo: ");
        Text name = new Text("");
        name.setFill(Color.ORANGE);
        Text cap = new Text("Caption: ");
        
        caption = new TextField();
        caption.setPromptText("Optional");
        
        dPane.setHgap(7D);
        dPane.setVgap(8D);

        GridPane.setConstraints(photo, 0, 0);
        GridPane.setConstraints(name, 3, 0);
        GridPane.setConstraints(cap, 0, 2);
        GridPane.setConstraints(browse, 1, 0);
        GridPane.setConstraints(caption, 1, 2);
        
        browser = new FileChooser();
        browser.setTitle("Open Photo");
        browser.getExtensionFilters().addAll(
                new ExtensionFilter("Image Files", "*.tif", "*.tiff"
                		, "*.gif", "*.jpeg", "*.jpg", "*.jif", "*.jfif"
                		, "*.jp2", "*.jpx", "*.j2k", "*.j2c"
                		, "*.fpx", "*.pcd", "*.png"
                ));
        browse.setOnAction(
                e -> {
                		Stage stage = new Stage();
                        File file = browser.showOpenDialog(stage);
                        if (file != null) {
                        	String temp = file.getName();
                        	if(temp.length() > 25){
                        		temp = temp.substring(0, 25);
                        		temp += "...";
                        	}
                           	name.setText(temp);
                        }
                        AddPhotoDialog.file = file;
                });
        dPane.getChildren().addAll(photo, name, cap, browse, caption);
        dialogPane.getButtonTypes().addAll(ok, cancel);
        dialogPane.setContent(dPane);
    }
    
    /**
     * Extracts the caption from the dialog box.
     * @return caption of photo
     */
    public String getCaption() {
    	return caption.getText();
    }
}