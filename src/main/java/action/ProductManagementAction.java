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
import javafx.stage.Stage;
import main.java.repository.ProductInfoRepository;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;

public class ProductManagementAction implements Initializable {

    @FXML TextField isbnField;
    @FXML TextField titleField;
    @FXML TextField authorField;
    @FXML TextField publisherField;
    @FXML TextField numOfPagesField;

    @FXML ChoiceBox language;
    @FXML ChoiceBox productType;
    @FXML ChoiceBox category;

    @FXML DatePicker publishDatePicker;

    @FXML Button addProductButton;
    @FXML Button back;
    @FXML Button clear;

    @FXML Label notification;

    ProductInfoRepository productInfoRepository;
    private String yerId;
    private int placeIncremental;
    private int productTypeInt;
    private String userIdorName;
    private boolean isID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.productInfoRepository = new ProductInfoRepository();
        language.setItems(FXCollections.observableArrayList("Turkish", "English", "German"));
        productType.setItems(FXCollections.observableArrayList("Book", "Journal"));

        yerId = "";
        placeIncremental = 0;
        addActionListeners();
    }

    public void addActionListeners() {

        productType.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                productTypeInt = productType.getSelectionModel().getSelectedIndex() + 1;
                category.setItems(FXCollections.observableArrayList
                        (productInfoRepository.getCategories(productTypeInt)));
            }
        });

        publishDatePicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalDate date = publishDatePicker.getValue();
            }
        });

        addProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String isbn = isbnField.getText();
                String title = titleField.getText();
                String author = authorField.getText();
                String publisher = publisherField.getText();
                String languageValue = "";
                String numOfPages = numOfPagesField.getText();
                LocalDate date = publishDatePicker.getValue();
                String categoryName = null;
                if(category.getSelectionModel().getSelectedItem() != null){
                    categoryName = category.getSelectionModel().getSelectedItem().toString();
                }
                String publishDate = "";
                String productTypeValue;

                if(language.getSelectionModel().getSelectedItem() != null){
                    languageValue = language.getSelectionModel().getSelectedItem().toString();
                }
                if(date!=null) {
                    publishDate = date.toString();
                }
                if(productType.getSelectionModel().getSelectedItem() != null){
                    productTypeValue = productType.getSelectionModel().getSelectedItem().toString();
                    if(!isbn.isEmpty()) {
                        LinkedHashMap<String, String> productInfos = new LinkedHashMap<>();
                        productInfos.put("yerId", getYerId(productTypeValue));
                        productInfos.put("type", productTypeValue);
                        productInfos.put("isbn", isbn);
                        productInfos.put("title", title);
                        productInfos.put("author", author);
                        productInfos.put("publisher", publisher);
                        productInfos.put("language", languageValue);
                        productInfos.put("numOfPages", numOfPages);
                        productInfos.put("publishDate", publishDate);
                        productInfos.put("categoryName", categoryName);

                        String result = "";
                        try {
                            result = productInfoRepository.addProduct(productInfos);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if(result.contains("Ok")){
                            notification.setText("Product successfully added!");
                            isbnField.setText("");
                            titleField.setText("");
                            authorField.setText("");
                            publisherField.setText("");
                            language.getSelectionModel().clearSelection();
                            numOfPagesField.setText("");
                            publishDatePicker.setValue(null);
                            category.getSelectionModel().clearSelection();
                            productType.getSelectionModel().clearSelection();

                        } else{
                            notification.setText(result);
                        }
                    } else {
                        notification.setText("At least ISBN must be specified!");
                    }
                } else {
                    notification.setText("Product Type must be picked!");
                }
            }
        });

        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                isbnField.setText("");
                titleField.setText("");
                authorField.setText("");
                publisherField.setText("");
                numOfPagesField.setText("");
                language.getSelectionModel().clearSelection();
                productType.getSelectionModel().clearSelection();
                publishDatePicker.setValue(null);

            }
        });

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                Stage closeStage = (Stage) addProductButton.getScene().getWindow();
                closeStage.close();

                try {

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/ComponentListStage.fxml"));

                    Parent root = (Parent)fxmlLoader.load();
                    WelcomeScreenAction controller = fxmlLoader.<WelcomeScreenAction>getController();
                    controller.setUserIDorName(userIdorName);
                    controller.setID(isID);
                    Scene scene = new Scene(root);

                    Stage componentListStage = new Stage();
                    componentListStage.setTitle("Aydinlik University Library Information System - Welcome");
                    componentListStage.setScene(scene);
                    componentListStage.setWidth(1320);
                    componentListStage.setHeight(740);
                    componentListStage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private String getYerId(String type) {
        placeIncremental++;
        return type+placeIncremental;
    }

    public int getProductTypeInt() {
        return productTypeInt;
    }

    public void setProductTypeInt(int productTypeInt) {
        this.productTypeInt = productTypeInt;
    }

    public String getUserIdorName() {
        return userIdorName;
    }

    public void setUserIdorName(String userIdorName) {
        this.userIdorName = userIdorName;
    }

    public boolean isID() {
        return isID;
    }

    public void setID(boolean ID) {
        isID = ID;
    }
}
