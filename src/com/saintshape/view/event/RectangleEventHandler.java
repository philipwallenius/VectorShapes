package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.menu.side.Tool;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * Created by 150019538 on 04/11/15.
 */
public class RectangleEventHandler implements ToolEventHandler {

    private View view;
    private Rectangle selected;
    private Controller controller;
    private MouseClick mouseClick;
    private MouseEventHandler mouseEventHandler;

    public RectangleEventHandler(View view, Controller controller, MouseEventHandler mouseEventHandler) {
        this.view = view;
        this.controller = controller;
        this.mouseEventHandler = mouseEventHandler;
        mouseClick = new MouseClick();
    }

    public void handleMousePress(MouseEvent event) {

        // keep track of mouse movements
        mouseClick.x = event.getX();
        mouseClick.y = event.getY();

        // create a new shape if not already done
        if(selected == null) {
            if(view.getSelectedTool() == Tool.RECTANGLE) {
                selected = new Rectangle(event.getX(), event.getY(), 0, 0);
                selected.setFill(view.getSelectedColor());
                mouseEventHandler.register(selected);
                controller.addNode(selected);
            }
        }

    };

    public void handleMouseRelease(MouseEvent event) {
        selected = null;
    };


    public void handleMouseMove(MouseEvent event) {

    }

    public void handleMouseDrag(MouseEvent event) {

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

        if(selected != null && event.isPrimaryButtonDown()) {

            if(event.isShiftDown()) {

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
