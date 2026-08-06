package com.rusmessanger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/ui/styles.css").toExternalForm());
        primaryStage.setTitle("RUS MESSANGER");
        primaryStage.setScene(scene);
        primaryStage.setWidth(1100);
        primaryStage.setHeight(750);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
