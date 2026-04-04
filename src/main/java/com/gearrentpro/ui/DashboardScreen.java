package com.gearrentpro.ui;

import com.gearrentpro.entity.SystemUser;
import com.gearrentpro.util.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardScreen {

    public void show(Stage stage, SystemUser user) {
        stage.setTitle("GearRent Pro - Dashboard");

        // Top bar
        Label titleLabel = new Label("GearRent Pro");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.WHITE);

        Label userLabel = new Label("Logged in: " + user.getFullName() + 
                                    " (" + user.getRole() + ")");
        userLabel.setTextFill(Color.WHITE);
        userLabel.setFont(Font.font("Arial", 12));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle(
            "-fx-background-color: #e74c3c; " +
            "-fx-text-fill: white; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5;"
        );
        logoutBtn.setOnAction(e -> {
            SessionManager.getInstance().logout();
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.show(stage);
        });

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setStyle("-fx-background-color: #2c3e50;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(titleLabel, spacer, userLabel, 
                                     new Label("  "), logoutBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Menu buttons
        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(20));
        menuBox.setStyle("-fx-background-color: #34495e;");
        menuBox.setPrefWidth(200);

        Label menuTitle = new Label("MENU");
        menuTitle.setTextFill(Color.web("#bdc3c7"));
        menuTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        menuBox.getChildren().add(menuTitle);

        // Add menu buttons based on role
        if (user.getRole().equals("ADMIN")) {
            menuBox.getChildren().add(
                createMenuButton("Manage Branches", stage, "BRANCHES"));
                  menuBox.getChildren().add(
                createMenuButton("Memberships", stage, "MEMBERSHIPS"));
                  menuBox.getChildren().add(
                createMenuButton("System Config", stage, "CONFIG"));
        }

        
        menuBox.getChildren().add(
            createMenuButton("Categories", stage, "CATEGORIES"));
        menuBox.getChildren().add(
            createMenuButton("Equipment", stage, "EQUIPMENT"));
        menuBox.getChildren().add(
            createMenuButton("Customers", stage, "CUSTOMERS"));
        menuBox.getChildren().add(
            createMenuButton("Reservations", stage, "RESERVATIONS"));
        menuBox.getChildren().add(
            createMenuButton("Rentals", stage, "RENTALS"));
        menuBox.getChildren().add(
            createMenuButton("Process Return", stage, "RETURNS"));
        menuBox.getChildren().add(
            createMenuButton("Overdue Rentals", stage, "OVERDUE"));
       if (!user.getRole().equals("STAFF")) {
        menuBox.getChildren().add(
            createMenuButton("Reports", stage, "REPORTS"));
}
        // Content area
        StackPane contentArea = new StackPane();
        contentArea.setStyle("-fx-background-color: #ecf0f1;");

        Label welcomeLabel = new Label("Welcome, " + user.getFullName() + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        welcomeLabel.setTextFill(Color.web("#2c3e50"));

        Label roleLabel = new Label("Role: " + user.getRole());
        roleLabel.setFont(Font.font("Arial", 16));
        roleLabel.setTextFill(Color.web("#7f8c8d"));

        Label branchLabel = new Label(user.getBranchId() != null ? 
            "Branch: " + user.getBranchId() : "All Branches");
        branchLabel.setFont(Font.font("Arial", 14));
        branchLabel.setTextFill(Color.web("#7f8c8d"));

        VBox welcomeBox = new VBox(10, welcomeLabel, roleLabel, branchLabel);
        welcomeBox.setAlignment(Pos.CENTER);
        contentArea.getChildren().add(welcomeBox);

        // Main layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(topBar);
        mainLayout.setLeft(menuBox);
        mainLayout.setCenter(contentArea);

        Scene scene = new Scene(mainLayout, 900, 600);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    private Button createMenuButton(String text, Stage stage, String screen) {
        Button btn = new Button(text);
        btn.setPrefWidth(180);
        btn.setPrefHeight(35);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(0, 0, 0, 10));
        btn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #ecf0f1; " +
            "-fx-font-size: 13px; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5;"
        );
        btn.setOnMouseEntered(e -> btn.setStyle(
            "-fx-background-color: #2c3e50; " +
            "-fx-text-fill: white; " +
            "-fx-font-size: 13px; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5;"
        ));
        btn.setOnMouseExited(e -> btn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #ecf0f1; " +
            "-fx-font-size: 13px; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5;"
        ));
        btn.setOnAction(e -> navigateTo(screen, stage));
        return btn;
    }

    private void navigateTo(String screen, Stage stage) {
        try {
            switch (screen) {
                case "BRANCHES":
                    new BranchScreen().show(stage);
                    break;
                case "CATEGORIES":
                    new CategoryScreen().show(stage);
                    break;
                case "EQUIPMENT":
                    new EquipmentScreen().show(stage);
                    break;
                case "CUSTOMERS":
                    new CustomerScreen().show(stage);
                    break;
                case "RESERVATIONS":
                    new ReservationScreen().show(stage);
                    break;
                case "RENTALS":
                    new RentalScreen().show(stage);
                    break;
                case "RETURNS":
                    new ReturnScreen().show(stage);
                    break;
                case "OVERDUE":
                    new OverdueScreen().show(stage);
                    break;
                case "REPORTS":
                    new ReportScreen().show(stage);
                    break;
                case "MEMBERSHIPS":
                    new MembershipScreen().show(stage);
                    break;  
                case "CONFIG":
                    new SystemConfigScreen().show(stage);
                    break;  
            }
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
}