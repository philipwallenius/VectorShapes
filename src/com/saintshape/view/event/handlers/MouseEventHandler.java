package com.saintshape.view.event.handlers;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.event.handlers.entity.Selection;
import com.saintshape.view.event.factory.EventHandlerFactory;
import com.saintshape.view.menu.side.Tool;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 *
 * This class is the main mouse event handler which redirects mouse events to appropriate handlers based on the tool
 *
 * Created by 150019538 on 03/11/15.
 */
public class MouseEventHandler {

    private View view;
    private Controller controller;
    private EventHandlerFactory eventHandlerFactory;

    public MouseEventHandler(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
        eventHandlerFactory = new EventHandlerFactory(view, controller, this);
    }

    /**
     * Registers node for mouse events
     * @param node to register for mouse events
     */
    public void register(Node node) {
        node.setOnMousePressed(mousePressEventHandler);
        node.setOnMouseMoved(mouseMoveEventHandler);
        node.setOnMouseDragged(mouseDragEventHandler);
        node.setOnMouseReleased(mouseReleaseEventHandler);
    }

    /**
     * Handles for mouse press events
     */
    public EventHandler<MouseEvent> mousePressEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            eventHandler.handleMousePress(event);
        }
    };

    /**
     * Handles mouse release events
     */
    public EventHandler<MouseEvent> mouseReleaseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            eventHandler.handleMouseRelease(event);
        }
    };

    /**
     * Handles mouse move events
     */
    public EventHandler<MouseEvent> mouseMoveEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            eventHandler.handleMouseMove(event);
        }
    };

    /**
     * Handles mouse drag events
     */
    public EventHandler<MouseEvent> mouseDragEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            eventHandler.handleMouseDrag(event);
        }
    };

    /**
     * Selects node in view
     * @param node to select in view
     */
    public void selectNode(Node node) {
        ToolEventHandler toolEventHandler = eventHandlerFactory.getEventHandler(Tool.SELECT);
        ((SelectEventHandler)toolEventHandler).createSelection(node);
    }

    /**
     * Registers selection for mouse events
     * @param selection to register for mouse events
     */
    public void registerSelection(Selection selection) {
        ToolEventHandler toolEventHandler = eventHandlerFactory.getEventHandler(Tool.SELECT);
        selection.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                toolEventHandler.handleMousePress(event);
            }
        });
        selection.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                toolEventHandler.handleMouseDrag(event);
            }
        });
        selection.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                toolEventHandler.handleMouseRelease(event);
            }
        });
        selection.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                toolEventHandler.handleMouseMove(event);
            }
        });
    }

    /**
     * Deselects all shapes in view
     */
    public void deselect() {
        SelectEventHandler selectEventHandler = (SelectEventHandler)eventHandlerFactory.getEventHandler(Tool.SELECT);
        selectEventHandler.deselect();
    }

    /**
     * Gets current selection
     * @return Returns current selection
     */
    public Selection getSelection() {
        SelectEventHandler selectEventHandler = (SelectEventHandler)eventHandlerFactory.getEventHandler(Tool.SELECT);
        return selectEventHandler.getSelection();
    }
}