/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grouper;

import BrightspaceManifest.BrightspaceManifest;
import DiscussionBoard.DiscussionBoard;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author hallm8
 */
public class AddBoard implements Initializable {

    @FXML
    private Label title;
    @FXML
    private TextField name;
    @FXML
    private TextField score;
    @FXML
    private TextField numTopics;
    @FXML
    private CheckBox isNewThread;
    @FXML
    private ChoiceBox upvoteOptions;
    @FXML
    private Button addBoard;
    @FXML
    private Button writeFile;
    @FXML
    private Button deleteBoard;
    @FXML
    private TableView discussionList;
    @FXML
    private TableColumn dBoardName;
    @FXML
    private TableColumn dBoardScore;
    @FXML
    private TableColumn dBoardTopics;

    private ArrayList<DiscussionBoard> discussionBoards;

    private final ObservableList<DiscussionBoard> data = FXCollections.observableArrayList();

    public AddBoard() {

    }

    /**
     * FILL TABLE
     *
     * When the fill table button is pressed, information is drawn from the
     * fields and inserted into the discussion board class. Then, the table is
     * populated with said values.
     *
     * @param event
     */
    @FXML
    public void addBoardEvent(ActionEvent event) {
        DiscussionBoard discussionBoard = new DiscussionBoard();
        discussionBoard.setName(name.getText());
        discussionBoard.setPointValue(score.getText());
        discussionBoard.setGroupSize(Integer.valueOf(numTopics.getText()));
        discussionBoard.setTopics(numTopics.getText());
        discussionBoard.setIsPostFirst(isNewThread.isSelected());
        discussionBoard.setUpvote(upvoteOptions.getSelectionModel().getSelectedIndex());
        discussionBoards.add(discussionBoard);
        data.add(discussionBoard);
    }

    /**
     *
     * @param event
     */
    @FXML
    public void deleteBoardEvent(ActionEvent event) {
        if (!discussionBoards.isEmpty() && !discussionList.getSelectionModel().isEmpty()) {
            discussionBoards.remove(discussionList.getSelectionModel().getSelectedItem());
            data.remove(discussionList.getSelectionModel().getSelectedIndex());
            System.out.println(discussionBoards.size());
            System.out.println(data.size());
        }
    }

    /**
     *
     * @param event
     */
    @FXML
    public void createBoard(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.zip");
        fileChooser.getExtensionFilters().add(extFilter);
        //Show save file dialog
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            System.out.println(file.getAbsolutePath());
            System.out.println(file.getName());
            System.out.println(file.getParent());
            System.out.println(file.getPath());

            BrightspaceManifest manifest = new BrightspaceManifest();
            manifest.setDiscussionBoards(discussionBoards);
            manifest.setSavePath(file.getParent());
            manifest.setZipName(file.getName());
            manifest.buildManifest();

            try {
                Desktop.getDesktop().open(new File(file.getParent()));
            } catch (IOException ex) {
                Logger.getLogger(AddBoard.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    public void exitBoard(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setTitle("Confirmation");
        //alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you sure you want to quit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Platform.exit();
        } else {
        }
    }
    
    @FXML
    public void ilearn3(ActionEvent event){
        try {
            Desktop.getDesktop().browse(new URI("http://byui.brightspace.com"));
        } catch (IOException | URISyntaxException e1) {
        } 
    }
    
    @FXML
    public void byuiPage(ActionEvent event){
        try {
            Desktop.getDesktop().browse(new URI("http://www.byui.edu"));
        } catch (IOException | URISyntaxException e1) {
        } 
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        upvoteOptions.setItems(FXCollections.observableArrayList("No Ratings", new Separator(), "Five-Star Rating Scheme",
                "Up Vote/Down Vote Rating Scheme", "Up Vote Only Rating Scheme"));
        upvoteOptions.getSelectionModel().select(0);
        dBoardName.setCellValueFactory(new PropertyValueFactory<DiscussionBoard, String>("name"));
        dBoardScore.setCellValueFactory(new PropertyValueFactory<DiscussionBoard, String>("pointValue"));
        dBoardTopics.setCellValueFactory(new PropertyValueFactory<DiscussionBoard, String>("topics"));
        discussionList.setItems(data);
        discussionBoards = new ArrayList<>();

    }

}
