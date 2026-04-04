package com.gearrentpro.ui;

import com.gearrentpro.entity.Category;
import com.gearrentpro.entity.SystemUser;
import com.gearrentpro.service.CategoryService;
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

public class CategoryScreen {

    private CategoryService categoryService = new CategoryService();
    private TableView<Category> table = new TableView<>();
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();

    private TextField idField = new TextField();
    private TextField nameField = new TextField();
    private TextField descField = new TextField();
    private TextField priceFactorField = new TextField();
    private TextField weekendMultField = new TextField();
    private TextField lateFeeField = new TextField();
    private CheckBox activeCheck = new CheckBox("Active");
    private Label messageLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Manage Categories");

        HBox topBar = createTopBar(stage);
        setupTable();
        VBox form = createForm();
        HBox buttons = createButtons();

        VBox rightPanel = new VBox(15, new Label("Category Details"),
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
        Label title = new Label("Manage Categories");
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
        TableColumn<Category, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        idCol.setPrefWidth(70);

        TableColumn<Category, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        nameCol.setPrefWidth(120);

        TableColumn<Category, BigDecimal> factorCol = new TableColumn<>("Price Factor");
        factorCol.setCellValueFactory(new PropertyValueFactory<>("basePriceFactor"));
        factorCol.setPrefWidth(100);

        TableColumn<Category, BigDecimal> weekendCol = new TableColumn<>("Weekend Mult.");
        weekendCol.setCellValueFactory(new PropertyValueFactory<>("weekendMultiplier"));
        weekendCol.setPrefWidth(110);

        TableColumn<Category, BigDecimal> lateFeeCol = new TableColumn<>("Late Fee/Day");
        lateFeeCol.setCellValueFactory(new PropertyValueFactory<>("lateFeePerDay"));
        lateFeeCol.setPrefWidth(110);

        TableColumn<Category, Boolean> activeCol = new TableColumn<>("Active");
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        activeCol.setPrefWidth(70);

        table.getColumns().addAll(idCol, nameCol, factorCol, 
                                   weekendCol, lateFeeCol, activeCol);
        table.setItems(categoryList);

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateForm(newVal);
            });
    }

    private VBox createForm() {
        idField.setPromptText("e.g. CAT001");
        nameField.setPromptText("Category name");
        descField.setPromptText("Description");
        priceFactorField.setPromptText("e.g. 1.50");
        weekendMultField.setPromptText("e.g. 1.20");
        lateFeeField.setPromptText("e.g. 1500.00");
        activeCheck.setSelected(true);

        VBox form = new VBox(8,
            new Label("Category ID:"), idField,
            new Label("Name:"), nameField,
            new Label("Description:"), descField,
            new Label("Base Price Factor:"), priceFactorField,
            new Label("Weekend Multiplier:"), weekendMultField,
            new Label("Late Fee Per Day:"), lateFeeField,
            activeCheck
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
            Category category = getCategoryFromForm();
            categoryService.addCategory(category);
            showMessage("Category added successfully!", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void handleUpdate() {
        try {
            Category category = getCategoryFromForm();
            categoryService.updateCategory(category);
            showMessage("Category updated successfully!", false);
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
                showMessage("Select a category first.", true);
                return;
            }
            categoryService.deactivateCategory(id);
            showMessage("Category deactivated.", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private Category getCategoryFromForm() {
        Category category = new Category();
        category.setCategoryId(idField.getText().trim());
        category.setCategoryName(nameField.getText().trim());
        category.setDescription(descField.getText().trim());
        category.setBasePriceFactor(new BigDecimal(
            priceFactorField.getText().trim()));
        category.setWeekendMultiplier(new BigDecimal(
            weekendMultField.getText().trim()));
        category.setLateFeePerDay(new BigDecimal(
            lateFeeField.getText().trim()));
        category.setActive(activeCheck.isSelected());
        return category;
    }

    private void populateForm(Category category) {
        idField.setText(category.getCategoryId());
        nameField.setText(category.getCategoryName());
        descField.setText(category.getDescription());
        priceFactorField.setText(category.getBasePriceFactor().toString());
        weekendMultField.setText(category.getWeekendMultiplier().toString());
        lateFeeField.setText(category.getLateFeePerDay().toString());
        activeCheck.setSelected(category.isActive());
        messageLabel.setText("");
    }

    private void clearForm() {
        idField.clear();
        nameField.clear();
        descField.clear();
        priceFactorField.clear();
        weekendMultField.clear();
        lateFeeField.clear();
        activeCheck.setSelected(true);
        messageLabel.setText("");
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            categoryList.setAll(categories);
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}