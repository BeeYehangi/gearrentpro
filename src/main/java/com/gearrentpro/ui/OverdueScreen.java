package com.gearrentpro.ui;

import com.gearrentpro.entity.Rental;
import com.gearrentpro.entity.SystemUser;
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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class OverdueScreen {

    private RentalService rentalService = new RentalService();
    private TableView<Rental> table = new TableView<>();
    private ObservableList<Rental> rentalList = FXCollections.observableArrayList();
    private Label messageLabel = new Label();
    private Label countLabel = new Label();

    public void show(Stage stage) {
        stage.setTitle("GearRent Pro - Overdue Rentals");

        HBox topBar = createTopBar(stage);
        setupTable();

        countLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        countLabel.setTextFill(Color.RED);
        countLabel.setPadding(new Insets(10, 20, 5, 20));

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle(
            "-fx-background-color: #2980b9; -fx-text-fill: white;");
        refreshBtn.setOnAction(e -> loadData());

        HBox actionBar = new HBox(10, countLabel, refreshBtn);
        actionBar.setPadding(new Insets(10, 20, 5, 20));
        actionBar.setAlignment(Pos.CENTER_LEFT);

        VBox center = new VBox(5, actionBar, table, messageLabel);
        center.setPadding(new Insets(10));

        BorderPane layout = new BorderPane();
        layout.setTop(topBar);
        layout.setCenter(center);

        loadData();

        Scene scene = new Scene(layout, 900, 600);
        stage.setScene(scene);
        stage.show();
    }

    private HBox createTopBar(Stage stage) {
        Label title = new Label("Overdue Rentals");
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
        idCol.setPrefWidth(100);

        TableColumn<Rental, String> custCol = new TableColumn<>("Customer");
        custCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        custCol.setPrefWidth(110);

        TableColumn<Rental, String> equipCol = new TableColumn<>("Equipment");
        equipCol.setCellValueFactory(new PropertyValueFactory<>("equipmentId"));
        equipCol.setPrefWidth(110);

        TableColumn<Rental, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branchId"));
        branchCol.setPrefWidth(100);

        TableColumn<Rental, LocalDate> endCol = new TableColumn<>("Due Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        endCol.setPrefWidth(110);

        // Days overdue column
        TableColumn<Rental, String> daysCol = new TableColumn<>("Days Overdue");
        daysCol.setPrefWidth(110);
        daysCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Rental rental = (Rental) getTableRow().getItem();
                    long days = ChronoUnit.DAYS.between(
                        rental.getEndDate(), LocalDate.now());
                    setText(days + " days");
                    setTextFill(Color.RED);
                    setFont(Font.font("Arial", FontWeight.BOLD, 13));
                }
            }
        });

        table.getColumns().addAll(idCol, custCol, equipCol, 
                                   branchCol, endCol, daysCol);
        table.setItems(rentalList);

        // Row coloring for overdue
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Rental rental, boolean empty) {
                super.updateItem(rental, empty);
                if (rental == null || empty) {
                    setStyle("");
                } else {
                    setStyle("-fx-background-color: #fdecea;");
                }
            }
        });
    }

    private void loadData() {
        try {
            List<Rental> overdueRentals = rentalService.getOverdueRentals();
            rentalList.setAll(overdueRentals);
            countLabel.setText("Total Overdue: " + overdueRentals.size() + " rental(s)");
        } catch (Exception ex) {
            messageLabel.setText(ex.getMessage());
            messageLabel.setTextFill(Color.RED);
        }
    }
}