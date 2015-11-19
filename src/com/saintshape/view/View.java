package com.saintshape.view;

import com.saintshape.controller.Controller;
import com.saintshape.model.Model;
import com.saintshape.model.util.HistoryUtil;
import com.saintshape.observer.ModelObserver;
import com.saintshape.view.event.handlers.MouseEventHandler;
import com.saintshape.view.event.handlers.entity.Selection;
import com.saintshape.view.menu.StatusBar;
import com.saintshape.view.menu.side.SideMenu;
import com.saintshape.view.menu.side.Tool;
import com.saintshape.view.menu.top.TopMenu;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 *
 * MVC View responsible for GUI. This class observes the Model class.
 *
 * Created by 150019538 on 01/11/15.
 */
public class View implements ModelObserver {

    private final static String APPLICATION_NAME = "Saintshape";
    private final static int WINDOW_WIDTH = 1024, WINDOW_HEIGHT = 768;

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
    private StatusBar statusBar;

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

    /**
     * Initializes the View
     */
    public void initialize() {
        primaryStage.setTitle(APPLICATION_NAME);
        primaryStage.getIcons().add(new Image("logo.png"));
        group = new Group();
        selectionGroup = new Group();
        controls = new VBox();
        sideMenu = new SideMenu(this, model, controller);
        controls.getChildren().addAll(sideMenu);
        canvasHolder = new StackPane(group);
        scrollPane = new ScrollPane(canvasHolder);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setStyle("-fx-focus-color: transparent;");

        // center the drawing canvas
        canvasHolder.minWidthProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPane.getViewportBounds().getWidth(), scrollPane.viewportBoundsProperty()));
        canvasHolder.minHeightProperty().bind(Bindings.createDoubleBinding(() ->
                scrollPane.getViewportBounds().getHeight(), scrollPane.viewportBoundsProperty()));

        // register mouse events
        mouseEventHandler = new MouseEventHandler(this, controller);
        mouseEventHandler.register(model.getRootCanvas());

        borderPane = new BorderPane();
        topMenu = new TopMenu(this, controller);
        statusBar = new StatusBar();
        borderPane.setTop(topMenu);
        borderPane.setLeft(controls);
        borderPane.setCenter(scrollPane);
        borderPane.setBottom(statusBar);
        scene = new Scene(borderPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.ESCAPE || ke.getCode() == KeyCode.ENTER) {
                    mouseEventHandler.deselect();
                }
                if (ke.getCode() == KeyCode.DELETE || ke.getCode() == KeyCode.BACK_SPACE) {
                    Selection selection = getMouseEventHandler().getSelection();
                    if(selection != null) {
                        Node node = selection.getShape();
                        mouseEventHandler.deselect();
                        model.removeNode(node);
                        HistoryUtil.getInstance().addHistoryPoint();
                    }
                }
            }
        });
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

    /**
     * Draws checked canvas background
     * @param canvas to draw checked background on
     * @return Returns a Canvas with drawn checked background
     */
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

    /**
     * Updates view
     */
    @Override
    public void update() {
        primaryStage.setTitle(APPLICATION_NAME + " - " + model.getName());
        group.getChildren().clear();
        group.getChildren().add(drawCheckedBackground(model.getRootCanvas()));
        group.getChildren().addAll(model.getNodes());
        group.getChildren().add(selectionGroup);
    }

    /**
     * Updates view
     * @param model
     */
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

    public MouseEventHandler getMouseEventHandler() {
        return mouseEventHandler;
    }

    public void deselect() {
        mouseEventHandler.deselect();
    }

    /**
     * Changes cursor
     * @param cursor to set
     */
    public void changeCursor(Cursor cursor) {
        controller.getRootCanvas().setCursor(cursor);
        for(Node node : model.getNodes()) {
            node.setCursor(cursor);
        }
    }

    /**
     * Displays unsaved changes dialog
     * @return Returns a Boolean with user choice
     */
    public Boolean unsavedChangesDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unsaved Changes");
        alert.setHeaderText("Do you want to save the changes you made to "+model.getName()+"?");
        alert.setContentText("Your changes will be lost if you don't save them.");

        ButtonType buttonTypeDontSave = new ButtonType("Don't Save");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType buttonTypeSave = new ButtonType("Save");

        alert.getButtonTypes().setAll(buttonTypeDontSave, buttonTypeCancel, buttonTypeSave);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeDontSave){
            return false;
        } else if (result.get() == buttonTypeSave) {
            return true;
        } else {
            return null;
        }
    }

    /**
     * Save model with Save As... Dialog
     */
    public void saveAsDialog() {

        try {
            controller.saveModel(true);
        } catch(Exception exception) {
            exception.printStackTrace();
            showError("Save Error", "Error", "Failed to save vector drawing.");
        }

    }

    /**
     * Save model without Save As... Dialog
     */
    public void save() {

        try {
            controller.saveModel(false);
        } catch(Exception exception) {
            exception.printStackTrace();
            showError("Save Error", "Error", "Failed to save vector drawing.");
        }

    }

    /**
     * Display an error popup to user
     * @param title for popup
     * @param headerText for popup
     * @param desc for popup
     */
    public void showError(String title, String headerText, String desc) {
        // show error to user
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(desc);
        alert.show();
    }

    /**
     * Shows import image dialog to user
     */
    public void showImportImageDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Image");

        // filter file types
        FileChooser.ExtensionFilter extFilterAll = new FileChooser.ExtensionFilter("*", "*.JPG", "*.JPEG", "*.PNG", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.GIF");
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG", "*.jpg");
        FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG", "*.jpeg");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG", "*.png");
        FileChooser.ExtensionFilter extFilterGIF = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.GIF", "*.gif");
        fileChooser.getExtensionFilters().addAll(extFilterAll, extFilterJPG, extFilterJPEG, extFilterPNG, extFilterGIF);

        File file = fileChooser.showOpenDialog(controller.getPrimaryStage());

        if (file != null) {
            try {
                // import image to canvas
                controller.importImage(file);
            } catch(IOException exception) {
                showError("Import Error", "Error", "Failed to import image: " + file.getName());
            }
        }
    }

    /**
     * Shows Save As... Dialog to user
     * @param name of the project
     * @return Returns the File to save model to
     */
    public File showSaveDialog(String name) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As...");
        fileChooser.setInitialFileName(name+".svg");

        // filter file types
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("svg files (*.svg)", "*.svg");
        fileChooser.getExtensionFilters().add(extFilter);

        return fileChooser.showSaveDialog(controller.getPrimaryStage());
    }

}
