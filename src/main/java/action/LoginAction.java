package main.java.action;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.repository.UserInfoRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LoginAction implements Initializable{

    @FXML TextField usernameIDField;
    @FXML TextField passwordField;

    @FXML Label name;

    @FXML ImageView logo;
    @FXML AnchorPane anchorPane;

    @FXML Button loginButton;
    @FXML Button signUpButton;

    public UserInfoRepository userInfoRepository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userInfoRepository = new UserInfoRepository();
        this.name.setText("Library Information System \nof Aydinlik University");

        loginButton.setPrefSize(77,20);
        signUpButton.setPrefSize(77,20);

        addActionListeners();
    }

    public void addActionListeners(){
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String usernameOrID = usernameIDField.getText();
                String userPassword = passwordField.getText();

                try {
                    if(userInfoRepository.checkUser(usernameOrID,userPassword)){
                        Parent root = FXMLLoader.load(getClass().getResource("../ui/ComponentListStage.fxml"));
                        Stage componentListStage = new Stage();
                        componentListStage.setTitle("Aydinlik University Library Information System");
                        componentListStage.setScene(new Scene(root, 500, 400));
                        componentListStage.show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("../ui/SignUpStage.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage closeStage = (Stage) signUpButton.getScene().getWindow();
                closeStage.close();

                Stage componentListStage = new Stage();
                componentListStage.setTitle("Aydinlik University Library Information System");
                componentListStage.setScene(new Scene(root, 850, 550));
                componentListStage.show();
            }
        });


    }
}
