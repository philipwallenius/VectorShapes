package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 * Created by 150019538 on 08/11/15.
 */
public class ResizeEventHandler {

    private View view;
    private Controller controller;

    private MouseClick click = new MouseClick();
    private double selectionWidth = 0;
    private double selectionHeight = 0;
    private double selectionX = 0;
    private double selectionY = 0;

    public ResizeEventHandler(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }

    public void register(List<Point> points) {
        for(Point point : points) {
            point.setOnMousePressed(mousePressEventHandler);
            point.setOnMouseDragged(mouseDragEventHandler);
        }
    }

    public EventHandler<MouseEvent> mousePressEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            click.x = event.getX();
            click.y = event.getY();
            if (event.getSource() instanceof Point) {
                Point point = (Point) event.getSource();
                selectionWidth = point.getSelection().getWidth();
                selectionHeight = point.getSelection().getHeight();
                selectionX = point.getSelection().getX();
                selectionY = point.getSelection().getY();
            }
        }
    };

    public EventHandler<MouseEvent> mouseDragEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(event.getSource() instanceof Point) {
                Point point = (Point) event.getSource();
                Selection rectangle = (Selection)point.getSelection();
                if (event.isPrimaryButtonDown()) {
                    if(rectangle != null ) {

                        // get current mouse coordinates
                        double currentX = event.getX();
                        double currentY = event.getY();

                        // enforce resize within canvas bounds
                        currentX = Math.max(0, currentX);
                        currentX = Math.min(controller.getRootCanvas().getWidth(), currentX);
                        currentY = Math.max(0, currentY);
                        currentY = Math.min(controller.getRootCanvas().getHeight(), currentY);

                        // calculate width and height difference
                        double width = click.x-currentX;
                        double height = click.y-currentY;

                        // fix ratio if shift is pressed
                        if(event.isShiftDown()) {
                            width = Math.min(width, height);
                            height = Math.min(width, height);
                        }

                        // resize differently depending on which corner is selected
                        if(point.getId().equals("1")) {

                            // enforce minimum width and height of 1 px
                            width = Math.max(width, -selectionWidth + 1);
                            height = Math.max(height, -selectionHeight + 1);

                            double newWidth = selectionWidth + width;
                            double newHeight = selectionHeight + height;

                            if (width > 0) {
                                rectangle.setX(selectionX - Math.abs(width));
                            } else {
                                rectangle.setX(selectionX + Math.abs(width));
                            }

                            if (height > 0) {
                                rectangle.setY(selectionY - Math.abs(height));
                            } else {
                                rectangle.setY(selectionY + Math.abs(height));
                            }

                            rectangle.setWidth(newWidth);
                            rectangle.setHeight(newHeight);

                        } else if(point.getId().equals("2")) {

                            // enforce minimum width and height of 1 px
                            width = Math.min(width, selectionWidth - 1);
                            height = Math.max(height, -selectionHeight + 1);

                            if(height > 0) {
                                rectangle.setY(selectionY -Math.abs(height));
                            } else {
                                rectangle.setY(selectionY +Math.abs(height));
                            }
                            rectangle.setWidth(selectionWidth -width);
                            rectangle.setHeight(selectionHeight +height);

                        } else if(point.getId().equals("3")) {

                            // enforce minimum width and height of 1 px
                            width = Math.max(width, -selectionWidth + 1);
                            height = Math.min(height, selectionHeight - 1);

                            if(width > 0) {
                                rectangle.setX(selectionX -Math.abs(width));
                            } else {
                                rectangle.setX(selectionX +Math.abs(width));
                            }
                            rectangle.setWidth(selectionWidth +width);
                            rectangle.setHeight(selectionHeight -height);

                        } else if(point.getId().equals("4")) {

                            // enforce minimum width and height of 1 px
                            width = Math.min(width, selectionWidth - 1);
                            height = Math.min(height, selectionHeight - 1);

                            rectangle.setWidth(selectionWidth -width);
                            rectangle.setHeight(selectionHeight -height);
                        }
                    }
                }
            }
        }
    };


}
