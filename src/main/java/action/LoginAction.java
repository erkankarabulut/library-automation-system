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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import main.java.repository.UserInfoRepository;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class LoginAction implements Initializable{

    @FXML TextField usernameIDField;
    @FXML TextField passwordField;

    @FXML Label name;
    @FXML Label loginErrorMessage;
    @FXML Label passwordLabel;
    @FXML Label usernameIdLabel;

    @FXML ImageView logo;

    @FXML Button loginButton;
    @FXML Button signUpButton;

    @FXML RadioButton loginWithID;
    @FXML RadioButton loginWithUsername;

    @FXML TextFlow forgotPassword;
    @FXML TextFlow help;
    @FXML TextFlow about;
    @FXML TextFlow exit;

    Hyperlink linkToChangePsw;
    Hyperlink exitLink;
    Hyperlink aboutLink;
    Hyperlink helpLink;
    FXMLLoader fxmlLoader;

    ToggleGroup toggleGroup;
    Boolean     idLogin;

    public UserInfoRepository userInfoRepository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userInfoRepository = new UserInfoRepository();
        this.name.setText("Library Information System \nof Aydinlik University");

        loginButton.setPrefSize(77,20);
        signUpButton.setPrefSize(77,20);

        toggleGroup = new ToggleGroup();
        loginWithID.setToggleGroup(toggleGroup);
        loginWithUsername.setToggleGroup(toggleGroup);
        loginWithUsername.setSelected(true);

        linkToChangePsw = new Hyperlink("Forgot your password?");
        exitLink = new Hyperlink("Exit");
        helpLink = new Hyperlink("Help");
        aboutLink = new Hyperlink("About");

        setTextActions();
        idLogin = false;

        forgotPassword.getChildren().add(linkToChangePsw);
        about.getChildren().add(aboutLink);
        exit.getChildren().add(exitLink);
        help.getChildren().add(helpLink);

        addActionListeners();
    }

    public WelcomeScreenAction getController() {
        return (WelcomeScreenAction) fxmlLoader.getController();
    }

    public void addActionListeners(){
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String usernameOrID = usernameIDField.getText();
                String userPassword = passwordField.getText();

                try {
                    if(userInfoRepository.checkUser(usernameOrID,userPassword,idLogin)){

                        Stage closeStage = (Stage) loginWithUsername.getScene().getWindow();
                        closeStage.close();


                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/ComponentListStage.fxml"));

                        Parent root = (Parent)fxmlLoader.load();
                        WelcomeScreenAction controller = fxmlLoader.getController();
                        controller.setUserIDorName(usernameOrID);
                        controller.setID(idLogin);
                        Scene scene = new Scene(root);

                        Stage componentListStage = new Stage();
                        componentListStage.setTitle("Aydinlik University Library Information System");
                        componentListStage.setScene(scene);
                        componentListStage.setWidth(1320);
                        componentListStage.setHeight(740);
                        componentListStage.show();

                    }else{
                        if(idLogin){
                            loginErrorMessage.setText("Invalid Card ID ..!");
                        }else{
                            loginErrorMessage.setText("Username or password is incorrect ..!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage closeStage = (Stage) signUpButton.getScene().getWindow();
                closeStage.close();

                try {

                    Parent root = FXMLLoader.load(getClass().getResource("../ui/SignUpStage.fxml"));
                    Stage componentListStage = new Stage();
                    componentListStage.setTitle("Aydinlik University Library Information System");
                    componentListStage.setScene(new Scene(root, 850, 550));
                    componentListStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        loginWithUsername.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                passwordField.setVisible(true);
                passwordLabel.setVisible(true);

                usernameIdLabel.setText("Username: ");
                usernameIDField.setText("");
                passwordField.setText("");
                idLogin = false;
            }
        });

        loginWithID.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                passwordLabel.setVisible(false);
                passwordField.setVisible(false);
                usernameIdLabel.setText("ID: ");
                usernameIDField.setText("");
                passwordField.setText("");
                idLogin = true;
            }
        });

    }

    public void setTextActions(){

        linkToChangePsw.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {

                try {

                    Stage closeStage = (Stage) loginWithUsername.getScene().getWindow();
                    closeStage.close();

                    Parent root = FXMLLoader.load(getClass().
                            getResource("../ui/ChangePasswordStage.fxml"));
                    Stage componentListStage = new Stage();
                    componentListStage.setTitle("Aydinlik University Library " +
                            "Information System - Change Password");
                    componentListStage.setScene(new Scene(root, 500, 400));
                    componentListStage.show();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        exitLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage closeStage = (Stage) loginWithUsername.getScene().getWindow();
                closeStage.close();

            }
        });

        aboutLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage closeStage = (Stage) loginWithUsername.getScene().getWindow();
                closeStage.close();

                try{

                    Parent root = FXMLLoader.load(getClass().
                            getResource("../ui/AboutStage.fxml"));
                    Stage componentListStage = new Stage();
                    componentListStage.setTitle("Aydinlik University Library " +
                            "Information System - About");
                    componentListStage.setScene(new Scene(root, 500, 400));
                    componentListStage.show();

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });

        helpLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage closeStage = (Stage) loginWithUsername.getScene().getWindow();
                closeStage.close();

                try{

                    Parent root = FXMLLoader.load(getClass().
                            getResource("../ui/HelpStage.fxml"));
                    Stage componentListStage = new Stage();
                    componentListStage.setTitle("Aydinlik University Library " +
                            "Information System - Help");
                    componentListStage.setScene(new Scene(root, 500, 400));
                    componentListStage.show();

                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        });

    }
}
