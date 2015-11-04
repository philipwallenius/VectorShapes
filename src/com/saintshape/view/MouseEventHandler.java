package com.saintshape.view;

import com.saintshape.controller.Controller;
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
                if(view.getSelectedTool() == Tool.RECTANGLE) {
                    selectedShape = new Rectangle(event.getX(), event.getY(), 0, 0);
                    selectedShape.setFill(view.getSelectedColor());
                    register(selectedShape);
                    controller.addNode(selectedShape);
                }
                if(view.getSelectedTool() == Tool.ELLIPSE) {
                    selectedShape = new Ellipse(event.getX(), event.getY(), 0, 0);
//                    selectedShape.setFill(view.getSelectedColor());
                    selectedShape.setStroke(view.getSelectedColor());
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


            if(selectedShape != null && event.isPrimaryButtonDown()) {

                if(selectedShape instanceof Rectangle) {
                    Rectangle rectangle = (Rectangle)selectedShape;

                    // Resize shape based on mouse movement
                    if(event.isShiftDown()) {

                        double d = Math.abs(delta.x - currentX);

                        if ((currentX - delta.x) >= 0) {
                            rectangle.setX(delta.x);
                            rectangle.setWidth(d);
                        } else {
                            rectangle.setX(delta.x-d);
                            rectangle.setWidth(d);
                        }

                        if ((currentY - delta.y) >= 0) {
//                            if((delta.y+d) > controller.getRootCanvas().getHeight()) {
//                                d -= ((delta.y+d)-controller.getRootCanvas().getHeight());
//                            }
                            rectangle.setY(delta.y);
                            rectangle.setHeight(d);
                        } else {
//                            if((delta.y-d) < 0) {
//                                d += Math.abs(0-(delta.y-d));
//                            }
                            rectangle.setY(delta.y-d);
                            rectangle.setHeight(d);
                        }



                    } else {
                        if ((currentX - delta.x) >= 0) {
                            rectangle.setX(delta.x);
                            rectangle.setWidth(currentX - delta.x);
                        } else {
                            rectangle.setX(currentX);
                            rectangle.setWidth(delta.x - currentX);
                        }
                        if ((currentY - delta.y) >= 0) {
                            rectangle.setY(delta.y);
                            rectangle.setHeight(currentY - delta.y);
                        } else {
                            rectangle.setY(currentY);
                            rectangle.setHeight(delta.y - currentY);
                        }
                    }

                    // make sure bounds don't go outside the canvas
//                    if (event.getX() > controller.getRootCanvas().getWidth()) {
//                        rectangle.setWidth(controller.getRootCanvas().getWidth() - rectangle.getX());
//                    }
//                    if (event.getX() < 0) {
//                        rectangle.setX(0);
//                        rectangle.setWidth(0 + delta.x);
//                    }
//                    if (event.getY() > controller.getRootCanvas().getHeight()) {
//                        rectangle.setHeight(controller.getRootCanvas().getHeight() - rectangle.getY());
//                    }
//                    if (event.getY() < 0) {
//                        rectangle.setY(0);
//                        rectangle.setHeight(0 + delta.y);
//                    }
                }

                if(selectedShape instanceof Ellipse) {
                    Ellipse ellipse = (Ellipse)selectedShape;

                    if(event.isShiftDown()) {
                        double d = Math.abs(delta.x - currentX);

                        if ((currentX - delta.x) >= 0) {
                            ellipse.setCenterX(delta.x + (d / 2));
                            ellipse.setRadiusX(Math.abs(d) / 2);
                        } else {
                            ellipse.setCenterX(delta.x - (d / 2));
                            ellipse.setRadiusX(Math.abs(d) / 2);
                        }

                        if ((currentY - delta.y) >= 0) {
                            ellipse.setCenterY(delta.y + (d / 2));
                            ellipse.setRadiusY(Math.abs(d) / 2);
                        } else {
                            ellipse.setCenterY(delta.y - (d / 2));
                            ellipse.setRadiusY(Math.abs(d) / 2);
                        }


                    } else {

                        if ((currentX - delta.x) >= 0) {
                            ellipse.setCenterX(delta.x + (Math.abs(delta.x - currentX) / 2));
                            ellipse.setRadiusX(Math.abs(currentX - delta.x) / 2);
                        } else {
                            ellipse.setCenterX(delta.x - (Math.abs(delta.x - currentX) / 2));
                            ellipse.setRadiusX(Math.abs(currentX - delta.x) / 2);
                        }

                        if ((currentY - delta.y) >= 0) {
                            ellipse.setCenterY(delta.y + (Math.abs(delta.y - currentY) / 2));
                            ellipse.setRadiusY(Math.abs(currentY - delta.y) / 2);
                        } else {
                            ellipse.setCenterY(delta.y - (Math.abs(delta.y - currentY) / 2));
                            ellipse.setRadiusY(Math.abs(currentY - delta.y) / 2);
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
