package com.saintshape.view;

import com.saintshape.controller.Controller;
import com.saintshape.model.Model;
import com.saintshape.observer.ModelObserver;
import com.saintshape.view.event.EllipseEventHandler;
import com.saintshape.view.event.MouseEventHandler;
import com.saintshape.view.event.RectangleEventHandler;
import com.saintshape.view.menu.side.SideMenu;
import com.saintshape.view.menu.side.Tool;
import com.saintshape.view.menu.top.TopMenu;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

/**
 *
 * MVC View responsible for GUI. This class observes the Model class.
 *
 * Created by 150019538 on 01/11/15.
 */
public class View implements ModelObserver {

    private final static String APPLICATION_NAME = "Saintshape";
    private final static int WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600;

    private Controller controller;
    private Model model;

    private Stage primaryStage;
    private StackPane canvasHolder;
    private Group group;
    private BorderPane borderPane;
    private Scene scene;
    private ScrollPane scrollPane;
    private VBox controls;

    private SideMenu sideMenu;
    private TopMenu topMenu;
    private MouseEventHandler mouseEventHandler;
    private Group selectionGroup;

    /**
     * Constructor that initializes the view and creates the main window
     * @param controller responsible for business logic
     * @param model representing the state of application
     * @param primaryStage from java fx
     */
    public View(Controller controller, Model model, Stage primaryStage) {
        this.controller = controller;
        this.model = model;
        this.primaryStage = primaryStage;
        this.primaryStage = primaryStage;
        model.registerObserver(this);
        initialize();
    }

    public void initialize() {
        primaryStage.setTitle(APPLICATION_NAME);
        group = new Group();
        selectionGroup = new Group();
        controls = new VBox();
        sideMenu = new SideMenu(this, model);
        controls.getChildren().addAll(sideMenu);
        canvasHolder = new StackPane(group);
        scrollPane = new ScrollPane(canvasHolder);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // center the drawing canvas
        canvasHolder.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        canvasHolder.minHeightProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPane.getViewportBounds().getHeight(), scrollPane.viewportBoundsProperty()));

        // register mouse events
        mouseEventHandler = new MouseEventHandler(this, controller);
        mouseEventHandler.register(model.getRootCanvas());

        borderPane = new BorderPane();
        topMenu = new TopMenu(controller);
        borderPane.setTop(topMenu);
        borderPane.setLeft(controls);
        borderPane.setCenter(scrollPane);
        scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Tool getSelectedTool() {
        return sideMenu.getSelectedTool();
    }

    public Color getSelectedColor() {
        return sideMenu.getSelectedColor();
    }

    public void setSelectedColor(Color color) {
        sideMenu.setSelectedColor(color);
    }

    public ColorPicker getColorPicker() {
        return sideMenu.getColorPicker();
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
        primaryStage.setTitle(APPLICATION_NAME + " - " + model.getName());
        group.getChildren().clear();
        group.getChildren().add(drawCheckedBackground(model.getRootCanvas()));
        group.getChildren().addAll(model.getNodes());
        group.getChildren().add(selectionGroup);
    }

    @Override
    public void update(Model model) {

    }

    public Group getSelectionGroup() {
        return selectionGroup;
    }

    public ToggleGroup getTools() {
        return sideMenu.getTools();
    }

    public void selectNode(Node node) {
        mouseEventHandler.selectNode(node);
    }

    public void selectNodeInList(Node node) {
        sideMenu.selectNodeInList(node);
    }
}
