package com.gearrentpro.ui;

import com.gearrentpro.entity.SystemConfig;
import com.gearrentpro.entity.SystemUser;
import com.gearrentpro.service.SystemConfigService;
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

public class SystemConfigScreen {

    private SystemConfigService configService = new SystemConfigService();
    private TableView<SystemConfig> table = new TableView<>();
    private ObservableList<SystemConfig> configList =
        FXCollections.observableArrayList();

    private TextField keyField = new TextField();
    private TextField valueField = new TextField();
    private TextField descField = new TextField();
    private Label messageLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - System Configuration");

        HBox topBar = createTopBar(stage);
        setupTable();
        VBox form = createForm();
        HBox buttons = createButtons();

        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle(
            "-fx-background-color: #fff3cd; " +
            "-fx-border-color: #ffc107; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5;");
        Label infoLabel = new Label(
            "ℹ Config Keys:\n" +
            "MAX_RENTAL_DAYS — max days per rental\n" +
            "LONG_RENTAL_DAYS — days to qualify for long rental discount\n" +
            "LONG_RENTAL_DISCOUNT — discount % for long rentals\n" +
            "MAX_DEPOSIT_PER_CUSTOMER — max deposit held per customer (LKR)");
        infoLabel.setFont(Font.font("Arial", 11));
        infoLabel.setWrapText(true);
        infoBox.getChildren().add(infoLabel);

        VBox rightPanel = new VBox(12,
            new Label("Edit Configuration"),
            form, buttons, infoBox, messageLabel);
        rightPanel.setPadding(new Insets(20));
        rightPanel.setPrefWidth(350);
        rightPanel.setStyle("-fx-background-color: white;");

        HBox content = new HBox(10, table, rightPanel);
        HBox.setHgrow(table, Priority.ALWAYS);
        content.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(content);

        loadData();

        Scene scene = new Scene(layout, 900, 500);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar(Stage stage) {
        Label title = new Label("System Configuration");
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
        TableColumn<SystemConfig, String> keyCol = new TableColumn<>("Config Key");
        keyCol.setCellValueFactory(new PropertyValueFactory<>("configKey"));
        keyCol.setPrefWidth(200);

        TableColumn<SystemConfig, String> valueCol = new TableColumn<>("Value");
        valueCol.setCellValueFactory(new PropertyValueFactory<>("configValue"));
        valueCol.setPrefWidth(120);

        TableColumn<SystemConfig, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(200);

        table.getColumns().addAll(keyCol, valueCol, descCol);
        table.setItems(configList);

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateForm(newVal);
            });
    }

    private VBox createForm() {
        keyField.setPromptText("Config key");
        keyField.setEditable(false);
        keyField.setStyle("-fx-background-color: #f0f0f0;");
        valueField.setPromptText("Config value");
        descField.setPromptText("Description");
        descField.setEditable(false);
        descField.setStyle("-fx-background-color: #f0f0f0;");

        VBox form = new VBox(8,
            new Label("Config Key:"), keyField,
            new Label("Value:"), valueField,
            new Label("Description:"), descField
        );
        return form;
    }

    private HBox createButtons() {
        Button updateBtn = new Button("Update");
        Button clearBtn = new Button("Clear");

        updateBtn.setStyle(
            "-fx-background-color: #2980b9; -fx-text-fill: white; " +
            "-fx-border-radius: 5; -fx-background-radius: 5; " +
            "-fx-pref-width: 100;");
        clearBtn.setStyle(
            "-fx-background-color: #95a5a6; -fx-text-fill: white; " +
            "-fx-border-radius: 5; -fx-background-radius: 5; " +
            "-fx-pref-width: 100;");

        updateBtn.setOnAction(e -> handleUpdate());
        clearBtn.setOnAction(e -> clearForm());

        return new HBox(8, updateBtn, clearBtn);
    }

    private void handleUpdate() {
        try {
            String key = keyField.getText().trim();
            String value = valueField.getText().trim();

            if (key.isEmpty()) {
                showMessage("Select a config item first.", true);
                return;
            }
            if (value.isEmpty()) {
                showMessage("Value cannot be empty.", true);
                return;
            }

            // Validate numeric values
            try {
                Double.parseDouble(value);
            } catch (NumberFormatException ex) {
                showMessage("Value must be a number.", true);
                return;
            }

            SystemConfig config = new SystemConfig();
            config.setConfigKey(key);
            config.setConfigValue(value);
            configService.updateConfig(config);
            showMessage("Configuration updated successfully!", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void populateForm(SystemConfig config) {
        keyField.setText(config.getConfigKey());
        valueField.setText(config.getConfigValue());
        descField.setText(config.getDescription());
        messageLabel.setText("");
    }

    private void clearForm() {
        keyField.clear();
        valueField.clear();
        descField.clear();
        messageLabel.setText("");
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        try {
            List<SystemConfig> configs = configService.getAllConfigs();
            configList.setAll(configs);
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}