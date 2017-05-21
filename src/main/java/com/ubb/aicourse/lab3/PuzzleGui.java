package com.ubb.aicourse.lab3;/**
 * @author Marius Adam
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PuzzleGui extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        InputStream fxml = new BufferedInputStream(getClass().getResourceAsStream("/Puzzle.fxml"));
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(fxml);

        stage.setTitle("Puzzle AI");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }
}
