package com.saintshape.model.util;

import com.saintshape.model.Model;
import com.saintshape.view.event.handlers.MouseEventHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * HistoryUtil is a singleton class responsible for keeping track of model changes to enable undo and redo
 *
 * Created by 150019538 on 16/11/15.
 */
public class HistoryUtil {

    private ObservableList<List<Node>> history;

    private IntegerProperty historyPointer;
    private Model model;
    private MouseEventHandler mouseEventHandler;

    private BooleanProperty disableUndo;
    private BooleanProperty disableRedo;

    private static HistoryUtil instance;

    private HistoryUtil() {
        history = FXCollections.observableList(new ArrayList<>());
        historyPointer = new SimpleIntegerProperty(0);
        disableUndo = new SimpleBooleanProperty();
        disableUndo.bind(historyPointer.subtract(1).lessThan(0));
        disableRedo = new SimpleBooleanProperty();
        disableRedo.bind(historyPointer.add(1).greaterThanOrEqualTo(Bindings.size(history)));
    }

    /**
     * Returns a singleton instance of this class or creates one if it doesn't exist
     * @return Returns instance of the class
     */
    public static HistoryUtil getInstance() {
        if(instance == null) {
            instance = new HistoryUtil();
        }
        return instance;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Removes all history of the model
     */
    public void resetHistory() {
        for(List<Node> h : history) {
            h.clear();
        }
        history.clear();
        historyPointer.setValue(-1);
        addHistoryPoint();
    }

    /**
     * Adds a new history point. It saves a copy of the entire model.
     */
    public void addHistoryPoint() {
        model.setHasUnsavedChanges(true);

        // if change is made when history pointer isn't at the end of the history list
        // it remove all trailing entries.
        if (historyPointer.get() != (history.size() - 1)) {
            int diff = (history.size() - 1) - historyPointer.get();
            while (diff > 0) {
                history.remove(historyPointer.get() + diff);
                diff--;
            }
        }

        // save a copy of the model and move history pointer forward
        List<Node> nodes = model.cloneNodes();
        register(nodes);
        history.add(nodes);
        historyPointer.set(historyPointer.getValue() + 1);
    }

    /**
     * Undo method. Steps back in the history.
     */
    public void undo() {
        if((historyPointer.get() - 1) >= 0) {
            historyPointer.set(historyPointer.getValue() - 1);
            List<Node> nodes = model.cloneNodes(history.get(historyPointer.get()));
            register(nodes);
            model.setNodes(nodes);
            model.notifyObservers();
        }
    }

    /**
     * Redo method. Steps forward in the history.
     */
    public void redo() {
        if((historyPointer.get() + 1) < history.size()) {
            historyPointer.set(historyPointer.getValue()+1);
            List<Node> nodes = model.cloneNodes(history.get(historyPointer.get()));
            register(nodes);
            model.setNodes(nodes);
            model.notifyObservers();
        }
    }

    public void setMouseEventHandler(MouseEventHandler mouseEventHandler) {
        this.mouseEventHandler = mouseEventHandler;
    }

    /**
     * Registers nodes from history for mouse events
     * @param nodes
     */
    private void register(List<Node> nodes) {
        for(Node node: nodes) {
            mouseEventHandler.register(node);
            if(node instanceof Shape) {
                Shape s = (Shape) node;
                s.fillProperty().addListener((observable, oldValue, newValue) -> {
                    HistoryUtil.getInstance().addHistoryPoint();
                });
                s.strokeProperty().addListener((observable, oldValue, newValue) -> {
                    HistoryUtil.getInstance().addHistoryPoint();
                });
            }
        }
    }

    public boolean getDisableRedo() {
        return disableRedo.get();
    }

    public BooleanProperty disableRedoProperty() {
        return disableRedo;
    }

    public void setDisableRedo(boolean disableRedo) {
        this.disableRedo.set(disableRedo);
    }

    public boolean getDisableUndo() {
        return disableUndo.get();
    }

    public BooleanProperty disableUndoProperty() {
        return disableUndo;
    }

    public void setDisableUndo(boolean disableUndo) {
        this.disableUndo.set(disableUndo);
    }
}
