package main.java.action;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.repository.UserInfoRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ChangePasswordAction implements Initializable {

    @FXML TextField usernameField;
    @FXML TextField idField;
    @FXML PasswordField pswField;
    @FXML PasswordField pswField2;

    @FXML Label errorMessage;

    @FXML Button changePsw;
    @FXML Button loginPage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginPage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage closeStage = (Stage) loginPage.getScene().getWindow();
                closeStage.close();

                try {

                    Parent root = FXMLLoader.load(getClass().getResource("../ui/LoginStage.fxml"));
                    Stage loginStage = new Stage();
                    loginStage.setTitle("Aydinlik University Library Information System");
                    loginStage.setScene(new Scene(root, 500, 500));
                    loginStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        changePsw.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String username = usernameField.getText();
                String id       = idField.getText();
                String psw      = pswField.getText();
                String psw2     = pswField2.getText();

                if(username.length() > 0 && id.length() > 0 && psw .length() > 0 && psw2.length() > 0){

                    UserInfoRepository userInfoRepository = new UserInfoRepository();
                    if(psw.equals(psw2)){

                        if(userInfoRepository.checkUsernameId(username, id)){
                            userInfoRepository.changePassword(id, psw);
                            errorMessage.setText("Password is changed successfully!\n" +
                                    "Click the button at the right side to go to login page.");
                            loginPage.setVisible(true);
                        }

                    }else{
                        errorMessage.setText("Warning: Passwords does not match ..!");
                    }

                }else{

                    errorMessage.setText("Warning: Please fill all the fields in the form ..!");

                }

            }
        });

    }
}
