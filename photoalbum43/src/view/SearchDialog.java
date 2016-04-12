package view;

import java.time.LocalDate;

import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class SearchDialog extends Dialog<ButtonType> {
	private ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
    private ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    private DatePicker dateTime;
    private DatePicker dateTime2;
    private TextField key;
    private TextField value;
    
    /**
     * Creates a search dialog box
     */
    public SearchDialog() {
    	DialogPane dialogPane = this.getDialogPane();
    	setTitle("Search for photos");
        setHeaderText(null);
        
        GridPane dPane = new GridPane();
        Text date = new Text("Start Date for Search: ");
        Text date2 = new Text("End Date for Search: ");
        Text key_search = new Text("Search tag keys: ");
        Text value_search = new Text("Search tag values: ");
                
        dateTime = new DatePicker();
        dateTime.setPromptText("Optional");
        dateTime2 = new DatePicker();
        dateTime2.setPromptText("Optional");
        key = new TextField();
        key.setPromptText("Optional");
        value = new TextField();
        value.setPromptText("Optional");

        dPane.setHgap(7D);
        dPane.setVgap(8D);

        GridPane.setConstraints(date, 0, 0);
        GridPane.setConstraints(dateTime, 1, 0);
        GridPane.setConstraints(date2, 0, 1);
        GridPane.setConstraints(dateTime2, 1, 1);
        GridPane.setConstraints(key_search, 0, 2);
        GridPane.setConstraints(key, 1, 2);
        GridPane.setConstraints(value_search, 0, 3);
        GridPane.setConstraints(value, 1, 3);

        
        dPane.getChildren().addAll(date, date2, dateTime, dateTime2, key_search, key, value_search, value);
        dialogPane.getButtonTypes().addAll(ok, cancel);
        dialogPane.setContent(dPane);
    }
    
    /**
     * Extracts start date from the dialog box.
     * @return date of photo
     */
    public LocalDate getStartDate() {
    	return dateTime.getValue();
    }
    
    /**
     * Extracts end date from the dialog box.
     * @return date of photo
     */
    public LocalDate getEndDate() {
    	return dateTime2.getValue();
    }
    
    /**
     * Gets the String value of the key search
     * @return search key
     */
    public String getKey() {
    	return key.getText();
    }
    
    /**
     * Gets the String value of the value search
     * @return search value
     */
    public String getValue() {
    	return value.getText();
    }
}
