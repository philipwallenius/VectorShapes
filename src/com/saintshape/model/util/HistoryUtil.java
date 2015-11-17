package com.saintshape.model.util;

import com.saintshape.model.Model;
import com.saintshape.view.event.MouseEventHandler;
import javafx.scene.Node;
import javafx.scene.shape.Shape;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 150019538 on 16/11/15.
 */
public class HistoryUtil {

    private List<List<Node>> history;
    private int historyPointer;
    private Model model;
    private MouseEventHandler mouseEventHandler;

    private static HistoryUtil instance;

    private HistoryUtil() {
        history = new LinkedList<>();
        historyPointer = 0;
    }

    public static HistoryUtil getInstance() {
        if(instance == null) {
            instance = new HistoryUtil();
        }
        return instance;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void resetHistory() {
        for(List<Node> h : history) {
            h.clear();
        }
        history.clear();
        historyPointer = -1;
        addHistoryPoint();
    }

    public void addHistoryPoint() {

        if(historyPointer != (history.size()-1)) {
            int diff = (history.size()-1)-historyPointer;
            while(diff != 0) {
                history.remove(historyPointer+diff);
                diff--;
            }
        }

        List<Node> nodes = model.cloneNodes();
        register(nodes);
        history.add(nodes);
        historyPointer++;
    }

    public void undo() {
        if((historyPointer - 1) >= 0) {
            historyPointer--;
            List<Node> nodes = model.cloneNodes(history.get(historyPointer));
            register(nodes);
            model.setNodes(nodes);
            model.notifyObservers();
        }
    }

    public void redo() {
        if((historyPointer + 1) < history.size()) {
            historyPointer++;
            model.setNodes(history.get(historyPointer));
            model.notifyObservers();
        }
    }

    public void setMouseEventHandler(MouseEventHandler mouseEventHandler) {
        this.mouseEventHandler = mouseEventHandler;
    }

    private void register(List<Node> nodes) {
        for(Node node: nodes) {
            mouseEventHandler.register(node);
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
