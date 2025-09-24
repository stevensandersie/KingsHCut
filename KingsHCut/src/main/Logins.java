package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Connect;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Logins extends Application {

	BorderPane bpane;
    GridPane loginForm;
    TextField loginEmailField;
    PasswordField loginPasswordField;
    Button loginButton;
    Hyperlink registerLink;
    Label loginTitle, emailLabel, passwordLabel;
    Alert alert;

    private void initialize() {
    	bpane = new BorderPane();
        loginForm = new GridPane();	
        loginEmailField = new TextField();
        loginPasswordField = new PasswordField();
        loginButton = new Button("Login");
        registerLink = new Hyperlink("Don't have an account yet? Register Here!");
        loginTitle = new Label("Login");
        emailLabel = new Label("Email");
        passwordLabel = new Label("Password");
        
    }

    private void layout() {
        loginForm.setHalignment(loginTitle, HPos.CENTER);
        loginTitle.setFont(new Font(24));
        loginTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        loginForm.add(loginTitle, 0, 0);
        
        loginForm.add(emailLabel, 0, 1);
        loginForm.add(loginEmailField, 0, 2, 2, 1);
        loginForm.add(passwordLabel, 0, 3);
        loginForm.add(loginPasswordField, 0, 4, 2, 1);
        
        loginForm.add(loginButton, 0, 5);
        loginForm.setHalignment(loginButton, HPos.CENTER);

        loginForm.add(registerLink, 0, 6);
        loginForm.setHalignment(registerLink, HPos.CENTER);
        
        loginForm.setPadding(new Insets(5));
        loginForm.setHgap(8);
        loginForm.setVgap(5);
        loginForm.setAlignment(Pos.CENTER);
        
        loginButton.setPrefSize(100, 40);
        loginButton.setPrefSize(100, 40);
        registerLink.setStyle("-fx-text-fill: #1E90FF; -fx-underline: true;");
        bpane.setCenter(loginForm);
    }


    private void eventHandler(Stage primaryStage) {
        loginButton.setOnAction(e -> handleLogin(primaryStage));
        registerLink.setOnAction(e -> navigateToRegisterPage(primaryStage));
    }

    private void handleLogin(Stage primaryStage) {
        String email = loginEmailField.getText();
        String password = loginPasswordField.getText();

        if (validateLoginInput(email, password)) {
            if (isAdmin(email)) {
                goToAdmin(primaryStage);
            } else {
                goToUser(primaryStage);
            }
        }
    }

    private boolean validateLoginInput(String email, String password) {
        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Email cannot be empty");
            return false;
        }
        if (password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Password cannot be empty");
            return false;
        }
        if (!checkCredential(email, password)) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Credentials are invalid");
            return false;
        }
        return true;
    }

    private void navigateToRegisterPage(Stage primaryStage) {
        Regist registerPage = new Regist();
        try {
            registerPage.start(primaryStage);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean checkCredential(String email, String password) {

        try (PreparedStatement ps = Connect.getConnection().prepareStatement
        		("SELECT UserPassword FROM msuser WHERE UserEmail = ?")) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return password.equals(rs.getString("UserPassword"));
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return false;
    }
    
    private boolean isAdmin(String email) {
        String query = "SELECT Role FROM msuser WHERE UserEmail = ? LIMIT 1";

        try (PreparedStatement ps = Connect.getConnection().prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String role = rs.getString("Role");
                return "Admin".equalsIgnoreCase(role);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void goToAdmin(Stage primaryStage) {
        MenuAdmin adminMenu = new MenuAdmin();
        try {
            adminMenu.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToUser(Stage primaryStage) {
        MenuUser userMenu = new MenuUser();
        try {
            userMenu.start(primaryStage);	
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        initialize();
        layout();
        eventHandler(primaryStage);

        Scene scene = new Scene(bpane, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("KingsHcut");
        primaryStage.show();
    }
}
