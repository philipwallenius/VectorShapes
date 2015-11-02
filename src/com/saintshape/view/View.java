package com.saintshape.view;

import com.saintshape.controller.Controller;
import com.saintshape.model.Model;
import com.saintshape.observer.ModelObserver;
import com.saintshape.view.menu.side.SideMenu;
import com.saintshape.view.menu.side.Tool;
import com.saintshape.view.menu.top.TopMenu;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
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
    private final static int WINDOW_WIDTH = 600, WINDOW_HEIGHT = 400;

    private Controller controller;
    private Model model;

    private Stage primaryStage;
    private Group group;
    private Shape selectedShape;

    private SideMenu sideMenu;
    private TopMenu topMenu;

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

        model.registerObserver(this);

        this.primaryStage = primaryStage;
        primaryStage.setTitle(APPLICATION_NAME);
        group = new Group();
        VBox controls = new VBox();
        sideMenu = new SideMenu();
        controls.getChildren().addAll(sideMenu);
        StackPane canvasHolder = new StackPane(group);
        final ScrollPane scrollPane = new ScrollPane(canvasHolder);

        // Register mouse events
        createMouseListeners();

        BorderPane borderPane = new BorderPane();
        topMenu = new TopMenu(controller);
        borderPane.setTop(topMenu);
        borderPane.setLeft(controls);
        borderPane.setCenter(scrollPane);
        Scene scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void createMouseListeners() {
        model.getRootCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent t) {
                        if(sideMenu.getSelectedTool() == Tool.RECTANGLE) {
                            selectedShape = new Rectangle(t.getX(), t.getY(), 0, 0);
                            selectedShape.setFill(sideMenu.getSelectedColor());
                            controller.addShape(selectedShape);
                        }
                    }
                });
        model.getRootCanvas().addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(selectedShape != null) {
                    selectedShape = null;
                }
            }
        });
        model.getRootCanvas().addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(selectedShape != null) {
                    Rectangle r = (Rectangle)selectedShape;
                    r.setHeight(event.getY());
                    r.setWidth(event.getX());
                }
            }
        });
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
        group.getChildren().addAll(model.getShapes());
    }

}
