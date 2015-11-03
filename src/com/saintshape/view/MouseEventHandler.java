package com.saintshape.view;

import com.saintshape.controller.Controller;
import com.saintshape.view.menu.side.Tool;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Created by 150019538 on 03/11/15.
 */
public class MouseEventHandler {

    private View view;
    private Shape selectedShape;
    private Controller controller;
    private MouseDelta delta;

    public MouseEventHandler(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
        delta = new MouseDelta();
    }

    public void register(Node node) {
        node.setOnMousePressed(resizeOnMousePressedEventHandler);
        node.setOnMouseMoved(resizeOnMouseMoveEventHandler);
        node.setOnMouseDragged(resizeOnMouseDragEventHandler);
        node.setOnMouseReleased(resizeOnMouseReleasedEventHandler);
    }

    public EventHandler<MouseEvent> resizeOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            // keep track of mouse movements
            delta.x = event.getX();
            delta.y = event.getY();

            // create a new shape if not already done
            if(selectedShape == null) {

//                if((event.getSceneX() > 0 && event.getSceneX() <= view.getRootCanvas().getWidth()) && (event.getSceneY() > 0 && event.getSceneY() <= view.getRootCanvas().getHeight())) {
                if(view.getSelectedTool() == Tool.RECTANGLE) {
                    selectedShape = new Rectangle(event.getX(), event.getY(), 0, 0);
                    selectedShape.setFill(view.getSelectedColor());
                    controller.addShape(selectedShape);
                }
//                }

            }

        }
    };

    public EventHandler<MouseEvent> resizeOnMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            // unselect shape when mouse released
            if(selectedShape != null) {
                selectedShape = null;
            }
        }
    };

    public EventHandler<MouseEvent> resizeOnMouseMoveEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            updateCursor(event);
        }
    };

    public EventHandler<MouseEvent> resizeOnMouseDragEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            if(selectedShape != null && event.isPrimaryButtonDown()) {

                if(selectedShape instanceof Rectangle) {
                    Rectangle rectangle = (Rectangle)selectedShape;

                    // Resize shape based on mouse movement
                    if ((event.getX() - delta.x) >= 0) {
                        rectangle.setWidth(event.getX() - delta.x);
                    } else {
                        rectangle.setX(event.getX());
                        rectangle.setWidth(delta.x - event.getX());
                    }
                    if ((event.getY() - delta.y) >= 0) {
                        rectangle.setHeight(event.getY() - delta.y);
                    } else {
                        rectangle.setY(event.getY());
                        rectangle.setHeight(delta.y - event.getY());
                    }

                    // make sure bounds don't go outside the canvas
                    if (event.getX() > view.getRootCanvas().getWidth()) {
                        rectangle.setWidth(view.getRootCanvas().getWidth() - rectangle.getX());
                    }
                    if (event.getX() < 0) {
                        rectangle.setX(0);
                        rectangle.setWidth(0 + delta.x);
                    }
                    if (event.getY() > view.getRootCanvas().getHeight()) {
                        rectangle.setHeight(view.getRootCanvas().getHeight() - rectangle.getY());
                    }
                    if (event.getY() < 0) {
                        rectangle.setY(0);
                        rectangle.setHeight(0 + delta.y);
                    }
                }

            }

        }
    };

    public void updateCursor(MouseEvent event) {
        if(event.getSource() instanceof Canvas) {
            if(view.getSelectedTool() == Tool.RECTANGLE) {
                view.getRootCanvas().setCursor(Cursor.CROSSHAIR);
            } else {
                view.getRootCanvas().setCursor(Cursor.DEFAULT);
            }
        }

    }

}
