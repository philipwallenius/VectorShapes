package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.menu.side.Tool;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

/**
 * Created by 150019538 on 04/11/15.
 */
public class SelectEventHandler implements ToolEventHandler {

    private View view;
    private Node selected;
    private Controller controller;
    private MouseClick mouseClick;
    private MouseEventHandler mouseEventHandler;
    public Selection selection;
    private Color previousColor;

    private double selectedOriginalX, selectedOriginalY, clickDiffX, clickDiffY;

    public SelectEventHandler(View view, Controller controller, MouseEventHandler mouseEventHandler) {
        this.view = view;
        this.controller = controller;
        this.mouseEventHandler = mouseEventHandler;
        mouseClick = new MouseClick();
        subscribeToColorPicker();
        subscribeToTools();
    }

    @Override
    public void handleMousePress(MouseEvent event) {
        // keep track of mouse movements
        mouseClick.x = event.getX();
        mouseClick.y = event.getY();

        Node source = (Node)event.getSource();

        int selectionBoundary = 5;
        if(source instanceof Rectangle) {
            Rectangle rectangle = (Rectangle)source;
            clickDiffX = mouseClick.x-(rectangle.getX()+selectionBoundary);
            clickDiffY = mouseClick.y-(rectangle.getY()+selectionBoundary);
        } else if(source instanceof Ellipse) {
            Ellipse ellipse = (Ellipse)source;
            clickDiffX = mouseClick.x-(ellipse.getCenterX()-ellipse.getRadiusX());
            clickDiffY = mouseClick.y-(ellipse.getCenterY()-ellipse.getRadiusY());
        } else if(source instanceof Line) {
            Line line = (Line)source;
            clickDiffX = mouseClick.x-(Math.min(line.getStartX(), line.getEndX()));
            clickDiffY = mouseClick.y-(Math.min(line.getStartY(), line.getEndY()));
        }



        if(!(source instanceof Selection)) {
            clearSelection();
            if (source instanceof Rectangle) {
                Rectangle rectangle = (Rectangle) source;
                selectedOriginalX = rectangle.getX();
                selectedOriginalY = rectangle.getY();
                selection = new Selection(rectangle);
                mouseEventHandler.register(selection);
                selected = selection;
                view.getSelectionGroup().getChildren().clear();
                view.getSelectionGroup().getChildren().add(selection);
                view.getSelectionGroup().getChildren().addAll(selection.getPoints());
                previousColor = view.getSelectedColor();
                view.setSelectedColor((Color)rectangle.getFill());
            } else if(source instanceof Ellipse) {
                Ellipse ellipse = (Ellipse)source;
                selection = new Selection(ellipse);
                selectedOriginalX = selection.getX();
                selectedOriginalY = selection.getY();
                mouseEventHandler.register(selection);
                selected = selection;
                view.getSelectionGroup().getChildren().clear();
                view.getSelectionGroup().getChildren().add(selection);
                view.getSelectionGroup().getChildren().addAll(selection.getPoints());
                previousColor = view.getSelectedColor();
                view.setSelectedColor((Color)ellipse.getFill());
            } else if(source instanceof Line) {
                Line line = (Line)source;
                selection = new Selection(line);
                selectedOriginalX = selection.getX();
                selectedOriginalY = selection.getY();
                mouseEventHandler.register(selection);
                selected = selection;
                view.getSelectionGroup().getChildren().clear();
                view.getSelectionGroup().getChildren().add(selection);
                view.getSelectionGroup().getChildren().addAll(selection.getPoints());
                previousColor = view.getSelectedColor();
                view.setSelectedColor((Color)line.getStroke());
            }
        } else {
            Rectangle rectangle = ((Rectangle) source);
            selectedOriginalX = rectangle.getX();
            selectedOriginalY = rectangle.getY();
        }
        
    }

    public void clearSelection() {
        view.getSelectionGroup().getChildren().clear();
        selection = null;
        selected = null;
        if(previousColor != null) {
            view.setSelectedColor(previousColor);
        }
    }

    @Override
    public void handleMouseMove(MouseEvent event) {

    }

    @Override
    public void handleMouseRelease(MouseEvent event) {

    }

    @Override
    public void handleMouseDrag(MouseEvent event) {
        if(selected != null && event.isPrimaryButtonDown()) {

            // get current mouse position
            double currentX = event.getX();
            double currentY = event.getY();

            Rectangle rectangle = (Rectangle)selected;

            // Make sure x-position not outside of canvas
            if(currentX-clickDiffX < 0) {
                currentX = currentX + (Math.abs(0-(currentX-clickDiffX)));
            } else if(((rectangle.getWidth()-(Selection.BORDER_MARGIN*2))-clickDiffX)+currentX > controller.getRootCanvas().getWidth()) {
                currentX = currentX - (Math.abs(controller.getRootCanvas().getWidth()-(currentX+((rectangle.getWidth()-(Selection.BORDER_MARGIN*2))-clickDiffX))));
            }

            // Make sure y-position not outside of canvas
            if(currentY-clickDiffY < 0) {
                currentY = currentY + (Math.abs(0-(currentY-clickDiffY)));
            } else if(((rectangle.getHeight()-(Selection.BORDER_MARGIN*2))-clickDiffY)+currentY > controller.getRootCanvas().getHeight()) {
                currentY = currentY - (Math.abs(controller.getRootCanvas().getHeight()-(currentY+((rectangle.getHeight()-(Selection.BORDER_MARGIN*2))-clickDiffY))));
            }

            // Calculate rectangle movement
            double offsetX = currentX - mouseClick.x;
            double offsetY = currentY - mouseClick.y;

            // Update movement
            rectangle.setX(selectedOriginalX+offsetX);
            rectangle.setY(selectedOriginalY+offsetY);

        }
    }

    private void subscribeToColorPicker() {
        ColorPicker colorPicker = view.getColorPicker();
        colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                if(selected != null) {
                    if(selected instanceof Shape) {
                        Shape s = (Shape)selection.getShape();
                        if(s instanceof Line) {
                            s.setStroke(newValue);
                        } else {
                            s.setFill(newValue);
                        }
                    }
                } else {
                    previousColor = newValue;
                }
            }
        });
    }

    public void subscribeToTools() {
        ToggleGroup toggleGroup = view.getTools();
        toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
                if(newValue.getUserData() != Tool.SELECT) {
                    selected = null;
                    selection = null;
                    view.getSelectionGroup().getChildren().clear();
                    if(previousColor != null) {
                        view.setSelectedColor(previousColor);
                    }
                }
            }
        });
    }
}
