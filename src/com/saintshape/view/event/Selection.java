package com.saintshape.view.event;

import com.saintshape.view.View;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 150019538 on 05/11/15.
 */
public class Selection extends Rectangle {

    private List<Anchor> anchors;
    private Node shape;
    private View view;

    public Selection(View view, Node shape) {
        this.view = view;
        this.shape = shape;
        initialize();
    }

    public void initialize() {

//        subscribeToColorPicker();

        anchors = new ArrayList<>();
        if(shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle)shape;
            view.setSelectedColor((Color)rectangle.getFill());
            setFill(Color.TRANSPARENT);
            setStroke(Color.DEEPSKYBLUE);
            setStrokeWidth(1);
            setX(rectangle.getX() - 5);
            setY(rectangle.getY() - 5);
            setWidth(rectangle.getWidth() + 10);
            setHeight(rectangle.getHeight() + 10);

            rectangle.xProperty().bind(xProperty().add(5));
            rectangle.yProperty().bind(yProperty().add(5));

            Anchor anchor = new Anchor("1", new SimpleDoubleProperty(getX()), new SimpleDoubleProperty(getY()));
            Anchor anchor2 = new Anchor("2", new SimpleDoubleProperty(getX()+getWidth()), new SimpleDoubleProperty(getY()));
            Anchor anchor3 = new Anchor("3", new SimpleDoubleProperty(getX()), new SimpleDoubleProperty(getY()+getHeight()));
            Anchor anchor4 = new Anchor("4", new SimpleDoubleProperty(getX()+getWidth()), new SimpleDoubleProperty(getY()+getHeight()));
            anchor.centerXProperty().bind(xProperty());
            anchor.centerYProperty().bind(yProperty());
            anchor2.centerXProperty().bind(xProperty().add(getWidth()));
            anchor2.centerYProperty().bind(yProperty());
            anchor3.centerXProperty().bind(xProperty());
            anchor3.centerYProperty().bind(yProperty().add(getHeight()));
            anchor4.centerXProperty().bind(xProperty().add(getHeight()));
            anchor4.centerYProperty().bind(yProperty().add(getHeight()));
            anchors.add(anchor);
            anchors.add(anchor2);
            anchors.add(anchor3);
            anchors.add(anchor4);
        }
    }

//    private void subscribeToColorPicker() {
//        ColorPicker colorPicker = view.getColorPicker();
//        colorPicker.valueProperty().addListener(new ChangeListener<Color>() {
//            @Override
//            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
//                if(shape instanceof Shape) {
//                    Shape s = (Shape)shape;
//                    s.setFill(newValue);
//                }
//            }
//        });
//    }

    public List<Anchor> getAnchors() {
        return anchors;
    }

    public Node getShape() {
        return shape;
    }

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
