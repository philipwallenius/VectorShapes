package com.saintshape.view.menu.side;

import com.saintshape.controller.Controller;
import com.saintshape.model.Model;
import com.saintshape.model.shape.Line;
import com.saintshape.view.View;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * This class represents a side menu containing tools and a colorpicker.
 *
 * Created by 150019538 on 02/11/15.
 */
public class SideMenu extends VBox {

    private final static int SIDEMENU_WIDTH = 150;
    private TitledPane titledPaneTools;
    private TitledPane titledPaneColor;
    private TitledPane titledPaneNodes;
    private Spinner strokeWidthSpinner;
    private ColorPicker colorPickerFill;
    private ColorPicker colorPickerStroke;
    private ToggleGroup toggleGroup;
    private final Model model;
    private final View view;
    private final Controller controller;

    /**
     * Constructor that initializes the drawing tools and colorpicker
     */
    public SideMenu(View view, Model model, Controller controller) {
        this.view = view;
        this.model = model;
        this.controller = controller;
        initialize();
    }

    /**
     * Initializes the side menu by creating the tool buttons and a color picker
     */
    private void initialize() {
        titledPaneTools = createToolsPane();
        titledPaneColor = createColorPane();
        titledPaneNodes = createNodesList();
        getChildren().addAll(titledPaneTools, titledPaneColor, titledPaneNodes);
        setPrefWidth(SIDEMENU_WIDTH);
    }

    private TitledPane createColorPane() {
        VBox vbox = new VBox(10);
        colorPickerFill = createColorPicker();
        colorPickerStroke = createColorPicker();
        strokeWidthSpinner = createSpinner();
        vbox.getChildren().addAll(new VBox(new Label("Fill Color:"), colorPickerFill), new VBox(new Label("Stroke Color:"), colorPickerStroke), new VBox(new Label("Stroke Width:"), strokeWidthSpinner));
        return new TitledPane("Color", vbox);
    }

    private VBox createOtherTools() {
        VBox vBox = new VBox();

        Button buttonImage = new Button("Image...");
        HBox.setHgrow(buttonImage, Priority.ALWAYS);
        buttonImage.setMaxWidth(Double.MAX_VALUE);
        buttonImage.setOnAction(event -> view.showImportImageDialog());
        buttonImage.disableProperty().bind(controller.getWorkSpaceEmpty());

        vBox.getChildren().addAll(buttonImage);

        return vBox;
    }

    private Spinner createSpinner() {
        SpinnerValueFactory spv = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 1);
        Spinner spinner = new Spinner();
        spinner.setValueFactory(spv);
        spinner.setMaxWidth(Double.MAX_VALUE);
        return spinner;
    }

    private TitledPane createNodesList() {
        return new NodesList(view, model, controller);
    }

    /**
     * Creates a TitledPane with the drawing tools
     * @return Returns a TitledPane with the drawing tools
     */
    private TitledPane createToolsPane() {

        toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observableValue,
                                Toggle toggle, Toggle newToggle) {
                if (newToggle == null) {
                    toggle.setSelected(true);
                }
            }
        });

        ToggleButton buttonSelect = new ToggleButton("Select");
        buttonSelect.setToggleGroup(toggleGroup);
        buttonSelect.setUserData(Tool.SELECT);
        buttonSelect.setSelected(true);

        ToggleButton buttonLine = new ToggleButton("Line");
        buttonLine.setToggleGroup(toggleGroup);
        buttonLine.setUserData(Tool.LINE);

        ToggleButton buttonRectangle = new ToggleButton("Rectangle");
        buttonRectangle.setToggleGroup(toggleGroup);
        buttonRectangle.setUserData(Tool.RECTANGLE);

        ToggleButton buttonParallelogram = new ToggleButton("Parallelogram");
        buttonParallelogram.setToggleGroup(toggleGroup);
        buttonParallelogram.setUserData(Tool.PARALLELOGRAM);

        ToggleButton buttonCircle = new ToggleButton("Ellipse");
        buttonCircle.setToggleGroup(toggleGroup);
        buttonCircle.setUserData(Tool.ELLIPSE);

        HBox.setHgrow(buttonSelect, Priority.ALWAYS);
        HBox.setHgrow(buttonLine, Priority.ALWAYS);
        HBox.setHgrow(buttonRectangle, Priority.ALWAYS);
        HBox.setHgrow(buttonParallelogram, Priority.ALWAYS);
        HBox.setHgrow(buttonCircle, Priority.ALWAYS);
        buttonSelect.setMaxWidth(Double.MAX_VALUE);
        buttonLine.setMaxWidth(Double.MAX_VALUE);
        buttonRectangle.setMaxWidth(Double.MAX_VALUE);
        buttonParallelogram.setMaxWidth(Double.MAX_VALUE);
        buttonCircle.setMaxWidth(Double.MAX_VALUE);


        VBox otherTools = createOtherTools();

        VBox vbox = new VBox();
        vbox.getChildren().addAll(buttonSelect, buttonLine, buttonRectangle, buttonParallelogram, buttonCircle, otherTools);

        return new TitledPane("Tools", vbox);

    }

    /**
     * Creates a color picker which updates the selected color
     * @return Returns a colorpicker
     */
    private ColorPicker createColorPicker() {
        ColorPicker picker = new ColorPicker(Color.BLACK);
        picker.setMaxWidth(Double.MAX_VALUE);
        return picker;
    }

    public Tool getSelectedTool() {
        return (Tool)toggleGroup.getSelectedToggle().getUserData();
    }

    public Color getSelectedFillColor() {
        return colorPickerFill.getValue();
    }

    public Color getSelectedStrokeColor() {
        return colorPickerStroke.getValue();
    }

    public ColorPicker getColorPickerFill() {
        return colorPickerFill;
    }

    public ColorPicker getColorPickerStroke() {
        return colorPickerStroke;
    }

    public Spinner getStrokeWidthSpinner() {
        return strokeWidthSpinner;
    }

    public void setStrokeWidthSpinner(int width) {
        strokeWidthSpinner.getValueFactory().setValue(width);
    }

    public int getStrokeWidth() {
        return (int) strokeWidthSpinner.getValue();
    }

    public void setSelectedFillColor(Color color) {
        colorPickerFill.setValue(color);
    }

    public void setSelectedStrokeColor(Color color) {
        colorPickerStroke.setValue(color);
    }

    public ToggleGroup getTools() {
        return toggleGroup;
    }

    public void selectNodeInList(Node node) {
        ((NodesList)titledPaneNodes).selectNodeInList(node);
    }
}
