package com.vectorshapes.view.event.handlers;

import com.vectorshapes.model.util.HistoryUtil;
import com.vectorshapes.view.event.handlers.entity.MouseClick;
import com.vectorshapes.view.event.handlers.entity.Point;
import com.vectorshapes.view.event.handlers.entity.Selection;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import java.util.List;

/**
 *
 * This class handles resize mouse events for the select tool
 *
 * Created by 150019538 on 08/11/15.
 */
class ResizeEventHandler {

    private final MouseClick click = new MouseClick();
    private double selectionWidth;
    private double selectionHeight;
    private double selectionX;
    private double selectionY;
    private boolean resized = false;

    public void register(List<Point> points) {
        for(Point point : points) {
            point.setOnMousePressed(mousePressEventHandler);
            point.setOnMouseDragged(mouseDragEventHandler);
            point.setOnMouseReleased(mouseReleaseEventHandler);
        }
    }

    private final EventHandler<MouseEvent> mousePressEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            resized = false;
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

    private final EventHandler<MouseEvent> mouseReleaseEventHandler = event -> {
        if(resized) {
            HistoryUtil.getInstance().addHistoryPoint();
        }
        resized = false;
    };

    private final EventHandler<MouseEvent> mouseDragEventHandler = new EventHandler<MouseEvent>() {
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

                        // calculate width and height difference
                        double widthChange = click.x-currentX;
                        double heightChange = click.y-currentY;

                        // resize differently depending on which corner is selected
                        if(point.getId().equals("NW")) {

                            // fix ratio if shift is pressed
                            if(event.isShiftDown()) {
                                widthChange = Math.min(widthChange, heightChange);
                                heightChange = Math.min(widthChange, heightChange);
                            }

                            // enforce minimum width and height of 1 px
                            widthChange = Math.max(widthChange, -selectionWidth + 1);
                            heightChange = Math.max(heightChange, -selectionHeight + 1);

                            rectangle.setX(selectionX - widthChange);
                            rectangle.setY(selectionY - heightChange);
                            rectangle.setWidth(selectionWidth + widthChange);
                            rectangle.setHeight(selectionHeight + heightChange);

                        } else if(point.getId().equals("NE")) {

                            // fix ratio if shift is pressed
                            if(event.isShiftDown()) {
                                widthChange = Math.min(-widthChange, heightChange);
                                heightChange = Math.min(widthChange, heightChange);
                                widthChange = -widthChange;
                            }

                            // enforce minimum width and height of 1 px
                            widthChange = Math.min(widthChange, selectionWidth - 1);
                            heightChange = Math.max(heightChange, -selectionHeight + 1);

                            rectangle.setY(selectionY - heightChange);
                            rectangle.setWidth(selectionWidth - widthChange);
                            rectangle.setHeight(selectionHeight + heightChange);

                        } else if(point.getId().equals("SW")) {

                            // fix ratio if shift is pressed
                            if(event.isShiftDown()) {

                                widthChange = Math.max(-widthChange, heightChange);
                                heightChange = Math.max(widthChange, heightChange);
                                widthChange = -widthChange;

                            }

                            // enforce minimum width and height of 1 px
                            widthChange = Math.max(widthChange, -selectionWidth + 1);
                            heightChange = Math.min(heightChange, selectionHeight - 1);

                            if(widthChange > 0) {
                                rectangle.setX(selectionX -Math.abs(widthChange));
                            } else {
                                rectangle.setX(selectionX +Math.abs(widthChange));
                            }
                            rectangle.setWidth(selectionWidth + widthChange);
                            rectangle.setHeight(selectionHeight - heightChange);

                        } else if(point.getId().equals("SE")) {

                            // fix ratio if shift is pressed
                            if(event.isShiftDown()) {
                                widthChange = Math.max(widthChange, heightChange);
                                heightChange = Math.max(widthChange, heightChange);
                            }

                            // enforce minimum width and height of 1 px
                            widthChange = Math.min(widthChange, selectionWidth - 1);
                            heightChange = Math.min(heightChange, selectionHeight - 1);

                            rectangle.setWidth(selectionWidth - widthChange);
                            rectangle.setHeight(selectionHeight - heightChange);
                        } else if(point.getId().equals("W")) {
                            // enforce minimum width and height of 1 px
                            widthChange = Math.max(widthChange, -selectionWidth + 1);
                            rectangle.setX(selectionX - widthChange);
                            rectangle.setWidth(selectionWidth + widthChange);
                        } else if(point.getId().equals("N")) {
                            // enforce minimum width and height of 1 px
                            heightChange = Math.max(heightChange, -selectionHeight + 1);
                            rectangle.setY(selectionY - heightChange);
                            rectangle.setHeight(selectionHeight + heightChange);
                        } else if(point.getId().equals("E")) {
                            // enforce minimum width and height of 1 px
                            widthChange = Math.min(widthChange, selectionWidth - 1);
                            rectangle.setWidth(selectionWidth - widthChange);
                        } else if(point.getId().equals("S")) {
                            // enforce minimum width and height of 1 px
                            heightChange = Math.min(heightChange, selectionHeight - 1);
                            rectangle.setHeight(selectionHeight - heightChange);
                        }
                    }
                }
            }
            resized = true;
        }
    };


}
