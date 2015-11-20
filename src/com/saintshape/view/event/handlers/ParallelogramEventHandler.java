package com.saintshape.view.event.handlers;

import com.saintshape.controller.Controller;
import com.saintshape.model.util.HistoryUtil;
import com.saintshape.view.View;
import com.saintshape.view.event.handlers.entity.MouseClick;
import com.saintshape.view.menu.side.Tool;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Shear;

/**
 *
 * This class handles events for the parallelogram tool
 *
 * Created by 150019538 on 17/11/15.
 */
public class ParallelogramEventHandler implements ToolEventHandler {

    enum State {
        NOT_CREATED, CREATED
    }

    private final View view;
    private Rectangle parallelogram;
    private final Controller controller;
    private final MouseClick mouseClick;
    private final MouseEventHandler mouseEventHandler;
    private State state;
    private double shearX, sceneX;

    public ParallelogramEventHandler(View view, Controller controller, MouseEventHandler mouseEventHandler) {
        this.view = view;
        this.controller = controller;
        this.mouseEventHandler = mouseEventHandler;
        mouseClick = new MouseClick();
        state = State.NOT_CREATED;
    }

    @Override
    public void handleMousePress(MouseEvent event) {

        // keep track of mouse movements
        mouseClick.x = event.getX();
        mouseClick.y = event.getY();

        sceneX = event.getSceneX();

        // if parallelogram isn't created yet, create it
        if(parallelogram == null) {
            if(state == State.NOT_CREATED) {
                if(view.getSelectedTool() == Tool.PARALLELOGRAM) {
                    // create a new rectangle, set its color and register it to event handler
                    parallelogram = new com.saintshape.model.shape.Parallelogram(event.getX(), event.getY(), 0, 0);
                    parallelogram.setFill(view.getSelectedFillColor());
                    parallelogram.setStroke(view.getSelectedStrokeColor());
                    parallelogram.setStrokeWidth(view.getSelectedStrokeWidth());
                    mouseEventHandler.register(parallelogram);
                    controller.addNode(parallelogram);
                    shearX = 0;
                }
            }
        }

    }

    @Override
    public void handleMouseRelease(MouseEvent event) {

        if(parallelogram.getWidth() < 1 || parallelogram.getHeight() < 1) {
            controller.removeNode(parallelogram);
            parallelogram = null;
            state = State.NOT_CREATED;
            return;
        }

        if(state == State.NOT_CREATED) {
            // parallelogram is now created, change state and cursor
            state = State.CREATED;
            parallelogram.setCursor(Cursor.W_RESIZE);
        } else if(state == State.CREATED) {
            // parallelogram has now been created and sheared. creation is completed
            HistoryUtil.getInstance().addHistoryPoint();
            view.selectNode(parallelogram);
            parallelogram = null;
            state = State.NOT_CREATED;
        }

    }

    @Override
    public void handleMouseMove(MouseEvent event) {
        if(state == State.NOT_CREATED) {
            view.changeCursor(Cursor.CROSSHAIR);
        } else if(state == State.CREATED) {
            view.changeCursor(Cursor.W_RESIZE);
        }
    }

    @Override
    public void handleMouseDrag(MouseEvent event) {

        // get current mouse coordinates
        double currentX = event.getX();
        double currentY = event.getY();

        // if parallelogram isn't created, resize it!
        if(state == State.NOT_CREATED) {

            // calculate width and height based on mouse movement
            double width = Math.abs(currentX - mouseClick.x);
            double height = Math.abs(currentY - mouseClick.y);

            if (parallelogram != null && event.isPrimaryButtonDown()) {

                // fix ratio if shift is pressed
                if (event.isShiftDown()) {

                    // set both to the same value, whichever is smallest, width or height
                    width = Math.min(width, height);
                    height = Math.min(width, height);

                    if ((currentX - mouseClick.x) >= 0) {
                        parallelogram.setX(mouseClick.x);
                        parallelogram.setWidth(width);
                    } else {
                        parallelogram.setX(mouseClick.x - width);
                        parallelogram.setWidth(width);
                    }

                    if ((currentY - mouseClick.y) >= 0) {
                        parallelogram.setY(mouseClick.y);
                        parallelogram.setHeight(height);
                    } else {
                        parallelogram.setY(mouseClick.y - height);
                        parallelogram.setHeight(height);
                    }

                } else {
                    if ((currentX - mouseClick.x) >= 0) {
                        parallelogram.setX(mouseClick.x);
                        parallelogram.setWidth(width);
                    } else {
                        parallelogram.setX(currentX);
                        parallelogram.setWidth(width);
                    }
                    if ((currentY - mouseClick.y) >= 0) {
                        parallelogram.setY(mouseClick.y);
                        parallelogram.setHeight(height);
                    } else {
                        parallelogram.setY(currentY);
                        parallelogram.setHeight(height);
                    }
                }

            }
        }
        // parallelogram is created, adjust shear instead
        else {

            double currentSceneX = event.getSceneX();
            double moveX = currentSceneX-sceneX;
            double width = parallelogram.getWidth()*4;

            // restrict how much parallelogram can be sheared
            if(moveX < 0) {
                moveX = Math.max(moveX, -(width));
            } else {
                moveX = Math.min(moveX, width);
            }

            moveX = moveX/1000;

            double diff = moveX-shearX;

            Shear shear = new Shear(diff, 0);
            parallelogram.getTransforms().add(shear);
            shearX += diff;
        }

    }

}
