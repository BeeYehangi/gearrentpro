package com.gearrentpro.ui;

import com.gearrentpro.entity.*;
import com.gearrentpro.service.*;
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

public class RentalScreen {

    private RentalService rentalService = new RentalService();
    private EquipmentService equipmentService = new EquipmentService();
    private CustomerService customerService = new CustomerService();
    private BranchService branchService = new BranchService();

    private TableView<Rental> table = new TableView<>();
    private ObservableList<Rental> rentalList = FXCollections.observableArrayList();

    private TextField idField = new TextField();
    private ComboBox<Equipment> equipmentCombo = new ComboBox<>();
    private ComboBox<Customer> customerCombo = new ComboBox<>();
    private ComboBox<Branch> branchCombo = new ComboBox<>();
    private DatePicker startDatePicker = new DatePicker();
    private DatePicker endDatePicker = new DatePicker();
    private Label rentalAmountLabel = new Label("LKR 0.00");
    private Label membershipDiscLabel = new Label("LKR 0.00");
    private Label longRentalDiscLabel = new Label("LKR 0.00");
    private Label finalAmountLabel = new Label("LKR 0.00");
    private Label depositLabel = new Label("LKR 0.00");
    private ComboBox<String> paymentCombo = new ComboBox<>();
    private Label messageLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Rentals");
        buildScene(stage);
    }

    public void showFromReservation(Reservation reservation) {
        Stage stage = new Stage();
        stage.setTitle("GearRent Pro - Convert to Rental");
        buildScene(stage);

        // Pre-fill from reservation
        idField.setText("RNT" + reservation.getReservationId().substring(3));
        equipmentCombo.getItems().stream()
            .filter(eq -> eq.getEquipmentId().equals(reservation.getEquipmentId()))
            .findFirst().ifPresent(equipmentCombo::setValue);
        customerCombo.getItems().stream()
            .filter(c -> c.getCustomerId().equals(reservation.getCustomerId()))
            .findFirst().ifPresent(customerCombo::setValue);
        branchCombo.getItems().stream()
            .filter(b -> b.getBranchId().equals(reservation.getBranchId()))
            .findFirst().ifPresent(branchCombo::setValue);
        startDatePicker.setValue(reservation.getStartDate());
        endDatePicker.setValue(reservation.getEndDate());
        calculateAmounts();
    }

    private void buildScene(Stage stage) {
        HBox topBar = createTopBar(stage);
        setupTable();
        VBox form = createForm();
        HBox buttons = createButtons();

        ScrollPane formScroll = new ScrollPane(form);
        formScroll.setFitToWidth(true);
        formScroll.setPrefHeight(500);

        VBox rightPanel = new VBox(10, new Label("Rental Details"),
                                    formScroll, buttons, messageLabel);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setPrefWidth(360);
        rightPanel.setStyle("-fx-background-color: white;");

        HBox content = new HBox(10, table, rightPanel);
        HBox.setHgrow(table, Priority.ALWAYS);
        content.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(content);

        loadCombos();
        loadData();

        Scene scene = new Scene(layout, 1050, 680);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar(Stage stage) {
        Label title = new Label("Manage Rentals");
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
        TableColumn<Rental, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("rentalId"));
        idCol.setPrefWidth(80);

        TableColumn<Rental, String> equipCol = new TableColumn<>("Equipment");
        equipCol.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        equipCol.setPrefWidth(90);

        TableColumn<Rental, String> custCol = new TableColumn<>("Customer");
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        custCol.setPrefWidth(90);

        TableColumn<Rental, LocalDate> startCol = new TableColumn<>("Start");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startCol.setPrefWidth(90);

        TableColumn<Rental, LocalDate> endCol = new TableColumn<>("End");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endCol.setPrefWidth(90);

        TableColumn<Rental, BigDecimal> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(
            new PropertyValueFactory<>("finalPayableAmount"));
        amountCol.setPrefWidth(100);

        TableColumn<Rental, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("rentalStatus"));
        statusCol.setPrefWidth(90);

        TableColumn<Rental, String> paymentCol = new TableColumn<>("Payment");
        paymentCol.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        paymentCol.setPrefWidth(80);

        table.getColumns().addAll(idCol, equipCol, custCol,
                startCol, endCol, amountCol, statusCol, paymentCol);
        table.setItems(rentalList);

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateForm(newVal);
            });
    }

    private VBox createForm() {
        idField.setPromptText("e.g. RNT001");
        paymentCombo.setItems(FXCollections.observableArrayList("PAID", "UNPAID"));
        paymentCombo.setValue("PAID");

        // Auto calculate on date/equipment change
        startDatePicker.setOnAction(e -> calculateAmounts());
        endDatePicker.setOnAction(e -> calculateAmounts());
        equipmentCombo.setOnAction(e -> calculateAmounts());
        customerCombo.setOnAction(e -> calculateAmounts());

        rentalAmountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        membershipDiscLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        longRentalDiscLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        finalAmountLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        finalAmountLabel.setTextFill(Color.web("#27ae60"));
        depositLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        VBox form = new VBox(8,
            new Label("Rental ID:"), idField,
            new Label("Equipment:"), equipmentCombo,
            new Label("Customer:"), customerCombo,
            new Label("Branch:"), branchCombo,
            new Label("Start Date:"), startDatePicker,
            new Label("End Date:"), endDatePicker,
            new Separator(),
            new Label("Rental Amount:"), rentalAmountLabel,
            new Label("Membership Discount:"), membershipDiscLabel,
            new Label("Long Rental Discount:"), longRentalDiscLabel,
            new Label("Security Deposit:"), depositLabel,
            new Label("Final Payable:"), finalAmountLabel,
            new Separator(),
            new Label("Payment Status:"), paymentCombo
        );
        form.setPadding(new Insets(5));
        return form;
    }

    private HBox createButtons() {
        Button createBtn = new Button("Create Rental");
        Button clearBtn = new Button("Clear");

        createBtn.setStyle("-fx-background-color: #27ae60; " +
                           "-fx-text-fill: white; -fx-border-radius: 5; " +
                           "-fx-background-radius: 5;");
        clearBtn.setStyle("-fx-background-color: #95a5a6; " +
                          "-fx-text-fill: white; -fx-border-radius: 5; " +
                          "-fx-background-radius: 5;");

        createBtn.setOnAction(e -> handleCreate());
        clearBtn.setOnAction(e -> clearForm());

        return new HBox(8, createBtn, clearBtn);
    }

    private void calculateAmounts() {
        try {
            Equipment equipment = equipmentCombo.getValue();
            Customer customer = customerCombo.getValue();
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();

            if (equipment == null || start == null || end == null) return;

            BigDecimal rentalAmount = rentalService.calculateRentalAmount(
                equipment.getEquipmentId(), start, end);

            BigDecimal membershipDisc = customer != null ?
                rentalService.calculateMembershipDiscount(
                    customer.getCustomerId(), rentalAmount) : BigDecimal.ZERO;

            BigDecimal longDisc = rentalService.calculateLongRentalDiscount(
                start, end, rentalAmount);

            BigDecimal finalAmount = rentalAmount
                .subtract(membershipDisc).subtract(longDisc);

            rentalAmountLabel.setText("LKR " + rentalAmount);
            membershipDiscLabel.setText("LKR " + membershipDisc);
            longRentalDiscLabel.setText("LKR " + longDisc);
            depositLabel.setText("LKR " + equipment.getDepositAmount());
            finalAmountLabel.setText("LKR " + finalAmount);

        } catch (Exception ex) {
            // Silently ignore incomplete form
        }
    }

    private void handleCreate() {
        try {
            Equipment equipment = equipmentCombo.getValue();
            Customer customer = customerCombo.getValue();
            Branch branch = branchCombo.getValue();
            LocalDate start = startDatePicker.getValue();
            LocalDate end = endDatePicker.getValue();

            if (equipment == null || customer == null || branch == null
                    || start == null || end == null
                    || idField.getText().trim().isEmpty()) {
                showMessage("Please fill all required fields.", true);
                return;
            }

            BigDecimal rentalAmount = rentalService.calculateRentalAmount(
                equipment.getEquipmentId(), start, end);
            BigDecimal membershipDisc = rentalService.calculateMembershipDiscount(
                customer.getCustomerId(), rentalAmount);
            BigDecimal longDisc = rentalService.calculateLongRentalDiscount(
                start, end, rentalAmount);
            BigDecimal finalAmount = rentalAmount
                .subtract(membershipDisc).subtract(longDisc);

            Rental rental = new Rental();
            rental.setRentalId(idField.getText().trim());
            rental.setEquipmentId(equipment.getEquipmentId());
            rental.setCustomerId(customer.getCustomerId());
            rental.setBranchId(branch.getBranchId());
            rental.setStartDate(start);
            rental.setEndDate(end);
            rental.setCalculatedRentalAmount(rentalAmount);
            rental.setSecurityDeposit(equipment.getDepositAmount());
            rental.setMembershipDiscount(membershipDisc);
            rental.setLongRentalDiscount(longDisc);
            rental.setFinalPayableAmount(finalAmount);
            rental.setLateFee(BigDecimal.ZERO);
            rental.setDamageCharges(BigDecimal.ZERO);
            rental.setPaymentStatus(paymentCombo.getValue());
            rental.setRentalStatus("ACTIVE");
            rental.setCreatedBy(
                SessionManager.getInstance().getLoggedInUser().getUserId());

            rentalService.createRental(rental);
            showMessage("Rental created successfully!", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void populateForm(Rental rental) {
        idField.setText(rental.getRentalId());
        startDatePicker.setValue(rental.getStartDate());
        endDatePicker.setValue(rental.getEndDate());
        paymentCombo.setValue(rental.getPaymentStatus());
        rentalAmountLabel.setText("LKR " + rental.getCalculatedRentalAmount());
        membershipDiscLabel.setText("LKR " + rental.getMembershipDiscount());
        longRentalDiscLabel.setText("LKR " + rental.getLongRentalDiscount());
        depositLabel.setText("LKR " + rental.getSecurityDeposit());
        finalAmountLabel.setText("LKR " + rental.getFinalPayableAmount());
        messageLabel.setText("");
    }

    private void clearForm() {
        idField.clear();
        equipmentCombo.setValue(null);
        customerCombo.setValue(null);
        branchCombo.setValue(null);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        paymentCombo.setValue("PAID");
        rentalAmountLabel.setText("LKR 0.00");
        membershipDiscLabel.setText("LKR 0.00");
        longRentalDiscLabel.setText("LKR 0.00");
        depositLabel.setText("LKR 0.00");
        finalAmountLabel.setText("LKR 0.00");
        messageLabel.setText("");
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        try {
            rentalList.setAll(rentalService.getAllRentals());
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void loadCombos() {
        try {
            equipmentCombo.setItems(FXCollections.observableArrayList(
                equipmentService.getAllEquipment()));
            customerCombo.setItems(FXCollections.observableArrayList(
                customerService.getAllCustomers()));
            branchCombo.setItems(FXCollections.observableArrayList(
                branchService.getAllBranches()));
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}
