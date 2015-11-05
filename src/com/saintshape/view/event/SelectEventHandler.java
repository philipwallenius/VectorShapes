package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.menu.side.Tool;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * Created by 150019538 on 04/11/15.
 */
public class SelectEventHandler implements ToolEventHandler {

    private View view;
    private Node selected;
    private Controller controller;
    private MouseClick mouseClick;
    private MouseEventHandler mouseEventHandler;

    private double selectedOriginalX, selectedOriginalY;

    public SelectEventHandler(View view, Controller controller, MouseEventHandler mouseEventHandler) {
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

        // create a new shape if not already done
//        if(selected == null) {
        if(view.getSelectedTool() == Tool.SELECT) {
            selected = (Node)event.getSource();
        }
//        }

        if(selected instanceof Rectangle) {
            Rectangle rectangle = ((Rectangle)selected);
            selectedOriginalX = rectangle.getX();
            selectedOriginalY = rectangle.getY();
        }

    }

    @Override
    public void handleMouseMove(MouseEvent event) {

    }

    @Override
    public void handleMouseDrag(MouseEvent event) {
        if(selected != null && event.isPrimaryButtonDown()) {

            double currentX = event.getX();
            double currentY = event.getY();

            double offsetX = currentX - mouseClick.x;
            double offsetY = currentY - mouseClick.y;

            if(selected instanceof Rectangle) {
                Rectangle rectangle = ((Rectangle)selected);
                rectangle.setX(selectedOriginalX+offsetX);
                rectangle.setY(selectedOriginalY+offsetY);
            }

        }
    }

    @Override
    public void handleMouseRelease(MouseEvent event) {
//        selected = null;
    }
}
