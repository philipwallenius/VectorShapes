package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.menu.side.Tool;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

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

    private double selectedOriginalX, selectedOriginalY;

    public SelectEventHandler(View view, Controller controller, MouseEventHandler mouseEventHandler) {
        this.view = view;
        this.controller = controller;
        this.mouseEventHandler = mouseEventHandler;
        mouseClick = new MouseClick();
        subscribeToColorPicker();
    }

    @Override
    public void handleMousePress(MouseEvent event) {

        // keep track of mouse movements
        mouseClick.x = event.getX();
        mouseClick.y = event.getY();

        Object source = event.getSource();

        if(!(source instanceof Selection)) {

            if(source instanceof Canvas) {
                selection = null;
                selected = null;
                view.getSelectionGroup().getChildren().clear();
            } else if (source instanceof Rectangle) {
                Rectangle rectangle = ((Rectangle) source);
                selectedOriginalX = rectangle.getX();
                selectedOriginalY = rectangle.getY();
                selection = new Selection(view, rectangle);
                mouseEventHandler.register(selection);
                selected = selection;
                view.getSelectionGroup().getChildren().clear();
                view.getSelectionGroup().getChildren().add(selection);
                view.getSelectionGroup().getChildren().addAll(selection.getAnchors());

            }
        } else {
            Rectangle rectangle = ((Rectangle) source);
            selectedOriginalX = rectangle.getX();
            selectedOriginalY = rectangle.getY();
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

    private void subscribeToColorPicker() {
        ColorPicker colorPicker = view.getColorPicker();
        colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                if(selected instanceof Shape) {
                    Shape s = (Shape)selection.getShape();
                    s.setFill(newValue);
                }
            }
        });
    }

//    class Anchor extends Circle {
//        Anchor(String id, DoubleProperty x, DoubleProperty y) {
//            super(x.get(), y.get(), 5);
//            setId(id);
//            setFill(Color.GOLD.deriveColor(1, 1, 1, 0.5));
//            setStroke(Color.GOLD);
//            setStrokeWidth(1);
//            setStrokeType(StrokeType.OUTSIDE);
//
//            x.bind(centerXProperty());
//            y.bind(centerYProperty());
//        }
//    }
}
