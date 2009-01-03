package com.saintshape.model.shape;

/**
 *
 * Parallelogram class to change its display name.
 *
 * Created by 150019538 on 17/11/15.
 */
public class Parallelogram extends Rectangle {

    public Parallelogram(double x, double y, double width, double height) {
        super(x, y, width ,height);
    }

    @Override
    public String getTypeSelector() {
        return "Parallelogram";
    }
}
