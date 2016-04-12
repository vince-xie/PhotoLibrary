package controller;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import app.PhotoAlbum;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import view.AddAlbumDialog;
import view.SearchDialog;

/**
 * @author Edmond Wu & Vincent Xie
 */
public class AlbumsController extends Controller{

	@FXML
	private ImageView imageView;

	@FXML
	private GridPane grid;
	
	@FXML
	private Button edit;
	
	@FXML
	private Button delete;
	
	@FXML
	private Button logout;
	
	@FXML
	private Button done;
	
	@FXML
	private Button search;
	
	@FXML
	private Button add;
	
	@FXML
	private Text select;
	
	@FXML
	private Pane pane;
	
	@FXML
	private Button back;
	
	@FXML
	private Button view;
	
	private ArrayList<Integer> selected;
	
	public static int selectint;

	public void start(Stage mainStage) {  
		if(!PhotoAlbum.logged_in.equals(PhotoAlbum.regular_user)){
			hideButton(edit);
			hideButton(add);
			hideButton(view);
			imageView.setOpacity(0);
			
			Text name = new Text(PhotoAlbum.regular_user.getUsername() + "'s Albums");
			name.setLayoutX(130);
			name.setLayoutY(44);
			name.setId("id");
			name.setWrappingWidth(560);
			pane.getChildren().add(name);
		} else {
			hideButton(back);
		}
		File file = new File("src/assets/Albums.png");
		Image image = new Image(file.toURI().toString());
		imageView.setImage(image);
		hideButton(done);
		hideButton(delete);
		select.setOpacity(0);
		displayAlbums();
	}

	/**
	 * Adds an album to the user's album list
	 * @param e
	 */
	public void add(ActionEvent e) {
		AddAlbumDialog dialog = new AddAlbumDialog();
		Optional<ButtonType> result = dialog.showAndWait();

		String ok = ButtonType.OK.getText();
		String click = result.get().getText();

		if (click.equals(ok)) {
			String album_name = dialog.getAlbumName();
			if (album_name.isEmpty() || album_name.trim().length() == 0) {
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
			PhotoAlbum.regular_user.getAlbums().add(added);
		}
		displayAlbums();
		PhotoAlbum.regular_user.serialize();
	}

	/**
	 * Removes from the list of albums
	 * @param e
	 */
	public void delete(ActionEvent e){
		ButtonType ok = new ButtonType("OK", ButtonData.OK_DONE);
		ButtonType cancel = new ButtonType("Cancel", ButtonData.NO);
		Dialog<ButtonType> dialog = new Dialog<ButtonType>();
		dialog.getDialogPane().getButtonTypes().add(ok);
		dialog.getDialogPane().getButtonTypes().add(cancel);
		dialog.setHeaderText("Confirm.");
		dialog.setContentText("Are you sure you would like to delete the selected albums? (WARNING: This will" + 
				" clear all unsaved name changes.)");
		dialog.showAndWait().ifPresent(response -> {
			if (response == ok) {
				ArrayList<Album> albums = PhotoAlbum.regular_user.getAlbums();
				for(Integer i: selected){
					albums.set(i, null);
				}
				while(albums.remove(null));
				selected.clear();
				selectint = 0;
				select.setText(selectint + " albums selected.");
				displayAlbumsEdit();
				PhotoAlbum.regular_user.serialize();
			} else {
				ObservableList<Node> list = grid.getChildren();
				List<String> names = new ArrayList<String>();
				for(int i = 0; i < list.size(); i++){
					Node node = list.get(i);
					if(node instanceof TextField){
						names.add(((TextField)node).getText());
					} else {
						names.add("");
					}
				}
				displayAlbumsEdit();
				for(int i = 0; i < list.size(); i++){
					Node node = list.get(i);
					if(node instanceof TextField){
						((TextField)node).setText(names.get(i));
					} 
				}
				selected.clear();
				selectint = 0;
				select.setText(selectint + " albums selected.");
				return;
			}
		});
	}

	/**
	 * Allows editing of albums.
	 * @param e
	 */
	public void edit(ActionEvent e) {
		selected = new ArrayList<Integer>();
		select.setOpacity(1);
		selectint = 0;
		select.setText(selected.size() + " albums selected.");
		hideButton(view);
		hideButton(edit);
		hideButton(logout);
		hideButton(search);
		hideButton(add);
		showButton(done);
		showButton(delete);
		displayAlbumsEdit();
		PhotoAlbum.regular_user.serialize();
	}
	
	/**
	 * Finish editing.
	 * @param e
	 */
	public void done(ActionEvent e) {
		select.setOpacity(0);
		select.setText("");
		showButton(view);
		showButton(edit);
		showButton(logout);
		showButton(search);
		showButton(add);
		hideButton(done);
		hideButton(delete);
		ObservableList<Node> list = grid.getChildren();
		ArrayList<Album> albums = PhotoAlbum.regular_user.getAlbums();
		List<String> names = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++){
			Node node = list.get(i);
			if(node instanceof TextField){
				String name = ((TextField)node).getText();
				if(name.length() == 0){
					Alert error = new Alert(AlertType.INFORMATION);
					error.setHeaderText("Error!");
					error.setContentText("Cannot have empty album names!");
					error.show();
					selectint = 0;
					displayAlbums();
					return;
				}
				if(names.contains(name)){
					Alert error = new Alert(AlertType.INFORMATION);
					error.setHeaderText("Error!");
					error.setContentText("Duplicate album names!");
					error.show();
					selectint = 0;
					displayAlbums();
					return;
				}
				names.add(name);
			}
		}
		for(int i = 0; i < list.size(); i++){
			Node node = list.get(i);
			if(node instanceof TextField){
				albums.get((i - 1) / 2).changeName(((TextField)node).getText());
			}
		}
		selectint = 0;
		displayAlbums();
	}
	
	/**
	 * Allows searching of albums.
	 * @param e Mouse click event
	 */
	public void search(ActionEvent e) {
		SearchController.albums = true;
		SearchDialog dialog = new SearchDialog();
		Optional<ButtonType> result = dialog.showAndWait();
		
		String ok = ButtonType.OK.getText();
		String click = result.get().getText();
		if (click.equals(ok)) {
			PhotoAlbum.search = new ArrayList<Photo>();
			
			LocalDate startDate = dialog.getStartDate();
			LocalDate endDate = dialog.getEndDate();
			String key = dialog.getKey().trim().toLowerCase();
			String value = dialog.getValue().trim().toLowerCase();
			
			if (startDate == null && endDate == null && key.length() == 0 && value.length() == 0) {
				segue("/view/Search.fxml");
				return;
			}
			
			ArrayList<Album> albums = PhotoAlbum.regular_user.getAlbums();
			for(Album a : albums){
				ArrayList<Photo> photos = a.getPhotos();
				for(int i = 0; i < photos.size(); i++){
					File file = photos.get(i).getFile();
					Date d = new Date(file.lastModified());
					LocalDate date = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					if(startDate == null && endDate == null){
						if(key.length() > 0 && value.length() > 0){
							for (String k : photos.get(i).getTags().keySet()) {
								if (k.contains(key) && photos.get(i).getTags().get(k).contains(value)) {
									boolean duplicate = false;
									for(Photo p: PhotoAlbum.search){
										if(p.getFile().equals(photos.get(i).getFile())){
											duplicate = true;
										}
									}
									if(duplicate == false){
										PhotoAlbum.search.add(photos.get(i));
									}
								}
							}
						} else if (key.length() > 0 && value.length() == 0) {
							for (String k : photos.get(i).getTags().keySet()) {
								if (k.contains(key)) {
									boolean duplicate = false;
									for(Photo p: PhotoAlbum.search){
										if(p.getFile().equals(photos.get(i).getFile())){
											duplicate = true;
										}
									}
									if(duplicate == false){
										PhotoAlbum.search.add(photos.get(i));
									}
								}
							}
						} else if (value.length() > 0 && key.length() == 0) {
							for (String k : photos.get(i).getTags().keySet()) {
								if (photos.get(i).getTags().get(k).contains(value)) {
									boolean duplicate = false;
									for(Photo p: PhotoAlbum.search){
										if(p.getFile().equals(photos.get(i).getFile())){
											duplicate = true;
										}
									}
									if(duplicate == false){
										PhotoAlbum.search.add(photos.get(i));
									}
								}
							}
						} else {
							boolean duplicate = false;
							for(Photo p: PhotoAlbum.search){
								if(p.getFile().equals(photos.get(i).getFile())){
									duplicate = true;
								}
							}
							if(duplicate == false){
								PhotoAlbum.search.add(photos.get(i));
							}
						}
					} else if(startDate == null && endDate != null){
						if(date.compareTo(endDate) <= 0){
							if(key.length() > 0 && value.length() > 0){
								for (String k : photos.get(i).getTags().keySet()) {
									if (k.contains(key) && photos.get(i).getTags().get(k).contains(value)) {
										boolean duplicate = false;
										for(Photo p: PhotoAlbum.search){
											if(p.getFile().equals(photos.get(i).getFile())){
												duplicate = true;
											}
										}
										if(duplicate == false){
											PhotoAlbum.search.add(photos.get(i));
										}
									}
								}
							} else if (key.length() > 0 && value.length() == 0) {
								for (String k : photos.get(i).getTags().keySet()) {
									if (k.contains(key)) {
										boolean duplicate = false;
										for(Photo p: PhotoAlbum.search){
											if(p.getFile().equals(photos.get(i).getFile())){
												duplicate = true;
											}
										}
										if(duplicate == false){
											PhotoAlbum.search.add(photos.get(i));
										}
									}
								}
							} else if (value.length() > 0 && key.length() == 0) {
								for (String k : photos.get(i).getTags().keySet()) {
									if (photos.get(i).getTags().get(k).contains(value)) {
										boolean duplicate = false;
										for(Photo p: PhotoAlbum.search){
											if(p.getFile().equals(photos.get(i).getFile())){
												duplicate = true;
											}
										}
										if(duplicate == false){
											PhotoAlbum.search.add(photos.get(i));
										}
									}
								}
							} else {
								boolean duplicate = false;
								for(Photo p: PhotoAlbum.search){
									if(p.getFile().equals(photos.get(i).getFile())){
										duplicate = true;
									}
								}
								if(duplicate == false){
									PhotoAlbum.search.add(photos.get(i));
								}
							}
						}
					} else if (endDate == null && startDate != null){
						if(date.compareTo(startDate) >= 0){ 
							if(key.length() > 0 && value.length() > 0){
								for (String k : photos.get(i).getTags().keySet()) {
									if (k.contains(key) && photos.get(i).getTags().get(k).contains(value)) {
										boolean duplicate = false;
										for(Photo p: PhotoAlbum.search){
											if(p.getFile().equals(photos.get(i).getFile())){
												duplicate = true;
											}
										}
										if(duplicate == false){
											PhotoAlbum.search.add(photos.get(i));
										}
									}
								}
							} else if (key.length() > 0 && value.length() == 0) {
								for (String k : photos.get(i).getTags().keySet()) {
									if (k.contains(key)) {
										boolean duplicate = false;
										for(Photo p: PhotoAlbum.search){
											if(p.getFile().equals(photos.get(i).getFile())){
												duplicate = true;
											}
										}
										if(duplicate == false){
											PhotoAlbum.search.add(photos.get(i));
										}
									}
								}
							} else if (value.length() > 0 && key.length() == 0) {
								for (String k : photos.get(i).getTags().keySet()) {
									if (photos.get(i).getTags().get(k).contains(value)) {
										boolean duplicate = false;
										for(Photo p: PhotoAlbum.search){
											if(p.getFile().equals(photos.get(i).getFile())){
												duplicate = true;
											}
										}
										if(duplicate == false){
											PhotoAlbum.search.add(photos.get(i));
										}
									}
								}
							} else {
								boolean duplicate = false;
								for(Photo p: PhotoAlbum.search){
									if(p.getFile().equals(photos.get(i).getFile())){
										duplicate = true;
									}
								}
								if(duplicate == false){
									PhotoAlbum.search.add(photos.get(i));
								}
							}
						}
					} else {
						if(date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0){ 
							if(key.length() > 0 && value.length() > 0){
								for (String k : photos.get(i).getTags().keySet()) {
									if (k.contains(key) && photos.get(i).getTags().get(k).contains(value)) {
										boolean duplicate = false;
										for(Photo p: PhotoAlbum.search){
											if(p.getFile().equals(photos.get(i).getFile())){
												duplicate = true;
											}
										}
										if(duplicate == false){
											PhotoAlbum.search.add(photos.get(i));
										}
									}
								}
							} else if (key.length() > 0 && value.length() == 0) {
								for (String k : photos.get(i).getTags().keySet()) {
									if (k.contains(key)) {
										boolean duplicate = false;
										for(Photo p: PhotoAlbum.search){
											if(p.getFile().equals(photos.get(i).getFile())){
												duplicate = true;
											}
										}
										if(duplicate == false){
											PhotoAlbum.search.add(photos.get(i));
										}
									}
								}
							} else if (value.length() > 0 && key.length() == 0) {
								for (String k : photos.get(i).getTags().keySet()) {
									if (photos.get(i).getTags().get(k).contains(value)) {
										boolean duplicate = false;
										for(Photo p: PhotoAlbum.search){
											if(p.getFile().equals(photos.get(i).getFile())){
												duplicate = true;
											}
										}
										if(duplicate == false){
											PhotoAlbum.search.add(photos.get(i));
										}
									}
								}
							} else {
								boolean duplicate = false;
								for(Photo p: PhotoAlbum.search){
									if(p.getFile().equals(photos.get(i).getFile())){
										duplicate = true;
									}
								}
								if(duplicate == false){
									PhotoAlbum.search.add(photos.get(i));
								}
							}
						}
					}
				}
			}
			segue("/view/Search.fxml");
		}
	}

	/**
	 * Displays albums.
	 */
	public void displayAlbums(){
		grid.getChildren().clear();
		grid.getRowConstraints().clear();
		ArrayList<Album> albums = PhotoAlbum.regular_user.getAlbums();
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
					File file = new File("src/assets/empty.jpg");;
					ArrayList<Photo> photos = albums.get(2 * i + j).getPhotos();
					for(int k = 0; k < photos.size(); k++){
						file = photos.get(k).getFile();
						if(file.exists()){
							break;
						} else {
							photos.set(k, null);
						}
					}
					if(!file.exists()){
						file = new File("src/assets/empty.jpg");
					}
					while(photos.remove(null));
					PhotoAlbum.regular_user.serialize();
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
						PhotoAlbum.album = PhotoAlbum.regular_user
								.getAlbums().get(
										2 * GridPane.getRowIndex(cover) + GridPane.getColumnIndex(cover));
						segue("/view/Album.fxml");
					});
					Text name = new Text();
					name.setText(albums.get(2 * i + j).getName());
					name.setWrappingWidth(366);
					grid.add(name, j, i);
					GridPane.setHalignment(name, HPos.CENTER);
					GridPane.setValignment(name, VPos.BOTTOM);
					name.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> { 
						PhotoAlbum.album = PhotoAlbum.regular_user
								.getAlbums().get(
										2 * GridPane.getRowIndex(cover) + GridPane.getColumnIndex(cover));
						segue("/view/Album.fxml");
					});
				}
			}
		}
	}
	
	/**
	 * Displays albums for editing.
	 */
	public void displayAlbumsEdit(){
		grid.getChildren().clear();
		grid.getRowConstraints().clear();
		ArrayList<Album> albums = PhotoAlbum.regular_user.getAlbums();
		grid.setPrefHeight(70 + (int)((albums.size() + 1) / 2) * 211);
		if(albums.size() <= 2){
			grid.setPrefHeight(240);
		} else if(albums.size() <= 4){
			grid.setPrefHeight(468);
		}
		for(int i = 0; i < Math.ceil(albums.size() / 2.0); i++){
			RowConstraints row = new RowConstraints();
			row.setMinHeight(10);
			row.setPrefHeight(30);
			row.setVgrow(Priority.SOMETIMES);
			grid.getRowConstraints().add(row);
		}
		for(int i = 0; i <= albums.size() / 2; i++){
			for(int j = 0; j < 2; j++){
				if(2 * i + j < albums.size()){
					File file = new File("src/assets/empty.jpg");;
					ArrayList<Photo> photos = albums.get(2 * i + j).getPhotos();
					for(int k = 0; k < photos.size(); k++){
						file = photos.get(k).getFile();
						if(file.exists()){
							break;
						} else {
							photos.set(k, null);
						}
					}
					if(!file.exists()){
						file = new File("src/assets/empty.jpg");
					}
					while(photos.remove(null));
					PhotoAlbum.regular_user.serialize();
					Image image = new Image(file.toURI().toString());
					ImageView cover = new ImageView();
					cover.setFitHeight(190);
					cover.setFitWidth(320);
					cover.setPreserveRatio(true);
					cover.setPickOnBounds(true);
					cover.setImage(image);
					cover.setOpacity(0.5);
					grid.add(cover, j, i);
					GridPane.setHalignment(cover, HPos.CENTER);
					GridPane.setValignment(cover, VPos.CENTER);
					GridPane.setMargin(cover, new Insets(0, 0, 10, 0));
					cover.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> { 
						if(cover.getOpacity() == 0.5){
							cover.setOpacity(1);
							AlbumsController.selectint++;
							select.setText(AlbumsController.selectint + " albums selected.");
							selected.add(2 * GridPane.getRowIndex(cover) + GridPane.getColumnIndex(cover));
						} else {
							cover.setOpacity(0.5);
							AlbumsController.selectint--;
							select.setText(AlbumsController.selectint + " albums selected.");
							selected.remove(new Integer(2 * GridPane.getRowIndex(cover) + GridPane.getColumnIndex(cover)));
						}
					});
					TextField name = new TextField();
					name.setText(albums.get(2 * i + j).getName());
					name.setPrefWidth(250);
					name.setMaxWidth(250);
					name.setAlignment(Pos.CENTER);
					grid.add(name, j, i);
					GridPane.setHalignment(name, HPos.CENTER);
					GridPane.setValignment(name, VPos.BOTTOM);
					name.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> { 
						PhotoAlbum.album = PhotoAlbum.regular_user
								.getAlbums().get(
										2 * GridPane.getRowIndex(cover) + GridPane.getColumnIndex(cover));
					});
				}
			}
		}
	}
	
	/**
	 * Allows users to view other user's albums if they are public
	 * @param e
	 */
	public void view(ActionEvent e){
		segue("/view/Users.fxml");
	}
	
	/**
	 * Goes back to logged in user's page.
	 * @param e
	 */
	public void back(ActionEvent e){
		PhotoAlbum.regular_user = PhotoAlbum.logged_in;
		segue("/view/Users.fxml");
	}
}