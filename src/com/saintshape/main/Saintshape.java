package com.saintshape.main;

import com.saintshape.controller.Controller;
import com.saintshape.model.Model;
import com.saintshape.view.NewDialog;
import com.saintshape.view.View;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Saintshape extends Application {

    private Controller controller;
    private View view;
    private Model model;

    private final static String APPLICATION_NAME = "Saintshape";
    private final static int WINDOW_WIDTH = 600, WINDOW_HEIGHT = 400;
    private Scene scene;
    private List<Shape> shapes;
    private Canvas rootCanvas;
    private List<Canvas> layers;
    private Shape selectedShape;
    private Group group;
    private Stage primaryStage;

    public Saintshape() {
        shapes = new ArrayList<Shape>();
        layers = new ArrayList<Canvas>();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        model = new Model();
        controller = new Controller(model, primaryStage);

        this.primaryStage = primaryStage;
        primaryStage.setTitle(APPLICATION_NAME);
        group = new Group();
        VBox controls = new VBox();
        controls.getChildren().addAll(createControlPanel());
        StackPane canvasHolder = new StackPane(group);
        final ScrollPane scrollPane = new ScrollPane(canvasHolder);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createMenuBar());
        borderPane.setLeft(controls);
        borderPane.setCenter(scrollPane);
        Scene scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setScene(scene);
        primaryStage.show();

    }


    private List<Node> createControlPanel() {
        List<Node> nodes = new ArrayList<Node>(2);

        Button buttonLine = new Button("Line");
        Button buttonRectangle = new Button("Rectangle");
        Button buttonCircle = new Button("Circle");

        HBox.setHgrow(buttonLine, Priority.ALWAYS);
        HBox.setHgrow(buttonRectangle, Priority.ALWAYS);
        HBox.setHgrow(buttonCircle, Priority.ALWAYS);
        buttonLine.setMaxWidth(Double.MAX_VALUE);
        buttonRectangle.setMaxWidth(Double.MAX_VALUE);
        buttonCircle.setMaxWidth(Double.MAX_VALUE);

        VBox tools = new VBox();
        tools.getChildren().addAll(buttonLine, buttonRectangle, buttonCircle);

        TitledPane tiledPane1 = new TitledPane("Tools", tools);
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setMaxWidth(Double.MAX_VALUE);
        TitledPane tiledPane2 = new TitledPane("Layers", new Label("Layers goes here"));
        nodes.add(tiledPane1);
        nodes.add(colorPicker);
        nodes.add(tiledPane2);
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

        final Saintshape th = this;
        menuItemNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                new NewDialog(th, primaryStage);
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

    public void createNewProject(String name, double width, double height) {
        primaryStage.setTitle(String.format("%s - %s", APPLICATION_NAME, name));
        createRootCanvas(width, height);
        layers.clear();
        layers.add(new Canvas(width, height));
    }

    private void createRootCanvas(double width, double height) {
        if(rootCanvas == null) {
            rootCanvas = new Canvas(width, height);
        } else {
            rootCanvas.setWidth(width);
            rootCanvas.setHeight(height);
        }
        drawCheckedBackground(rootCanvas);

        if(!group.getChildren().contains(rootCanvas)) {
            group.getChildren().add(rootCanvas);
        }
    }

    private void drawCheckedBackground(Canvas canvas) {
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
    }


    public static void main(String[] args) {
        launch(args);
    }
}
