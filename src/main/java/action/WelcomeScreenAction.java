package main.java.action;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Popup;
import javafx.stage.Stage;
import main.java.beans.Book;
import main.java.beans.Journal;
import main.java.repository.BorrowProductRepository;
import main.java.repository.ProductInfoRepository;
import main.java.repository.ReserveProductRepository;
import main.java.repository.UserInfoRepository;
//import sun.java2d.jules.JulesRenderingEngine;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WelcomeScreenAction implements Initializable {

    @FXML Button addProductButton;
    @FXML Button reserve;
    @FXML Button borrow;
    @FXML Button search;
    @FXML Button exit;
    @FXML Button logoff;
    @FXML RadioButton book;
    @FXML RadioButton journal;

    @FXML Label bookInventoryString;
    @FXML Label journeyInventoryString;

    @FXML TextField productName;

    @FXML TableView table;

    @FXML TableColumn status;
    @FXML TableColumn title;
    @FXML TableColumn author;
    @FXML TableColumn pageCount;
    @FXML TableColumn place;
    @FXML TableColumn publisher;
    @FXML TableColumn publishDate;
    @FXML TableColumn language;
    @FXML TableColumn kategoriIsim;

    private ProductInfoRepository productInfoRepository;
    private Integer isBook;
    private BorrowProductRepository borrowProductRepository;
    private ReserveProductRepository reserveProductRepository;
    private UserInfoRepository userInfoRepository;
    private String userIDorName;
    private Boolean isID;

    public void initialize(URL location, ResourceBundle resources) {

        productInfoRepository = new ProductInfoRepository();
        borrowProductRepository = new BorrowProductRepository();
        reserveProductRepository = new ReserveProductRepository();
        userInfoRepository = new UserInfoRepository();

        journeyInventoryString.setVisible(false);
        ObservableList<Book> data = null;
        isBook = 1;

        rearrangeColumnTypes(true);

        try {
            data = FXCollections.observableArrayList(productInfoRepository.getAllBooks(isBook));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setItems(data);

        ToggleGroup toggleGroup = new ToggleGroup();
        book.setToggleGroup(toggleGroup);
        journal.setToggleGroup(toggleGroup);
        book.setSelected(true);
        addActionListeners();

    }


    public void addActionListeners() {

        addProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(userInfoRepository.checkAuthorization(userIDorName, isID)){
                    Stage closeStage = (Stage) addProductButton.getScene().getWindow();
                    closeStage.close();

                    try {

                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../ui/ProductManagementStage.fxml"));

                        Parent root = (Parent)fxmlLoader.load();
                        ProductManagementAction controller = fxmlLoader.<ProductManagementAction>getController();
                        controller.setUserIdorName(userIDorName);
                        controller.setID(isID);
                        Scene scene = new Scene(root);

                        Stage componentListStage = new Stage();
                        componentListStage.setTitle("Aydinlik University Library Information System - Add Product");
                        componentListStage.setScene(scene);
                        componentListStage.setWidth(850);
                        componentListStage.setHeight(620);
                        componentListStage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    showPopup("Unauthorized!","Only the system manager can add " +
                            "new products to system ..!");
                }

            }
        });

        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) exit.getScene().getWindow();
                stage.close();
            }
        });

        logoff.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage closeStage = (Stage) logoff.getScene().getWindow();
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

        borrow.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                    if(isBook == 1){
                        Book book = ((Book) table.getSelectionModel().getSelectedItem());
                        if(userInfoRepository.checkIfUserAlreadyHave(userIDorName, isID, book.getID())) {
                            if(borrowProductRepository.isProductSuitable(book.getID())){
                                if(borrowProductRepository.isUserSuitable(userIDorName, isID,
                                        book.getKategoriIsim(), true)){

                                    borrowProductRepository.borrow(book.getID(), userIDorName, isID);
                                    showPopup("Information", "Book is borrowed!");
                                    try {
                                        ObservableList<Book> data = null;
                                        data = FXCollections.observableArrayList(productInfoRepository.getAllBooks(isBook));
                                        table.setItems(data);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    if(productInfoRepository.isTextbookReason(userIDorName, isID, book.getKategoriIsim())){
                                        showPopup("Warning!", "Only the instructors can take textbooks." );
                                    }else if(productInfoRepository.isMaxReason(userIDorName, isID)){
                                        showPopup("Warning!", "You can not borrow any more product ..!");
                                    }

                                }
                            }else{
                                showPopup("Warning!", "Product is already taken or reserved. " +
                                        "Please check the status ..!" );
                            }
                        }else {
                            showPopup("Warning!", "You already borrowed or reserved this book ..!");
                        }

                    }else{
                        Journal journal = ((Journal) table.getSelectionModel().getSelectedItem());
                        if(userInfoRepository.checkIfUserAlreadyHave(userIDorName, isID, journal.getID())) {
                            if(borrowProductRepository.isProductSuitable(journal.getID())){
                                if(borrowProductRepository.isUserSuitable(userIDorName, isID,
                                        journal.getKategoriIsim(), true)){
                                    borrowProductRepository.borrow(journal.getID(), userIDorName, isID);
                                    showPopup("Information", "Journal is borrowed!");
                                    try {
                                        ObservableList<Journal> data = null;
                                        data = FXCollections.observableArrayList(productInfoRepository.getAllJournals(isBook));
                                        table.setItems(data);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    if(productInfoRepository.isMaxReason(userIDorName, isID)){
                                        showPopup("Warning!", "You can not borrow any more product ..!");
                                    }
                                }
                            }else{
                                showPopup("Warning!", "Product is already taken or reserved. " +
                                        "Please check the status ..!" );
                            }
                        }else {
                            showPopup("Warning!", "You already borrowed or reserved this journal ..!");
                        }
                    }
            }
        });

        reserve.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(isBook == 1){
                    Book book = ((Book) table.getSelectionModel().getSelectedItem());
                    Integer status = reserveProductRepository.getStatus(book.getID());

                    if(userInfoRepository.checkIfUserAlreadyHave(userIDorName, isID, book.getID())){
                        if(status == 1){

                            if(reserveProductRepository.isUserSuitable(userIDorName, isID,
                                    book.getKategoriIsim(), true)){

                                reserveProductRepository.reserve(userIDorName, isID, book.getID());
                                showPopup("Information", "Product is reserved.");
                                try {
                                    ObservableList<Book> data = null;
                                    data = FXCollections.observableArrayList(productInfoRepository.getAllBooks(isBook));
                                    table.setItems(data);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                if(reserveProductRepository.isMaxReason(userIDorName, isID)){
                                    showPopup("Warning!", "You can not borrow or reserve" +
                                            " any more product ..!");
                                }else if(reserveProductRepository.isTextbookReason(userIDorName,
                                        isID, book.getKategoriIsim())){

                                    showPopup("Warning!", "Only the instructors can take textbooks." );
                                }
                            }

                        }else{
                            if(status == 2){
                                showPopup("Warning!", "This product has been already " +
                                        "reserved ..!");
                            }else if(status == 0){
                                showPopup("Information", "This product is alreay suitable " +
                                        "to borrow. No need to reserve it.");
                            }
                        }
                    }else{
                        showPopup("Warning!","You already borrowed or reserved this book ..!");
                    }

                }else{
                    Journal journal = ((Journal) table.getSelectionModel().getSelectedItem());
                    Integer status = reserveProductRepository.getStatus(journal.getID());

                    if(userInfoRepository.checkIfUserAlreadyHave(userIDorName, isID, journal.getID())){
                        if(status == 1){

                            if(reserveProductRepository.isUserSuitable(userIDorName, isID,
                                    journal.getKategoriIsim(), true)){

                                reserveProductRepository.reserve(userIDorName, isID, journal.getID());
                                showPopup("Information", "Product is reserved.");
                                try {
                                    ObservableList<Journal> data = null;
                                    data = FXCollections.observableArrayList(productInfoRepository.getAllJournals(isBook));
                                    table.setItems(data);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                if(reserveProductRepository.isMaxReason(userIDorName, isID)){
                                    showPopup("Warning!", "You can not borrow or reserve" +
                                            " any more product ..!");
                                }
                            }

                        }else{

                            if(status == 2){
                                showPopup("Warning!", "This product has been already " +
                                        "reserved ..!");
                            }else if(status == 0){
                                showPopup("Information", "This product is alreay suitable " +
                                        "to borrow. No need to reserve it.");
                            }

                        }
                    }else{
                        showPopup("Warning!","You already borrowed or reserved this journal ..!");
                    }

                }

            }
        });

        book.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try{
                    isBook = 1;
                    author.setVisible(true);
                    rearrangeColumnWidths(1);
                    bookInventoryString.setVisible(true);
                    journeyInventoryString.setVisible(false);
                    rearrangeColumnTypes(true);
                    ObservableList<Book> data = null;
                    data = FXCollections.observableArrayList(productInfoRepository.getAllBooks(isBook));
                    table.setItems(data);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        journal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                try{
                    isBook = 2;
                    author.setVisible(false);
                    rearrangeColumnWidths(2);
                    bookInventoryString.setVisible(false);
                    journeyInventoryString.setVisible(true);
                    rearrangeColumnTypes(false);
                    ObservableList<Journal> data = null;
                    data = FXCollections.observableArrayList(productInfoRepository.getAllJournals(isBook));
                    table.setItems(data);
                }catch (SQLException e){
                    e.printStackTrace();
                }

            }
        });

        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String value = productName.getText();
                if(value == null || value.length() < 2){
                    try {
                        if(isBook == 1){
                            ObservableList<Book> data = null;
                            data = FXCollections.observableArrayList(productInfoRepository.getAllBooks(isBook));
                            table.setItems(data);
                        }else{
                            ObservableList<Journal> data = null;
                            data = FXCollections.observableArrayList(productInfoRepository.getAllJournals(isBook));
                            table.setItems(data);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    if(isBook == 1){
                        ObservableList<Book> data = null;
                        data = FXCollections.observableArrayList(productInfoRepository.getSearchedBooks(value,isBook));
                        table.setItems(data);
                    }else{
                        ObservableList<Journal> data = null;
                        data = FXCollections.observableArrayList(productInfoRepository.getSearchedJournals(value, isBook));
                        table.setItems(data);
                    }
                }
            }
        });
    }

    public String getUserIDorName() {
        return userIDorName;
    }

    public void setUserIDorName(String userIDorName) {
        this.userIDorName = userIDorName;
    }

    public Boolean getID() {
        return isID;
    }

    public void setID(Boolean ID) {
        isID = ID;
    }

    public void rearrangeColumnWidths(int type){
        Integer width = 125;

        if(type == 2){
            width = 140;
        }

        status.setPrefWidth(width);
        title.setPrefWidth(width);
        author.setPrefWidth(width);
        pageCount.setPrefWidth(width);
        place.setPrefWidth(width);
        publisher.setPrefWidth(width);
        publishDate.setPrefWidth(width);
        language.setPrefWidth(width);
        kategoriIsim.setPrefWidth(width);

    }

    public void rearrangeColumnTypes(boolean isBook){
        if(isBook){
            title.setCellValueFactory(
                    new PropertyValueFactory<Book,String>("Title")
            );

            author.setCellValueFactory(
                    new PropertyValueFactory<Book,String>("Author"));

            kategoriIsim.setCellValueFactory(
                    new PropertyValueFactory<Book,String>("kategoriIsim")
            );

            publisher.setCellValueFactory(
                    new PropertyValueFactory<Book, String>("Publisher")
            );

            publishDate.setCellValueFactory(
                    new PropertyValueFactory<Book,String>("PublishDate")
            );

            language.setCellValueFactory(
                    new PropertyValueFactory<Book,String>("Language")
            );

            pageCount.setCellValueFactory(
                    new PropertyValueFactory<Book,Integer>("PageCount")
            );

            place.setCellValueFactory(
                    new PropertyValueFactory<Book,String>("Place")
            );

            status.setCellValueFactory(
                    new PropertyValueFactory<Book,String>("Status")
            );
        }else{
            title.setCellValueFactory(
                    new PropertyValueFactory<Journal,String>("Title")
            );

            kategoriIsim.setCellValueFactory(
                    new PropertyValueFactory<Journal,String>("kategoriIsim")
            );

            author.setCellValueFactory(
                    new PropertyValueFactory<Journal, String>("Publisher")
            );

            publisher.setCellValueFactory(
                    new PropertyValueFactory<Journal, String>("Publisher")
            );

            publishDate.setCellValueFactory(
                    new PropertyValueFactory<Journal,String>("PublishDate")
            );

            language.setCellValueFactory(
                    new PropertyValueFactory<Journal,String>("Language")
            );

            pageCount.setCellValueFactory(
                    new PropertyValueFactory<Journal,Integer>("PageCount")
            );

            place.setCellValueFactory(
                    new PropertyValueFactory<Journal,String>("Place")
            );

            status.setCellValueFactory(
                    new PropertyValueFactory<Journal,String>("Status")
            );
        }
    }

    public void showPopup(String title, String msg){
        Stage newStage = new Stage();
        newStage.setTitle(title);
        final Popup popup = new Popup(); popup.setX(300); popup.setY(200);
        popup.getContent().addAll(new Circle(25, 25, 50, Color.WHITE));

        Button show = new Button("Ok");
        show.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent event) {
                Stage closeStage = (Stage) show.getScene().getWindow();
                closeStage.close();
            }
        });

        Label message = new Label(msg);

        HBox layout = new HBox(10);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
        layout.getChildren().addAll(message, show);

        newStage.setScene(new Scene(layout));
        newStage.show();
    }
}
