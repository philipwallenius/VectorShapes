package com.vectorshapes.view.event.handlers;

import com.vectorshapes.controller.Controller;
import com.vectorshapes.model.util.HistoryUtil;
import com.vectorshapes.view.View;
import com.vectorshapes.view.event.handlers.entity.MouseClick;
import com.vectorshapes.view.menu.side.Tool;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;

/**
 *
 * This class handles events for the line tool
 *
 * Created by 150019538 on 05/11/15.
 */
public class LineEventHandler implements ToolEventHandler {

    private final View view;
    private Line line;
    private final Controller controller;
    private final MouseClick mouseClick;
    private final MouseEventHandler mouseEventHandler;

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
                line = new com.vectorshapes.model.shape.Line(event.getX(), event.getY(), event.getX(), event.getY());
                line.setStroke(view.getSelectedStrokeColor());
                line.setStrokeWidth(view.getSelectedStrokeWidth());
                mouseEventHandler.register(line);
                controller.addNode(line);
            }
        }
    }

    @Override
    public void handleMouseMove(MouseEvent event) {
        view.changeCursor(Cursor.CROSSHAIR);
    }

    @Override
    public void handleMouseDrag(MouseEvent event) {

        double currentX = event.getX();
        double currentY = event.getY();

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
        if(Math.abs(line.getStartX()-line.getEndX()) < 1 && Math.abs(line.getStartY()-line.getEndY()) < 1) {
            controller.removeNode(line);
        } else {
            HistoryUtil.getInstance().addHistoryPoint();
            view.selectNode(line);
        }
        line = null;
    }
}
