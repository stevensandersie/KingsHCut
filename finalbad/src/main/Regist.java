package main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.Connect;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Regist extends Application {

    BorderPane bpane;
    GridPane registerForm;
    Label registerTitle, username, email, pass, confirmPass, phone;
    TextField UsernameField, EmailField, PhoneField;
    PasswordField PasswordField, ConfirmPasswordField;
    RadioButton maleRadio, femaleRadio;
    ToggleGroup gender;
    Button registerButton;
    Hyperlink loginLink;
    Alert alert;

    private void initialize() {
    	registerTitle = new Label("Register");
		registerTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        bpane = new BorderPane();
        registerForm = new GridPane();
        UsernameField = new TextField();
        EmailField = new TextField();
        PasswordField = new PasswordField();
        ConfirmPasswordField = new PasswordField();
        PhoneField = new TextField();
        
        maleRadio = new RadioButton("Male");
        femaleRadio = new RadioButton("Female");
        gender = new ToggleGroup();
        maleRadio.setToggleGroup(gender);
        femaleRadio.setToggleGroup(gender);
        registerButton = new Button("Register");
        
        loginLink = new Hyperlink("Already have an account? Click here to login!");
        username = new Label("Username");
        email = new Label("Email");
        pass = new Label("Password");
        confirmPass = new Label("Confirm Password");
        phone = new Label("Phone Number");
    }

    private void layout() {
    	registerForm.add(registerTitle, 0, 0, 2, 1);
        registerForm.add(username, 0, 1);
        registerForm.add(UsernameField, 0, 2, 2, 1);
        registerForm.add(email, 0, 3);
        registerForm.add(EmailField, 0, 4, 2, 1);
        registerForm.add(pass, 0, 5);
        registerForm.add(PasswordField, 0, 6, 2, 1);
        registerForm.add(confirmPass, 0, 7);
        registerForm.add(ConfirmPasswordField, 0, 8, 2, 1);
        registerForm.add(phone, 0, 9);
        registerForm.add(PhoneField, 0, 10, 2, 1);

        registerForm.setHalignment(registerTitle, HPos.CENTER);
        
        HBox genderContainer = new HBox(10, maleRadio, femaleRadio);
        registerForm.add(genderContainer, 0, 11);

        registerForm.add(registerButton, 0, 12, 2, 1);
        registerForm.setHalignment(registerButton, HPos.CENTER);

        registerForm.add(loginLink, 0, 13, 2, 1); 
        registerForm.setHalignment(loginLink, HPos.CENTER);

        registerForm.setPadding(new Insets(5));
        registerForm.setHgap(8); 
        registerForm.setVgap(3);  
        registerForm.setAlignment(Pos.CENTER); 
        
        registerButton.setPrefSize(100, 40);
        loginLink.setStyle("-fx-text-fill: #1E90FF; -fx-underline: true;");

        bpane.setCenter(registerForm);
    }

    private String generateID() {
        String base = "US001";
        String query = "SELECT MAX(CAST(SUBSTRING(UserID, 3) AS UNSIGNED)) AS NewID FROM msuser";

        try (PreparedStatement ps = Connect.getConnection().prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int newID = rs.getInt("NewID");
                base = String.format("US%03d", newID + 1);
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }

        return base;
    }


    private void clearFields() {
        UsernameField.clear();
        EmailField.clear();
        PasswordField.clear();
        ConfirmPasswordField.clear();
        PhoneField.clear();
        maleRadio.setSelected(false);
        femaleRadio.setSelected(false);
    }

    private boolean checkUniqueUsername(String userEmail) {
        String query = "SELECT Username FROM msuser WHERE UserEmail = ?";
        try (PreparedStatement ps = Connect.getConnection().prepareStatement(query)) {
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void eventHandler(Stage primaryStage) {
        registerButton.setOnAction(e -> {
            String username = UsernameField.getText();
            String email = EmailField.getText();
            String pass = PasswordField.getText();
            String confirmPass = ConfirmPasswordField.getText();
            String phoneUnique = PhoneField.getText();
            Toggle genderToggle = gender.getSelectedToggle();
            String genders = null;
            String role = null;

            String id = generateID();
            String phoneNumber = formatPhone(phoneUnique);

            if (isInvalidInput(username, email, pass, confirmPass, phoneUnique, genderToggle)) {
                return;
            }

            role = username.toLowerCase().contains("admin") ? "Admin" : "User";
            RadioButton selectedGender = (RadioButton) genderToggle;
            genders = selectedGender.getText();

            insertData(id, username, pass, email, role, genders, phoneNumber);

            clearFields();

            Logins loginPage = new Logins();
            try {
                loginPage.start(primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        loginLink.setOnAction(e -> {
            Logins loginPage = new Logins();
            try {
                loginPage.start(primaryStage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
    }
    
    private boolean isInvalidInput(String username, String email, String pass, String confirmPass, 
            String phoneUnique, Toggle genderToggle) {
		if (username.isEmpty()) {
		showAlert(AlertType.ERROR, "Username can't be empty", "Error");
		return true;
		}
		if (username.length() < 5 || username.length() > 30) {
		showAlert(AlertType.ERROR, "Username must be 5 - 30 characters", "Error");
		return true;
		}
		if (!usernameUnique(username)) {
		showAlert(AlertType.ERROR, "Username must be unique", "Error");
		return true;
		}
		if (email.isEmpty()) {
		showAlert(AlertType.ERROR, "Email can't be empty", "Error");
		return true;
		}
		if (!email.endsWith("@gmail.com")) {
		showAlert(AlertType.ERROR, "Email must end with @gmail.com", "Error");
		return true;
		}
		if (!emailUnique(email)) {
		showAlert(AlertType.ERROR, "Email must be unique", "Error");
		return true;
		}
		if (pass.isEmpty()) {
		showAlert(AlertType.ERROR, "Password can't be empty", "Error");
		return true;
		}
		if (!alphanumeric(pass)) {
		showAlert(AlertType.ERROR, "Password must be alphanumeric", "Error");
		return true;
		}
		if (confirmPass.isEmpty()) {
		showAlert(AlertType.ERROR, "Confirm password can't be empty", "Error");
		return true;
		}
		if (!confirmPass.equals(pass)) {
		showAlert(AlertType.ERROR, "Password does not match", "Error");
		return true;
		}
		if (phoneUnique.isEmpty()) {
		showAlert(AlertType.ERROR, "Phone number can't be empty", "Error");
		return true;
		}
		if (phoneUnique.length() < 9 || phoneUnique.length() > 13) {
		showAlert(AlertType.ERROR, "Phone number must be 9 - 13 characters", "Error");
		return true;
		}
		if (!phoneUnique.startsWith("62")) {
		showAlert(AlertType.ERROR, "Phone number must start with 62", "Error");
		return true;
		}
		if (!phoneUnique(phoneUnique)) {
		showAlert(AlertType.ERROR, "Phone number must be unique", "Error");
		return true;
		}
		if (genderToggle == null) {
		showAlert(AlertType.ERROR, "Gender must be selected", "Error");
		return true;
		}
		return false;
		}

    private String formatPhone(String phoneUnique) {
        if (phoneUnique.startsWith("62")) {
            return "0" + phoneUnique.substring(2);
        }
        return phoneUnique;
    }

    private boolean phoneUnique(String phoneNumber) {
        String query = "SELECT * FROM msuser WHERE UserPhoneNumber = ? LIMIT 1";

        try (PreparedStatement ps = Connect.getConnection().prepareStatement(query)) {
            ps.setString(1, phoneNumber);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean alphanumeric(String pass) {
        boolean angka = false;
        boolean huruf = false;
        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);
            if (((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) {
                huruf = true;
            } else if (c >= '0' && c <= '9') {
                angka = true;
            } else {
                return false;
            }
        }
        return huruf && angka;
    }

    private boolean emailUnique(String email) {
        String query = "SELECT * FROM msuser WHERE UserEmail = ? LIMIT 1";

        try (PreparedStatement ps = Connect.getConnection().prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private boolean usernameUnique(String username) {
        String query = "SELECT * FROM msuser WHERE Username = ? LIMIT 1";

        try (PreparedStatement ps = Connect.getConnection().prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void showAlert(Alert.AlertType alertType, String message, String title) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void insertData(String id, String username, String pass, String email, String role, String gender,
			String phoneNumber) {
		String query = "INSERT INTO msuser (UserID, Username, UserPassword, UserEmail, UserRole , UserGender, UserPhoneNumber) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try {
			PreparedStatement ps = Connect.getConnection().prepareStatement(query);
			ps.setString(1, id);
			ps.setString(2, username);
			ps.setString(3, pass);
			ps.setString(4, email);
			ps.setString(5, role);
			ps.setString(6, gender);
			ps.setString(7, phoneNumber);
			ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
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