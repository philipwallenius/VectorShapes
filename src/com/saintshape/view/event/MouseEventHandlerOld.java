package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.menu.side.Tool;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 * Created by 150019538 on 03/11/15.
 */
public class MouseEventHandlerOld {

    private View view;
    private Shape selectedShape;
    private Controller controller;
    private MouseClick mouseClick;

    public MouseEventHandlerOld(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
        mouseClick = new MouseClick();
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
            mouseClick.x = event.getX();
            mouseClick.y = event.getY();

            // create a new shape if not already done
            if(selectedShape == null) {
                if(view.getSelectedTool() == Tool.RECTANGLE) {
                    selectedShape = new Rectangle(event.getX(), event.getY(), 0, 0);
                    selectedShape.setFill(view.getSelectedColor());
                    register(selectedShape);
                    controller.addNode(selectedShape);
                }
                if(view.getSelectedTool() == Tool.ELLIPSE) {
                    selectedShape = new Ellipse(event.getX(), event.getY(), 0, 0);
                    selectedShape.setFill(view.getSelectedColor());
                    register(selectedShape);
                    controller.addNode(selectedShape);
                }
            }

        }
    };

    public EventHandler<MouseEvent> resizeOnMouseReleasedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            // unselect shape when mouse released
            selectedShape = null;
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

            double currentX = event.getX();
            double currentY = event.getY();

            // make sure mouse is within bounds
            if(currentX < 0) {
                currentX = 0;
            } else if(currentX > controller.getRootCanvas().getWidth()) {
                currentX = controller.getRootCanvas().getWidth();
            }

            if(currentY < 0) {
                currentY = 0;
            } else if(currentY > controller.getRootCanvas().getHeight()) {
                currentY = controller.getRootCanvas().getHeight();
            }

            double width = Math.abs(currentX - mouseClick.x);
            double height = Math.abs(currentY - mouseClick.y);

            if(selectedShape != null && event.isPrimaryButtonDown()) {

                if(selectedShape instanceof Rectangle) {

                    Rectangle rectangle = (Rectangle)selectedShape;

                    if(event.isShiftDown()) {

                        width = Math.min(width, height);
                        height = Math.min(width, height);

                        if ((currentX - mouseClick.x) >= 0) {
                            rectangle.setX(mouseClick.x);
                            rectangle.setWidth(width);
                        } else {
                            rectangle.setX(mouseClick.x - width);
                            rectangle.setWidth(width);
                        }

                        if ((currentY - mouseClick.y) >= 0) {
                            rectangle.setY(mouseClick.y);
                            rectangle.setHeight(height);
                        } else {
                            rectangle.setY(mouseClick.y - height);
                            rectangle.setHeight(height);
                        }

                    } else {
                        if ((currentX - mouseClick.x) >= 0) {
                            rectangle.setX(mouseClick.x);
                            rectangle.setWidth(width);
                        } else {
                            rectangle.setX(currentX);
                            rectangle.setWidth(width);
                        }
                        if ((currentY - mouseClick.y) >= 0) {
                            rectangle.setY(mouseClick.y);
                            rectangle.setHeight(height);
                        } else {
                            rectangle.setY(currentY);
                            rectangle.setHeight(height);
                        }
                    }

                }

                if(selectedShape instanceof Ellipse) {
                    Ellipse ellipse = (Ellipse)selectedShape;

                    if(event.isShiftDown()) {

                        width = Math.min(width, height);
                        height = Math.min(width, height);

                        if ((currentX - mouseClick.x) >= 0) {
                            ellipse.setCenterX(mouseClick.x + (width / 2));
                            ellipse.setRadiusX(Math.abs(width) / 2);
                        } else {
                            ellipse.setCenterX(mouseClick.x - (width / 2));
                            ellipse.setRadiusX(Math.abs(width) / 2);
                        }

                        if ((currentY - mouseClick.y) >= 0) {
                            ellipse.setCenterY(mouseClick.y + (height / 2));
                            ellipse.setRadiusY(Math.abs(height) / 2);
                        } else {
                            ellipse.setCenterY(mouseClick.y - (height / 2));
                            ellipse.setRadiusY(Math.abs(height) / 2);
                        }


                    } else {

                        if ((currentX - mouseClick.x) >= 0) {
                            ellipse.setCenterX(mouseClick.x + (width / 2));
                            ellipse.setRadiusX(width / 2);
                        } else {
                            ellipse.setCenterX(mouseClick.x - (width / 2));
                            ellipse.setRadiusX(width / 2);
                        }

                        if ((currentY - mouseClick.y) >= 0) {
                            ellipse.setCenterY(mouseClick.y + (height / 2));
                            ellipse.setRadiusY(height / 2);
                        } else {
                            ellipse.setCenterY(mouseClick.y - (height / 2));
                            ellipse.setRadiusY(height / 2);
                        }
                    }

                }

            }

        }
    };

    public void updateCursor(MouseEvent event) {
            if(view.getSelectedTool() == Tool.RECTANGLE) {
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

}
