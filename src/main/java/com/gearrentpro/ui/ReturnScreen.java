package com.gearrentpro.ui;

import com.gearrentpro.entity.*;
import com.gearrentpro.service.RentalService;
import com.gearrentpro.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReturnScreen {

    private RentalService rentalService = new RentalService();
    private TableView<Rental> table = new TableView<>();
    private ObservableList<Rental> rentalList = FXCollections.observableArrayList();

    private Label rentalIdLabel = new Label("-");
    private Label customerLabel = new Label("-");
    private Label equipmentLabel = new Label("-");
    private Label startLabel = new Label("-");
    private Label endLabel = new Label("-");
    private Label depositLabel = new Label("-");
    private DatePicker returnDatePicker = new DatePicker();
    private CheckBox damagedCheck = new CheckBox("Equipment Damaged");
    private TextField damageDescField = new TextField();
    private TextField damageChargeField = new TextField();
    private Label lateFeeLabel = new Label("LKR 0.00");
    private Label totalChargesLabel = new Label("LKR 0.00");
    private Label refundLabel = new Label("LKR 0.00");
    private Label messageLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Process Return");

        HBox topBar = createTopBar(stage);
        setupTable();
        VBox detailPanel = createDetailPanel();

        HBox content = new HBox(10, table, detailPanel);
        HBox.setHgrow(table, Priority.ALWAYS);
        content.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(content);

        loadActiveRentals();

        Scene scene = new Scene(layout, 1000, 650);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar(Stage stage) {
        Label title = new Label("Process Return");
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

    private void setupTable() {
        TableColumn<Rental, String> idCol = new TableColumn<>("Rental ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        idCol.setPrefWidth(90);

        TableColumn<Rental, String> custCol = new TableColumn<>("Customer");
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        custCol.setPrefWidth(100);

        TableColumn<Rental, String> equipCol = new TableColumn<>("Equipment");
        equipCol.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        equipCol.setPrefWidth(100);

        TableColumn<Rental, LocalDate> endCol = new TableColumn<>("Due Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endCol.setPrefWidth(100);

        TableColumn<Rental, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));
        statusCol.setPrefWidth(90);

        table.getColumns().addAll(idCol, custCol, equipCol, endCol, statusCol);
        table.setItems(rentalList);

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateDetails(newVal);
            });
    }

    private VBox createDetailPanel() {
        returnDatePicker.setValue(LocalDate.now());
        damageDescField.setPromptText("Describe damage...");
        damageChargeField.setPromptText("e.g. 5000.00");
        damageDescField.setDisable(true);
        damageChargeField.setDisable(true);

        damagedCheck.setOnAction(e -> {
            boolean damaged = damagedCheck.isSelected();
            damageDescField.setDisable(!damaged);
            damageChargeField.setDisable(!damaged);
        });

        returnDatePicker.setOnAction(e -> calculateCharges());
        damageChargeField.textProperty().addListener(
            (obs, oldVal, newVal) -> calculateCharges());

        Button processBtn = new Button("Process Return");
        processBtn.setPrefWidth(200);
        processBtn.setStyle(
            "-fx-background-color: #27ae60; -fx-text-fill: white; " +
            "-fx-font-size: 14px; -fx-border-radius: 5; " +
            "-fx-background-radius: 5;");
        processBtn.setOnAction(e -> handleProcessReturn());

        lateFeeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        totalChargesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        refundLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        refundLabel.setTextFill(Color.web("#27ae60"));

        VBox panel = new VBox(10,
            new Label("Rental Details"),
            new Separator(),
            new Label("Rental ID:"), rentalIdLabel,
            new Label("Customer:"), customerLabel,
            new Label("Equipment:"), equipmentLabel,
            new Label("Start Date:"), startLabel,
            new Label("Due Date:"), endLabel,
            new Label("Security Deposit:"), depositLabel,
            new Separator(),
            new Label("Return Date:"), returnDatePicker,
            damagedCheck,
            new Label("Damage Description:"), damageDescField,
            new Label("Damage Charge (LKR):"), damageChargeField,
            new Separator(),
            new Label("Late Fee:"), lateFeeLabel,
            new Label("Total Charges:"), totalChargesLabel,
            new Label("Refund / Extra Payment:"), refundLabel,
            new Separator(),
            processBtn,
            messageLabel
        );
        panel.setPadding(new Insets(20));
        panel.setPrefWidth(340);
        panel.setStyle("-fx-background-color: white;");
        return panel;
    }

    private void populateDetails(Rental rental) {
        rentalIdLabel.setText(rental.getRentalId());
        customerLabel.setText(rental.getCustomerId());
        equipmentLabel.setText(rental.getEquipmentId());
        startLabel.setText(rental.getStartDate().toString());
        endLabel.setText(rental.getEndDate().toString());
        depositLabel.setText("LKR " + rental.getSecurityDeposit());
        returnDatePicker.setValue(LocalDate.now());
        damagedCheck.setSelected(false);
        damageDescField.clear();
        damageChargeField.clear();
        damageDescField.setDisable(true);
        damageChargeField.setDisable(true);
        messageLabel.setText("");
        calculateCharges();
    }

    private void calculateCharges() {
        Rental rental = table.getSelectionModel().getSelectedItem();
        if (rental == null || returnDatePicker.getValue() == null) return;

        try {
            LocalDate returnDate = returnDatePicker.getValue();
            LocalDate dueDate = rental.getEndDate();
            BigDecimal deposit = rental.getSecurityDeposit();

            BigDecimal lateFee = BigDecimal.ZERO;
            if (returnDate.isAfter(dueDate)) {
                long daysLate = java.time.temporal.ChronoUnit.DAYS.between(
                    dueDate, returnDate);
                lateFee = BigDecimal.valueOf(daysLate * 1000);
            }

            BigDecimal damageCharge = BigDecimal.ZERO;
            if (damagedCheck.isSelected() && 
                !damageChargeField.getText().trim().isEmpty()) {
                damageCharge = new BigDecimal(damageChargeField.getText().trim());
            }

            BigDecimal totalCharges = lateFee.add(damageCharge);
            BigDecimal refund = deposit.subtract(totalCharges);

            lateFeeLabel.setText("LKR " + lateFee);
            totalChargesLabel.setText("LKR " + totalCharges);

            if (refund.compareTo(BigDecimal.ZERO) >= 0) {
                refundLabel.setText("Refund: LKR " + refund);
                refundLabel.setTextFill(Color.web("#27ae60"));
            } else {
                refundLabel.setText("Extra Payment: LKR " + refund.abs());
                refundLabel.setTextFill(Color.RED);
            }
        } catch (Exception ex) {
            // Ignore parse errors while typing
        }
    }

    private void handleProcessReturn() {
        Rental rental = table.getSelectionModel().getSelectedItem();
        if (rental == null) {
            showMessage("Select a rental to process.", true);
            return;
        }
        if (returnDatePicker.getValue() == null) {
            showMessage("Return date is required.", true);
            return;
        }

        try {
            rental.setActualReturnDate(returnDatePicker.getValue());
            rental.setReturnedBy(
                SessionManager.getInstance().getLoggedInUser().getUserId());

            Damage damage = null;
            if (damagedCheck.isSelected()) {
                if (damageDescField.getText().trim().isEmpty()) {
                    showMessage("Please describe the damage.", true);
                    return;
                }
                if (damageChargeField.getText().trim().isEmpty()) {
                    showMessage("Please enter damage charge amount.", true);
                    return;
                }
                damage = new Damage();
                damage.setRentalId(rental.getRentalId());
                damage.setEquipmentId(rental.getEquipmentId());
                damage.setDescription(damageDescField.getText().trim());
                damage.setChargeAmount(
                    new BigDecimal(damageChargeField.getText().trim()));
                damage.setReportedBy(
                    SessionManager.getInstance().getLoggedInUser().getUserId());
            }

            rentalService.processReturn(rental, damage);
            showMessage("Return processed successfully!", false);
            loadActiveRentals();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void loadActiveRentals() {
        try {
            List<Rental> rentals = rentalService.getAllRentals();
            rentalList.setAll(rentals.stream()
                .filter(r -> r.getRentalStatus().equals("ACTIVE") ||
                             r.getRentalStatus().equals("OVERDUE"))
                .toList());
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}