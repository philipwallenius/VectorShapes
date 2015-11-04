package com.saintshape.model;

import com.saintshape.observer.ModelObserver;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * MVC Model representing the state of the application. It is the observable.
 *
 * Created by 150019538 on 01/11/15.
 */
public class Model {

    private String name;
    private Canvas rootCanvas;
    private List<Node> nodes;
    private boolean hasUnsavedChanges;
    private List<ModelObserver> observers;

    /**
     * Constructor
     */
    public Model() {
        hasUnsavedChanges = true;
        rootCanvas = new Canvas();
        nodes = new ArrayList<Node>();
        observers = new ArrayList<ModelObserver>();
    }

    public void reset() {
        name = "";
        nodes.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyObservers();
    }

    public Canvas getRootCanvas() {
        return rootCanvas;
    }

    public void setRootCanvas(Canvas rootCanvas) {
        this.rootCanvas = rootCanvas;
    }

    public void setDimensions(double width, double height) {
        rootCanvas.setWidth(width);
        rootCanvas.setHeight(height);
        notifyObservers();
    }

    public boolean hasUnsavedChanges() {
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
            observer.update(this);
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public void addNode(Node node) {
        nodes.add(node);
        notifyObservers();
    }

    public void removeNode(Node node) {
        nodes.remove(node);
        notifyObservers();
    }

}
