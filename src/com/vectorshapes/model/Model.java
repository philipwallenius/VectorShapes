package com.vectorshapes.model;

import com.vectorshapes.model.shape.Ellipse;
import com.vectorshapes.model.shape.Line;
import com.vectorshapes.model.shape.Rectangle;
import com.vectorshapes.model.util.HistoryUtil;
import com.vectorshapes.observer.ModelObserver;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Shape;

import java.io.File;
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
    private final List<ModelObserver> observers;
    private final HistoryUtil historyUtil;
    private File file;
    private final BooleanProperty fileProperty;

    /**
     * Constructor
     */
    public Model() {
        hasUnsavedChanges = false;
        rootCanvas = new Canvas();
        nodes = new ArrayList<>();
        observers = new ArrayList<>();
        historyUtil = HistoryUtil.getInstance();
        fileProperty = new SimpleBooleanProperty(true);
    }

    public void reset() {
        name = "";
        nodes.clear();
        setFile(null);
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

    /**
     * Notifies observers of changes
     */
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

    /**
     * Adds new shape/image to model
     * @param node to add to model
     */
    public void addNode(Node node) {
        addColorChangeListener(node);
        nodes.add(node);
        notifyObservers();
    }

    /**
     * Listen to color changes of nodes so that we can support undo and redo
     * @param node to listen for color changes
     */
    public void addColorChangeListener(Node node) {
        // Add history point if color of shape is changed
        if(node instanceof Shape) {
            Shape s = (Shape)node;
            s.fillProperty().addListener((observable, oldValue, newValue) -> {
                HistoryUtil.getInstance().addHistoryPoint();
            });
            s.strokeProperty().addListener((observable, oldValue, newValue) -> {
                HistoryUtil.getInstance().addHistoryPoint();
            });
            s.strokeWidthProperty().addListener((observable, oldValue, newValue) -> {
                HistoryUtil.getInstance().addHistoryPoint();
            });
        }
    }

    /**
     * Removes node from model
     * @param node to remove from model
     */
    public void removeNode(Node node) {
        nodes.remove(node);
        notifyObservers();
    }

    /**
     * Clones all nodes in model
     * @return List with all node clones
     */
    public List<Node> cloneNodes() {
        return cloneNodes(nodes);
    }

    /**
     * Clones all nodes in list passed to it
     * @param nodesToClone nodes to clone
     * @return List with all node clones
     */
    public List<Node> cloneNodes(List<Node> nodesToClone) {
        List<Node> result = new ArrayList<>(nodesToClone.size());
        for(Node node : nodesToClone) {
            if(node instanceof Rectangle) {
                result.add(((Rectangle) node).clone());
            } else if(node instanceof Ellipse) {
                result.add(((Ellipse) node).clone());
            } else if(node instanceof Line) {
                result.add(((Line) node).clone());
            } else if(node instanceof ImageView) {
                ImageView oldImage = (ImageView)node;
                ImageView newImageView = new ImageView(oldImage.getImage());
                newImageView.setX(oldImage.getX());
                newImageView.setY(oldImage.getY());
                newImageView.setFitWidth(oldImage.getFitWidth());
                newImageView.setFitHeight(oldImage.getFitHeight());
                newImageView.getTransforms().addAll(oldImage.getTransforms());
                result.add(newImageView);
            }
        }
        return result;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        if(file != null) {
            fileProperty.setValue(false);
        }
    }

    public ObservableBooleanValue getFileProperty() {
        return fileProperty;
    }

    public void moveUp(Node node) {
        int currentIndex = nodes.indexOf(node);
        if(currentIndex != nodes.size()-1) {
            nodes.remove(currentIndex);
            nodes.add(currentIndex + 1, node);
            HistoryUtil.getInstance().addHistoryPoint();
            notifyObservers();
        }
    }

    public void moveDown(Node node) {
        int currentIndex = nodes.indexOf(node);
        if(currentIndex != 0) {
            nodes.remove(currentIndex);
            nodes.add(currentIndex - 1, node);
            HistoryUtil.getInstance().addHistoryPoint();
            notifyObservers();
        }
    }
}
