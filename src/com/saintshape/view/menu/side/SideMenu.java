package com.saintshape.view.menu.side;

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

    private ToggleGroup toggleGroup;
    private ToggleButton buttonLine, buttonRectangle, buttonCircle;
    private VBox vbox;
    private TitledPane titledPaneTools;
    private ColorPicker colorPicker;

    /**
     * Constructor that initializes the drawing tools and colorpicker
     */
    public SideMenu() {
        initialize();
    }

    /**
     * Initializes the side menu by creating the tool buttons and a color picker
     */
    private void initialize() {
        TitledPane titledPaneTools = createTools();
        ColorPicker colorPicker = createColorPicker();
        getChildren().addAll(titledPaneTools, colorPicker);
    }

    /**
     * Creates a TitledPane with the drawing tools
     * @return Returns a TitledPane with the drawing tools
     */
    private TitledPane createTools() {

        toggleGroup = new ToggleGroup();

        buttonLine = new ToggleButton("Line");
        buttonLine.setToggleGroup(toggleGroup);
        buttonLine.setUserData(Tool.LINE);
        buttonLine.setSelected(true);

        buttonRectangle = new ToggleButton("Rectangle");
        buttonRectangle.setToggleGroup(toggleGroup);
        buttonRectangle.setUserData(Tool.RECTANGLE);

        buttonCircle = new ToggleButton("Circle");
        buttonCircle.setToggleGroup(toggleGroup);
        buttonCircle.setUserData(Tool.CIRCLE);

        HBox.setHgrow(buttonLine, Priority.ALWAYS);
        HBox.setHgrow(buttonRectangle, Priority.ALWAYS);
        HBox.setHgrow(buttonCircle, Priority.ALWAYS);
        buttonLine.setMaxWidth(Double.MAX_VALUE);
        buttonRectangle.setMaxWidth(Double.MAX_VALUE);
        buttonCircle.setMaxWidth(Double.MAX_VALUE);

        vbox = new VBox();
        vbox.getChildren().addAll(buttonLine, buttonRectangle, buttonCircle);

        titledPaneTools = new TitledPane("Tools", vbox);

        return titledPaneTools;

    }

    /**
     * Creates a color picker which updates the selected color
     * @return Returns a colorpicker
     */
    private ColorPicker createColorPicker() {
        colorPicker = new ColorPicker();
        colorPicker.setMaxWidth(Double.MAX_VALUE);
        return colorPicker;
    }

    public Tool getSelectedTool() {
        return (Tool)toggleGroup.getSelectedToggle().getUserData();
    }

    public Color getSelectedColor() {
        return colorPicker.getValue();
    }

}
