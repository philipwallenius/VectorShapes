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
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 150019538 on 05/11/15.
 */
public class Selection extends Rectangle {

    public static int BORDER_MARGIN = 5;
    private List<Anchor> anchors;
    private Node shape;

    public Selection(Node shape) {
        this.shape = shape;
        initialize();
    }

    public void initialize() {

        anchors = new ArrayList<>();
        if(shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle)shape;
            setFill(Color.TRANSPARENT);
            setStroke(Color.DEEPSKYBLUE);
            setStrokeWidth(1);
            setX(rectangle.getX() - BORDER_MARGIN);
            setY(rectangle.getY() - BORDER_MARGIN);
            setWidth(rectangle.getWidth() + (BORDER_MARGIN*2));
            setHeight(rectangle.getHeight() + (BORDER_MARGIN*2));

            rectangle.xProperty().bind(xProperty().add(BORDER_MARGIN));
            rectangle.yProperty().bind(yProperty().add(BORDER_MARGIN));

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
            anchor4.centerXProperty().bind(xProperty().add(getWidth()));
            anchor4.centerYProperty().bind(yProperty().add(getHeight()));
            anchors.add(anchor);
            anchors.add(anchor2);
            anchors.add(anchor3);
            anchors.add(anchor4);
        } else if(shape instanceof Ellipse) {
            Ellipse ellipse = (Ellipse)shape;
            setFill(Color.TRANSPARENT);
            setStroke(Color.DEEPSKYBLUE);
            setStrokeWidth(1);
            setX(ellipse.getCenterX() - ellipse.getRadiusX() - BORDER_MARGIN);
            setY(ellipse.getCenterY() - ellipse.getRadiusY() - BORDER_MARGIN);
            setWidth((ellipse.getRadiusX()*2) + (BORDER_MARGIN*2));
            setHeight((ellipse.getRadiusY()*2) + (BORDER_MARGIN*2));

            ellipse.centerXProperty().bind(xProperty().add(BORDER_MARGIN).add(ellipse.radiusXProperty()));
            ellipse.centerYProperty().bind(yProperty().add(BORDER_MARGIN).add(ellipse.radiusYProperty()));

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
            anchor4.centerXProperty().bind(xProperty().add(getWidth()));
            anchor4.centerYProperty().bind(yProperty().add(getHeight()));
            anchors.add(anchor);
            anchors.add(anchor2);
            anchors.add(anchor3);
            anchors.add(anchor4);
        } else if(shape instanceof Line) {
            Line line = (Line)shape;
            setFill(Color.TRANSPARENT);
            setStroke(Color.DEEPSKYBLUE);
            setStrokeWidth(1);

            if(line.getStartX() < line.getEndX()) {
                setX(line.getStartX() - BORDER_MARGIN);
            } else {
                setX(line.getEndX() - BORDER_MARGIN);
            }
            if(line.getStartY() < line.getEndY()) {
                setY(line.getStartY() - BORDER_MARGIN);
            } else {
                setY(line.getEndY() - BORDER_MARGIN);
            }

            setWidth(Math.abs(line.getStartX()-line.getEndX()) + (BORDER_MARGIN*2));
            setHeight(Math.abs(line.getStartY()-line.getEndY()) + (BORDER_MARGIN*2));

            if(line.getStartX() < line.getEndX()) {
                line.startXProperty().bind(xProperty().add(BORDER_MARGIN));
                line.endXProperty().bind(xProperty().add(getWidth()).subtract(BORDER_MARGIN));
            } else {
                line.startXProperty().bind(xProperty().add(getWidth()).subtract(BORDER_MARGIN));
                line.endXProperty().bind(xProperty().add(BORDER_MARGIN));
            }

            if(line.getStartY() < line.getEndY()) {
                line.startYProperty().bind(yProperty().add(BORDER_MARGIN));
                line.endYProperty().bind(yProperty().add(getHeight()).subtract(BORDER_MARGIN));
            } else {
                line.startYProperty().bind(yProperty().add(getHeight()).subtract(BORDER_MARGIN));
                line.endYProperty().bind(yProperty().add(BORDER_MARGIN));
            }

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
            anchor4.centerXProperty().bind(xProperty().add(getWidth()));
            anchor4.centerYProperty().bind(yProperty().add(getHeight()));
            anchors.add(anchor);
            anchors.add(anchor2);
            anchors.add(anchor3);
            anchors.add(anchor4);
        }
    }

    public List<Anchor> getAnchors() {
        return anchors;
    }

    public Node getShape() {
        return shape;
    }

    class Anchor extends Circle {
        Anchor(String id, DoubleProperty x, DoubleProperty y) {
            super(x.get(), y.get(), BORDER_MARGIN);
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
