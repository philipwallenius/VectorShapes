package com.vectorshapes.model.shape;

/**
 *
 * Line class that makes Lines cloneable
 *
 * Created by philipwallenius on 10/11/15.
 */
public class Line extends javafx.scene.shape.Line {

    public Line(double startX, double startY, double endX, double endY) {
        super(startX, startY, endX, endY);
    }

    public javafx.scene.shape.Line clone() {
        Line result = new Line(getStartX(), getStartY(), getEndX(), getEndY());
        result.setRotate(getRotate());
        result.setFill(getFill());
        result.setStroke(getStroke());
        result.setStrokeWidth(getStrokeWidth());
        result.getTransforms().addAll(getTransforms());
        return result;
    }

}
