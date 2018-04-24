package main.java.action;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AboutAction implements Initializable {

    @FXML Text aboutString;

    @FXML Button back;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        aboutString.setText("Library Information System of Aydinlik University is developed" +
                " as a\nterm project for the Software Engineering lesson in Yildiz Technical\n" +
                "University by 3rd grade students (without considering any order)\n\n" +
                "  - Erkan Karabulut\n" +
                "  - Mürüvvet Hasanbaşoğlu\n" +
                "  - Canan Sayar\n" +
                "  - Miray Aygüzel");

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try{

                    Stage closeStage = (Stage) back.getScene().getWindow();
                    closeStage.close();

                    Parent root = FXMLLoader.load(getClass().
                            getResource("../ui/LoginStage.fxml"));
                    Stage componentListStage = new Stage();
                    componentListStage.setTitle("Aydinlik University Library Information " +
                            "System - About");
                    componentListStage.setScene(new Scene(root, 500, 500));
                    componentListStage.show();

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });

    }
}
