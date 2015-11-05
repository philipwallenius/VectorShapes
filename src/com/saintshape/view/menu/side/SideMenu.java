package com.saintshape.view.menu.side;

import com.saintshape.model.Model;
import com.saintshape.view.View;
import com.saintshape.view.event.SelectEventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

    private TitledPane titledPaneTools;
    private TitledPane titledPaneNodes;
    private ColorPicker colorPicker;
    private ToggleGroup toggleGroup;
    private ListView<NodeItem> listView;
    private Model model;
    private View view;

    /**
     * Constructor that initializes the drawing tools and colorpicker
     */
    public SideMenu(View view, Model model) {
        this.view = view;
        this.model = model;
        initialize();
    }

    /**
     * Initializes the side menu by creating the tool buttons and a color picker
     */
    private void initialize() {
        titledPaneTools = createTools();
        colorPicker = createColorPicker();
        titledPaneNodes = createNodesList();
        getChildren().addAll(titledPaneTools, colorPicker, titledPaneNodes);
    }

    private TitledPane createNodesList() {
        return new NodesList(model);
    }

    /**
     * Creates a TitledPane with the drawing tools
     * @return Returns a TitledPane with the drawing tools
     */
    private TitledPane createTools() {

        toggleGroup = new ToggleGroup();
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> observableValue,
                                Toggle toggle, Toggle newToggle) {

                if (newToggle == null) {
                    toggle.setSelected(true);
                } else {
                    if(newToggle.getUserData() != Tool.SELECT) {
                        view.getSelectionGroup().getChildren().clear();
                    }
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

        ToggleButton buttonCircle = new ToggleButton("Circle");
        buttonCircle.setToggleGroup(toggleGroup);
        buttonCircle.setUserData(Tool.ELLIPSE);

        HBox.setHgrow(buttonSelect, Priority.ALWAYS);
        HBox.setHgrow(buttonLine, Priority.ALWAYS);
        HBox.setHgrow(buttonRectangle, Priority.ALWAYS);
        HBox.setHgrow(buttonCircle, Priority.ALWAYS);
        buttonSelect.setMaxWidth(Double.MAX_VALUE);
        buttonLine.setMaxWidth(Double.MAX_VALUE);
        buttonRectangle.setMaxWidth(Double.MAX_VALUE);
        buttonCircle.setMaxWidth(Double.MAX_VALUE);

        VBox vbox = new VBox();
        vbox.getChildren().addAll(buttonSelect, buttonLine, buttonRectangle, buttonCircle);

        TitledPane titledPane = new TitledPane("Tools", vbox);

        return titledPane;

    }

    /**
     * Creates a color picker which updates the selected color
     * @return Returns a colorpicker
     */
    private ColorPicker createColorPicker() {
        ColorPicker picker = new ColorPicker(Color.BLACK);
        picker.setMaxWidth(Double.MAX_VALUE);
        picker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        return picker;
    }

    public Tool getSelectedTool() {
        return (Tool)toggleGroup.getSelectedToggle().getUserData();
    }

    public Color getSelectedColor() {
        return colorPicker.getValue();
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public void addNode(String name, Node node) {
        listView.getItems().add(new NodeItem(name, node));
    }

    public void removeNode(NodeItem nodeItem) {
        listView.getItems().remove(nodeItem);
    }

    public void setSelectedColor(Color color) {
        colorPicker.setValue(color);
    }
}
