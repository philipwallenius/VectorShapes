package com.vectorshapes.controller;

import com.vectorshapes.controller.util.OpenSaveInterface;
import com.vectorshapes.controller.util.svg.SvgUtil;
import com.vectorshapes.model.Model;
import com.vectorshapes.model.util.HistoryUtil;
import com.vectorshapes.view.View;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 *
 * MVC Controller responsible for application logic
 *
 * Created by philipwallenius on 01/11/15.
 */
public class Controller {

    private final BooleanProperty workSpaceEmpty = new SimpleBooleanProperty(true);
    private final Model model;
    private final View view;
    private final Stage primaryStage;
    private final OpenSaveInterface openSaveUtil = SvgUtil.getInstance();

    public Controller(Model model, Stage primaryStage) {
        this.model = model;
        view = new View(this, model, primaryStage);
        this.primaryStage = primaryStage;
        HistoryUtil.getInstance().setModel(model);
        HistoryUtil.getInstance().setMouseEventHandler(view.getMouseEventHandler());
    }

    /**
     * Resets workspace and creates a new project
     * @param name of the new project
     * @param width of the canvas
     * @param height of the canvas
     * @throws Exception
     */
    public void newProject(String name, double width, double height) throws Exception {

        // check for unsaved changes
        Boolean cancel = checkUnsavedChanges();
        if(cancel) {
            return;
        }

        // reset application
        view.getSelectionGroup().getChildren().clear();
        if(!workSpaceEmpty.get()) {
            model.reset();
        }

        // add initial history point for undo/redo
        HistoryUtil.getInstance().addHistoryPoint();

        // set name and dimensions of model
        model.setName(name);
        model.setDimensions(width, height);
        workSpaceEmpty.setValue(false);
    }

    /**
     * Checks if there is any unsaved changes and prompts user if he/she wants to save those
     * @return Returns a Boolean representing whether or not user cancelled the action
     * @throws Exception
     */
    private Boolean checkUnsavedChanges() throws Exception {

        // prompt to save if there are unsaved changes
        Boolean save;
        if(model.hasUnsavedChanges()) {
            save = view.unsavedChangesDialog();
        } else {
            save = false;
        }

        // if save is null, user clicked cancel or there were no changes
        if(save == null) {
            return true;
        }

        // save if user chose to save changes
        if(save) {
            boolean saved = saveModel(false);
            if(!saved) {
                return true;
            }
        }

        return false;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void addNode(Node node) {
        model.addNode(node);
    }

    public Canvas getRootCanvas() {
        return model.getRootCanvas();
    }

    public List<Node> getNodes() {
        return model.getNodes();
    }

    public void undo() {
        view.deselect();
        HistoryUtil.getInstance().undo();
    }

    public void redo() {
        view.deselect();
        HistoryUtil.getInstance().redo();
    }

    /**
     * Imports an image into canvas
     * @param file to import image from
     * @throws IOException
     */
    public void importImage(File file) throws IOException {

        // create a new image node and set its dimensions
        Image image = new Image(Files.newInputStream(file.toPath()));
        ImageView imageView = new ImageView(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());

        // register image for mouse events and add it to model
        view.getMouseEventHandler().register(imageView);
        model.addNode(imageView);

        // add a history point
        HistoryUtil.getInstance().addHistoryPoint();
    }

    /**
     * Saves model to SVG file
     * @param saveAs whether or not to display Save As... dialog to user
     * @return Returns a boolean representing if the file was saved (user could have clicked cancel)
     * @throws Exception
     */
    public boolean saveModel(boolean saveAs) throws Exception {
        File file = null;

        // if saveAs is false, get file from model
        if(!saveAs) {
            file = model.getFile();
        }

        // if file is null, request File from user
        if(file == null) {
            file = view.showSaveDialog(model.getName());
            if(file == null) {
                return false;
            }
        }

        model.setFile(file);

        // export model
        openSaveUtil.exportFile(model);

        // update model file and name
        model.setName(file.getName().substring(0, file.getName().lastIndexOf(".")));
        model.setHasUnsavedChanges(false);
        return true;
    }

    /**
     * Imports model from SVG file
     * @param file to import model from
     * @throws Exception
     */
    public void open(File file) throws Exception {

        // check for unsaved changes
        if(!workSpaceEmpty.get()) {
            Boolean cancel = checkUnsavedChanges();
            if(cancel) {
                return;
            }
        }

        // import new model from SVG file
        Model importedModel = openSaveUtil.importFile(file);

        // reset current model
        model.reset();
        view.getMouseEventHandler().deselect();

        // update model properties
        model.setName(importedModel.getName());
        model.setDimensions(importedModel.getRootCanvas().getWidth(), importedModel.getRootCanvas().getHeight());

        // clone all nodes to current model
        for(Node node : importedModel.getNodes()) {
            view.getMouseEventHandler().register(node);
            model.addNode(node);
        }
        workSpaceEmpty.setValue(false);
        model.setHasUnsavedChanges(false);
        model.setFile(file);
    }

    public BooleanProperty getWorkSpaceEmpty() {
        return workSpaceEmpty;
    }

    public Model getModel() {
        return model;
    }

    /**
     * Checks for unsaved changes and quits the application
     */
    public void quit() {

        // check for unsaved changes
        try {
            Boolean cancel = checkUnsavedChanges();
            if (cancel) {
                return;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        System.exit(0);
    }

    public void removeNode(Node node) {
        model.removeNode(node);
    }

    public void moveUp(Node node) {
        model.moveUp(node);
    }

    public void moveDown(Node node) {
        model.moveDown(node);
    }
}
