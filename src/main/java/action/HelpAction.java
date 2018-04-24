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

public class HelpAction implements Initializable {

    @FXML Text helpString;

    @FXML Button back;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        helpString.setText("Library Information System of Aydinlik University is designed\n" +
                "for managing the donation, borrowing and reservation processes of \nbook, " +
                "journel and DVD. To achieve these processes, after sign up \nand login, " +
                "you need to follow the instructions.");

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
                            "System - Help");
                    componentListStage.setScene(new Scene(root, 500, 500));
                    componentListStage.show();

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });

    }
}
