package main.java.action;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.repository.UserInfoRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class SignUpAction implements Initializable{

    @FXML TextField pswField;
    @FXML TextField pswField2;
    @FXML TextField idField;
    @FXML TextField nameField;
    @FXML TextField surnameField;
    @FXML TextField telField;
    @FXML TextField username;
    @FXML TextField emailField;

    @FXML AnchorPane anchorPane;
    @FXML ChoiceBox gender;
    @FXML ChoiceBox userType;

    @FXML Button clearButton;
    @FXML Button registerButton;
    @FXML Button backButton;

    @FXML Label registerErrorMessage;

    UserInfoRepository userInfoRepository;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userInfoRepository = new UserInfoRepository();
        userType.setItems(FXCollections.observableArrayList("Student", "Instructor", "Officer"));
        gender.setItems(FXCollections.observableArrayList("Male", "Female"));

        clearButton.setPrefSize(80, 20);
        registerButton.setPrefSize(80,20);
        backButton.setPrefSize(80,20);

        //back.setVisible(false);

        addActionListeners();
    }

    public void addActionListeners(){
        registerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) throws NullPointerException {
                String email = emailField.getText();
                String psw = pswField.getText();
                String psw2 = pswField2.getText();
                String id = idField.getText();
                String type = "";
                String usernameField = username.getText();

                if(userType.getSelectionModel().getSelectedItem() != null){
                    type = userType.getSelectionModel().getSelectedItem().toString();
                }

                if(email.length() > 0 && psw.length() > 0 && psw2.length() > 0 &&
                        id.length() > 0 && type.length() > 0 && usernameField.length() > 0){
                    if(email.contains("@") && email.contains(".")){
                        if(psw.equals(psw2)){
                            String name = nameField.getText();
                            String surname = surnameField.getText();
                            String tel = telField.getText();
                            String genderValue = "";
                            if(gender.getSelectionModel().getSelectedItem() != null){
                                genderValue = gender.getSelectionModel().getSelectedItem().toString();
                            }
                            HashMap<String, String> userInfos = new HashMap<>();

                            userInfos.put("username", usernameField);
                            userInfos.put("email", email);
                            userInfos.put("password", psw);
                            userInfos.put("usertype", type);
                            userInfos.put("name", name);
                            userInfos.put("surname", surname);
                            userInfos.put("tel", tel);
                            userInfos.put("gender", genderValue);
                            userInfos.put("id", id);

                            String result = "";
                            try {
                                result = userInfoRepository.registerUser(userInfos);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            if(result.contains("Ok")){
                                //goLoginPageButton.setVisible(true);
                                registerErrorMessage.setText("You are successfully registered!\nClick on the back button to go to the login page ...");
                            } else{
                                registerErrorMessage.setText(result);
                            }
                        }else{
                            registerErrorMessage.setText("Warning: Passwords are not equal ..!");
                        }
                    }else{
                        registerErrorMessage.setText("Warning: Please enter a valid email ..!");
                    }
                }else{
                    registerErrorMessage.setText("Warning: Please fill all the necessary fields ..!");
                }
            }
        });

        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                pswField.setText("");
                pswField2.setText("");
                idField.setText("");
                nameField.setText("");
                surnameField.setText("");
                telField.setText("");
                username.setText("");
                emailField.setText("");

                gender.getSelectionModel().clearSelection();
                userType.getSelectionModel().clearSelection();

            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {

                    Stage closeStage = (Stage) backButton.getScene().getWindow();
                    closeStage.close();

                    Parent root = FXMLLoader.load(getClass().getResource("../ui/LoginStage.fxml"));
                    Stage componentListStage = new Stage();
                    componentListStage.setTitle("Aydinlik University Library Information System");
                    componentListStage.setScene(new Scene(root, 500, 500));
                    componentListStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
