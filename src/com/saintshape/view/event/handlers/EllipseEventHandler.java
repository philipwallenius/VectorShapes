package com.saintshape.view.event.handlers;

import com.saintshape.controller.Controller;
import com.saintshape.model.util.HistoryUtil;
import com.saintshape.view.View;
import com.saintshape.view.event.handlers.entity.MouseClick;
import com.saintshape.view.menu.side.Tool;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Ellipse;

/**
 *
 * This class handles events for the ellipse tool
 *
 * Created by 150019538 on 04/11/15.
 */
public class EllipseEventHandler implements ToolEventHandler {

    private final View view;
    private Ellipse ellipse;
    private final Controller controller;
    private final MouseClick mouseClick;
    private final MouseEventHandler mouseEventHandler;

    public EllipseEventHandler(View view, Controller controller, MouseEventHandler mouseEventHandler) {
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
        if (ellipse == null) {
            if (view.getSelectedTool() == Tool.ELLIPSE) {
                ellipse = new com.saintshape.model.shape.Ellipse(event.getX(), event.getY(), 0, 0);
                ellipse.setFill(view.getSelectedColor());
                mouseEventHandler.register(ellipse);
                controller.addNode(ellipse);
            }
        }
    }

    @Override
    public void handleMouseRelease(MouseEvent event) {
        if (ellipse.getRadiusX() < 1 || ellipse.getRadiusY() < 1) {
            controller.removeNode(ellipse);
        } else {
            HistoryUtil.getInstance().addHistoryPoint();
            view.selectNode(ellipse);
        }
        ellipse = null;
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

        if(ellipse != null && event.isPrimaryButtonDown()) {

            // fix ratio if shift is held
            if(event.isShiftDown()) {

                // set both to the same value, whichever is smallest, width or height
                width = Math.min(width, height);
                height = Math.min(width, height);

                if ((currentX - mouseClick.x) >= 0) {
                    ellipse.setCenterX(mouseClick.x + (width / 2));
                    ellipse.setRadiusX(Math.abs(width) / 2);
                } else {
                    ellipse.setCenterX(mouseClick.x - (width / 2));
                    ellipse.setRadiusX(Math.abs(width) / 2);
                }

                if ((currentY - mouseClick.y) >= 0) {
                    ellipse.setCenterY(mouseClick.y + (height / 2));
                    ellipse.setRadiusY(Math.abs(height) / 2);
                } else {
                    ellipse.setCenterY(mouseClick.y - (height / 2));
                    ellipse.setRadiusY(Math.abs(height) / 2);
                }


            } else {

                if ((currentX - mouseClick.x) >= 0) {
                    ellipse.setCenterX(mouseClick.x + (width / 2));
                    ellipse.setRadiusX(width / 2);
                } else {
                    ellipse.setCenterX(mouseClick.x - (width / 2));
                    ellipse.setRadiusX(width / 2);
                }

                if ((currentY - mouseClick.y) >= 0) {
                    ellipse.setCenterY(mouseClick.y + (height / 2));
                    ellipse.setRadiusY(height / 2);
                } else {
                    ellipse.setCenterY(mouseClick.y - (height / 2));
                    ellipse.setRadiusY(height / 2);
                }
            }
        }

    }

}
