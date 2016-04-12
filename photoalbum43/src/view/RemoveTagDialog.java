package view;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class RemoveTagDialog extends Dialog<ButtonType> {
	private ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
    private ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    private TextField key;
    
    /**
     * Creates a remove tag dialog box
     */
    public RemoveTagDialog() {
    	DialogPane dialogPane = this.getDialogPane();
    	setTitle("Delete Tags");
        setHeaderText(null);
        
        GridPane dPane = new GridPane();
        Text key_search = new Text("Remove tag key: ");
                
        key = new TextField();

        dPane.setHgap(7D);
        dPane.setVgap(8D);

        GridPane.setConstraints(key_search, 0, 0);
        GridPane.setConstraints(key, 1, 0);

        
        dPane.getChildren().addAll(key_search, key);
        dialogPane.getButtonTypes().addAll(ok, cancel);
        dialogPane.setContent(dPane);
    }
    
    /**
     * Gets the String value of the key search
     * @return search key
     */
    public String getKey() {
    	return key.getText();
    }
}
