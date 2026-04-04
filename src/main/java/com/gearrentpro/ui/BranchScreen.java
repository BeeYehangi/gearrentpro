package com.gearrentpro.ui;

import com.gearrentpro.controller.AuthController;
import com.gearrentpro.entity.Branch;
import com.gearrentpro.entity.SystemUser;
import com.gearrentpro.service.BranchService;
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

import java.util.List;

public class BranchScreen {

    private BranchService branchService = new BranchService();
    private TableView<Branch> table = new TableView<>();
    private ObservableList<Branch> branchList = FXCollections.observableArrayList();

    private TextField idField = new TextField();
    private TextField nameField = new TextField();
    private TextField addressField = new TextField();
    private TextField phoneField = new TextField();
    private TextField emailField = new TextField();
    private Label messageLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Manage Branches");

        // Top bar
        HBox topBar = createTopBar(stage);

        // Table
        setupTable();

        // Form
        VBox form = createForm();

        // Buttons
        HBox buttons = createButtons();

        VBox rightPanel = new VBox(15, new Label("Branch Details"), 
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

        loadData();

        Scene scene = new Scene(layout, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar(Stage stage) {
        Label title = new Label("Manage Branches");
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
        TableColumn<Branch, String> idCol = new TableColumn<>("Branch ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("branchId"));
        idCol.setPrefWidth(90);

        TableColumn<Branch, String> nameCol = new TableColumn<>("Branch Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("branchName"));
        nameCol.setPrefWidth(150);

        TableColumn<Branch, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setPrefWidth(180);

        TableColumn<Branch, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("contactPhone"));
        phoneCol.setPrefWidth(110);

        TableColumn<Branch, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
        emailCol.setPrefWidth(160);

        table.getColumns().addAll(idCol, nameCol, addressCol, phoneCol, emailCol);
        table.setItems(branchList);

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateForm(newVal);
            });
    }

    private VBox createForm() {
        idField.setPromptText("e.g. BR004");
        nameField.setPromptText("Branch name");
        addressField.setPromptText("Address");
        phoneField.setPromptText("Phone number");
        emailField.setPromptText("Email address");

        VBox form = new VBox(8,
            new Label("Branch ID:"), idField,
            new Label("Branch Name:"), nameField,
            new Label("Address:"), addressField,
            new Label("Phone:"), phoneField,
            new Label("Email:"), emailField
        );
        return form;
    }

    private HBox createButtons() {
        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update");
        Button deleteBtn = new Button("Delete");
        Button clearBtn = new Button("Clear");

        String btnStyle = "-fx-text-fill: white; -fx-border-radius: 5; " +
                          "-fx-background-radius: 5; -fx-pref-width: 70;";
        addBtn.setStyle("-fx-background-color: #27ae60;" + btnStyle);
        updateBtn.setStyle("-fx-background-color: #2980b9;" + btnStyle);
        deleteBtn.setStyle("-fx-background-color: #e74c3c;" + btnStyle);
        clearBtn.setStyle("-fx-background-color: #95a5a6;" + btnStyle);

        addBtn.setOnAction(e -> handleAdd());
        updateBtn.setOnAction(e -> handleUpdate());
        deleteBtn.setOnAction(e -> handleDelete());
        clearBtn.setOnAction(e -> clearForm());

        HBox buttons = new HBox(8, addBtn, updateBtn, deleteBtn, clearBtn);
        return buttons;
    }

    private void handleAdd() {
        try {
            Branch branch = getBranchFromForm();
            branchService.addBranch(branch);
            showMessage("Branch added successfully!", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void handleUpdate() {
        try {
            Branch branch = getBranchFromForm();
            branchService.updateBranch(branch);
            showMessage("Branch updated successfully!", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void handleDelete() {
        try {
            String id = idField.getText().trim();
            if (id.isEmpty()) {
                showMessage("Select a branch to delete.", true);
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete branch " + id + "?", ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    try {
                        branchService.deleteBranch(id);
                        showMessage("Branch deleted.", false);
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

    private Branch getBranchFromForm() {
        Branch branch = new Branch();
        branch.setBranchId(idField.getText().trim());
        branch.setBranchName(nameField.getText().trim());
        branch.setAddress(addressField.getText().trim());
        branch.setContactPhone(phoneField.getText().trim());
        branch.setContactEmail(emailField.getText().trim());
        return branch;
    }

    private void populateForm(Branch branch) {
        idField.setText(branch.getBranchId());
        nameField.setText(branch.getBranchName());
        addressField.setText(branch.getAddress());
        phoneField.setText(branch.getContactPhone());
        emailField.setText(branch.getContactEmail());
        messageLabel.setText("");
    }

    private void clearForm() {
        idField.clear();
        nameField.clear();
        addressField.clear();
        phoneField.clear();
        emailField.clear();
        messageLabel.setText("");
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        try {
            List<Branch> branches = branchService.getAllBranches();
            branchList.setAll(branches);
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}