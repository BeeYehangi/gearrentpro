package com.gearrentpro.ui;

import com.gearrentpro.entity.Branch;
import com.gearrentpro.entity.SystemUser;
import com.gearrentpro.service.BranchService;
import com.gearrentpro.service.ReportService;
import com.gearrentpro.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ReportScreen {

    private ReportService reportService = new ReportService();
    private BranchService branchService = new BranchService();

    private ComboBox<Branch> branchCombo = new ComboBox<>();
    private DatePicker fromDatePicker = new DatePicker();
    private DatePicker toDatePicker = new DatePicker();
    private TextArea reportArea = new TextArea();
    private Label messageLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Reports");

        HBox topBar = createTopBar(stage);
        VBox controls = createControls();
        
        reportArea.setEditable(false);
        reportArea.setFont(Font.font("Courier New", 13));
        reportArea.setPrefHeight(400);

        VBox center = new VBox(10, controls, 
                               new Label("Report Output:"), 
                               reportArea, messageLabel);
        center.setPadding(new Insets(20));

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(center);

        loadBranches();

        // Default dates
        fromDatePicker.setValue(LocalDate.now().withDayOfMonth(1));
        toDatePicker.setValue(LocalDate.now());

        Scene scene = new Scene(layout, 900, 650);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar(Stage stage) {
        Label title = new Label("Reports");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.WHITE);

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            SystemUser user = SessionManager.getInstance().getLoggedInUser();
            new DashboardScreen().show(stage, user);
        });

        HBox topBar = new HBox();
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setStyle("-fx-background-color: #2c3e50;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(title, spacer, backBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        return topBar;
    }

    private VBox createControls() {
        fromDatePicker.setPromptText("From date");
        toDatePicker.setPromptText("To date");
        branchCombo.setPromptText("Select branch");

        Button revenueBtn = new Button("Branch Revenue Report");
        Button utilizationBtn = new Button("Equipment Utilization Report");

        revenueBtn.setStyle(
            "-fx-background-color: #2980b9; -fx-text-fill: white; " +
            "-fx-border-radius: 5; -fx-background-radius: 5;");
        utilizationBtn.setStyle(
            "-fx-background-color: #8e44ad; -fx-text-fill: white; " +
            "-fx-border-radius: 5; -fx-background-radius: 5;");

        revenueBtn.setOnAction(e -> generateRevenueReport());
        utilizationBtn.setOnAction(e -> generateUtilizationReport());

        HBox filters = new HBox(10,
            new Label("Branch:"), branchCombo,
            new Label("From:"), fromDatePicker,
            new Label("To:"), toDatePicker,
            revenueBtn, utilizationBtn
        );
        filters.setAlignment(Pos.CENTER_LEFT);
        filters.setPadding(new Insets(10));
        filters.setStyle(
            "-fx-background-color: #f8f9fa; " +
            "-fx-border-color: #dee2e6; " +
            "-fx-border-radius: 5;");

        return new VBox(filters);
    }

    private void generateRevenueReport() {
        try {
            Branch branch = branchCombo.getValue();
            LocalDate from = fromDatePicker.getValue();
            LocalDate to = toDatePicker.getValue();

            if (branch == null || from == null || to == null) {
                showMessage("Please select branch and date range.", true);
                return;
            }

            Map<String, Object> report = reportService.getBranchRevenueReport(
                branch.getBranchId(), from, to);

            StringBuilder sb = new StringBuilder();
            sb.append("╔════════════════════════════════════════╗\n");
            sb.append("║       BRANCH REVENUE REPORT            ║\n");
            sb.append("╚════════════════════════════════════════╝\n\n");
            sb.append("Branch       : ").append(branch.getBranchName()).append("\n");
            sb.append("Period       : ").append(from).append(" to ").append(to).append("\n");
            sb.append("─".repeat(45)).append("\n");
            sb.append(String.format("%-25s : %s%n", "Total Rentals",
                report.get("totalRentals")));
            sb.append(String.format("%-25s : LKR %s%n", "Total Rental Income",
                report.get("totalRentalIncome")));
            sb.append(String.format("%-25s : LKR %s%n", "Total Late Fees",
                report.get("totalLateFees")));
            sb.append(String.format("%-25s : LKR %s%n", "Total Damage Charges",
                report.get("totalDamageCharges")));
            sb.append("─".repeat(45)).append("\n");
            sb.append(String.format("%-25s : LKR %s%n", "GRAND TOTAL",
                report.get("grandTotal")));

            reportArea.setText(sb.toString());
            showMessage("Report generated successfully!", false);
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void generateUtilizationReport() {
        try {
            Branch branch = branchCombo.getValue();
            LocalDate from = fromDatePicker.getValue();
            LocalDate to = toDatePicker.getValue();

            if (branch == null || from == null || to == null) {
                showMessage("Please select branch and date range.", true);
                return;
            }

            List<Map<String, Object>> report = 
                reportService.getEquipmentUtilizationReport(
                    branch.getBranchId(), from, to);

            StringBuilder sb = new StringBuilder();
            sb.append("╔════════════════════════════════════════════════════╗\n");
            sb.append("║         EQUIPMENT UTILIZATION REPORT               ║\n");
            sb.append("╚════════════════════════════════════════════════════╝\n\n");
            sb.append("Branch  : ").append(branch.getBranchName()).append("\n");
            sb.append("Period  : ").append(from).append(" to ").append(to).append("\n");
            sb.append("─".repeat(60)).append("\n");
            sb.append(String.format("%-10s %-15s %-15s %10s %12s %10s%n",
                "ID", "Brand", "Model",
                "Rented", "Available", "Util%"));
            sb.append("─".repeat(60)).append("\n");

            for (Map<String, Object> row : report) {
                sb.append(String.format("%-10s %-15s %-15s %10s %12s %10s%n",
                    row.get("equipmentId"),
                    row.get("brand"),
                    row.get("model"),
                    row.get("rentedDays") + "d",
                    row.get("availableDays") + "d",
                    row.get("utilizationPct")));
            }

            reportArea.setText(sb.toString());
            showMessage("Report generated successfully!", false);
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void loadBranches() {
        try {
            List<Branch> branches = branchService.getAllBranches();
            branchCombo.setItems(FXCollections.observableArrayList(branches));
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}