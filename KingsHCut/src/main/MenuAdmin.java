package main;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.User;

public class MenuAdmin extends Application {
	
	BorderPane bpane;
	GridPane adminMenu;
	MenuBar adminMenuBar;
	Menu menu;
	MenuItem serviceManagement, reservationManagement, logOut;
	String username;
	
	public MenuAdmin(String username) {
		this.username = username;
	}

	public MenuAdmin() {
		// TODO Auto-generated constructor stub
	}

	private void initialize() {
		bpane = new BorderPane();
		adminMenu = new GridPane();

		adminMenuBar = new MenuBar();
		menu = new Menu("Menu");
		serviceManagement = new MenuItem("Service Management");
		reservationManagement = new MenuItem("Reservation Management");
		logOut = new MenuItem("Log Out");
	}

	private void layout() {
		menu.getItems().addAll(serviceManagement, reservationManagement, logOut);
		adminMenuBar.getMenus().add(menu);
		adminMenu.setAlignment(Pos.CENTER);

		bpane.setTop(adminMenuBar);
		bpane.setCenter(adminMenu);
	}

	private void eventHandler(Stage primaryStage) {
		serviceManagement.setOnAction(e -> {
			ReservationManagement smPage = new ReservationManagement(username);
			try {
				smPage.start(primaryStage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});

		reservationManagement.setOnAction(e -> {
			ServiceManagement rmPage = new ServiceManagement(username);
			try {
				rmPage.start(primaryStage);
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
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		initialize();
		layout();
		eventHandler(primaryStage);

		Scene scene = new Scene(bpane, 700, 100);
		primaryStage.setScene(scene);
		primaryStage.setTitle("KingsHcut");
		primaryStage.show();
	}
}
