package com.vectorshapes.view.menu.side;

import javafx.scene.Node;

/**
 *
 * This class represents a list item in the NodesList. it holds a reference to the node which it represents.
 *
 * Created by 150019538 on 11/4/2015.
 */
class NodeItem {

    private String name;
    private Node node;

    public NodeItem(String name, Node node) {
        this.name = name;
        this.node = node;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
