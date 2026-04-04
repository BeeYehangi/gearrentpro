package com.gearrentpro.ui;

import com.gearrentpro.entity.Customer;
import com.gearrentpro.entity.Membership;
import com.gearrentpro.entity.SystemUser;
import com.gearrentpro.service.CustomerService;
import com.gearrentpro.service.MembershipService;
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
import java.util.List;

public class CustomerScreen {

    private CustomerService customerService = new CustomerService();
    private MembershipService membershipService = new MembershipService();
    private TableView<Customer> table = new TableView<>();
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    private TextField idField = new TextField();
    private TextField nicField = new TextField();
    private TextField nameField = new TextField();
    private TextField emailField = new TextField();
    private TextField phoneField = new TextField();
    private TextField addressField = new TextField();
    private ComboBox<Membership> membershipCombo = new ComboBox<>();
    private Label messageLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Manage Customers");

        HBox topBar = createTopBar(stage);
        setupTable();
        VBox form = createForm();
        HBox buttons = createButtons();

        VBox rightPanel = new VBox(10, new Label("Customer Details"),
                                    form, buttons, messageLabel);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setPrefWidth(320);
        rightPanel.setStyle("-fx-background-color: white;");

        HBox content = new HBox(10, table, rightPanel);
        HBox.setHgrow(table, Priority.ALWAYS);
        content.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(content);

        loadMemberships();
        loadData();

        Scene scene = new Scene(layout, 950, 620);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar(Stage stage) {
        Label title = new Label("Manage Customers");
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
        TableColumn<Customer, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        idCol.setPrefWidth(80);

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        nameCol.setPrefWidth(150);

        TableColumn<Customer, String> nicCol = new TableColumn<>("NIC/Passport");
        nicCol.setCellValueFactory(new PropertyValueFactory<>("nicPassport"));
        nicCol.setPrefWidth(120);

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        phoneCol.setPrefWidth(110);

        TableColumn<Customer, String> membershipCol = new TableColumn<>("Membership");
        membershipCol.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        membershipCol.setPrefWidth(100);

        TableColumn<Customer, BigDecimal> depositCol = new TableColumn<>("Deposit Held");
        depositCol.setCellValueFactory(new PropertyValueFactory<>("totalDepositHeld"));
        depositCol.setPrefWidth(110);

        table.getColumns().addAll(idCol, nameCol, nicCol, 
                                   phoneCol, membershipCol, depositCol);
        table.setItems(customerList);

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateForm(newVal);
            });
    }

    private VBox createForm() {
        idField.setPromptText("e.g. C001");
        nicField.setPromptText("NIC or Passport");
        nameField.setPromptText("Full name");
        emailField.setPromptText("Email address");
        phoneField.setPromptText("Phone number");
        addressField.setPromptText("Address");

        VBox form = new VBox(8,
            new Label("Customer ID:"), idField,
            new Label("NIC/Passport:"), nicField,
            new Label("Full Name:"), nameField,
            new Label("Email:"), emailField,
            new Label("Phone:"), phoneField,
            new Label("Address:"), addressField,
            new Label("Membership:"), membershipCombo
        );
        return form;
    }

    private HBox createButtons() {
        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deactivateBtn = new Button("Deactivate");
        Button clearBtn = new Button("Clear");

        String btnStyle = "-fx-text-fill: white; -fx-border-radius: 5; " +
                          "-fx-background-radius: 5; -fx-pref-width: 80;";
        addBtn.setStyle("-fx-background-color: #27ae60;" + btnStyle);
        updateBtn.setStyle("-fx-background-color: #2980b9;" + btnStyle);
        deactivateBtn.setStyle("-fx-background-color: #e74c3c;" + btnStyle);
        clearBtn.setStyle("-fx-background-color: #95a5a6;" + btnStyle);

        addBtn.setOnAction(e -> handleAdd());
        updateBtn.setOnAction(e -> handleUpdate());
        deactivateBtn.setOnAction(e -> handleDeactivate());
        clearBtn.setOnAction(e -> clearForm());

        return new HBox(8, addBtn, updateBtn, deactivateBtn, clearBtn);
    }

    private void handleAdd() {
        try {
            Customer customer = getCustomerFromForm();
            customerService.addCustomer(customer);
            showMessage("Customer added successfully!", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void handleUpdate() {
        try {
            Customer customer = getCustomerFromForm();
            customerService.updateCustomer(customer);
            showMessage("Customer updated successfully!", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void handleDeactivate() {
        try {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                showMessage("Select a customer first.", true);
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Deactivate customer " + id + "?",
                ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    try {
                        customerService.deactivateCustomer(id);
                        showMessage("Customer deactivated.", false);
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

    private Customer getCustomerFromForm() {
        Customer customer = new Customer();
        customer.setCustomerId(idField.getText().trim());
        customer.setNicPassport(nicField.getText().trim());
        customer.setFullName(nameField.getText().trim());
        customer.setEmail(emailField.getText().trim());
        customer.setPhone(phoneField.getText().trim());
        customer.setAddress(addressField.getText().trim());
        Membership selected = membershipCombo.getValue();
        customer.setMembershipId(selected != null ? 
            selected.getMembershipId() : "REG");
        customer.setTotalDepositHeld(BigDecimal.ZERO);
        customer.setActive(true);
        return customer;
    }

    private void populateForm(Customer customer) {
        idField.setText(customer.getCustomerId());
        nicField.setText(customer.getNicPassport());
        nameField.setText(customer.getFullName());
        emailField.setText(customer.getEmail());
        phoneField.setText(customer.getPhone());
        addressField.setText(customer.getAddress());
        membershipCombo.getItems().stream()
            .filter(m -> m.getMembershipId().equals(customer.getMembershipId()))
            .findFirst().ifPresent(membershipCombo::setValue);
        messageLabel.setText("");
    }

    private void clearForm() {
        idField.clear();
        nicField.clear();
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        membershipCombo.setValue(null);
        messageLabel.setText("");
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            customerList.setAll(customers);
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void loadMemberships() {
        try {
            List<Membership> memberships = membershipService.getAllMemberships();
            membershipCombo.setItems(FXCollections.observableArrayList(memberships));
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}