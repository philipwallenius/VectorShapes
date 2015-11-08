package com.saintshape.view.event;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Class representing a selection.
 *
 * Created by 150019538 on 05/11/15.
 */
public class Selection extends Rectangle {

    private List<Point> resizePoints;
    private Point rotatePoint;
    private Node shape;

    public Selection(Node shape) {
        this.shape = shape;
        initialize();
    }

    public void initialize() {

        resizePoints = new ArrayList<>();

        if(shape instanceof Rectangle) {
            bindToRectangle((Rectangle)shape);
        } else if(shape instanceof Ellipse) {
            bindToEllipse((Ellipse)shape);
        } else if(shape instanceof Line) {
            bindToLine((Line)shape);
        }
        createResizePoints();
        createRotatePoint();
    }

    private void createRotatePoint() {
        rotatePoint = new Point(this, "5", getX(), getY());
        rotatePoint.setFill(Color.GREENYELLOW.deriveColor(1, 1, 1, 0.5));
        rotatePoint.setStroke(Color.GREENYELLOW);
        rotatePoint.centerXProperty().bind(xProperty().add(widthProperty().divide(2)));
        rotatePoint.centerYProperty().bind(yProperty().subtract(20));
        rotatePoint.setCursor(Cursor.H_RESIZE);
    }

    private void bindToLine(Line line) {
        setFill(Color.TRANSPARENT);
        setStroke(Color.DEEPSKYBLUE);
        setStrokeWidth(1);

        if(line.getStartX() < line.getEndX()) {
            setX(line.getStartX());
        } else {
            setX(line.getEndX());
        }
        if(line.getStartY() < line.getEndY()) {
            setY(line.getStartY());
        } else {
            setY(line.getEndY());
        }

        setWidth(Math.abs(line.getStartX()-line.getEndX()));
        setHeight(Math.abs(line.getStartY()-line.getEndY()));

        // bind position
        if(line.getStartX() < line.getEndX()) {
            line.startXProperty().bind(xProperty());
            line.endXProperty().bind(xProperty().add(widthProperty()));
        } else {
            line.startXProperty().bind(xProperty().add(widthProperty()));
            line.endXProperty().bind(xProperty());
        }

        if(line.getStartY() < line.getEndY()) {
            line.startYProperty().bind(yProperty());
            line.endYProperty().bind(yProperty().add(heightProperty()));
        } else {
            line.startYProperty().bind(yProperty().add(heightProperty()));
            line.endYProperty().bind(yProperty());
        }

        // bind size
        // not needed for line

        // bind rotation
        line.rotateProperty().bind(rotateProperty());
    }

    private void bindToEllipse(Ellipse ellipse) {
        setFill(Color.TRANSPARENT);
        setStroke(Color.DEEPSKYBLUE);
        setStrokeWidth(1);
        setX(ellipse.getCenterX() - ellipse.getRadiusX());
        setY(ellipse.getCenterY() - ellipse.getRadiusY());
        setWidth((ellipse.getRadiusX()*2));
        setHeight((ellipse.getRadiusY()*2));

        // bind position
        ellipse.centerXProperty().bind(xProperty().add(ellipse.radiusXProperty()));
        ellipse.centerYProperty().bind(yProperty().add(ellipse.radiusYProperty()));

        // bind size
        ellipse.radiusXProperty().bind(widthProperty().divide(2));
        ellipse.radiusYProperty().bind(heightProperty().divide(2));

        // bind rotation
        ellipse.rotateProperty().bind(rotateProperty());
    }

    private void bindToRectangle(Rectangle rectangle) {
        setFill(Color.TRANSPARENT);
        setStroke(Color.DEEPSKYBLUE);
        setStrokeWidth(1);
        setX(rectangle.getX());
        setY(rectangle.getY());
        setWidth(rectangle.getWidth());
        setHeight(rectangle.getHeight());

        // bind position
        rectangle.xProperty().bind(xProperty());
        rectangle.yProperty().bind(yProperty());

        // bind size
        rectangle.widthProperty().bind(widthProperty());
        rectangle.heightProperty().bind(heightProperty());

        // bind rotation
        rectangle.rotateProperty().bind(rotateProperty());
    }

    private void createResizePoints() {
        Point point = new Point(this, "1", getX(), getY());
        Point point2 = new Point(this, "2", getX()+getWidth(), getY());
        Point point3 = new Point(this, "3", getX(), getY()+getHeight());
        Point point4 = new Point(this, "4", getX()+getWidth(), getY()+getHeight());

        point.centerXProperty().bind(xProperty());
        point.centerYProperty().bind(yProperty());
        point.setCursor(Cursor.NW_RESIZE);

        point2.centerXProperty().bind(xProperty().add(widthProperty()));
        point2.centerYProperty().bind(yProperty());
        point2.setCursor(Cursor.NE_RESIZE);

        point3.centerXProperty().bind(xProperty());
        point3.centerYProperty().bind(yProperty().add(heightProperty()));
        point3.setCursor(Cursor.SW_RESIZE);

        point4.centerXProperty().bind(xProperty().add(widthProperty()));
        point4.centerYProperty().bind(yProperty().add(heightProperty()));
        point4.setCursor(Cursor.SE_RESIZE);

        resizePoints.add(point);
        resizePoints.add(point2);
        resizePoints.add(point3);
        resizePoints.add(point4);
    }

    public List<Point> getResizePoints() {
        return resizePoints;
    }

    public Point getRotatePoint() {
        return rotatePoint;
    }

    public Node getShape() {
        return shape;
    }



}
