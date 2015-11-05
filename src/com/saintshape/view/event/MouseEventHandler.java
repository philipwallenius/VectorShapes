package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.menu.side.Tool;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
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

    public void register(Node node) {
        node.setOnMousePressed(mousePressEventHandler);
        node.setOnMouseMoved(mouseMoveEventHandler);
        node.setOnMouseDragged(mouseDragEventHandler);
        node.setOnMouseReleased(mouseReleaseEventHandler);
    }

    public EventHandler<MouseEvent> mousePressEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            eventHandler.handleMousePress(event);
        }
    };

    public EventHandler<MouseEvent> mouseReleaseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            eventHandler.handleMouseRelease(event);
        }
    };

    public EventHandler<MouseEvent> mouseMoveEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            updateCursor();
            eventHandler.handleMouseMove(event);
        }
    };

    public EventHandler<MouseEvent> mouseDragEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            ToolEventHandler eventHandler = eventHandlerFactory.getEventHandler(view.getSelectedTool());
            eventHandler.handleMouseDrag(event);
        }
    };

    public void updateCursor() {
            if(isCrosshairTool(view.getSelectedTool())) {
                controller.getRootCanvas().setCursor(Cursor.CROSSHAIR);
                for(Node node : controller.getNodes()) {
                    node.setCursor(Cursor.CROSSHAIR);
                }
            } else {
                controller.getRootCanvas().setCursor(Cursor.DEFAULT);
                for(Node node : controller.getNodes()) {
                    node.setCursor(Cursor.DEFAULT);
                }
            }
    }

    public boolean isCrosshairTool(Tool tool) {
        switch(tool) {
            case RECTANGLE: {}
            case LINE: {}
            case ELLIPSE: {
                return true;
            }
        }
        return false;
    }

}
