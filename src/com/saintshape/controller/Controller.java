package com.saintshape.controller;

import com.saintshape.model.Model;
import com.saintshape.view.View;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
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

    public Controller(Model model, Stage primaryStage) {
        this.model = model;
        view = new View(this, model, primaryStage);
        this.primaryStage = primaryStage;
    }

    public void newProject(String name, double width, double height) {

        if(model.hasUnsavedChanges()) {
            // TODO: prompt save changes
        }

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

}
