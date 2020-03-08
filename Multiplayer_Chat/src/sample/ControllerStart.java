package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class ControllerStart {

    public TextField HostTxt;
    public TextField PortTxt;
    public Button connectBtn;

    public void onConnectButtonClicked(ActionEvent actionEvent) throws IOException {
        Controller.host = HostTxt.getText();
        Controller.port = Integer.parseInt(PortTxt.getText());
        Parent secondaryWindow = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene secondScene = new Scene(secondaryWindow);
        Stage newWindow = new Stage();
        newWindow.setTitle("Hello World");
        newWindow.setScene(secondScene);
        newWindow.sizeToScene();
        newWindow.show();

        Stage stage = (Stage) connectBtn.getScene().getWindow();
        stage.close();
    }
}
