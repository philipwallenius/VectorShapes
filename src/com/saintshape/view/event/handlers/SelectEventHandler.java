package com.saintshape.view.event.handlers;

import com.saintshape.controller.Controller;
import com.saintshape.model.util.HistoryUtil;
import com.saintshape.view.View;
import com.saintshape.view.event.handlers.entity.MouseClick;
import com.saintshape.view.event.handlers.entity.Selection;
import com.saintshape.view.menu.side.Tool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 *
 * This class handles events for the select tool
 *
 * Created by 150019538 on 04/11/15.
 */
public class SelectEventHandler implements ToolEventHandler {

    private View view;
    private Controller controller;
    private MouseClick mouseClick;
    private MouseEventHandler mouseEventHandler;
    public Selection selection;
    private ResizeEventHandler resizeEventHandler;
    private RotateEventHandler rotateEventHandler;
    private boolean moved = false;

    private double selectedOriginalX, selectedOriginalY, clickDiffX, clickDiffY;

    public SelectEventHandler(View view, Controller controller, MouseEventHandler mouseEventHandler) {
        this.view = view;
        this.controller = controller;
        this.mouseEventHandler = mouseEventHandler;
        mouseClick = new MouseClick();
        resizeEventHandler = new ResizeEventHandler(view, controller);
        rotateEventHandler = new RotateEventHandler(view, controller);
        subscribeToColorPicker();
        subscribeToTools();
    }

    @Override
    public void handleMousePress(MouseEvent event) {
        Node source = (Node)event.getSource();
        moved = false;

        // keep track of mouse movements
        mouseClick.x = event.getX();
        mouseClick.y = event.getY();

        // change cursor
        if(source instanceof Shape || source instanceof ImageView) {
            source.setCursor(Cursor.CLOSED_HAND);
        }

        // get the offset between click xy and shape xy
        if(source instanceof Rectangle) {
            Rectangle rectangle = (Rectangle)source;
            clickDiffX = mouseClick.x-(rectangle.getX());
            clickDiffY = mouseClick.y-(rectangle.getY());
        } else if(source instanceof Ellipse) {
            Ellipse ellipse = (Ellipse)source;
            clickDiffX = mouseClick.x-(ellipse.getCenterX()-ellipse.getRadiusX());
            clickDiffY = mouseClick.y-(ellipse.getCenterY()-ellipse.getRadiusY());
        } else if(source instanceof Line) {
            Line line = (Line)source;
            clickDiffX = mouseClick.x-(Math.min(line.getStartX(), line.getEndX()));
            clickDiffY = mouseClick.y-(Math.min(line.getStartY(), line.getEndY()));
        } else if(source instanceof ImageView) {
            ImageView image = (ImageView)source;
            clickDiffX = mouseClick.x-(image.getX());
            clickDiffY = mouseClick.y-(image.getY());
        }

        // if shape clicked, select it
        if(!(source instanceof Selection || source instanceof Canvas)) {
            createSelection(source);
        }

        // save shape's original coordinates for drag handling
        if(selection != null) {
            selection.setCursor(Cursor.OPEN_HAND);
            selectedOriginalX = selection.getX();
            selectedOriginalY = selection.getY();
        }

        // deselect if user clicked on canvas
        if(source instanceof Canvas) {
            clearSelection();
        }

    }

    public void createSelection(Node source) {
        clearSelection();
        selection = new Selection(source);
        resizeEventHandler.register(selection.getResizePoints());
        rotateEventHandler.register(selection.getRotatePoint());
        view.selectNodeInList(source);
        selection.setCursor(Cursor.OPEN_HAND);
        mouseEventHandler.registerSelection(selection);
        view.getSelectionGroup().getChildren().clear();
        view.getSelectionGroup().getChildren().add(selection);
        view.getSelectionGroup().getChildren().addAll(selection.getResizePoints());
        view.getSelectionGroup().getChildren().add(selection.getRotatePoint());
        if(source instanceof Line) {
            view.setSelectedColor((Color)((Line)source).getStroke());
        } else if(source instanceof Shape) {
            view.setSelectedColor((Color)((Shape)source).getFill());
        }
    }

    public void clearSelection() {
        view.getSelectionGroup().getChildren().clear();
        selection = null;
    }

    @Override
    public void handleMouseMove(MouseEvent event) {
        view.changeCursor(Cursor.OPEN_HAND);
    }

    @Override
    public void handleMouseRelease(MouseEvent event) {
        if(selection != null) {
            if(moved) {
                HistoryUtil.getInstance().addHistoryPoint();
            }
            selection.setCursor(Cursor.OPEN_HAND);
            moved = false;
        }
    }

    @Override
    public void handleMouseDrag(MouseEvent event) {
        if(selection != null && event.isPrimaryButtonDown()) {
            selection.setCursor(Cursor.CLOSED_HAND);
            // get current mouse position
            double currentX = event.getX();
            double currentY = event.getY();

            Rectangle rectangle = selection;

            // Calculate rectangle movement
            double offsetX = currentX - mouseClick.x;
            double offsetY = currentY - mouseClick.y;

            // Update movement
            rectangle.setX(selectedOriginalX+offsetX);
            rectangle.setY(selectedOriginalY+offsetY);
            moved = true;
        }
    }

    /**
     * Listens to color changes in the colorpicker and changes color of shape if one is selected
     */
    private void subscribeToColorPicker() {
        ColorPicker colorPicker = view.getColorPicker();
        colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                if(selection != null && selection.getShape() instanceof Shape) {
                    Shape s = (Shape)selection.getShape();
                    if(s instanceof Line) {
                        s.setStroke(newValue);
                    } else {
                        s.setFill(newValue);
                    }
                }
            }
        });
    }

    /**
     * Listens to tool changes and deselects any selection if tool is moved
     */
    public void subscribeToTools() {
        ToggleGroup toggleGroup = view.getTools();
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue.getUserData() != Tool.SELECT) {
                    deselect();
                }
            }
        });
    }

    public void deselect() {
        selection = null;
        view.getSelectionGroup().getChildren().clear();
    }

    public Selection getSelection() {
        return selection;
    }
}
