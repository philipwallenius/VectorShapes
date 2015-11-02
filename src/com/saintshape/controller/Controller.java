package com.saintshape.controller;

import com.saintshape.model.Model;
import com.saintshape.view.View;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.List;

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

        if(model.isHasUnsavedChanges()) {
            // TODO: prompt save changes
        }

        model.getShapes().clear();
        model.setName(name);
        model.getRootCanvas().setWidth(width);
        model.getRootCanvas().setHeight(height);


        model.notifyObservers();
    }

    public void createShape(Color color, double x, double y) {
        Rectangle r = new Rectangle(x, y, 10, 10);
        r.setFill(color);
        model.getShapes().add(r);
        model.notifyObservers();
    }

}
