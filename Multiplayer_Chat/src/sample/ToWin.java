package sample;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;

public class ToWin extends Application {
    private String contentText;
    private Parent root;
    private FadeTransition fade;
    private Stage thisStage;

    private final double WIDTH = 400, HEIGHT = 100;

    ToWin(String contentText) {
        this.contentText = contentText;
    }

    void show() {
        try {
            init();
            start(new Stage(StageStyle.TRANSPARENT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() throws Exception {
        super.init();
        root = new Group(new StackPane(new Rectangle(WIDTH, HEIGHT,
                new Color(1, 0.7, 0.1, .9)),
                new Label(contentText)));
        fade = new FadeTransition(Duration.seconds(2), root);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        thisStage = primaryStage;
        thisStage.setAlwaysOnTop(true);
        thisStage.setScene(new Scene(root, Color.TRANSPARENT));
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        thisStage.setX(size.getWidth() - WIDTH - 0);
        thisStage.setY(size.getHeight() - HEIGHT - 0);
        thisStage.show();

        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        Thread thread = new Thread(() -> {
            try{
                Thread.sleep(1000);
            } catch (InterruptedException ignored) { }
            try{
                stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        fade.setOnFinished(event -> thread.start());
    }

    @Override
    public void stop(){
        fade.setFromValue(.9);
        fade.setToValue(0);
        fade.play();
        fade.setOnFinished(event -> thisStage.close());
    }
}
