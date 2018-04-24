package main.java.action;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import main.java.repository.ProductInfoRepository;

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
    @FXML ChoiceBox language;
    @FXML TextField numOfPagesField;
    @FXML DatePicker publishDatePicker;
    @FXML ChoiceBox productType;

    @FXML Button addProductButton;
    @FXML Label notification;

    ProductInfoRepository productInfoRepository;
    private String yerId;
    private int placeIncremental;

    @Override
    public void initialize(URL location, ResourceBundle resources) {        this.productInfoRepository = new ProductInfoRepository();
        language.setItems(FXCollections.observableArrayList("Turkish", "English", "German"));
        productType.setItems(FXCollections.observableArrayList("Book", "Journal"));

        yerId = "";
        placeIncremental = 0;
        addActionListeners();
    }
    public void addActionListeners() {
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
    }

    private String getYerId(String type) {
        placeIncremental++;
        return type+placeIncremental;
    }
}
