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
        rotatePoint = new Point(this, "5");
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

        // set rotation from shape
        setRotate(line.getRotate());

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
        setHeight((ellipse.getRadiusY() * 2));

        // set rotation from shape
        setRotate(ellipse.getRotate());

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

        // set rotation from shape
        setRotate(rectangle.getRotate());

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

        Point pointNW = new Point(this, "NW");
        pointNW.centerXProperty().bind(xProperty());
        pointNW.centerYProperty().bind(yProperty());
        pointNW.setCursor(Cursor.NW_RESIZE);

        Point pointNE = new Point(this, "NE");
        pointNE.centerXProperty().bind(xProperty().add(widthProperty()));
        pointNE.centerYProperty().bind(yProperty());
        pointNE.setCursor(Cursor.NE_RESIZE);

        Point pointSW = new Point(this, "SW");
        pointSW.centerXProperty().bind(xProperty());
        pointSW.centerYProperty().bind(yProperty().add(heightProperty()));
        pointSW.setCursor(Cursor.SW_RESIZE);

        Point pointSE = new Point(this, "SE");
        pointSE.centerXProperty().bind(xProperty().add(widthProperty()));
        pointSE.centerYProperty().bind(yProperty().add(heightProperty()));
        pointSE.setCursor(Cursor.SE_RESIZE);

        Point pointW = new Point(this, "W");
        pointW.centerXProperty().bind(xProperty());
        pointW.centerYProperty().bind(yProperty().add(heightProperty().divide(2)));
        pointW.setCursor(Cursor.W_RESIZE);

        Point pointN = new Point(this, "N");
        pointN.centerXProperty().bind(xProperty().add(widthProperty().divide(2)));
        pointN.centerYProperty().bind(yProperty());
        pointN.setCursor(Cursor.N_RESIZE);

        Point pointE = new Point(this, "E");
        pointE.centerXProperty().bind(xProperty().add(widthProperty()));
        pointE.centerYProperty().bind(yProperty().add(heightProperty().divide(2)));
        pointE.setCursor(Cursor.E_RESIZE);

        Point pointS = new Point(this, "S");
        pointS.centerXProperty().bind(xProperty().add(widthProperty().divide(2)));
        pointS.centerYProperty().bind(yProperty().add(heightProperty()));
        pointS.setCursor(Cursor.S_RESIZE);

        resizePoints.add(pointNW);
        resizePoints.add(pointNE);
        resizePoints.add(pointSW);
        resizePoints.add(pointSE);
        resizePoints.add(pointW);
        resizePoints.add(pointN);
        resizePoints.add(pointE);
        resizePoints.add(pointS);
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
