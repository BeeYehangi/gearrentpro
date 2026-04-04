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
import java.util.List;

public class EquipmentScreen {

    private EquipmentService equipmentService = new EquipmentService();
    private CategoryService categoryService = new CategoryService();
    private BranchService branchService = new BranchService();

    private TableView<Equipment> table = new TableView<>();
    private ObservableList<Equipment> equipmentList = FXCollections.observableArrayList();

    private TextField idField = new TextField();
    private TextField brandField = new TextField();
    private TextField modelField = new TextField();
    private TextField serialField = new TextField();
    private TextField yearField = new TextField();
    private TextField priceField = new TextField();
    private TextField depositField = new TextField();
    private TextField notesField = new TextField();
    private ComboBox<Category> categoryCombo = new ComboBox<>();
    private ComboBox<Branch> branchCombo = new ComboBox<>();
    private ComboBox<String> statusCombo = new ComboBox<>();
    private Label messageLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Manage Equipment");

        HBox topBar = createTopBar(stage);
        setupTable();
        VBox form = createForm();
        HBox buttons = createButtons();

        ScrollPane formScroll = new ScrollPane(form);
        formScroll.setFitToWidth(true);
        formScroll.setPrefHeight(450);

        VBox rightPanel = new VBox(10, new Label("Equipment Details"),
                                    formScroll, buttons, messageLabel);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setPrefWidth(340);
        rightPanel.setStyle("-fx-background-color: white;");

        HBox content = new HBox(10, table, rightPanel);
        HBox.setHgrow(table, Priority.ALWAYS);
        content.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(content);

        loadCategories();
        loadBranches();
        loadData();

        Scene scene = new Scene(layout, 1000, 650);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar(Stage stage) {
        Label title = new Label("Manage Equipment");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.WHITE);

        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        backBtn.setOnAction(e -> {
            SystemUser user = SessionManager.getInstance().getLoggedInUser();
            new DashboardScreen().show(stage, user);
        });

        // Filter by branch
        ComboBox<Branch> filterBranch = new ComboBox<>();
        filterBranch.setPromptText("Filter by branch");
        try {
            filterBranch.setItems(FXCollections.observableArrayList(
                branchService.getAllBranches()));
        } catch (Exception ex) { }

        filterBranch.setOnAction(e -> {
            Branch selected = filterBranch.getValue();
            if (selected != null) {
                try {
                    equipmentList.setAll(
                        equipmentService.getEquipmentByBranch(
                            selected.getBranchId()));
                } catch (Exception ex) {
                    showMessage(ex.getMessage(), true);
                }
            }
        });

        Button showAllBtn = new Button("Show All");
        showAllBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        showAllBtn.setOnAction(e -> loadData());

        HBox topBar = new HBox(10);
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setStyle("-fx-background-color: #2c3e50;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().addAll(title, spacer, 
            filterBranch, showAllBtn, backBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        return topBar;
    }

    private void setupTable() {
        TableColumn<Equipment, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        idCol.setPrefWidth(80);

        TableColumn<Equipment, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brandCol.setPrefWidth(100);

        TableColumn<Equipment, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setPrefWidth(120);

        TableColumn<Equipment, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        categoryCol.setPrefWidth(90);

        TableColumn<Equipment, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branchId"));
        branchCol.setPrefWidth(80);

        TableColumn<Equipment, BigDecimal> priceCol = new TableColumn<>("Daily Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("baseDailyPrice"));
        priceCol.setPrefWidth(90);

        TableColumn<Equipment, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);

        table.getColumns().addAll(idCol, brandCol, modelCol, 
                                   categoryCol, branchCol, priceCol, statusCol);
        table.setItems(equipmentList);

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateForm(newVal);
            });
    }

    private VBox createForm() {
        idField.setPromptText("e.g. EQ001");
        brandField.setPromptText("Brand name");
        modelField.setPromptText("Model name");
        serialField.setPromptText("Serial number");
        yearField.setPromptText("e.g. 2022");
        priceField.setPromptText("e.g. 5000.00");
        depositField.setPromptText("e.g. 25000.00");
        notesField.setPromptText("Optional notes");

        statusCombo.setItems(FXCollections.observableArrayList(
            "AVAILABLE", "RESERVED", "RENTED", "UNDER_MAINTENANCE"));
        statusCombo.setValue("AVAILABLE");

        VBox form = new VBox(8,
            new Label("Equipment ID:"), idField,
            new Label("Category:"), categoryCombo,
            new Label("Branch:"), branchCombo,
            new Label("Brand:"), brandField,
            new Label("Model:"), modelField,
            new Label("Serial Number:"), serialField,
            new Label("Purchase Year:"), yearField,
            new Label("Base Daily Price:"), priceField,
            new Label("Deposit Amount:"), depositField,
            new Label("Status:"), statusCombo,
            new Label("Notes:"), notesField
        );
        form.setPadding(new Insets(5));
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

        return new HBox(8, addBtn, updateBtn, deleteBtn, clearBtn);
    }

    private void handleAdd() {
        try {
            Equipment equipment = getEquipmentFromForm();
            equipmentService.addEquipment(equipment);
            showMessage("Equipment added successfully!", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void handleUpdate() {
        try {
            Equipment equipment = getEquipmentFromForm();
            equipmentService.updateEquipment(equipment);
            showMessage("Equipment updated successfully!", false);
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
                showMessage("Select equipment first.", true);
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete equipment " + id + "?",
                ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    try {
                        equipmentService.deleteEquipment(id);
                        showMessage("Equipment deleted.", false);
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

    private Equipment getEquipmentFromForm() {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(idField.getText().trim());
        equipment.setBrand(brandField.getText().trim());
        equipment.setModel(modelField.getText().trim());
        equipment.setSerialNumber(serialField.getText().trim());
        equipment.setPurchaseYear(Integer.parseInt(yearField.getText().trim()));
        equipment.setBaseDailyPrice(new BigDecimal(priceField.getText().trim()));
        equipment.setDepositAmount(new BigDecimal(depositField.getText().trim()));
        equipment.setNotes(notesField.getText().trim());
        equipment.setStatus(statusCombo.getValue());
        Category selectedCat = categoryCombo.getValue();
        equipment.setCategoryId(selectedCat != null ? 
            selectedCat.getCategoryId() : "");
        Branch selectedBranch = branchCombo.getValue();
        equipment.setBranchId(selectedBranch != null ? 
            selectedBranch.getBranchId() : "");
        return equipment;
    }

    private void populateForm(Equipment equipment) {
        idField.setText(equipment.getEquipmentId());
        brandField.setText(equipment.getBrand());
        modelField.setText(equipment.getModel());
        serialField.setText(equipment.getSerialNumber() != null ? 
            equipment.getSerialNumber() : "");
        yearField.setText(String.valueOf(equipment.getPurchaseYear()));
        priceField.setText(equipment.getBaseDailyPrice().toString());
        depositField.setText(equipment.getDepositAmount().toString());
        notesField.setText(equipment.getNotes() != null ? equipment.getNotes() : "");
        statusCombo.setValue(equipment.getStatus());
        categoryCombo.getItems().stream()
            .filter(c -> c.getCategoryId().equals(equipment.getCategoryId()))
            .findFirst().ifPresent(categoryCombo::setValue);
        branchCombo.getItems().stream()
            .filter(b -> b.getBranchId().equals(equipment.getBranchId()))
            .findFirst().ifPresent(branchCombo::setValue);
        messageLabel.setText("");
    }

    private void clearForm() {
        idField.clear(); brandField.clear(); modelField.clear();
        serialField.clear(); yearField.clear(); priceField.clear();
        depositField.clear(); notesField.clear();
        statusCombo.setValue("AVAILABLE");
        categoryCombo.setValue(null);
        branchCombo.setValue(null);
        messageLabel.setText("");
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        try {
            equipmentList.setAll(equipmentService.getAllEquipment());
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void loadCategories() {
        try {
            categoryCombo.setItems(FXCollections.observableArrayList(
                categoryService.getAllCategories()));
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void loadBranches() {
        try {
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