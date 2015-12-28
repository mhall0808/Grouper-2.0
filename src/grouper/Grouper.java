/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grouper;

import Controller.ControllerTest;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author hallm8
 */
public class Grouper extends Application {

    @FXML
    private Label title;
    @FXML
    private Button addBoard;

    @Override
    public void start(Stage primaryStage) {
        
        
        ControllerTest test = new ControllerTest();
        
        test.runTest();
        
        

        try {
            primaryStage.initStyle(StageStyle.UNDECORATED);
            Parent parent = FXMLLoader.load(getClass().getResource("OtherIdeas.fxml"));
            Scene scene = new Scene(parent, 768, 620);
            String css = this.getClass().getResource("JMetroLightTheme.css").toExternalForm();
            primaryStage.setTitle("Grouper Tool");
            primaryStage.setScene(scene);
            scene.getStylesheets().add(css);

            primaryStage.show();
            
            

        } catch (IOException ex) {
            Logger.getLogger(Grouper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void addBoard(ActionEvent event) {
        System.out.println("Button click successful!");
        System.out.println(title.getText());

    }

}
