package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.menu.side.Tool;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

/**
 * Created by 150019538 on 05/11/15.
 */
public class LineEventHandler implements ToolEventHandler {

    private View view;
    private Line selected;
    private Controller controller;
    private MouseClick mouseClick;
    private MouseEventHandler mouseEventHandler;

    public LineEventHandler(View view, Controller controller, MouseEventHandler mouseEventHandler) {
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
        if (selected == null) {
            if (view.getSelectedTool() == Tool.LINE) {
                selected = new Line(event.getX(), event.getY(), event.getX(), event.getY());
                selected.setFill(view.getSelectedColor());
                mouseEventHandler.register(selected);
                controller.addNode(selected);
            }
        }
    }

    @Override
    public void handleMouseMove(MouseEvent event) {

    }

    @Override
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


        if(selected != null && event.isPrimaryButtonDown()) {

            if(event.isShiftDown()) {
                if(Math.abs(currentY-mouseClick.y) > Math.abs(currentX-mouseClick.x)) {
                    currentX = mouseClick.x;
                }
                if(Math.abs(currentX-mouseClick.x) > Math.abs(currentY-mouseClick.y)) {
                    currentY = mouseClick.y;
                }
            }

            selected.setEndX(currentX);
            selected.setEndY(currentY);
        }
    }

    @Override
    public void handleMouseRelease(MouseEvent event) {
        selected = null;
    }
}
