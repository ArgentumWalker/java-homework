package ru.spbau.svidchenko.hw04.gui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.spbau.svidchenko.hw04.server.FileServer;
import ru.spbau.svidchenko.hw04.server.MultithreadFileServer;

import java.io.IOException;

/**
 * Server window of application
 */
public class Server {
    private FileServer server;

    @FXML
    private TextField portTextField;
    @FXML
    private Text errorMsgText;

    void open() {
        try {
            Parent root = FXMLLoader.load(Main.loader.getResource("Server.fxml"));
            Stage stage = new Stage();
            stage.setTitle("FTP Client-server browser");
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onStartServerClicked() {
        try {
            server = new MultithreadFileServer(Integer.decode(portTextField.getText()));
            server.start();
            errorMsgText.setText("Ok");
        }
        catch (IOException e) {
            errorMsgText.setText(e.getMessage());
        }
        catch (Throwable e) {
            errorMsgText.setText(e.getMessage());
        }
    }
    @FXML
    public void onStopServerClicked() {
        try {
            server.stop();
            errorMsgText.setText("Ok");
        }
        catch (IOException e) {
            errorMsgText.setText(e.getMessage());
        }
        catch (Throwable e) {
            errorMsgText.setText(e.getMessage());
        }
    }
}
