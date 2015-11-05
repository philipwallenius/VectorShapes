package com.saintshape.view.event;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import org.w3c.dom.css.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 150019538 on 05/11/15.
 */
public class Selection extends Rectangle {

    public static int BORDER_MARGIN = 5;
    private List<Point> points;
    private Node shape;

    public Selection(Node shape) {
        this.shape = shape;
        initialize();
    }

    public void initialize() {

        points = new ArrayList<>();
        if(shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle)shape;
            setFill(Color.TRANSPARENT);
            setStroke(Color.DEEPSKYBLUE);
            setStrokeWidth(1);
            setX(rectangle.getX() - BORDER_MARGIN);
            setY(rectangle.getY() - BORDER_MARGIN);
            setWidth(rectangle.getWidth() + (BORDER_MARGIN*2));
            setHeight(rectangle.getHeight() + (BORDER_MARGIN*2));

            // bind position
            rectangle.xProperty().bind(xProperty().add(BORDER_MARGIN));
            rectangle.yProperty().bind(yProperty().add(BORDER_MARGIN));

            // bind size
            rectangle.widthProperty().bind(widthProperty().subtract(BORDER_MARGIN*2));
            rectangle.heightProperty().bind(heightProperty().subtract(BORDER_MARGIN*2));

            Point point = new Point(this, "1", getX(), getY());
            Point point2 = new Point(this, "2", getX()+getWidth(), getY());
            Point point3 = new Point(this, "3", getX(), getY()+getHeight());
            Point point4 = new Point(this, "4", getX()+getWidth(), getY()+getHeight());

            point.centerXProperty().bind(xProperty());
            point.centerYProperty().bind(yProperty());

            point2.centerXProperty().bind(xProperty().add(widthProperty()));
            point2.centerYProperty().bind(yProperty());

            point3.centerXProperty().bind(xProperty());
            point3.centerYProperty().bind(yProperty().add(heightProperty()));

            point4.centerXProperty().bind(xProperty().add(widthProperty()));
            point4.centerYProperty().bind(yProperty().add(heightProperty()));

            points.add(point);
            points.add(point2);
            points.add(point3);
            points.add(point4);
        } else if(shape instanceof Ellipse) {
            Ellipse ellipse = (Ellipse)shape;
            setFill(Color.TRANSPARENT);
            setStroke(Color.DEEPSKYBLUE);
            setStrokeWidth(1);
            setX(ellipse.getCenterX() - ellipse.getRadiusX() - BORDER_MARGIN);
            setY(ellipse.getCenterY() - ellipse.getRadiusY() - BORDER_MARGIN);
            setWidth((ellipse.getRadiusX()*2) + (BORDER_MARGIN*2));
            setHeight((ellipse.getRadiusY()*2) + (BORDER_MARGIN*2));

            // bind position
            ellipse.centerXProperty().bind(xProperty().add(BORDER_MARGIN).add(ellipse.radiusXProperty()));
            ellipse.centerYProperty().bind(yProperty().add(BORDER_MARGIN).add(ellipse.radiusYProperty()));

            // bind size
            ellipse.radiusXProperty().bind(widthProperty().divide(2).subtract(BORDER_MARGIN));
            ellipse.radiusYProperty().bind(heightProperty().divide(2).subtract(BORDER_MARGIN));

            Point point = new Point(this, "1", getX(), getY());
            Point point2 = new Point(this, "2", getX()+getWidth(), getY());
            Point point3 = new Point(this, "3", getX(), getY()+getHeight());
            Point point4 = new Point(this, "4", getX()+getWidth(), getY()+getHeight());
            point.centerXProperty().bind(xProperty());
            point.centerYProperty().bind(yProperty());
            point2.centerXProperty().bind(xProperty().add(widthProperty()));
            point2.centerYProperty().bind(yProperty());
            point3.centerXProperty().bind(xProperty());
            point3.centerYProperty().bind(yProperty().add(heightProperty()));
            point4.centerXProperty().bind(xProperty().add(widthProperty()));
            point4.centerYProperty().bind(yProperty().add(heightProperty()));
            points.add(point);
            points.add(point2);
            points.add(point3);
            points.add(point4);
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

            // bind position
            if(line.getStartX() < line.getEndX()) {
                line.startXProperty().bind(xProperty().add(BORDER_MARGIN));
                line.endXProperty().bind(xProperty().add(widthProperty()).subtract(BORDER_MARGIN));
            } else {
                line.startXProperty().bind(xProperty().add(widthProperty()).subtract(BORDER_MARGIN));
                line.endXProperty().bind(xProperty().add(BORDER_MARGIN));
            }

            if(line.getStartY() < line.getEndY()) {
                line.startYProperty().bind(yProperty().add(BORDER_MARGIN));
                line.endYProperty().bind(yProperty().add(heightProperty()).subtract(BORDER_MARGIN));
            } else {
                line.startYProperty().bind(yProperty().add(heightProperty()).subtract(BORDER_MARGIN));
                line.endYProperty().bind(yProperty().add(BORDER_MARGIN));
            }

            // bind size
            // perhaps not needed for lines?


            Point point = new Point(this, "1", getX(), getY());
            Point point2 = new Point(this, "2", getX()+getWidth(), getY());
            Point point3 = new Point(this, "3", getX(), getY()+getHeight());
            Point point4 = new Point(this, "4", getX()+getWidth(), getY()+getHeight());
            point.centerXProperty().bind(xProperty());
            point.centerYProperty().bind(yProperty());
            point2.centerXProperty().bind(xProperty().add(widthProperty()));
            point2.centerYProperty().bind(yProperty());
            point3.centerXProperty().bind(xProperty());
            point3.centerYProperty().bind(yProperty().add(heightProperty()));
            point4.centerXProperty().bind(xProperty().add(widthProperty()));
            point4.centerYProperty().bind(yProperty().add(heightProperty()));
            points.add(point);
            points.add(point2);
            points.add(point3);
            points.add(point4);
        }
        registerPoints(points);
    }

    private double ww = 0;
    private double hh = 0;
    private double xx = 0;
    private double yy = 0;

    public void registerPoints(List<Point> points) {

        final MouseClick click = new MouseClick();

        for(Point p : points) {
            p.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    click.x = event.getX();
                    click.y = event.getY();
                    if (event.getSource() instanceof Point) {
                        Point point = (Point) event.getSource();
                        ww = point.getSelection().getWidth();
                        hh = point.getSelection().getHeight();
                        xx = point.getSelection().getX();
                        yy = point.getSelection().getY();
                    }
                }
            });
            p.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(event.getSource() instanceof Point) {
                        Point point = (Point) event.getSource();
                        Selection rectangle = (Selection)point.getSelection();
                        if (event.isPrimaryButtonDown()) {
                            if(rectangle != null ) {
                                double wi = click.x-event.getX();
                                double hi = click.y-event.getY();
                                if(event.isShiftDown()) {
                                    wi = Math.min(wi, hi);
                                    hi = Math.min(wi, hi);
                                }

                                if(point.getId().equals("1")) {
                                    if(wi > 0) {
                                        rectangle.setX(xx-Math.abs(wi));
                                    } else {
                                        rectangle.setX(xx+Math.abs(wi));
                                    }
                                    if(hi > 0) {
                                        rectangle.setY(yy-Math.abs(hi));
                                    } else {
                                        rectangle.setY(yy+Math.abs(hi));
                                    }
                                    rectangle.setWidth(ww+wi);
                                    rectangle.setHeight(hh+hi);

                                } else if(point.getId().equals("2")) {
                                    if(hi > 0) {
                                        rectangle.setY(yy-Math.abs(hi));
                                    } else {
                                        rectangle.setY(yy+Math.abs(hi));
                                    }
                                    rectangle.setWidth(ww-wi);
                                    rectangle.setHeight(hh+hi);

                                } else if(point.getId().equals("3")) {

                                    if(wi > 0) {
                                        rectangle.setX(xx-Math.abs(wi));
                                    } else {
                                        rectangle.setX(xx+Math.abs(wi));
                                    }
                                    rectangle.setWidth(ww+wi);
                                    rectangle.setHeight(hh-hi);

                                } else if(point.getId().equals("4")) {
                                    rectangle.setWidth(ww-wi);
                                    rectangle.setHeight(hh-hi);
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    public List<Point> getPoints() {
        return points;
    }

    public Node getShape() {
        return shape;
    }

    /**
     * Class representing the resize points on selected objects
     */
    class Point extends Circle {

        private Rectangle selection;

        Point(Rectangle selection, String id, double x, double y) {
            super(x, y, BORDER_MARGIN);
            this.selection = selection;
            setStrokeWidth(1);
            setId(id);
            setFill(Color.GOLD.deriveColor(1, 1, 1, 0.5));
            setStrokeType(StrokeType.OUTSIDE);
            setStroke(Color.GOLD);
        }

        public Rectangle getSelection() {
            return selection;
        }
    }

}
