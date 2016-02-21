package com.vectorshapes.view.event.handlers;

import com.vectorshapes.model.util.HistoryUtil;
import com.vectorshapes.view.View;
import com.vectorshapes.view.event.handlers.entity.MouseClick;
import com.vectorshapes.view.event.handlers.entity.Selection;
import com.vectorshapes.view.menu.side.Tool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/**
 *
 * This class handles events for the select tool
 *
 * Created by 150019538 on 04/11/15.
 */
public class SelectEventHandler implements ToolEventHandler {

    private final View view;
    private final MouseClick mouseClick;
    private final MouseEventHandler mouseEventHandler;
    private Selection selection;
    private final ResizeEventHandler resizeEventHandler;
    private final RotateEventHandler rotateEventHandler;
    private boolean moved = false;

    private double selectedOriginalX, selectedOriginalY;

    public SelectEventHandler(View view, MouseEventHandler mouseEventHandler) {
        this.view = view;
        this.mouseEventHandler = mouseEventHandler;
        mouseClick = new MouseClick();
        resizeEventHandler = new ResizeEventHandler();
        rotateEventHandler = new RotateEventHandler();
        subscribeToFillColorPicker();
        subscribeToStrokeColorPicker();
        subscribeToStrokeWidth();
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
        if(source instanceof Shape) {
            view.setSelectedStrokeColor((Color) ((Shape) source).getStroke());
            view.setSelectedStrokeWidth((int) ((Shape) source).getStrokeWidth());
        }
        if(source instanceof Shape && !(source instanceof Line)) {
            view.setSelectedFillColor((Color) ((Shape) source).getFill());
        }
    }

    void clearSelection() {
        view.getSelectionGroup().getChildren().clear();
        selection = null;
    }

    @Override
    public void handleMouseMove(MouseEvent event) {
        if(event.getSource() instanceof Shape || event.getSource() instanceof ImageView) {
            view.changeCursor(Cursor.OPEN_HAND);
        } else {
            view.changeCursor(Cursor.DEFAULT);
        }
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
            rectangle.setY(selectedOriginalY + offsetY);
            moved = true;
        }
    }

    /**
     * Listens to color changes in the fill color-picker and changes color of shape if one is selected
     */
    private void subscribeToFillColorPicker() {
        ColorPicker colorPicker = view.getFillColorPicker();
        colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                if(selection != null && selection.getShape() instanceof Shape) {
                    Shape s = (Shape)selection.getShape();
                    s.setFill(newValue);
                }
            }
        });
    }

    /**
     * Listens to color changes in the stroke color-picker and changes color of shape if one is selected
     */
    private void subscribeToStrokeColorPicker() {
        ColorPicker colorPicker = view.getStrokeColorPicker();
        colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(selection != null && selection.getShape() instanceof Shape) {
                Shape s = (Shape)selection.getShape();
                s.setStroke(newValue);
            }
        });
    }

    /**
     * Listens to color changes in the color-picker and changes color of shape if one is selected
     */
    private void subscribeToStrokeWidth() {
        Spinner spinner = view.getStrokeWidthSpinner();
        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            Integer newV = (Integer)newValue;
            if(selection != null && selection.getShape() instanceof Shape) {
                Shape s = (Shape)selection.getShape();
                if(s instanceof Line) {
                    if((double)newV < 1) {
                        return;
                    }
                }
                s.setStrokeWidth((double) newV);
            }
        });
    }

    /**
     * Listens to tool changes and deselects any selection if tool is moved
     */
    void subscribeToTools() {
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
