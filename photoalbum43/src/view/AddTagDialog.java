package view;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class AddTagDialog extends Dialog<ButtonType> {
	private ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
    private ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    private TextField tag_key;
    private TextField tag_value;
    
    /**
     * Creates a dialog box to add a tag
     */
    public AddTagDialog() {
    	DialogPane dialogPane = this.getDialogPane();
    	
        setTitle("Add tag");
        setHeaderText(null);
        
        GridPane dPane = new GridPane();
        Text key = new Text("Tag key: ");
        Text value = new Text("Tag value: ");
        
        tag_key = new TextField();
        tag_value = new TextField();
        
        dPane.setHgap(7D);
        dPane.setVgap(8D);

        GridPane.setConstraints(key, 0, 0);
        GridPane.setConstraints(value, 0, 1);
        GridPane.setConstraints(tag_key, 1, 0);
        GridPane.setConstraints(tag_value, 1, 1);
        
        dPane.getChildren().addAll(key, value, tag_key, tag_value);
        dialogPane.getButtonTypes().addAll(ok, cancel);
        dialogPane.setContent(dPane);
    }
    
    /**
     * Gets the key of the tag in String form
     * @return tag's key
     */
    public String getKey() {
    	return tag_key.getText();
    }
    
    /**
     * Gets the value of the tag in String form
     * @return tag's value
     */
    public String getValue() {
    	return tag_value.getText();
    }
}
