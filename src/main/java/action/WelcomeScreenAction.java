package main.java.action;

import javafx.fxml.Initializable;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WelcomeScreenAction implements Initializable {
    @FXML Button addProductButton;

    public void initialize(URL location, ResourceBundle resources) {
        addProductButton.setPrefSize(77,20);
        addActionListeners();

    }
    public void addActionListeners() {
        addProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("../ui/ProductManagementStage.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage closeStage = (Stage) addProductButton.getScene().getWindow();
                closeStage.close();

                Stage addProductStage = new Stage();
                addProductStage.setTitle("Aydinlik University Library Information System-Add Product Window");
                addProductStage.setScene(new Scene(root, 850, 550));
                addProductStage.show();
            }
        });
    }
}
