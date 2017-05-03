package ru.spbau.svidchenko.hw04.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import ru.spbau.svidchenko.hw04.client.FileClient;
import ru.spbau.svidchenko.hw04.client.SimpleFileClient;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Client window of app
 */
public class Client {
    @FXML
    private ListView fileListView;
    @FXML
    private Text pathText;
    @FXML
    private Text errorMsgText;
    @FXML
    private TextField portTextField;
    @FXML
    private TextField hostTextField;
    @FXML
    private Text connectionInfoText;

    private int port;
    private InetAddress address;
    private String host;
    private String currentPath;
    private FileClient client;
    private HashMap<String, Boolean> isDirectory = new HashMap<>();
    private ArrayList<String> files = new ArrayList<>();

    //TODO: Ask server about path divider
    //TODO: Ask server about root directory

    void open() {
        try {
            Parent root = FXMLLoader.load(Main.loader.getResource("Client.fxml"));
            Stage stage = new Stage();
            stage.setTitle("FTP Client-server browser");
            stage.setScene(new Scene(root));
            stage.setResizable(true);
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onConnectClick() {
        try {
            port = Integer.decode(portTextField.getText());
            host = hostTextField.getText();
            address = InetAddress.getByName(host);
            client = new SimpleFileClient(address, port);
            currentPath = File.listRoots()[0].getAbsolutePath(); //FIXME: Ask about root
            goToDirectory(Paths.get(currentPath));
            connectionInfoText.setText("Connected to " + host + "::" + port);
            errorMsgText.setText("OK");
        }
        catch (Throwable e) {
            errorMsgText.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onListElementClick() {
        try {
            String selected = (String)fileListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                return;
            }
            Path p = Paths.get(currentPath, selected); //FIXME: Ask about server path divider
            if (isDirectory.get(selected)) {
                goToDirectory(p);
            } else {
                Pair<byte[], Long> fileContent = client.getTask(p.toString());
                int divideAt = selected.lastIndexOf(".");
                File tmpFile = Files.createTempFile(selected.substring(0, divideAt-1), selected.substring(divideAt)).toFile();
                FileOutputStream output = new FileOutputStream(tmpFile);
                output.write(fileContent.getKey(), 0, (int)(long)fileContent.getValue());
                output.close();
                runFile(tmpFile);
            }
            errorMsgText.setText("OK");
        }
        catch (Throwable e) {
            errorMsgText.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onTurnBackClick() {
        try {
            Path p = Paths.get(currentPath).getParent(); //FIXME: Ask about server path divider
            goToDirectory(p);
            errorMsgText.setText("OK");
        }
        catch (Throwable e) {
            errorMsgText.setText(e.getMessage());
            e.printStackTrace();
        }
    }

    private void goToDirectory(Path p) throws IOException {
        pathText.setText(p.toString());
        List<Pair<String, Boolean>> newFiles = client.listTask(p.toString());
        files.clear();
        isDirectory.clear();
        currentPath = p.toString();
        for (Pair<String, Boolean> pair : newFiles) {
            files.add(pair.getKey());
            isDirectory.put(pair.getKey(), pair.getValue());
        }
        fileListView.setItems(FXCollections.observableArrayList(files));
    }

    private void runFile(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }
}
