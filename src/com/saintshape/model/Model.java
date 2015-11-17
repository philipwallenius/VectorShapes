package com.saintshape.model;

import com.saintshape.model.util.HistoryUtil;
import com.saintshape.model.shape.Ellipse;
import com.saintshape.model.shape.Line;
import com.saintshape.model.shape.Rectangle;
import com.saintshape.observer.ModelObserver;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Paint;
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
    private HistoryUtil historyUtil;

    /**
     * Constructor
     */
    public Model() {
        hasUnsavedChanges = true;
        rootCanvas = new Canvas();
        nodes = new ArrayList<>();
        observers = new ArrayList<>();
        historyUtil = HistoryUtil.getInstance();
        historyUtil.setModel(this);
    }

    public void reset() {
        name = "";
        nodes.clear();
        historyUtil.resetHistory();
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
        addColorChangeListener(node);
        nodes.add(node);
        notifyObservers();
    }

    private void addColorChangeListener(Node node) {
        // Add history point if color of shape is changed
        if(node instanceof Shape) {
            Shape s = (Shape)node;
            s.fillProperty().addListener((observable, oldValue, newValue) -> {
                HistoryUtil.getInstance().addHistoryPoint();
            });
            s.strokeProperty().addListener((observable, oldValue, newValue) -> {
                HistoryUtil.getInstance().addHistoryPoint();
            });
        }
    }

    public void removeNode(Node node) {
        nodes.remove(node);
        notifyObservers();
    }

    public List<Node> cloneNodes() {
        return cloneNodes(nodes);
    }

    public List<Node> cloneNodes(List<Node> nodesToClone) {
        List<Node> result = new ArrayList<>(nodesToClone.size());
        for(Node node : nodesToClone) {
            if(node instanceof Rectangle) {
                result.add(((Rectangle) node).clone());
            } else if(node instanceof Ellipse) {
                result.add(((Ellipse) node).clone());
            } else if(node instanceof Line) {
                result.add(((Line) node).clone());
            }
        }
        return result;
    }

}
