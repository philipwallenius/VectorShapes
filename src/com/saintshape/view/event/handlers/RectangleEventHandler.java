package com.saintshape.view.event.handlers;

import com.saintshape.controller.Controller;
import com.saintshape.model.util.HistoryUtil;
import com.saintshape.view.View;
import com.saintshape.view.event.handlers.entity.MouseClick;
import com.saintshape.view.menu.side.Tool;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 *
 * This class handles events for the rectangle tool
 *
 * Created by 150019538 on 04/11/15.
 */
public class RectangleEventHandler implements ToolEventHandler {

    private final View view;
    private Rectangle selected;
    private final Controller controller;
    private final MouseClick mouseClick;
    private final MouseEventHandler mouseEventHandler;

    public RectangleEventHandler(View view, Controller controller, MouseEventHandler mouseEventHandler) {
        this.view = view;
        this.controller = controller;
        this.mouseEventHandler = mouseEventHandler;
        mouseClick = new MouseClick();
    }

    @Override
    public void handleMousePress(MouseEvent event) {

        // keep track of mouse movements
        mouseClick.x = event.getX();
        mouseClick.y = event.getY();


        if(selected == null) {
            if(view.getSelectedTool() == Tool.RECTANGLE) {
                // create a new rectangle, set its color and register it to event handler
                selected = new com.saintshape.model.shape.Rectangle(event.getX(), event.getY(), 0, 0);
                selected.setFill(view.getSelectedColor());
                mouseEventHandler.register(selected);
                controller.addNode(selected);
            }
        }

    }

    @Override
    public void handleMouseRelease(MouseEvent event) {
        if(selected.getWidth() < 1 || selected.getHeight() < 1) {
            controller.removeNode(selected);
        } else {
            HistoryUtil.getInstance().addHistoryPoint();
            view.selectNode(selected);
        }
        selected = null;
    }

    @Override
    public void handleMouseMove(MouseEvent event) {
        view.changeCursor(Cursor.CROSSHAIR);
    }

    @Override
    public void handleMouseDrag(MouseEvent event) {

        // get current mouse coordinates
        double currentX = event.getX();
        double currentY = event.getY();

        // calculate width and height based on mouse movement
        double width = Math.abs(currentX - mouseClick.x);
        double height = Math.abs(currentY - mouseClick.y);

        if(selected != null && event.isPrimaryButtonDown()) {

            // fix ratio if shift is pressed
            if(event.isShiftDown()) {

                // set both to the same value, whichever is smallest, width or height
                width = Math.min(width, height);
                height = Math.min(width, height);

                if ((currentX - mouseClick.x) >= 0) {
                    selected.setX(mouseClick.x);
                    selected.setWidth(width);
                } else {
                    selected.setX(mouseClick.x - width);
                    selected.setWidth(width);
                }

                if ((currentY - mouseClick.y) >= 0) {
                    selected.setY(mouseClick.y);
                    selected.setHeight(height);
                } else {
                    selected.setY(mouseClick.y - height);
                    selected.setHeight(height);
                }

            } else {
                if ((currentX - mouseClick.x) >= 0) {
                    selected.setX(mouseClick.x);
                    selected.setWidth(width);
                } else {
                    selected.setX(currentX);
                    selected.setWidth(width);
                }
                if ((currentY - mouseClick.y) >= 0) {
                    selected.setY(mouseClick.y);
                    selected.setHeight(height);
                } else {
                    selected.setY(currentY);
                    selected.setHeight(height);
                }
            }

        }

    }

}
