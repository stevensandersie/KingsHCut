package main;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ReservationManagementModel;

public class ReservationManagement extends Application {

	BorderPane bpane;
	GridPane gpane;
	VBox vbox1, vbox2;
	HBox hbox;
	Label testerLabel, reserveLabel, idLabel, dateLabel, startLabel, endLabel, statusLabel;
	Button cancelButton;
	MenuBar userMenuBar;
	Menu menu;
	MenuItem reserveService, customerReservation, logOut;
	String username;

	TableView<ReservationManagementModel> reservationManagementTable;
		
	public ReservationManagement() {
		this.username = "KingsHcut";
	}

	public ReservationManagement(String username) {
		this.username = (username == null || username.isEmpty() ? "null" : username);
	}

	private void initialize() {
		bpane = new BorderPane();
		gpane = new GridPane();

		vbox1 = new VBox();
		vbox2 = new VBox();
		
		hbox = new HBox();

		userMenuBar = new MenuBar();
		menu = new Menu("Menu");
		reserveService = new MenuItem("Reserve Service");
		customerReservation = new MenuItem("Customer Reservation");
		logOut = new MenuItem("Log Out");
		menu.getItems().addAll(reserveService, customerReservation, logOut);
		userMenuBar.getMenus().add(menu);

		testerLabel = new Label("tester's Reservation");
		reserveLabel = new Label("Reservation History");
		idLabel = new Label("ID: ");
		dateLabel = new Label("Date: ");
		startLabel = new Label("Start Time: ");
		endLabel = new Label("End Time: ");
		statusLabel = new Label("Status: ");

		cancelButton = new Button("Cancel");

		reservationManagementTable = new TableView<>();
		
		reservationManagementTable.getItems().addAll(
				new ReservationManagementModel("RS015", Date.valueOf("2024-06-28"), Time.valueOf("12:00:00"), Time.valueOf("13:30:00"), "In Progress"),
                new ReservationManagementModel("RS016", Date.valueOf("2024-06-28"), Time.valueOf("14:00:00"), Time.valueOf("15:30:00"), "In Progress")
        );
	}

	private void layout() {
		gpane.setVgap(10);
        gpane.setHgap(10);
        gpane.setAlignment(Pos.CENTER);
        gpane.setPadding(new Insets(10));
        
		TableColumn<ReservationManagementModel, String> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<ReservationManagementModel, Date> dateColumn = new TableColumn<>("Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

		TableColumn<ReservationManagementModel, Time> startColumn = new TableColumn<>("Start");
		startColumn.setCellValueFactory(new PropertyValueFactory<>("start"));

		TableColumn<ReservationManagementModel, Time> endColumn = new TableColumn<>("End");
		endColumn.setCellValueFactory(new PropertyValueFactory<>("end"));

		TableColumn<ReservationManagementModel, String> statusColumn = new TableColumn<>("Status");
		statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

		reservationManagementTable.getColumns().addAll(idColumn, dateColumn, startColumn, endColumn, statusColumn);
		reservationManagementTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		vbox1.getChildren().addAll(testerLabel, reserveLabel, reservationManagementTable);
		vbox2.getChildren().addAll(idLabel, dateLabel, startLabel, endLabel, statusLabel, cancelButton);
		vbox2.setAlignment(Pos.CENTER_LEFT);
		
		gpane.add(vbox1, 0, 2);
        gpane.add(vbox2, 1, 2);
		bpane.setTop(userMenuBar);
		
		hbox = new HBox(20, vbox1, vbox2);
		hbox.setSpacing(20);
		hbox.setPadding(new Insets(10));
		hbox.setAlignment(Pos.CENTER);
		bpane.setCenter(hbox);
		
		testerLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
		reserveLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
		
		idLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
		dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
		startLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
		endLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
		statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
		
		cancelButton.setPrefSize(300, 40);
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void eventHandler(Stage primaryStage) {
		reserveService.setOnAction(e -> {
			ServiceManagement rsPage = new ServiceManagement(username);
			try {
				rsPage.start(primaryStage);
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		});

		customerReservation.setOnAction(e -> {
			ReservationManagement rsPage = new ReservationManagement(username);
			try {
				rsPage.start(primaryStage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		logOut.setOnAction(e -> {
			Logins loginPage = new Logins();
			try {
				loginPage.start(primaryStage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		reserveService.setOnAction(e -> navigateToPage(new ReserveService(username), primaryStage));
        customerReservation.setOnAction(e -> navigateToPage(new ReservationManagement(username), primaryStage));
        logOut.setOnAction(e -> navigateToPage(new Logins(), primaryStage));

	}

	private void navigateToPage(Application page, Stage primaryStage) {
        try {
            page.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void start(Stage primaryStage) {
		initialize();
		layout();
		eventHandler(primaryStage);

		Scene scene = new Scene(bpane, 700, 500);
		primaryStage.setTitle("Customer Reservation");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void setOnAction(Object object) {
		// TODO Auto-generated method stub

	}

}
