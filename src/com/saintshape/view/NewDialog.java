package com.saintshape.view;

import com.saintshape.controller.Controller;
import com.saintshape.main.Saintshape;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * This class represents the New dialog when a user clicks the
 * "New..."-button in the File menu in the menu bar.
 *
 * Created by 150019538 on 01/11/15.
 */
public class NewDialog extends Stage {

    private final static String DEFAULT_NAME = "Untitled-1";
    private final static String DEFAULT_WIDTH = "300";
    private final static String DEFAULT_HEIGHT = "300";

    private Stage parentStage;
    private Scene scene;
    private Label labelName, labelWidth, labelHeight, labelWidthUnit, labelHeightUnit;
    private TextField inputName, inputWidth, inputHeight;
    private Button buttonOK, buttonCancel;
    private Controller controller;

    public NewDialog(Controller controller, Stage parentStage) {
        super();
        this.controller = controller;
        this.parentStage = parentStage;
        initialize();
    }

    /**
     * Initializes the NewDialog and displays it to the user
     */
    private void initialize() {

        // Setup the stage
        setTitle("New");
        setResizable(false);
        initStyle(StageStyle.UTILITY);
        initModality(Modality.APPLICATION_MODAL);
        initOwner(parentStage);

        // Create input fields and buttons
        GridPane gridInputs = createGridInputs();
        GridPane gridButtons = createGridButtons();

        // Add them to stage and display stage
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 20, 20, 20));
        hbox.getChildren().add(gridInputs);
        hbox.getChildren().add(gridButtons);
        scene = new Scene(hbox);
        setScene(scene);
        show();

    }

    /**
     * Creates and styles a GridPane that contains name, width and height input fields
     * @return Returns a GridPane containing the labels and text inputs
     */
    private GridPane createGridInputs() {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 20, 0, 0));

        labelName = new Label("Name: ");
        labelWidth = new Label("Width: ");
        labelHeight = new Label("Height: ");
        labelWidthUnit = new Label(" px");
        labelHeightUnit = new Label(" px");
        inputName = new TextField(DEFAULT_NAME);
        inputWidth = new TextField(DEFAULT_WIDTH);
        inputHeight = new TextField(DEFAULT_HEIGHT);

        // Ensure that only numbers are entered into width and height fields
        inputWidth.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("\\d*")) {
                    try {
                        Double.parseDouble(newValue);
                    } catch (Exception e) {}
                } else {
                    inputWidth.setText(oldValue);
                }
            }
        });
        inputHeight.textProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue.matches("\\d*")) {
                    try {
                        Double.parseDouble(newValue);
                    } catch (Exception e) {}
                } else {
                    inputHeight.setText(oldValue);
                }
            }
        });

        labelName.setMaxWidth(Double.MAX_VALUE);
        labelWidth.setMaxWidth(Double.MAX_VALUE);
        labelHeight.setMaxWidth(Double.MAX_VALUE);

        labelName.setAlignment(Pos.CENTER_RIGHT);
        labelName.setPrefWidth(labelHeight.getWidth());
        labelWidth.setAlignment(Pos.CENTER_RIGHT);
        labelWidth.setPrefWidth(labelHeight.getWidth());
        labelHeight.setAlignment(Pos.CENTER_RIGHT);

        gridPane.add(labelName, 0, 0, 1, 1);
        gridPane.add(inputName, 1, 0, 2, 1);
        gridPane.add(labelWidth, 0, 1, 1, 1);
        gridPane.add(inputWidth, 1, 1, 1, 1);
        gridPane.add(labelWidthUnit, 2, 1, 1, 1);
        gridPane.add(labelHeight, 0, 2, 1, 1);
        gridPane.add(inputHeight, 1, 2, 1, 1);
        gridPane.add(labelHeightUnit, 2, 2, 1, 1);

        return gridPane;
    }

    /**
     * Creates and styles a GridPane that contains OK and Cancel buttons
     * @return Returns a GridPane containing the buttons
     */
    private GridPane createGridButtons() {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.TOP_CENTER);
        gridPane.setVgap(10);

        buttonOK = new Button("OK");
        buttonCancel = new Button("Cancel");

        // Make buttons are equal width
        HBox.setHgrow(buttonOK, Priority.ALWAYS);
        HBox.setHgrow(buttonCancel, Priority.ALWAYS);
        buttonOK.setMaxWidth(Double.MAX_VALUE);
        buttonCancel.setMaxWidth(Double.MAX_VALUE);

        // Add event handlers to buttons
        buttonOK.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                double width;
                double height;
                String name = inputName.getText().trim();

                // Validate name input
                if(name.length() == 0) {
                    showInvalidInputAlert("Invalid Input", "Please enter a name for the project.");
                    inputName.requestFocus();
                    return;
                }

                // Validate width input
                try {
                    width = Double.parseDouble(inputWidth.getText());
                } catch (NumberFormatException e) {
                    showInvalidInputAlert("Invalid Input", "Invalid numeric entry. Width must be a number.");
                    inputWidth.requestFocus();
                    return;
                }

                // Validate height input
                try {
                    height = Double.parseDouble(inputHeight.getText());
                } catch(NumberFormatException e) {
                    showInvalidInputAlert("Invalid Input", "Invalid numeric entry. Height must be a number.");
                    inputHeight.requestFocus();
                    return;
                }

                // Create a new project with the inputs and close this dialog
                controller.newProject(name, width, height);
                close();
            }
        });
        buttonCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                close();
            }
        });

        gridPane.add(buttonOK, 0, 1);
        gridPane.add(buttonCancel, 0, 2);

        return gridPane;
    }

    /**
     * Displays a simple popup to the user with given title and message with an OK button
     * @param title of popup window
     * @param message displayed in popup window
     */
    private void showInvalidInputAlert(String title, String message) {

        final Stage stage = new Stage();
        stage.setTitle(title);
        stage.setResizable(false);
        stage.initStyle(StageStyle.UTILITY);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(parentStage);

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20, 20, 20, 20));

        Label label = new Label(message);
        Button button = new Button("OK");
        button.setAlignment(Pos.CENTER);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        HBox hbox = new HBox(button);
        hbox.setAlignment(Pos.BOTTOM_CENTER);
        vbox.getChildren().addAll(label, hbox);
        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();

    }

}
