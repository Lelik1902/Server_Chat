package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import javax.sound.sampled.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;


public class Controller {

    public TextField MessageInput;
    public Button sendButton;
    public TextFlow ChatViewier;
    public ScrollPane scrollPane;

    PrintWriter out;
    BufferedReader in;
    String fromServer;
    Socket socket;
    String receivedMmessage;
    ActionListener taskPerformer;

    public Controller() throws IOException {
        socket = new Socket("localhost", 8456);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        runReadMessage();
    }

    public void onSendButtonClick(ActionEvent actionEvent) {
        try {
            File f = new File("D:\\Programms\\Sound\\sound.mp3");
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        String text = MessageInput.getText();
        out.println(text);
        MessageInput.clear();
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onKeyPress(KeyEvent keyEvent){
        if (keyEvent.getCode().equals(KeyCode.ENTER)) { // делаем отправку сообщения по нажатию Enter
            try {
                File f = new File("D:\\Programms\\Sound\\sound.mp3");
                AudioInputStream ais = AudioSystem.getAudioInputStream(f);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
            String text = MessageInput.getText();
            out.println(text);
            MessageInput.clear();
        }
    }

    void readMessage() throws IOException {
        while (!(fromServer = in.readLine()).equals("kick")) {
            System.out.println("Server: " + fromServer);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Text text = new Text(fromServer + "\n");
                    text.setFill(Color.PURPLE);
                    ChatViewier.getChildren().add(text);
                    scrollPane.setVvalue(1.0);
                }
            });
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                break;
            }
        }
        System.out.println("You have been banned from this server \uD83D\uDE18");

        in.close();
    }

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

