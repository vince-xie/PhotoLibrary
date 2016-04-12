package controller;


import java.io.*;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import app.PhotoAlbum;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import view.AddAlbumDialog;
import view.AddTagDialog;
import view.MovePhotoDialog;
import view.RemoveTagDialog;

/**
 * @author Edmond Wu & Vincent Xie
 */
public class PhotoController extends Controller{

	@FXML
	private ImageView photo;

	@FXML
	private Text caption;

	@FXML
	private Text tags;

	@FXML
	private Text date;

	@FXML
	private Text likes;

	@FXML
	private Button addTag;

	@FXML
	private Button done;

	@FXML
	private Button edit;

	@FXML
	private Button logout;

	@FXML
	private Button like;

	@FXML
	private AnchorPane pane;

	@FXML
	private Button delete;

	@FXML
	private Button removeTag;
	
	@FXML
	private Button backward;
	
	@FXML
	private Button forward;
	
	@FXML
	private Button move;

	TextField newCaption;

	public static boolean search;

	private String tag_display;

	public void start(Stage mainStage) {
		hideButton(addTag);
		hideButton(removeTag);
		hideButton(done);
		hideButton(logout);
		if(!PhotoAlbum.logged_in.equals(PhotoAlbum.regular_user)){
			hideButton(edit);
			hideButton(delete);
			hideButton(move);
			showButton(logout);
		}
		File file1 = PhotoAlbum.photo.getFile();
		Image image1 = new Image(file1.toURI().toString());
		photo.setImage(image1);	
		photo.setPreserveRatio(true);
		centerImage(photo);
		caption.setText("Caption: " + PhotoAlbum.photo.getCaption());
		tag_display = PhotoAlbum.photo.getTagDisplay();
		tags.setText("Tags - " + tag_display);
		date.setText(PhotoAlbum.photo.getDate().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
		setUpLikeButton();
		int index = PhotoAlbum.album.getPhotos().indexOf(PhotoAlbum.photo);
		if(index == 0){
			backward.setOpacity(.5);
			backward.setDisable(true);
		} 
		if (index == PhotoAlbum.album.getPhotos().size() - 1){
			forward.setOpacity(.5);
			forward.setDisable(true);
		}
	}

	/**
	 * Sets up like button and counter.
	 */
	public void setUpLikeButton(){
		Photo photo = PhotoAlbum.photo;
		likes.setText(photo.getLikes() + "");
		like.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if(photo.liked()){
					likes.setText(photo.getLikes() + "   - Unlike this photo :(");
				} else {
					likes.setText(photo.getLikes() + "   - Like this photo!");
				}
			}
		});
		like.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				likes.setText(photo.getLikes() + "");
			}
		});
	}

	/**
	 * Goes to the previous page.
	 * @param e
	 */
	public void back(ActionEvent e){
		if(search == true){
			segue("/view/Search.fxml");
		} else {
			segue("/view/Album.fxml");
		}
	}

	/**
	 * Deletes the photo
	 * @param e
	 */
	public void delete(ActionEvent e){
		ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
		ButtonType cancel = new ButtonType("Cancel", ButtonData.NO);
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.getDialogPane().getButtonTypes().add(ok);
		dialog.getDialogPane().getButtonTypes().add(cancel);
		dialog.setHeaderText("Confirm.");
		dialog.setContentText("Are you sure you want to delete this photo?");
		dialog.showAndWait().ifPresent(response -> {
			if (response == ok) {
				ArrayList<Photo> photos = PhotoAlbum.album.getPhotos();
				for(int i = 0; i < photos.size(); i++){
					if(photos.get(i).equals(PhotoAlbum.photo)){
						photos.remove(i);
						PhotoAlbum.regular_user.serialize();
						segue("/view/Album.fxml");
						return;
					}
				}
			}
		});
	}

	/**
	 * Centers image in image view.
	 * @param ImageView photo
	 */
	public void centerImage(ImageView photo) {
		Image image = photo.getImage();
		if (image != null) {
			double ratioX = photo.getFitWidth() / image.getWidth();
			double ratioY = photo.getFitHeight() / image.getHeight();
			double ratio = (ratioX >= ratioY) ? ratioY : ratioX;

			photo.setX((photo.getFitWidth() - image.getWidth() * ratio) / 2);
			photo.setY((photo.getFitHeight() - image.getHeight() * ratio) / 2);

		}
	}

	/**
	 * Edit fields in the photo and allows adding of tags.
	 * @param e
	 */
	public void edit(ActionEvent e) {
		hideButton(edit);
		hideButton(move);
		hideButton(delete);
		showButton(addTag);
		showButton(done);
		showButton(removeTag);

		caption.setText("Caption: ");
		newCaption = new TextField();
		newCaption.setLayoutX(153);
		newCaption.setLayoutY(485);
		newCaption.setText(PhotoAlbum.photo.getCaption());
		newCaption.setPrefWidth(250);
		pane.getChildren().add(newCaption);
	}

	/**
	 * Adds a tag.
	 * @param e ActionEvent
	 */
	public void addTag(ActionEvent e) {
		AddTagDialog dialog = new AddTagDialog();
		Optional<ButtonType> result = dialog.showAndWait();

		String ok = ButtonType.OK.getText();
		String click = result.get().getText();

		if (click.equals(ok)) {
			String key = dialog.getKey().toLowerCase();
			String value = dialog.getValue().toLowerCase();
			if (key.trim().length() == 0 || value.trim().length() == 0) {
				Alert error = new Alert(AlertType.INFORMATION);
				error.setHeaderText("Error!");
				error.setContentText("Both key and value are required!");
				error.show();
				return;
			}
			ArrayList<String> vals;
			if (PhotoAlbum.photo.getTags().containsKey(key)) {
				vals = PhotoAlbum.photo.getTags().get(key);
			}
			else {
				vals = new ArrayList<String>();
				PhotoAlbum.photo.getTags().put(key, vals);
			}
			PhotoAlbum.photo.getTags().get(key).add(value);
			tag_display = PhotoAlbum.photo.getTagDisplay();
			tags.setText("Tags - " + tag_display);
		}
		tags.setText("Tags: " + tag_display);
	}

	/**
	 * Done editing.
	 * @param e
	 */
	public void done(ActionEvent e){
		hideButton(addTag);
		hideButton(done);
		hideButton(removeTag);
		showButton(edit);
		showButton(move);
		showButton(delete);
		PhotoAlbum.photo.setCaption(newCaption.getText());
		pane.getChildren().remove(newCaption);
		newCaption = new TextField();
		caption.setText("Caption: " + PhotoAlbum.photo.getCaption());
		PhotoAlbum.regular_user.serialize();
	}

	/**
	 * Likes image.
	 */
	public void like(ActionEvent e){
		Photo photo = PhotoAlbum.photo;
		if(photo.liked()){
			photo.removeUserFromLikers();
			likes.setText(photo.getLikes() + "");
		} else {
			photo.addUserToLikers();
			likes.setText(photo.getLikes() + "   - You like this photo!");
		}
		PhotoAlbum.regular_user.serialize();
	}

	/**
	 * Removes tags based on the key or value given
	 * @param e ActionEvent
	 */
	public void removeTag(ActionEvent e){
		RemoveTagDialog dialog = new RemoveTagDialog();
		Optional<ButtonType> result = dialog.showAndWait();

		String ok = ButtonType.OK.getText();
		String click = result.get().getText();
		if (click.equals(ok)) {
			Photo photo = PhotoAlbum.photo;
			HashMap<String, ArrayList<String>> taglist = photo.getTags();
			if(dialog.getKey().length() > 0){
				String key = dialog.getKey().trim();
				while(taglist.remove(key) != null);
			} 
			tag_display = PhotoAlbum.photo.getTagDisplay();
			tags.setText("Tags - " + tag_display);
			PhotoAlbum.regular_user.serialize();
		}
	}
	
	/**
	 * Goes to the next photo in the album.
	 * @param e
	 */
	public void forward(ActionEvent e){
		int index = PhotoAlbum.album.getPhotos().indexOf(PhotoAlbum.photo);
		if(index == PhotoAlbum.album.getPhotos().size() - 1){
			return;
		}
		PhotoAlbum.photo = PhotoAlbum.album.getPhotos().get(index + 1);
		segue("/view/Photo.fxml");
	}
	
	/**
	 * Goes to the previous photo in the album.
	 * @param e
	 */
	public void backward(ActionEvent e){
		int index = PhotoAlbum.album.getPhotos().indexOf(PhotoAlbum.photo);
		if(index == 0){
			return;
		}
		PhotoAlbum.photo = PhotoAlbum.album.getPhotos().get(index - 1);
		segue("/view/Photo.fxml");
	}
	
	/**
	 * Moves photo to another album.
	 * @param e
	 */
	public void move(ActionEvent e){
		MovePhotoDialog dialog = new MovePhotoDialog();
		Optional<ButtonType> result = dialog.showAndWait();

		String ok = ButtonType.OK.getText();
		String click = result.get().getText();

		if (click.equals(ok)) {
			String album_name = dialog.getAlbumName();
			if (album_name == null) {
				Alert error = new Alert(AlertType.INFORMATION);
				error.setHeaderText("Error!");
				error.setContentText("Album is required!");
				error.show();
				return;
			}

			for (int i = 0; i < PhotoAlbum.regular_user.getAlbums().size(); i++) {
				Album a = PhotoAlbum.regular_user.getAlbums().get(i);
				if (album_name.equals(a.getName())) {
					for(Photo p: a.getPhotos()){
						File file = PhotoAlbum.photo.getFile();
						if(p.getFile().equals(file)){
							Alert error = new Alert(AlertType.INFORMATION);
							error.setHeaderText("Error!");
							error.setContentText("Photo already in that album!");
							error.show();
							return;
						}
					}
					a.getPhotos().add(PhotoAlbum.photo);
					PhotoAlbum.album.getPhotos().remove(PhotoAlbum.photo);
					segue("/view/Album.fxml");
					return;
				}
			}	
		}
	}
}
