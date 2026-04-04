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

import java.time.LocalDate;
import java.util.List;

public class ReservationScreen {

    private ReservationService reservationService = new ReservationService();
    private EquipmentService equipmentService = new EquipmentService();
    private CustomerService customerService = new CustomerService();
    private BranchService branchService = new BranchService();

    private TableView<Reservation> table = new TableView<>();
    private ObservableList<Reservation> reservationList = 
        FXCollections.observableArrayList();

    private TextField idField = new TextField();
    private ComboBox<Equipment> equipmentCombo = new ComboBox<>();
    private ComboBox<Customer> customerCombo = new ComboBox<>();
    private ComboBox<Branch> branchCombo = new ComboBox<>();
    private DatePicker startDatePicker = new DatePicker();
    private DatePicker endDatePicker = new DatePicker();
    private Label statusLabel = new Label();
    private Label messageLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Reservations");

        HBox topBar = createTopBar(stage);
        setupTable();
        VBox form = createForm();
        HBox buttons = createButtons();

        VBox rightPanel = new VBox(10, new Label("Reservation Details"),
                                    form, buttons, messageLabel);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setPrefWidth(340);
        rightPanel.setStyle("-fx-background-color: white;");

        HBox content = new HBox(10, table, rightPanel);
        HBox.setHgrow(table, Priority.ALWAYS);
        content.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(content);

        loadCombos();
        loadData();

        Scene scene = new Scene(layout, 1000, 650);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar(Stage stage) {
        Label title = new Label("Manage Reservations");
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
        TableColumn<Reservation, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        idCol.setPrefWidth(90);

        TableColumn<Reservation, String> equipCol = new TableColumn<>("Equipment");
        equipCol.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        equipCol.setPrefWidth(100);

        TableColumn<Reservation, String> custCol = new TableColumn<>("Customer");
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        custCol.setPrefWidth(100);

        TableColumn<Reservation, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branchId"));
        branchCol.setPrefWidth(80);

        TableColumn<Reservation, LocalDate> startCol = new TableColumn<>("Start");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        startCol.setPrefWidth(100);

        TableColumn<Reservation, LocalDate> endCol = new TableColumn<>("End");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endCol.setPrefWidth(100);

        TableColumn<Reservation, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        table.getColumns().addAll(idCol, equipCol, custCol, 
                                   branchCol, startCol, endCol, statusCol);
        table.setItems(reservationList);

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateForm(newVal);
            });
    }

    private VBox createForm() {
        idField.setPromptText("e.g. RES001");
        startDatePicker.setPromptText("Start date");
        endDatePicker.setPromptText("End date");

        VBox form = new VBox(8,
            new Label("Reservation ID:"), idField,
            new Label("Equipment:"), equipmentCombo,
            new Label("Customer:"), customerCombo,
            new Label("Branch:"), branchCombo,
            new Label("Start Date:"), startDatePicker,
            new Label("End Date:"), endDatePicker,
            new Label("Status:"), statusLabel
        );
        return form;
    }

    private HBox createButtons() {
        Button createBtn = new Button("Create");
        Button cancelBtn = new Button("Cancel Res.");
        Button convertBtn = new Button("→ Rental");
        Button clearBtn = new Button("Clear");

        String btnStyle = "-fx-text-fill: white; -fx-border-radius: 5; " +
                          "-fx-background-radius: 5;";
        createBtn.setStyle("-fx-background-color: #27ae60;" + btnStyle);
        cancelBtn.setStyle("-fx-background-color: #e74c3c;" + btnStyle);
        convertBtn.setStyle("-fx-background-color: #8e44ad;" + btnStyle);
        clearBtn.setStyle("-fx-background-color: #95a5a6;" + btnStyle);

        createBtn.setOnAction(e -> handleCreate());
        cancelBtn.setOnAction(e -> handleCancel());
        convertBtn.setOnAction(e -> handleConvert(
            table.getSelectionModel().getSelectedItem()));
        clearBtn.setOnAction(e -> clearForm());

        return new HBox(8, createBtn, cancelBtn, convertBtn, clearBtn);
    }

    private void handleCreate() {
        try {
            Reservation reservation = getReservationFromForm();
            Equipment equipment = equipmentService.getEquipmentById(
                reservation.getEquipmentId());
            reservationService.createReservation(reservation,
                equipment.getDepositAmount());
            showMessage("Reservation created successfully!", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void handleCancel() {
        try {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                showMessage("Select a reservation first.", true);
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Cancel reservation " + id + "?",
                ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    try {
                        reservationService.cancelReservation(id);
                        showMessage("Reservation cancelled.", false);
                        loadData();
                        clearForm();
                    } catch (Exception ex) {
                        showMessage(ex.getMessage(), true);
                    }
                }
            });
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void handleConvert(Reservation reservation) {
        if (reservation == null) {
            showMessage("Select a reservation to convert.", true);
            return;
        }
        new RentalScreen().showFromReservation(reservation);
    }

    private Reservation getReservationFromForm() {
        Reservation reservation = new Reservation();
        reservation.setReservationId(idField.getText().trim());
        Equipment selectedEquip = equipmentCombo.getValue();
        reservation.setEquipmentId(selectedEquip != null ? 
            selectedEquip.getEquipmentId() : "");
        Customer selectedCust = customerCombo.getValue();
        reservation.setCustomerId(selectedCust != null ? 
            selectedCust.getCustomerId() : "");
        Branch selectedBranch = branchCombo.getValue();
        reservation.setBranchId(selectedBranch != null ? 
            selectedBranch.getBranchId() : "");
        reservation.setStartDate(startDatePicker.getValue());
        reservation.setEndDate(endDatePicker.getValue());
        reservation.setCreatedBy(
            SessionManager.getInstance().getLoggedInUser().getUserId());
        return reservation;
    }

    private void populateForm(Reservation reservation) {
        idField.setText(reservation.getReservationId());
        startDatePicker.setValue(reservation.getStartDate());
        endDatePicker.setValue(reservation.getEndDate());
        statusLabel.setText(reservation.getStatus());
        equipmentCombo.getItems().stream()
            .filter(eq -> eq.getEquipmentId().equals(reservation.getEquipmentId()))
            .findFirst().ifPresent(equipmentCombo::setValue);
        customerCombo.getItems().stream()
            .filter(c -> c.getCustomerId().equals(reservation.getCustomerId()))
            .findFirst().ifPresent(customerCombo::setValue);
        branchCombo.getItems().stream()
            .filter(b -> b.getBranchId().equals(reservation.getBranchId()))
            .findFirst().ifPresent(branchCombo::setValue);
        messageLabel.setText("");
    }

    private void clearForm() {
        idField.clear();
        equipmentCombo.setValue(null);
        customerCombo.setValue(null);
        branchCombo.setValue(null);
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        statusLabel.setText("");
        messageLabel.setText("");
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        try {
            reservationList.setAll(reservationService.getAllReservations());
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