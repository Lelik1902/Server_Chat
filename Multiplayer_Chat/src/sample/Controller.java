package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class Controller {

    public Button sendButton;
    public TextFlow ChatViewier;
    public ScrollPane scrollPane;
    public AnchorPane AnchPane;
    public VBox Vbox;
    public Button btnSendFile;
    public TextField MessageInput;

    PrintWriter out;
    BufferedReader in;
    String fromServer;
    String name_fromServer;
    String message_fromServer;
    Socket socket;
    String receivedMmessage;
    ActionListener taskPerformer;
    Stage primaryStage;
    Main m;

    public static String host = "localhost";
    public static int port = 8456;

    public Controller() throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        runReadMessage();
    }

    public void onBtnSendFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("D:\\"));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("image", "*.jpg", "*.png", "*.gif", "*.ico"));
        List<File> selectedFile = fc.showOpenMultipleDialog(null);
        if (selectedFile != null) {
            for (File file : selectedFile) {
                ChatViewier.getChildren().add(new Text(file.getAbsolutePath() + "\n"));
            }
        } else {
            System.out.println("File is not valid");
        }
    }

    public void onSendButtonClick(ActionEvent actionEvent) {
        String text = MessageInput.getText();
        out.println(text);
        MessageInput.clear();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) { // делаем отправку сообщения по нажатию Enter
            String text = MessageInput.getText();
            out.println(text);
            MessageInput.clear();
        }
    }

    public void onConnectionBtnClick(ActionEvent actionEvent) {
    }

    void readMessage() throws IOException {
        while (!(fromServer = in.readLine()).equals("/q")) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    name_fromServer = fromServer.split(":")[0].trim();
                    message_fromServer = fromServer.split(":")[1].trim();
                    Text text1 = new Text(name_fromServer + ":");
                    Text text2 = new Text(message_fromServer + "\n");
                    text1.setFill(Color.PURPLE);
                    text1.setFont(Font.font(16));
                    text2.setFont(Font.font(16));
                    ChatViewier.getChildren().add(text1);
                    ChatViewier.getChildren().add(text2);
                    scrollPane.setVvalue(1.0);
                    System.out.println(name_fromServer + message_fromServer);
                }
            });
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                break;
            }
        }
        in.close();
        out.close();

}

/*    public void sound() {
       File file = new File("D:\\Programms\\Sound\\sound.mp3");
       Media media = new Media(file.toURI().toString());
       MediaPlayer player = new MediaPlayer(media);

       player.play();
    }
    */

    void runReadMessage() {
        new Thread(() -> {
            try {
                readMessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


}

