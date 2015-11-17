package com.saintshape.controller;

import com.saintshape.model.util.HistoryUtil;
import com.saintshape.model.Model;
import com.saintshape.view.View;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

import java.util.List;

/**
 *
 * MVC Controller responsible for application logic
 *
 * Created by 150019538 on 01/11/15.
 */
public class Controller {

    private Model model;
    private View view;
    private Stage primaryStage;
    private HistoryUtil historyUtil;

    public Controller(Model model, Stage primaryStage) {
        this.model = model;
        view = new View(this, model, primaryStage);
        this.primaryStage = primaryStage;
        historyUtil = HistoryUtil.getInstance();
        historyUtil.setMouseEventHandler(view.getMouseEventHandler());
    }

    public void newProject(String name, double width, double height) {

        if(model.hasUnsavedChanges()) {
            // TODO: prompt save changes
        }
        view.getSelectionGroup().getChildren().clear();
        model.reset();
        model.setName(name);
        model.setDimensions(width, height);
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
}
