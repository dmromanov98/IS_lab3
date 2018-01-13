package Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class WindowClientController implements Initializable {

    @FXML
    private TextField textCommands;

    @FXML
    private TextField textToServer;

    private Thread server;

    private Client client;

    public void connectToServer() {
        if (!server.isAlive()) {
            server = new Thread(client);
            server.start();
        } else {
            JOptionPane.showMessageDialog(null, "You already connected!");
        }
    }

    //закрытие потока
    public void closeThread() {
        client.CloseSocket();
        server.stop();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new Client();
        server = new Thread(client);
    }

    public void sendToServer(ActionEvent actionEvent) {
        if (textCommands.getText().length() != 0)
            client.sendMessage(textToServer.getText(), textCommands.getText());
        else
            JOptionPane.showMessageDialog(null, "Введите команду!");
    }
}