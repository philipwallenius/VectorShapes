package com.vectorshapes.view.event.handlers;

import com.vectorshapes.model.util.HistoryUtil;
import com.vectorshapes.view.event.handlers.entity.MouseClick;
import com.vectorshapes.view.event.handlers.entity.Point;
import com.vectorshapes.view.event.handlers.entity.Selection;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;

/**
 *
 * This class handles rotate events for the select tool
 *
 * Created by 150019538 on 08/11/15.
 */
class RotateEventHandler {

    private boolean rotated = false;
    private final MouseClick click = new MouseClick();
    private double pX, pY, deltaX, deltaY, d2, previouslyRotated;

    public void register(Point point) {
        point.setOnMousePressed(mousePressEventHandler);
        point.setOnMouseDragged(mouseDragEventHandler);
        point.setOnMouseReleased(mouseReleaseEventHandler);
    }

    private final EventHandler<MouseEvent> mousePressEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            rotated = false;
            click.x = event.getX();
            click.y = event.getY();
            if(event.getSource() instanceof Point) {

                // keep track of original x and y
                pX = event.getSceneX();
                pY = event.getSceneY();
                deltaX = 0;
                deltaY = 0;
                previouslyRotated = 0;
            }

        }
    };

    private final EventHandler<MouseEvent> mouseReleaseEventHandler = event -> {
        if(rotated) {
            HistoryUtil.getInstance().addHistoryPoint();
        }
        rotated = false;
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
                        double currentX = event.getSceneX();
                        double currentY = event.getSceneY();

                        // calculate move movement
                        deltaX = currentX-pX;
                        deltaY = currentY-pY;

                        // calculate rotation degrees
                        double angles = angleFromDeltas(deltaX, deltaY);

                        // apply rotation transform diff since last rotation
                        Rotate rotationTransform = new Rotate(angles - previouslyRotated, rectangle.getX()+(rectangle.getWidth()/2), rectangle.getY()+(rectangle.getHeight()/2));
                        rectangle.getTransforms().add(rotationTransform);
                        rectangle.getShape().getTransforms().add(rotationTransform);
                        for(Point p : rectangle.getResizePoints()) {
                            p.getTransforms().add(rotationTransform);
                        }
                        rectangle.getRotatePoint().getTransforms().add(rotationTransform);
                        previouslyRotated += (angles- previouslyRotated);
                        rotated = true;
                    }
                }
            }
        }
    };

    /**
     * calculates angle between two points, deltaX and deltaY (mouse movement)
     * @param deltaX to use in calculation
     * @param deltaY to use in calculation
     * @return Returns angle difference between coordinates
     */
    private double angleFromDeltas(double deltaX, double deltaY) {

        double radians = Math.atan2(deltaY, deltaX);
        double d = Math.toDegrees(radians);
        if(d < 0){
            d += 360;
        }
        return d;
    }


}
