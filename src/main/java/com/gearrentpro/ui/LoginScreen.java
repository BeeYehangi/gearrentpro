package com.gearrentpro.ui;

import com.gearrentpro.controller.AuthController;
import com.gearrentpro.entity.SystemUser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginScreen {

    private AuthController authController = new AuthController();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Login");

        // Title
        Label titleLabel = new Label("GearRent Pro");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#2c3e50"));

        Label subtitleLabel = new Label("Equipment Rental Management System");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setTextFill(Color.web("#7f8c8d"));

        // Form fields
        Label usernameLabel = new Label("Username:");
        usernameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setPrefHeight(38);
        usernameField.setStyle("-fx-border-radius: 5; -fx-background-radius: 5;");

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setPrefHeight(38);
        passwordField.setStyle("-fx-border-radius: 5; -fx-background-radius: 5;");

        Label errorLabel = new Label("");
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("Arial", 12));

        // Login button
        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(200);
        loginButton.setPrefHeight(40);
        loginButton.setStyle(
            "-fx-background-color: #2c3e50; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 14px; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5;"
        );

        // Login action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter username and password.");
                return;
            }

            try {
                SystemUser user = authController.login(username, password);
                DashboardScreen dashboard = new DashboardScreen();
                dashboard.show(stage, user);
            } catch (Exception ex) {
                errorLabel.setText(ex.getMessage());
            }
        });

        // Allow Enter key to trigger login
        passwordField.setOnAction(e -> loginButton.fire());

        // Layout
        VBox formBox = new VBox(10);
        formBox.getChildren().addAll(
            usernameLabel, usernameField,
            passwordLabel, passwordField,
            errorLabel, loginButton
        );
        formBox.setAlignment(Pos.CENTER_LEFT);
        formBox.setPadding(new Insets(20));
        formBox.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10;"
        );
        formBox.setMaxWidth(350);

        VBox headerBox = new VBox(5, titleLabel, subtitleLabel);
        headerBox.setAlignment(Pos.CENTER);

        VBox mainBox = new VBox(20, headerBox, formBox);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(40));
        mainBox.setStyle("-fx-background-color: #ecf0f1;");

        Scene scene = new Scene(mainBox, 480, 420);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}