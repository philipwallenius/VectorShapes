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
    private Rectangle outline;

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
        if(view.getSelectedTool() == Tool.SELECT) {
            selected = (Node)event.getSource();

        }

        if(selected instanceof Rectangle) {
            Rectangle rectangle = ((Rectangle)selected);
            selectedOriginalX = rectangle.getX();
            selectedOriginalY = rectangle.getY();

            outline = new Rectangle(rectangle.getX()-5, rectangle.getY()-5, rectangle.getWidth() + 10, rectangle.getHeight() + 10);
            outline.setFill(Color.TRANSPARENT);
            outline.setStroke(Color.GRAY);
            Anchor anchor = new Anchor("1", new SimpleDoubleProperty(outline.getX()), new SimpleDoubleProperty(outline.getY()));
            Anchor anchor2 = new Anchor("2", new SimpleDoubleProperty(outline.getX()+outline.getWidth()), new SimpleDoubleProperty(outline.getY()));
            anchor.setCursor(Cursor.NW_RESIZE);
            outline.setStrokeWidth(2);
            outline.xProperty().bind(rectangle.xProperty());
            outline.yProperty().bind(rectangle.yProperty());
            controller.addNode(outline);
            controller.addNode(anchor);
            controller.addNode(anchor2);
            anchor.centerXProperty().bind(outline.xProperty());
            anchor.centerYProperty().bind(outline.yProperty());
            anchor2.centerXProperty().bind(outline.xProperty());
            anchor2.centerYProperty().bind(outline.yProperty());
            view.getSelectionGroup().getChildren().clear();
            view.getSelectionGroup().getChildren().addAll(outline, anchor);
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

    // an anchor displayed around a point.
    class Anchor extends Circle {
        Anchor(String id, DoubleProperty x, DoubleProperty y) {
            super(x.get(), y.get(), 5);
            setId(id);
            setFill(Color.GOLD.deriveColor(1, 1, 1, 0.5));
            setStroke(Color.GOLD);
            setStrokeWidth(1);
            setStrokeType(StrokeType.OUTSIDE);

            x.bind(centerXProperty());
            y.bind(centerYProperty());
        }
    }
}
