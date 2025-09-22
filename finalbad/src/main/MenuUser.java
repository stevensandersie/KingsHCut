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

public class MenuUser extends Application {

    BorderPane bpane;
    GridPane userMenu;
    MenuBar userMenuBar;
    Menu menu;
    MenuItem reserveService, customerReservation, logOut;
    String username;

	private void initialize() {
        bpane = new BorderPane();
        userMenu = new GridPane();
        userMenuBar = new MenuBar();
        menu = new Menu("Menu");
        reserveService = new MenuItem("Reserve Service");
        customerReservation = new MenuItem("Customer Reservation");
        logOut = new MenuItem("Log Out");
    }

    private void layout() {
        menu.getItems().addAll(reserveService, customerReservation, logOut);
        userMenuBar.getMenus().add(menu);
        userMenu.setAlignment(Pos.CENTER);
        bpane.setTop(userMenuBar);
        bpane.setCenter(userMenu);
    }

    private void eventHandler(Stage primaryStage) {
        reserveService.setOnAction(e -> {
            ReserveService rsPage = new ReserveService(username);
            rsPage.start(primaryStage);
        });

        customerReservation.setOnAction(e -> {
            CustomerReservation crPage = new CustomerReservation(username);
            try {
                crPage.start(primaryStage);
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
    
	public MenuUser() {
	    	this.username = username;
		}
	
    @Override
    public void start(Stage primaryStage) {
        initialize();
        layout();
        eventHandler(primaryStage);

        Scene scene = new Scene(bpane, 700, 100);
        primaryStage.setScene(scene);
        primaryStage.setTitle("KingsHcut");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
