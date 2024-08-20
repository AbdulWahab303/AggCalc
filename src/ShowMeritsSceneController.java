import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ShowMeritsSceneController {
    @FXML
    private Button exit;
    @FXML
    private TableView<UniversityRecord> tableView;
    @FXML
    private TableColumn<UniversityRecord, String> departmentColumn;
    @FXML
    private TableColumn<UniversityRecord, Double> aggregateColumn;
    @FXML
    private TableColumn<UniversityRecord, Integer> yearColumn;
    @FXML
    private ComboBox<UniversityItem> universityComboBox;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @FXML
    public void initialize() {
        // Set up columns
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        aggregateColumn.setCellValueFactory(new PropertyValueFactory<>("aggregate"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Initialize ComboBox with custom cell factory
        universityComboBox.setCellFactory(param -> new ListCell<UniversityItem>() {
            private final HBox hbox = new HBox();
            private final ImageView imageView = new ImageView();
            private final Text text = new Text();

            {
                hbox.getChildren().addAll(imageView, text);
            }

            @Override
            protected void updateItem(UniversityItem item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    imageView.setImage(item.getImage());
                    text.setText(item.getName());
                    setGraphic(hbox);
                }
            }
        });

        universityComboBox.setOnAction(e -> updateTableView());
        loadDataFromFile("src/record/r.txt");

        exit.setOnAction(e->switchScene(e,"MainScene.fxml"));

    }

    private void switchScene(ActionEvent e,String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage)((Node)e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setResizable(false);
            stage.setFullScreen(true);
        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    private void initializeBarChart() {
        xAxis.setLabel("Year");
        yAxis.setLabel("Aggregate");

        // Define years for xAxis
        ObservableList<String> years = FXCollections.observableArrayList();
        for (int year = 2015; year <= 2024; year++) {
            years.add(String.valueOf(year));
        }
        xAxis.setCategories(years);

        // Define sample data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Aggregate by Year");
        barChart.getData().add(series);
    }

    private void loadDataFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            UniversityItem currentItem = null;

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                if (line.equals("FAST") || line.equals("NUST") || line.equals("OTHER_UNIVERSITY")) {
                    currentItem = new UniversityItem(line, "images/" + line.toLowerCase() + ".jpg");
                    universityComboBox.getItems().add(currentItem);
                } else if (currentItem != null) {
                    String[] parts = line.split(" ");
                    if (parts.length == 3) {
                        String department = parts[0];
                        double aggregate = Double.parseDouble(parts[1]);
                        int year = Integer.parseInt(parts[2]);

                        currentItem.addRecord(new UniversityRecord(department, aggregate, year));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateBarChart(UniversityItem universityItem) {
        List<Double> aggregates = universityItem.getAggregates();
        List<Integer> years = universityItem.getYears();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Aggregate by Year");

        // Clear existing data
        barChart.getData().clear();

        // Ensure that the number of years and aggregates match
        if (years.size() == aggregates.size()) {
            for (int i = 0; i < years.size(); i++) {
                series.getData().add(new XYChart.Data<>(years.get(i).toString(), aggregates.get(i)));
            }
        } else {
            System.err.println("Mismatch between years and aggregates count");
        }

        barChart.getData().add(series);
    }

    private void updateTableView() {
        UniversityItem selectedItem = universityComboBox.getValue();
        if (selectedItem != null) {
            // Update TableView
            tableView.setItems(selectedItem.getRecords());

            // Update BarChart
            updateBarChart(selectedItem);
        }
    }

    public static class UniversityRecord {
        private final String department;
        private final double aggregate;
        private final int year;

        public UniversityRecord(String department, double aggregate, int year) {
            this.department = department;
            this.aggregate = aggregate;
            this.year = year;
        }

        public String getDepartment() {
            return department;
        }

        public double getAggregate() {
            return aggregate;
        }

        public int getYear() {
            return year;
        }
    }

    public static class UniversityItem {
        private final String name;
        private final Image image;
        private final ObservableList<UniversityRecord> records;

        public UniversityItem(String name, String imagePath) {
            this.name = name;
            this.image = new Image(imagePath);
            this.records = FXCollections.observableArrayList();
        }

        public String getName() {
            return name;
        }

        public Image getImage() {
            return image;
        }

        public ObservableList<UniversityRecord> getRecords() {
            return records;
        }

        public void addRecord(UniversityRecord record) {
            records.add(record);
        }

        public List<Double> getAggregates() {
            return records.stream()
                    .map(UniversityRecord::getAggregate)
                    .collect(Collectors.toList());
        }

        public List<Integer> getYears() {
            return records.stream()
                    .map(UniversityRecord::getYear)
                    .collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
