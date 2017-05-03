package ru.spbau.svidchenko.hw04.gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.nio.channels.ServerSocketChannel;
import java.util.ArrayList;

/**
 * Main window of application
 */
public class Main extends Application {
    static final ClassLoader loader = Main.class.getClassLoader();
    private String[] args;

    void main(String[] args) {
        Application.launch(args);
        this.args = args;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(loader.getResource("Main.fxml"));
        Stage stage = new Stage();
        stage.setTitle("FTP Client-server browser");
        stage.setScene(new Scene(root));
        stage.setResizable(true);
        stage.show();
    }

    @FXML
    public void onCreateServerClick() {
        new Server().open();
    }

    @FXML
    public void onCreateClientClick() {
        new Client().open();
    }
}
