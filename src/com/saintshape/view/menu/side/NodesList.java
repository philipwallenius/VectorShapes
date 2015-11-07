package com.saintshape.view.menu.side;

import com.saintshape.model.Model;
import com.saintshape.observer.ModelObserver;
import com.saintshape.view.View;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 150019538 on 11/4/2015.
 */
public class NodesList extends TitledPane implements ModelObserver {

    private List<NodeItem> nodeList;
    private ListView<NodeItem> listView;
    private ObservableList<NodeItem> myObservableList;
    private final int LIST_ITEM_HEIGHT = 25;
    private View view;

    public NodesList(View view, Model model) {
        super("Objects", new ListView<NodeItem>());
        this.view = view;
        model.registerObserver(this);
        initialize();
        setContent(listView);
    }

    private void initialize() {
        nodeList = new ArrayList<>();
        listView = new ListView<>();
        myObservableList = FXCollections.observableList(nodeList);
        listView.setPrefHeight(LIST_ITEM_HEIGHT + 2);
        listView.setItems(myObservableList);
        listView.setCellFactory(new Callback<ListView<NodeItem>, ListCell<NodeItem>>(){

            @Override
            public ListCell<NodeItem> call(ListView<NodeItem> p) {

                ListCell<NodeItem> cell = new ListCell<NodeItem>(){

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

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<NodeItem>() {
            @Override
            public void changed(ObservableValue<? extends NodeItem> observable, NodeItem oldValue, NodeItem newValue) {
                System.out.println("change list");
                if (newValue != null) {
                    view.selectNode(newValue.getNode());
                }
            }
        });

        listView.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(!newValue) {
                    listView.getSelectionModel().clearSelection();
                }
            }
        });

    }

    @Override
    public void update() {

    }

    @Override
    public void update(Model model) {
        List<Node> nodes = model.getNodes();
        myObservableList.clear();
        for(Node node : nodes) {
            myObservableList.add(new NodeItem(node.getTypeSelector(), node));
        }
        if(listView.getItems().size() > 0) {
            listView.setPrefHeight(myObservableList.size() * LIST_ITEM_HEIGHT + 2);
        } else {
            listView.setPrefHeight(LIST_ITEM_HEIGHT + 2);
        }

    }
}
