package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.util.List;

/**
 * Created by 150019538 on 08/11/15.
 */
public class RotateEventHandler {

    private View view;
    private Controller controller;

    private MouseClick click = new MouseClick();
    private double selX, selY, pX, pY, deltaX, deltaY, d2, predeg;

    public RotateEventHandler(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
    }

    public void register(Point point) {
        point.setOnMousePressed(mousePressEventHandler);
        point.setOnMouseDragged(mouseDragEventHandler);
        point.setOnMouseReleased(mouseReleaseEventHandler);
    }

    public EventHandler<MouseEvent> mousePressEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            click.x = event.getX();
            click.y = event.getY();
            if(event.getSource() instanceof Point) {
                Point point = (Point) event.getSource();
                selX = point.getSelection().getX();
                selY = point.getSelection().getY();

                Rectangle rectangle = point.getSelection();

                pX = rectangle.getX()+(rectangle.getWidth()/2);
                pY = rectangle.getY()+(rectangle.getHeight()/2);
                deltaX = 0;
                deltaY = 0;
                predeg = 0;
            }

        }
    };

    public EventHandler<MouseEvent> mouseReleaseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if(event.getSource() instanceof Point) {
                Point point = (Point) event.getSource();
                point.getTransforms().add(new Rotate(d2, pX, pY));
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
                        deltaX = currentX-pX;
                        deltaY = currentY-pY;

                        double radians = Math.atan2(deltaY, deltaX);
                        double d = Math.toDegrees(((-90 * Math.PI) / 180) - radians);
                        if(d < 0){
                            d += 360;
                        }
                        d2 = 360-d;
                        rectangle.setRotate(d2);
                        predeg = d2;
//                        System.out.println("d2: " + d2);
                    }
                }
            }
        }
    };


}
