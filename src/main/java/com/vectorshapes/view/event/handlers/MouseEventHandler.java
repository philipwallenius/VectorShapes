package com.vectorshapes.view.event.handlers;

import com.vectorshapes.controller.Controller;
import com.vectorshapes.view.View;
import com.vectorshapes.view.event.factory.EventHandlerFactory;
import com.vectorshapes.view.event.handlers.entity.Selection;
import com.vectorshapes.view.menu.side.Tool;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 *
 * This class is the main mouse event handler which redirects mouse events to appropriate handlers based on the tool
 *
 * Created by philipwallenius on 03/11/15.
 */
public class MouseEventHandler {

    private final View view;
    private final EventHandlerFactory eventHandlerFactory;

    public MouseEventHandler(View view, Controller controller) {
        this.view = view;
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
    private final EventHandler<MouseEvent> mousePressEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            eventHandler.handleMousePress(event);
        }
    };

    /**
     * Handles mouse release events
     */
    private final EventHandler<MouseEvent> mouseReleaseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            eventHandler.handleMouseRelease(event);
        }
    };

    /**
     * Handles mouse move events
     */
    private final EventHandler<MouseEvent> mouseMoveEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            eventHandler.handleMouseMove(event);
        }
    };

    /**
     * Handles mouse drag events
     */
    private final EventHandler<MouseEvent> mouseDragEventHandler = new EventHandler<MouseEvent>() {
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
