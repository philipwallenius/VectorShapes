package com.saintshape.model.shape;

/**
 * Created by 150019538 on 10/11/15.
 */
public class Ellipse extends javafx.scene.shape.Ellipse {

    public Ellipse(double centerX, double centerY, double radiusX, double radiusY) {
        super(centerX, centerY, radiusX, radiusY);
    }

    public javafx.scene.shape.Ellipse clone() {
        Ellipse result = new Ellipse(getCenterX(), getCenterY(), getRadiusX(), getRadiusY());
        result.setRotate(getRotate());
        result.setFill(getFill());
        result.setStroke(getStroke());
        return result;
    }

}
