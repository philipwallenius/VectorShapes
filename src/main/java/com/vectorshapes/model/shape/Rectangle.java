package com.vectorshapes.model.shape;

/**
 *
 * Rectangle class that makes Rectangles cloneable
 *
 * Created by philipwallenius on 10/11/15.
 */
public class Rectangle extends javafx.scene.shape.Rectangle {

    public Rectangle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public Rectangle clone() {
        Rectangle result = new Rectangle(getX(), getY(), getWidth(), getHeight());
        result.setRotate(getRotate());
        result.setFill(getFill());
        result.setStroke(getStroke());
        result.setStrokeWidth(getStrokeWidth());
        result.getTransforms().addAll(getTransforms());
        return result;
    }

}
