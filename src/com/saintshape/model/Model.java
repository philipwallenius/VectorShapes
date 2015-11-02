package com.saintshape.model;

import com.saintshape.observer.ModelObserver;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philipwallenius on 01/11/15.
 */
public class Model {

    private String name;
    private Canvas rootCanvas;
    private List<Shape> shapes;
    private boolean hasUnsavedChanges;
    private List<ModelObserver> observers;

    public Model() {
        hasUnsavedChanges = true;
        rootCanvas = new Canvas();
        shapes = new ArrayList<Shape>();
        observers = new ArrayList<ModelObserver>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Canvas getRootCanvas() {
        return rootCanvas;
    }

    public void setRootCanvas(Canvas rootCanvas) {
        this.rootCanvas = rootCanvas;
    }

    public boolean isHasUnsavedChanges() {
        return hasUnsavedChanges;
    }

    public void setHasUnsavedChanges(boolean isChanged) {
        this.hasUnsavedChanges = isChanged;
    }

    public void registerObserver(ModelObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(ModelObserver observer) {
        int index = observers.indexOf(observer);
        if(index >= 0) {
            observers.remove(index);
        }
    }

    public void notifyObservers() {
        for(ModelObserver observer : observers) {
            observer.update();
        }
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(List<Shape> shapes) {
        this.shapes = shapes;
    }

}
