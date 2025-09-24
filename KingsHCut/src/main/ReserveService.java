package main;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Connect;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ServiceTable;

public class ReserveService extends Application {

    BorderPane bpane;
    GridPane gpane;
    Label titleLabel, dateLabel, timeLabel, serviceLabel, reserveListLabel, userLabel;
    DatePicker datePicker;
    CheckBox hairCut, hairTreatment, hairPerming, hairColoring, hairTattoo;
    TextField timeField;
    TableView<ServiceTable> serviceTable;
    TableColumn<ServiceTable, String> idColumn, nameColumn;
    TableColumn<ServiceTable, Integer> priceColumn, durationColumn;
    ListView<ServiceTable> reserveList;
    Button addButton, cancelButton, reserveButton;
    String username;
    VBox vbox1, vbox2;
    HBox checkbox;
    MenuBar userMenuBar;
    Menu menu;
	MenuItem reserveService, customerReservation, logOut;


    public ReserveService() {
        this.username = username;
    }
    
    public ReserveService(String username) {
    	this.username = (username == null || username.isEmpty()) ? "null" : username;
    }

    private void initialize() {
        bpane = new BorderPane();
        gpane = new GridPane();
        vbox1 = new VBox();
        vbox2 = new VBox();
        checkbox = new HBox();
        
        userMenuBar = new MenuBar();
        menu = new Menu("Menu");
        reserveService = new MenuItem("Reserve Service");
        customerReservation = new MenuItem("Customer Reservation");
        logOut = new MenuItem("Log Out");
        menu.getItems().addAll(reserveService, customerReservation, logOut);
        userMenuBar.getMenus().add(menu);

        hairCut = new CheckBox("Haircut");
		hairTreatment = new CheckBox("Hair Treatment");
		hairPerming = new CheckBox("Hair Perming");
		hairColoring = new CheckBox("Hair Coloring");
		hairTattoo = new CheckBox("Hair Tattoo");
        
        titleLabel = new Label("Reserve Service");
        userLabel = new Label("User: Tester");
        
        dateLabel = new Label("Reservation Date:");
        timeLabel = new Label("Start Time (hh:mm):");
        serviceLabel = new Label("Available Services:");
        reserveListLabel = new Label("Reserve List:");
                
        idColumn = new TableColumn<>("ID");
        nameColumn = new TableColumn<>("Name");
        priceColumn = new TableColumn<>("Price");
        durationColumn = new TableColumn<>("Duration");
        
        datePicker = new DatePicker();
        timeLabel = new Label("Reservation Time");
        timeField = new TextField();
        timeField.setPromptText("hh:mm");

        serviceTable = new TableView<>();
        reserveList = new ListView<>();
        
        
        serviceTable.setPlaceholder(new Label("No services available"));

        
        TableColumn<ServiceTable, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn<ServiceTable, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn<ServiceTable, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<ServiceTable, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        
        serviceTable.getColumns().addAll(idColumn, nameColumn, priceColumn, durationColumn);
        serviceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		serviceTable.setPrefHeight(350);


        addButton = new Button("Add");
        cancelButton = new Button("Cancel");
        reserveButton = new Button("Reserve");
    }

    private void layout() {
        gpane.setVgap(10);
        gpane.setHgap(10);
        gpane.setAlignment(Pos.CENTER);
        gpane.setPadding(new Insets(10));

        gpane.add(titleLabel, 0, 0, 2, 1);
        gpane.setHalignment(titleLabel, HPos.LEFT);
        gpane.add(userLabel, 0, 1);
        
        checkbox.getChildren().addAll(hairCut, hairTreatment, hairPerming, hairColoring, hairTattoo);
        checkbox.setPadding(new Insets(7));
        hairCut.setStyle("-fx-font-size: 10px;");
        hairTreatment.setStyle("-fx-font-size: 10px;");
        hairPerming.setStyle("-fx-font-size: 10px;");
        hairColoring.setStyle("-fx-font-size: 10px;");
        hairTattoo.setStyle("-fx-font-size: 10px;");
        
        reserveListLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        userLabel.setStyle("-fx-font-size: 16px;");
        reserveList.setPrefSize(300, 100);
        
        datePicker.setPrefWidth(300);
        
        addButton.setPrefSize(300, 40);
        cancelButton.setPrefSize(300, 40);
        reserveButton.setPrefSize(300, 40);
        
        vbox2.setPadding(new Insets(5));
        vbox2.setSpacing(5);
        
        vbox1.getChildren().addAll(checkbox, serviceTable);
        vbox2.getChildren().addAll(reserveListLabel, reserveList, dateLabel, datePicker, timeLabel, timeField, addButton, cancelButton, reserveButton);
        gpane.add(vbox1, 0, 3);
        gpane.add(vbox2, 1, 3);
        bpane.setTop(userMenuBar);
        bpane.setCenter(gpane);
    }

    private void eventHandler(Stage primaryStage) {
        addButton.setOnAction(e -> handleAddService());
        cancelButton.setOnAction(e -> handleCancelService());
        reserveButton.setOnAction(e -> handleReservation());
        reserveService.setOnAction(e -> navigateToPage(new ReserveService(username), primaryStage));
        customerReservation.setOnAction(e -> navigateToPage(new ReservationManagement(username), primaryStage));
        logOut.setOnAction(e -> navigateToPage(new Logins(), primaryStage));
    }

    private void handleAddService() {
        ServiceTable selectedService = serviceTable.getSelectionModel().getSelectedItem();
        if (selectedService != null && !reserveList.getItems().contains(selectedService)) {
            reserveList.getItems().add(selectedService);
            showAlert(Alert.AlertType.INFORMATION, "Service added successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Please select a valid service.");
        }
    }

    private void handleCancelService() {
        ServiceTable selectedService = reserveList.getSelectionModel().getSelectedItem();
        if (selectedService != null) {
            reserveList.getItems().remove(selectedService);
            showAlert(Alert.AlertType.INFORMATION, "Service removed successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "No service selected to remove.");
        }
    }

    private void handleReservation() {
        if (datePicker.getValue() == null || timeField.getText().isEmpty() || reserveList.getItems().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please fill all the required fields.");
        } else {
            showAlert(Alert.AlertType.INFORMATION, "Reservation confirmed successfully!");
            reserveList.getItems().clear();
        }
    }

    private void navigateToPage(Application page, Stage primaryStage) {
        try {
            page.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }

    
    public class ReserveConfirmationPopup {

        public void display(Stage ownerStage, Runnable onConfirm) {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(ownerStage);
            popupStage.setTitle("Reserve Confirmation");

            Label confirmationLabel = new Label("Do you want to confirm the reservation?");
            Button yesButton = new Button("Yes");
            Button noButton = new Button("No");

            yesButton.setOnAction(e -> {
                onConfirm.run();
                popupStage.close();
            });

            noButton.setOnAction(e -> popupStage.close());

            VBox layout = new VBox(10);
            layout.getChildren().addAll(confirmationLabel, yesButton, noButton);
            layout.setAlignment(javafx.geometry.Pos.CENTER);

            Scene scene = new Scene(layout, 300, 150);
            popupStage.setScene(scene);
            popupStage.showAndWait();
        }
    }
    
    private void loadDatabase() {
		ResultSet rs = Connect.getConnection().executeQuery("SELECT * FROM msservice");
		try {
			while (rs.next()) {
				String id = rs.getString("ServiceID");
				String name = rs.getString("ServiceName");
				Integer price = rs.getInt("ServicePrice");
				Integer duration = rs.getInt("ServiceDuration");
				serviceTable.getItems().add(new ServiceTable(id, name, price, duration));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    @Override
    public void start(Stage primaryStage) {
        initialize();
        layout();
        eventHandler(primaryStage);

        Scene scene = new Scene(bpane, 700, 500);
        primaryStage.setTitle("Reserve Service");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
