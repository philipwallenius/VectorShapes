package com.saintshape.model.shape;

import javafx.scene.transform.Transform;

import java.util.List;

/**
 *
 * Ellipse class that makes Ellipses cloneable
 *
 * Created by 150019538 on 10/11/15.
 */
public class Ellipse extends javafx.scene.shape.Ellipse {

    public Ellipse(double centerX, double centerY, double radiusX, double radiusY) {
        super(centerX, centerY, radiusX, radiusY);
    }

    public javafx.scene.shape.Ellipse clone() {
        Ellipse result = new Ellipse(getCenterX(), getCenterY(), getRadiusX(), getRadiusY());
        result.setFill(getFill());
        result.setStroke(getStroke());
        result.getTransforms().addAll(getTransforms());
        return result;
    }

}
