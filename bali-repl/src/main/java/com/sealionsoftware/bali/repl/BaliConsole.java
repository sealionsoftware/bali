package com.sealionsoftware.bali.repl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BaliConsole extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Bali Evaluator");
        Parent root = FXMLLoader.load(getClass().getResource("repl.fxml"));

        stage.setResizable(false);
        stage.setHeight(400);
        stage.setWidth(1110);

        stage.setScene(new Scene(root));
        stage.show();
    }
}