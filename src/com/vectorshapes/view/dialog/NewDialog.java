package com.vectorshapes.view.dialog;

import com.vectorshapes.controller.Controller;
import com.vectorshapes.view.View;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    private final static String DEFAULT_WIDTH = "500";
    private final static String DEFAULT_HEIGHT = "500";

    private final Stage parentStage;
    private TextField inputName, inputWidth, inputHeight;
    private final View view;
    private final Controller controller;

    public NewDialog(View view, Controller controller, Stage parentStage) {
        super();
        this.view = view;
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
        Scene scene = new Scene(hbox);
        scene.setOnKeyPressed(new EventHandler<javafx.scene.input.KeyEvent>() {
            public void handle(javafx.scene.input.KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE) {
                    close();
                } else if (ke.getCode() == KeyCode.ENTER) {
                    okAction();
                }
            }
        });
        setScene(scene);
        show();
        requestFocus();

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

        Label labelName = new Label("Name: ");
        Label labelWidth = new Label("Width: ");
        Label labelHeight = new Label("Height: ");
        Label labelWidthUnit = new Label(" px");
        Label labelHeightUnit = new Label(" px");
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

        Button buttonOK = new Button("OK");
        Button buttonCancel = new Button("Cancel");

        // Make buttons are equal width
        HBox.setHgrow(buttonOK, Priority.ALWAYS);
        HBox.setHgrow(buttonCancel, Priority.ALWAYS);
        buttonOK.setMaxWidth(Double.MAX_VALUE);
        buttonCancel.setMaxWidth(Double.MAX_VALUE);

        // Add event handlers to buttons
        buttonOK.setOnAction(event -> okAction());
        buttonCancel.setOnAction(event -> close());

        gridPane.add(buttonOK, 0, 1);
        gridPane.add(buttonCancel, 0, 2);

        return gridPane;
    }

    private void okAction() {
        double width;
        double height;
        String name = inputName.getText().trim();

        // Validate name input
        if(name.length() == 0) {
            showInvalidInputAlert("Please enter a name for the project.");
            inputName.requestFocus();
            return;
        }

        // Validate width input
        try {
            width = Double.parseDouble(inputWidth.getText());
        } catch (NumberFormatException e) {
            showInvalidInputAlert("Invalid numeric entry. Width must be a number (min. 1).");
            inputWidth.requestFocus();
            return;
        }

        if(width <= 0) {
            showInvalidInputAlert("Invalid numeric entry. Width must be a number (min. 1).");
            inputWidth.requestFocus();
            return;
        }

        // Validate height input
        try {
            height = Double.parseDouble(inputHeight.getText());
        } catch(NumberFormatException e) {
            showInvalidInputAlert("Invalid numeric entry. Height must be a number (min. 1).");
            inputHeight.requestFocus();
            return;
        }

        if(height <= 0) {
            showInvalidInputAlert("Invalid numeric entry. Height must be a number (min. 1).");
            inputHeight.requestFocus();
            return;
        }

        // Create a new project with the inputs and close this dialog
        try {
            controller.newProject(name, width, height);
        } catch (Exception e) {
            e.printStackTrace();
            view.showError("Error", "Error", "Unable to save drawing");
        }
        close();
    }

    /**
     * Displays a simple popup to the user with given title and message with an OK button
     * @param message displayed in popup window
     */
    private void showInvalidInputAlert(String message) {
        // show error to user
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.show();
    }

}
