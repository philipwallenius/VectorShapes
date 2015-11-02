package com.saintshape.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by 150019538 on 02/11/15.
 */
public class SideMenu extends VBox {

    private Tool selectedTool;
    private Color selectedColor;
    private ToggleGroup toggleGroup;
    private ToggleButton buttonLine, buttonRectangle, buttonCircle;

    public SideMenu() {
        initialize();
    }

    public void initialize() {
        TitledPane titledPaneTools = createTools();
        ColorPicker colorPicker = createColorPicker();
        getChildren().addAll(titledPaneTools, colorPicker);
    }

    public TitledPane createTools() {

        final ToggleGroup toolGroup = new ToggleGroup();
        toolGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle toggle, Toggle new_toggle) {
                if (new_toggle != null) {
                    Tool t = (Tool)toolGroup.getSelectedToggle().getUserData();
                    System.out.println("Current tool is: " + t);
                    selectedTool = t;
                }
            }
        });

        ToggleButton buttonLine = new ToggleButton("Line");
        buttonLine.setToggleGroup(toolGroup);
        buttonLine.setUserData(Tool.LINE);
        buttonLine.setSelected(true);
        selectedTool = Tool.LINE;

        ToggleButton buttonRectangle = new ToggleButton("Rectangle");
        buttonRectangle.setToggleGroup(toolGroup);
        buttonRectangle.setUserData(Tool.RECTANGLE);

        ToggleButton buttonCircle = new ToggleButton("Circle");
        buttonCircle.setToggleGroup(toolGroup);
        buttonCircle.setUserData(Tool.CIRCLE);

        HBox.setHgrow(buttonLine, Priority.ALWAYS);
        HBox.setHgrow(buttonRectangle, Priority.ALWAYS);
        HBox.setHgrow(buttonCircle, Priority.ALWAYS);
        buttonLine.setMaxWidth(Double.MAX_VALUE);
        buttonRectangle.setMaxWidth(Double.MAX_VALUE);
        buttonCircle.setMaxWidth(Double.MAX_VALUE);

        VBox tools = new VBox();
        tools.getChildren().addAll(buttonLine, buttonRectangle, buttonCircle);

        TitledPane titledPaneTools = new TitledPane("Tools", tools);

        return titledPaneTools;

    }

    public ColorPicker createColorPicker() {
        final ColorPicker colorPicker = new ColorPicker();
        selectedColor = colorPicker.getValue();
        colorPicker.setMaxWidth(Double.MAX_VALUE);
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Color picker color: " + colorPicker.getValue());
                selectedColor = colorPicker.getValue();
            }
        });
        return colorPicker;
    }

    public Tool getSelectedTool() {
        return selectedTool;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

}
