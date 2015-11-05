package com.saintshape.view.menu.side;

import com.saintshape.model.Model;
import com.saintshape.observer.ModelObserver;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
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
    private final int LIST_ITEM_HEIGHT = 20;

    public NodesList(Model model) {
        super("Objects", new ListView<NodeItem>());
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
    }

    @Override
    public void update() {

    }

    @Override
    public void update(Model model) {
        List<Node> nodes = model.getNodes();
        myObservableList.clear();
        int i = 0;
        for(Node node : nodes) {
            myObservableList.add(new NodeItem(node.getTypeSelector() + " " + i, node));
            i++;
        }
        if(listView.getItems().size() > 0) {
            listView.setPrefHeight(myObservableList.size() * LIST_ITEM_HEIGHT + 2);
        } else {
            listView.setPrefHeight(LIST_ITEM_HEIGHT + 2);
        }

    }
}
