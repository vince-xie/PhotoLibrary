package controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;

import app.PhotoAlbum;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import view.AddAlbumDialog;

/**
 * @author Edmond Wu & Vincent Xie
 */
public class SearchController extends Controller {
	
	
	public static boolean albums;
	
	@FXML
	private ImageView imageView;
	
	@FXML
	private Button add;
	
	@FXML
	private GridPane grid;
	
	
	@FXML
	private ListView<String> list;
	
	public void start(Stage mainStage) {   
		File file = new File("src/assets/results.png");
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);	
		if(!PhotoAlbum.logged_in.equals(PhotoAlbum.regular_user)){
			hideButton(add);
		}
		displayAlbums();
	}
	
	/**
	 * Goes to the previous page.
	 * @param e
	 */
	public void back(ActionEvent e){
		if(albums){
			segue("/view/Albums.fxml");
		} else {
			segue("/view/Album.fxml");
		}
	}
	
	/**
	 * Displays albums.
	 */
	public void displayAlbums(){
		if(PhotoAlbum.search == null || PhotoAlbum.search.size() == 0){
			return;
		}
		grid.getChildren().clear();
		grid.getRowConstraints().clear();
		ArrayList<Photo> albums = PhotoAlbum.search;
		for(int i = 0; i < albums.size(); i++){
			File file = albums.get(i).getFile();
			if(!file.exists()){
				albums.set(i, null);
			}
		}
		while(albums.remove(null));
		PhotoAlbum.regular_user.serialize();
		grid.setPrefHeight(70 + (int)((albums.size() + 1) / 2) * 211);
		if(albums.size() <= 2){
			grid.setPrefHeight(240);
		} else if(albums.size() <= 4){
			grid.setPrefHeight(468);
		}
		for(int i = grid.getRowConstraints().size(); i < Math.ceil(albums.size() / 2.0); i++){
			RowConstraints row = new RowConstraints();
			row.setMinHeight(10);
			row.setPrefHeight(30);
			row.setVgrow(Priority.SOMETIMES);
			grid.getRowConstraints().add(row);
		}
		for(int i = 0; i <= albums.size() / 2; i++){
			for(int j = 0; j < 2; j++){
				if(2 * i + j < albums.size()){
					File file = albums.get(2 * i + j).getFile();
					Image image = new Image(file.toURI().toString());
					ImageView cover = new ImageView();
					cover.setFitHeight(190);
					cover.setFitWidth(320);
					cover.setPreserveRatio(true);
					cover.setPickOnBounds(true);
					cover.setImage(image);
					grid.add(cover, j, i);
					GridPane.setHalignment(cover, HPos.CENTER);
					GridPane.setValignment(cover, VPos.CENTER);
					GridPane.setMargin(cover, new Insets(0, 0, 10, 0));
					cover.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> { 
						PhotoAlbum.photo = PhotoAlbum.search.get(
										2 * GridPane.getRowIndex(cover) + GridPane.getColumnIndex(cover));
						PhotoController.search = true;
						segue("/view/Photo.fxml");
					});
				}
			}
		}
	}
	
	/**
	 * Add search results as an album.
	 * @param e
	 */
	public void addAsAlbum(ActionEvent e){
		AddAlbumDialog dialog = new AddAlbumDialog();
		Optional<ButtonType> result = dialog.showAndWait();

		String ok = ButtonType.OK.getText();
		String click = result.get().getText();

		if (click.equals(ok)) {
			String album_name = dialog.getAlbumName();
			if (album_name.isEmpty()) {
				Alert error = new Alert(AlertType.INFORMATION);
				error.setHeaderText("Error!");
				error.setContentText("Album name is required!");
				error.show();
				return;
			}

			for (int i = 0; i < PhotoAlbum.regular_user.getAlbums().size(); i++) {
				Album a = PhotoAlbum.regular_user.getAlbums().get(i);
				if (album_name.equalsIgnoreCase(a.getName())) {
					Alert error = new Alert(AlertType.INFORMATION);
					error.setHeaderText("Error!");
					error.setContentText("Album already exists!");
					error.show();
					return;
				}
			}
			Album added = new Album(album_name);
			added.setPhotos(PhotoAlbum.search);
			PhotoAlbum.regular_user.getAlbums().add(added);
			displayAlbums();
			PhotoAlbum.regular_user.serialize();
			Alert conf = new Alert(AlertType.INFORMATION);
			conf.setContentText("Album created successfully!");
			conf.show();
		}
	}
}
