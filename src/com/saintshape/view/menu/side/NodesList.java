package com.saintshape.view.menu.side;

import com.saintshape.controller.Controller;
import com.saintshape.model.Model;
import com.saintshape.observer.ModelObserver;
import com.saintshape.view.View;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * This class represents a list showing objects created in application
 *
 * Created by 150019538 on 11/4/2015.
 */
public class NodesList extends TitledPane implements ModelObserver {

    private List<NodeItem> nodeList;
    private ListView<NodeItem> listView;
    private ObservableList<NodeItem> myObservableList;
    private final int LIST_ITEM_HEIGHT = 25;
    private View view;
    private Controller controller;

    public NodesList(View view, Model model, Controller controller) {
        super("Objects", new ListView<NodeItem>());
        this.view = view;
        this.controller = controller;
        model.registerObserver(this);
        initialize();
    }

    /**
     * Initializes nodes list
     */
    private void initialize() {
        nodeList = new ArrayList<>();
        listView = new ListView<>();
        myObservableList = FXCollections.observableList(nodeList);
        listView.setPrefHeight(LIST_ITEM_HEIGHT + 2);
        listView.setItems(myObservableList);
        listView.setCellFactory(new Callback<ListView<NodeItem>, ListCell<NodeItem>>() {

            @Override
            public ListCell<NodeItem> call(ListView<NodeItem> p) {

                ListCell<NodeItem> cell = new ListCell<NodeItem>() {

                    @Override
                    protected void updateItem(NodeItem t, boolean bln) {
                        super.updateItem(t, bln);
                        if (t != null) {
                            setText(t.getName());
                        }
                    }

                };

                return cell;
            }
        });

        // focus on shape if clicked on in the list
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                view.selectNode(newValue.getNode());
            }
        });

        // deselect if esc or enter pressed
        listView.setOnKeyPressed(ke -> {
            if (ke.getCode() == KeyCode.ESCAPE || ke.getCode() == KeyCode.ENTER) {
                view.getMouseEventHandler().deselect();
                listView.getSelectionModel().clearSelection();
            }
        });

        Button buttonMoveUp = new Button("Up");
        HBox.setHgrow(buttonMoveUp, Priority.ALWAYS);
        buttonMoveUp.setMaxWidth(Double.MAX_VALUE/2);
        buttonMoveUp.setOnAction(event -> {
            NodeItem item = listView.getSelectionModel().getSelectedItem();
            if(item != null) {
                controller.moveUp(item.getNode());
                listView.getSelectionModel().select(item);
            }
        });

        Button buttonMoveDown = new Button("Down");
        HBox.setHgrow(buttonMoveDown, Priority.ALWAYS);
        buttonMoveDown.setMaxWidth(Double.MAX_VALUE/2);
        buttonMoveDown.setOnAction(event -> view.showImportImageDialog());
        buttonMoveDown.setOnAction(event -> {
            NodeItem item = listView.getSelectionModel().getSelectedItem();
            if(item != null) {
                controller.moveDown(item.getNode());
                listView.getSelectionModel().select(item);
            }
        });

        HBox boxRearrange = new HBox();
        boxRearrange.getChildren().addAll(buttonMoveUp, buttonMoveDown);
        getChildren().add(boxRearrange);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(0,0,0,0));
        vbox.getChildren().addAll(listView, boxRearrange);
        setMaxHeight(200);
        setContent(vbox);
    }

    /**
     * This class listens to changes in the model and updates the list of objects
     */
    @Override
    public void update() {}

    /**
     * This class listens to changes in the model and updates the list of objects
     * @param model of drawing application (MVC)
     */
    @Override
    public void update(Model model) {
        List<Node> nodes = model.getNodes();
        myObservableList.clear();
        for(Node node : nodes) {
            myObservableList.add(0, new NodeItem(node.getTypeSelector(), node));
        }
        if(listView.getItems().size() > 0) {
            listView.setPrefHeight(myObservableList.size() * LIST_ITEM_HEIGHT + 2);
        } else {
            listView.setPrefHeight(LIST_ITEM_HEIGHT + 2);
        }
    }

    /**
     * Selects a node in the list
     * @param node to select in list
     */
    public void selectNodeInList(Node node) {
        ObservableList<NodeItem> list = listView.getItems();
        Iterator it = list.iterator();
        while(it.hasNext()) {
            NodeItem nodeItem = (NodeItem)it.next();
            if(nodeItem.getNode() == node) {
                listView.getSelectionModel().select(nodeItem);
                break;
            }
        }
    }

}
