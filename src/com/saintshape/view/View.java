package com.saintshape.view;

import com.saintshape.controller.Controller;
import com.saintshape.model.Model;
import com.saintshape.observer.ModelObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philipwallenius on 01/11/15.
 */
public class View implements ModelObserver {

    private final static String APPLICATION_NAME = "Saintshape";
    private final static int WINDOW_WIDTH = 600, WINDOW_HEIGHT = 400;

    private Controller controller;
    private Model model;

    private Stage primaryStage;
    private Group group;
    private Tool selectedTool;
    private Color selectedColor;

    public enum Tool {
        LINE, RECTANGLE, CIRCLE
    }

    public View(Controller controller, Model model, Stage primaryStage) {
        this.controller = controller;
        this.model = model;
        this.primaryStage = primaryStage;

        model.registerObserver(this);

        this.primaryStage = primaryStage;
        primaryStage.setTitle(APPLICATION_NAME);
        group = new Group();
        VBox controls = new VBox();
        controls.getChildren().addAll(createControlPanel());
        StackPane canvasHolder = new StackPane(group);
        final ScrollPane scrollPane = new ScrollPane(canvasHolder);

        // Register control
        addMouseListeners();

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenuBar());
        borderPane.setLeft(controls);
        borderPane.setCenter(scrollPane);
        Scene scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void addMouseListeners() {
        model.getRootCanvas().addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        controller.createShape(selectedColor, t.getX(), t.getY());
                    }
                });
    }

    private List<Node> createControlPanel() {
        List<Node> nodes = new ArrayList<Node>(2);

        final ToggleButton buttonLine = new ToggleButton("Line");
        ToggleButton buttonRectangle = new ToggleButton("Rectangle");
        ToggleButton buttonCircle = new ToggleButton("Circle");
        final ToggleGroup toolGroup = new ToggleGroup();
        buttonLine.setToggleGroup(toolGroup);
        buttonLine.setUserData(Tool.LINE);
        buttonLine.setSelected(true);
        selectedTool = Tool.LINE;
        buttonRectangle.setToggleGroup(toolGroup);
        buttonRectangle.setUserData(Tool.RECTANGLE);
        buttonCircle.setToggleGroup(toolGroup);
        buttonCircle.setUserData(Tool.CIRCLE);

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

        HBox.setHgrow(buttonLine, Priority.ALWAYS);
        HBox.setHgrow(buttonRectangle, Priority.ALWAYS);
        HBox.setHgrow(buttonCircle, Priority.ALWAYS);
        buttonLine.setMaxWidth(Double.MAX_VALUE);
        buttonRectangle.setMaxWidth(Double.MAX_VALUE);
        buttonCircle.setMaxWidth(Double.MAX_VALUE);

        VBox tools = new VBox();
        tools.getChildren().addAll(buttonLine, buttonRectangle, buttonCircle);

        TitledPane titledPaneTools = new TitledPane("Tools", tools);
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
        nodes.add(titledPaneTools);
        nodes.add(colorPicker);
        return nodes;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        MenuItem menuItemNew = new MenuItem("New...");
        MenuItem menuItemExit = new MenuItem("Quit " + APPLICATION_NAME);
        MenuItem menuItemSave = new MenuItem("Save");
        MenuItem menuItemSaveAs = new MenuItem("Save As...");
        menuFile.getItems().addAll(menuItemNew, menuItemSave, menuItemSaveAs, menuItemExit);
        menuBar.getMenus().add(menuFile);

        menuItemNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                new NewDialog(controller, primaryStage);
            }
        });

        menuItemExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });

        return menuBar;
    }

    private Canvas drawCheckedBackground(Canvas canvas) {
        int checkerPatternSize = 10;
        double width = Math.ceil(canvas.getWidth());
        double height = Math.ceil(canvas.getHeight());
        double yNum = height/checkerPatternSize;
        double xNum = width/checkerPatternSize;
        GraphicsContext gc = canvas.getGraphicsContext2D();

        int currY = 0;
        boolean alternate = false;
        for(int i = 0; i < yNum; i++) {

            int currX = 0;
            if(alternate) {
                gc.setFill(Color.rgb(204, 204, 204));
            } else {
                gc.setFill(Color.rgb(255, 255, 255));
            }
            boolean color = false;
            if(alternate) {
                color = true;
            }
            for(int x = 0; x < xNum; x++) {


                gc.fillRect(currX, currY, checkerPatternSize, checkerPatternSize);
                currX += checkerPatternSize;
                if(color) {
                    gc.setFill(Color.rgb(255, 255, 255));
                } else {
                    gc.setFill(Color.rgb(204, 204, 204));
                }
                gc.fillRect(currX, currY, checkerPatternSize, checkerPatternSize);
                if(color) {
                    color = false;
                } else {
                    color = true;
                }
            }
            currY += checkerPatternSize;
            if(alternate) {
                alternate = false;
            } else {
                alternate = true;
            }
        }
        return canvas;
    }

    @Override
    public void update() {
        group.getChildren().clear();
        group.getChildren().add(drawCheckedBackground(model.getRootCanvas()));
        group.getChildren().addAll(model.getShapes());
    }

}
