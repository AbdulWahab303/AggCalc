import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import java.io.IOException;

public class CalculateAggregateSceneController {

    @FXML
    private Label answer;

    @FXML
    private Button pucitLink;

    @FXML
    private Button nustLink;

    @FXML
    private Button fastLink;

    @FXML
    private Button exit;
    @FXML
    private Label errorLabel;
    @FXML
    private ComboBox<HBox> universityBox;

    @FXML
    private TextField matricMarks;

    @FXML
    private TextField fscMarks;

    @FXML
    private RadioButton satButton;

    @FXML
    private RadioButton ntsButton;

    @FXML
    private RadioButton natButton;

    @FXML
    private TextField entryTest;

    @FXML
    private CheckBox hafizCheckBox;

    @FXML
    private Button calculateButton;

    private ToggleGroup testGroup;

    @FXML
    public void initialize() {

        ObservableList<HBox> items = FXCollections.observableArrayList();
        items.add(createItem("FAST", "images/fast.jpg"));
        items.add(createItem("NUST", "images/nust.jpg"));
        items.add(createItem("PUCIT", "images/pucit.jpg"));

        universityBox.setItems(items);


        testGroup = new ToggleGroup();
        satButton.setToggleGroup(testGroup);
        ntsButton.setToggleGroup(testGroup);
        natButton.setToggleGroup(testGroup);


        disableFields();


        universityBox.setOnAction(e -> handleUniversitySelection());


        calculateButton.setOnAction(e -> calculateAggregate());

        setNumericField(matricMarks, 1100);
        setNumericField(fscMarks, 1100);
        setRadioButtonListeners();
        errorLabel.setText("");
        exit.setOnAction(e->switchScene(e,"MainScene.fxml"));
        pucitLink.setOnAction(e->openWebPage("https://pucit.edu.pk/"));
        nustLink.setOnAction(e->openWebPage("https://nust.edu.pk/"));
        fastLink.setOnAction(e->openWebPage("https://www.nu.edu.pk/"));

    }
    private void openWebPage(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Desktop is not supported on your system.");
        }
    }
    private void switchScene(ActionEvent e, String fxmlFile) {
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

    private void disableFields() {
        matricMarks.setDisable(true);
        fscMarks.setDisable(true);
        satButton.setDisable(true);
        ntsButton.setDisable(true);
        natButton.setDisable(true);
        entryTest.setDisable(true);
        hafizCheckBox.setDisable(true);
        calculateButton.setDisable(true);
    }

    private void enableFields() {
        matricMarks.setDisable(false);
        fscMarks.setDisable(false);
        satButton.setDisable(false);
        ntsButton.setDisable(false);
        natButton.setDisable(false);
        hafizCheckBox.setDisable(false);
        calculateButton.setDisable(false);
    }

    private void handleUniversitySelection() {
        String selectedUniversity = ((Label) ((HBox) universityBox.getValue()).getChildren().get(1)).getText();

        enableFields();

        satButton.setVisible(false);
        ntsButton.setVisible(false);
        natButton.setVisible(false);

        // Adjust test buttons based on the selected university
        if ("FAST".equals(selectedUniversity)) {
            satButton.setVisible(true);
            ntsButton.setVisible(true);
            ntsButton.setText("NTS");
            natButton.setVisible(true);
            natButton.setText("NAT");

        } else if ("NUST".equals(selectedUniversity)) {
            satButton.setVisible(true);
            ntsButton.setVisible(true);
            ntsButton.setText("NU");
            natButton.setVisible(false);

        } else if ("PUCIT".equals(selectedUniversity)) {
            satButton.setVisible(false);
            ntsButton.setVisible(true);
            ntsButton.setText("NTS");
            natButton.setVisible(true);
            natButton.setText("USAT");

        }
    }

    private void setRadioButtonListeners() {
        testGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                entryTest.setDisable(false);

                RadioButton selectedRadioButton = (RadioButton) newToggle;
                switch (selectedRadioButton.getText()) {
                    case "NTS":
                        entryTest.setPromptText("Enter Marks out of 100");
                        setNumericField(entryTest, 100);
                        break;
                    case "SAT":
                        entryTest.setPromptText("Enter SAT Score");
                        setNumericField(entryTest, 1600);
                        break;
                    case "NAT":
                        entryTest.setPromptText("Enter NAT Score");
                        setNumericField(entryTest, 100);
                        break;
                    default:
                        entryTest.setPromptText("Enter your score");
                        break;
                }
            } else {
                entryTest.setDisable(true);
            }
        });
    }


    private HBox createItem(String universityName, String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitHeight(40);
        imageView.setFitWidth(40);
        HBox hBox = new HBox(imageView, new Label(universityName));
        hBox.setSpacing(10);
        return hBox;
    }

    private void setNumericField(TextField textField, int marks) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textField.setText(newValue.replaceAll("[^\\d]", ""));
            }
            if (!newValue.isEmpty()) {
                try {
                    int value = Integer.parseInt(newValue);
                    if (value < 0 || value > marks) {
                        if (!textField.getStyleClass().contains("text-field-invalid")) {
                            textField.getStyleClass().add("text-field-invalid");
                        }
                    } else {
                        textField.getStyleClass().removeAll("text-field-invalid");
                    }
                } catch (NumberFormatException e) {
                    if (!textField.getStyleClass().contains("text-field-invalid")) {
                        textField.getStyleClass().add("text-field-invalid");
                    }
                }
            } else {
                textField.getStyleClass().removeAll("text-field-invalid");
            }
        });
    }

    private void calculateAggregate() {
        double aggregate = 0.0;

        String selectedUniversity = ((Label) ((HBox) universityBox.getValue()).getChildren().get(1)).getText();
        RadioButton selectedTestButton = (RadioButton) testGroup.getSelectedToggle();

        if (matricMarks.getText().isEmpty() || fscMarks.getText().isEmpty() || entryTest.getText().isEmpty() || selectedTestButton == null) {
            errorLabel.setText("Please fill in all required fields.");
            return;
        }

        // Define total scores for each entry test
        int totalScore = 0;
        String selectedTest = selectedTestButton.getText();

        switch (selectedTest) {
            case "NTS":
                totalScore = 100;
                break;
            case "SAT":
                totalScore = 1600;
                break;
            case "NAT":
                totalScore = 100;
                break;
            default:
                errorLabel.setText("Unknown entry test selected.");
                return;
        }

        try {
            // Calculate aggregate based on selected university
            int matric = Integer.parseInt(matricMarks.getText());
            int fsc = Integer.parseInt(fscMarks.getText());
            int entry = Integer.parseInt(entryTest.getText());

            System.out.println("Matric Marks: " + matric);
            System.out.println("FSC Marks: " + fsc);
            System.out.println("Entry Test Score: " + entry);

            if ("FAST".equals(selectedUniversity)) {
                aggregate += (matric / 1100.0) * 100 * (15.0 / 100);
                aggregate += (fsc / 1100.0) * 100 * (35.0 / 100);
                aggregate += (entry / (double) totalScore) * 100 * (50.0 / 100);
            } else if ("NUST".equals(selectedUniversity)) {
                aggregate += (matric / 1100.0) * 100 * (20.0 / 100);
                aggregate += (fsc / 1100.0) * 100 * (30.0 / 100);
                aggregate += (entry / (double) totalScore) * 100 * (50.0 / 100);
            } else if ("PUCIT".equals(selectedUniversity)) {
                aggregate += (matric / 1100.0) * 100 * (25.0 / 100);
                aggregate += (fsc / 1100.0) * 100 * (25.0 / 100);
                aggregate += (entry / (double) totalScore) * 100 * (50.0 / 100);
            } else {
                errorLabel.setText("Unknown university selected.");
                return;
            }
        } catch (NumberFormatException e) {
            errorLabel.setText("Invalid input: " + e.getMessage());
            return;
        }

        String roundedAggregate = String.format("%.2f", aggregate);

        System.out.println("Aggregate before rounding: " + aggregate);
        System.out.println("Rounded Aggregate: " + roundedAggregate);

        answer.setText("Your Score is : " + roundedAggregate);
        errorLabel.setText("");
    }

}
