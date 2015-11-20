package com.saintshape.view.event.handlers.entity;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 *
 * Class representing the resize points on selected objects
 *
 * Created by 150019538 on 06/11/15.
 */
public class Point extends Circle {

    private final Rectangle selection;

    Point(Rectangle selection, String id, Color fill) {
        super(5);
        this.selection = selection;
        setStrokeWidth(1);
        setId(id);
        setFill(fill.deriveColor(1, 1, 1, 0.5));
        setStrokeType(StrokeType.OUTSIDE);
        setStroke(fill);
    }

    public Rectangle getSelection() {
        return selection;
    }
}