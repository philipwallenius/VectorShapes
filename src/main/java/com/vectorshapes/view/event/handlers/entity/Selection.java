package com.vectorshapes.view.event.handlers.entity;

import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Class representing a selection.
 *
 * Created by philipwallenius on 05/11/15.
 */
public class Selection extends Rectangle {

    private List<Point> resizePoints;
    private Point rotatePoint;
    private final Node shape;
    private Image rotateCursorImage;

    public Selection(Node shape) {
        this.shape = shape;
        initialize();
    }

    void initialize() {

        resizePoints = new ArrayList<>();

        if(shape instanceof Rectangle) {
            bindToRectangle((Rectangle)shape);
        } else if(shape instanceof Ellipse) {
            bindToEllipse((Ellipse)shape);
        } else if(shape instanceof Line) {
            bindToLine((Line)shape);
        } else if(shape instanceof ImageView) {
            bindToImageView((ImageView)shape);
        }
        createResizePoints();

        rotateCursorImage = new Image(this.getClass().getResourceAsStream("/rotate_cw.png"));
        createRotatePoint();

    }

    private void bindToImageView(ImageView imageView) {
        setFill(Color.TRANSPARENT);
        setStroke(Color.DEEPSKYBLUE);
        setStrokeWidth(1);
        setX(imageView.getX());
        setY(imageView.getY());
        setWidth(imageView.getFitWidth());
        setHeight(imageView.getFitHeight());

        // bind position
        imageView.xProperty().bind(xProperty());
        imageView.yProperty().bind(yProperty());

        // bind size
        imageView.fitWidthProperty().bind(widthProperty());
        imageView.fitHeightProperty().bind(heightProperty());

        // add all transforms to selection
        getTransforms().addAll(imageView.getTransforms());
    }

    private void createRotatePoint() {
        rotatePoint = new Point(this, "ROTATE", Color.GREENYELLOW);
        rotatePoint.centerXProperty().bind(xProperty().add(widthProperty().divide(2)));
        rotatePoint.centerYProperty().bind(yProperty().add(heightProperty().divide(2)));
        rotatePoint.setCursor(new ImageCursor(rotateCursorImage,rotateCursorImage.getWidth()/2,rotateCursorImage.getHeight()/2));

        // add all transforms to selection
        rotatePoint.getTransforms().addAll(getTransforms());
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

        // add all transforms to selection
        getTransforms().addAll(line.getTransforms());
    }

    private void bindToEllipse(Ellipse ellipse) {
        setFill(Color.TRANSPARENT);
        setStroke(Color.DEEPSKYBLUE);
        setStrokeWidth(1);
        setX(ellipse.getCenterX() - ellipse.getRadiusX());
        setY(ellipse.getCenterY() - ellipse.getRadiusY());
        setWidth((ellipse.getRadiusX()*2));
        setHeight((ellipse.getRadiusY() * 2));

        // bind position
        ellipse.centerXProperty().bind(xProperty().add(ellipse.radiusXProperty()));
        ellipse.centerYProperty().bind(yProperty().add(ellipse.radiusYProperty()));

        // bind size
        ellipse.radiusXProperty().bind(widthProperty().divide(2));
        ellipse.radiusYProperty().bind(heightProperty().divide(2));

        // add all transforms to selection
        getTransforms().addAll(ellipse.getTransforms());
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

        // add all transforms to selection
        getTransforms().addAll(rectangle.getTransforms());
    }

    private void createResizePoints() {

        Point pointNW = new Point(this, "NW", Color.DEEPSKYBLUE);
        pointNW.centerXProperty().bind(xProperty());
        pointNW.centerYProperty().bind(yProperty());
        pointNW.setCursor(Cursor.NW_RESIZE);

        Point pointNE = new Point(this, "NE", Color.DEEPSKYBLUE);
        pointNE.centerXProperty().bind(xProperty().add(widthProperty()));
        pointNE.centerYProperty().bind(yProperty());
        pointNE.setCursor(Cursor.NE_RESIZE);

        Point pointSW = new Point(this, "SW", Color.DEEPSKYBLUE);
        pointSW.centerXProperty().bind(xProperty());
        pointSW.centerYProperty().bind(yProperty().add(heightProperty()));
        pointSW.setCursor(Cursor.SW_RESIZE);

        Point pointSE = new Point(this, "SE", Color.DEEPSKYBLUE);
        pointSE.centerXProperty().bind(xProperty().add(widthProperty()));
        pointSE.centerYProperty().bind(yProperty().add(heightProperty()));
        pointSE.setCursor(Cursor.SE_RESIZE);

        Point pointW = new Point(this, "W", Color.DEEPSKYBLUE);
        pointW.centerXProperty().bind(xProperty());
        pointW.centerYProperty().bind(yProperty().add(heightProperty().divide(2)));
        pointW.setCursor(Cursor.W_RESIZE);

        Point pointN = new Point(this, "N", Color.DEEPSKYBLUE);
        pointN.centerXProperty().bind(xProperty().add(widthProperty().divide(2)));
        pointN.centerYProperty().bind(yProperty());
        pointN.setCursor(Cursor.N_RESIZE);

        Point pointE = new Point(this, "E", Color.DEEPSKYBLUE);
        pointE.centerXProperty().bind(xProperty().add(widthProperty()));
        pointE.centerYProperty().bind(yProperty().add(heightProperty().divide(2)));
        pointE.setCursor(Cursor.E_RESIZE);

        Point pointS = new Point(this, "S", Color.DEEPSKYBLUE);
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

        // add all transforms to resize points
        for(Point p : resizePoints) {
            p.getTransforms().addAll(getTransforms());
        }
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
