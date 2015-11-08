package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.menu.side.Tool;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

/**
 *
 * This class handles events for the line tool
 *
 * Created by 150019538 on 05/11/15.
 */
public class LineEventHandler implements ToolEventHandler {

    private View view;
    private Line line;
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
        if (line == null) {
            if (view.getSelectedTool() == Tool.LINE) {
                line = new Line(event.getX(), event.getY(), event.getX(), event.getY());
                line.setStroke(view.getSelectedColor());
                line.setStrokeWidth(2);
                mouseEventHandler.register(line);
                controller.addNode(line);
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

        // make sure mouse stays within bounds
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


        if(line != null && event.isPrimaryButtonDown()) {

            if(event.isShiftDown()) {

                // if y is larger than x, make a straight vertical line
                if(Math.abs(currentY-mouseClick.y) > Math.abs(currentX-mouseClick.x)) {
                    currentX = mouseClick.x;
                }
                // if x is larger than y, make a straight horizontal line
                if(Math.abs(currentX-mouseClick.x) > Math.abs(currentY-mouseClick.y)) {
                    currentY = mouseClick.y;
                }
            }

            line.setEndX(currentX);
            line.setEndY(currentY);
        }
    }

    @Override
    public void handleMouseRelease(MouseEvent event) {
        // deselect shape
        view.selectNode(line);
        line = null;
    }
}
