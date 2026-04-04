package com.gearrentpro.ui;

import com.gearrentpro.entity.Membership;
import com.gearrentpro.entity.SystemUser;
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

public class MembershipScreen {

    private MembershipService membershipService = new MembershipService();
    private TableView<Membership> table = new TableView<>();
    private ObservableList<Membership> membershipList = 
        FXCollections.observableArrayList();

    private TextField idField = new TextField();
    private TextField levelNameField = new TextField();
    private TextField discountField = new TextField();
    private TextField descriptionField = new TextField();
    private Label messageLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Membership Management");

        HBox topBar = createTopBar(stage);
        setupTable();
        VBox form = createForm();
        HBox buttons = createButtons();

        VBox rightPanel = new VBox(12,
            new Label("Membership Details"),
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

        Scene scene = new Scene(layout, 850, 500);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar(Stage stage) {
        Label title = new Label("Membership Management");
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
        TableColumn<Membership, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        idCol.setPrefWidth(80);

        TableColumn<Membership, String> nameCol = new TableColumn<>("Level Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("levelName"));
        nameCol.setPrefWidth(120);

        TableColumn<Membership, BigDecimal> discountCol = 
            new TableColumn<>("Discount %");
        discountCol.setCellValueFactory(
            new PropertyValueFactory<>("discountPercentage"));
        discountCol.setPrefWidth(100);

        TableColumn<Membership, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        descCol.setPrefWidth(250);

        table.getColumns().addAll(idCol, nameCol, discountCol, descCol);
        table.setItems(membershipList);

        table.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) populateForm(newVal);
            });
    }

    private VBox createForm() {
        idField.setPromptText("e.g. PLT");
        levelNameField.setPromptText("e.g. Platinum");
        discountField.setPromptText("e.g. 15.00");
        descriptionField.setPromptText("Description of membership level");

        // Disable ID field when updating
        idField.setEditable(true);

        VBox form = new VBox(8,
            new Label("Membership ID:"), idField,
            new Label("Level Name:"), levelNameField,
            new Label("Discount %:"), discountField,
            new Label("Description:"), descriptionField
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

        return new HBox(8, addBtn, updateBtn, deleteBtn, clearBtn);
    }

    private void handleAdd() {
        try {
            Membership membership = getMembershipFromForm();
            membershipService.addMembership(membership);
            showMessage("Membership added successfully!", false);
            loadData();
            clearForm();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void handleUpdate() {
        try {
            Membership membership = getMembershipFromForm();
            membershipService.updateMembership(membership);
            showMessage("Membership updated successfully!", false);
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
                showMessage("Select a membership to delete.", true);
                return;
            }
            if (id.equals("REG")) {
                showMessage("Cannot delete Regular membership.", true);
                return;
            }
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete membership " + id + "?",
                ButtonType.YES, ButtonType.NO);
            confirm.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    try {
                        membershipService.deleteMembership(id);
                        showMessage("Membership deleted.", false);
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

    private Membership getMembershipFromForm() {
        Membership membership = new Membership();
        membership.setMembershipId(idField.getText().trim());
        membership.setLevelName(levelNameField.getText().trim());
        membership.setDiscountPercentage(
            new BigDecimal(discountField.getText().trim()));
        membership.setDescription(descriptionField.getText().trim());
        return membership;
    }

    private void populateForm(Membership membership) {
        idField.setText(membership.getMembershipId());
        levelNameField.setText(membership.getLevelName());
        discountField.setText(membership.getDiscountPercentage().toString());
        descriptionField.setText(membership.getDescription());
        messageLabel.setText("");
    }

    private void clearForm() {
        idField.clear();
        levelNameField.clear();
        discountField.clear();
        descriptionField.clear();
        messageLabel.setText("");
        table.getSelectionModel().clearSelection();
    }

    private void loadData() {
        try {
            List<Membership> memberships = membershipService.getAllMemberships();
            membershipList.setAll(memberships);
        } catch (Exception ex) {
            showMessage(ex.getMessage(), true);
        }
    }

    private void showMessage(String msg, boolean isError) {
        messageLabel.setText(msg);
        messageLabel.setTextFill(isError ? Color.RED : Color.GREEN);
    }
}