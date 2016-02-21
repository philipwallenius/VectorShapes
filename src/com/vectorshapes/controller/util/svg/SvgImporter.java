package com.vectorshapes.controller.util.svg;

import com.vectorshapes.model.Model;
import com.vectorshapes.model.shape.Ellipse;
import com.vectorshapes.model.shape.Line;
import com.vectorshapes.model.shape.Parallelogram;
import com.vectorshapes.model.shape.Rectangle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Shear;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Class responsible for importing SVG files
 *
 * Created by 150019538 on 19/11/15.
 */
class SvgImporter {

    /**
     * Imports and converts a SVG file into a Model object
     * @param file to import SVG from
     * @return Returns a Model object with shapes from SVG file
     * @throws Exception
     */
    public Model importSvg(File file) throws Exception {

        Model model = new Model();

        // Parse XML document
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document doc = documentBuilder.parse(file);

        doc.getDocumentElement().normalize();

        Element root = doc.getDocumentElement();

        // Set model width and height from SVG file
        double svgWidth = Double.parseDouble(root.getAttribute("width"));
        double svgHeight = Double.parseDouble(root.getAttribute("height"));
        model.setDimensions(svgWidth, svgHeight);
        model.setName(file.getName().substring(0, file.getName().lastIndexOf(".")));

        // get all nodes and loop through them
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {

            org.w3c.dom.Node node = nodes.item(i);

            if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {

                Element element = (Element) node;
                String nodeName = element.getNodeName();

                Shape shape;

                // create shapes based on their SVG type
                switch(nodeName) {

                    // this one creates Rectangles and Parallelograms
                    case "rect" : {
                        shape = convertRect(element);
                        model.getNodes().add(shape);
                        break;
                    }
                    case "ellipse" : {
                        shape = convertEllipse(element);
                        model.getNodes().add(shape);
                        break;
                    }
                    case "line" : {
                        shape = convertLine(element);
                        model.getNodes().add(shape);
                        break;
                    }
                    case "image" : {
                        ImageView imageView = convertImage(element);
                        model.getNodes().add(imageView);
                        break;
                    }
                    default : {
                        System.out.println(String.format("Unsupported SVG element: %s", nodeName));
                    }
                }
            }
        }

        return model;
    }

    /**
     * Converts a SVG image to ImageView
     * @param element to convert to ImageView
     * @return Returns an ImageView
     */
    private ImageView convertImage(Element element) {
        ImageView result = new ImageView();

        // get dimensions of image
        double x = Double.valueOf(element.getAttribute("x"));
        double y = Double.valueOf(element.getAttribute("y"));
        double width = Double.valueOf(element.getAttribute("width"));
        double height = Double.valueOf(element.getAttribute("height"));

        String transform = element.getAttribute("transform");

        // set x, y, width and height of image
        result.setX(x);
        result.setY(y);
        result.setFitWidth(width);
        result.setFitHeight(height);

        // add rotation transforms, if any
        if(transform != null && !transform.equals("") && transform.contains("rotate")) {
            List<Rotate> rotates = getRotate(transform);
            if(rotates.size() > 0) {
                result.getTransforms().addAll(rotates);
            }
        }

        // decode base64 data and convert into a Java FX image
        String data = element.getAttribute("xlink:href");
        String base64 = data.replace("data:image/png;base64,", "");

        byte[] imageBytes = Base64.getDecoder().decode(base64);
        ByteArrayInputStream in = new ByteArrayInputStream(imageBytes);

        Image image = new Image(in);
        result.setImage(image);
        return result;
    }

    /**
     * Converts a SVG line to a Line shape
     * @param element to convert
     * @return Returns a Line shape
     */
    private Shape convertLine(Element element) {
        Shape result;

        // get dimensions
        double startX = Double.valueOf(element.getAttribute("x1"));
        double startY = Double.valueOf(element.getAttribute("y1"));
        double endX = Double.valueOf(element.getAttribute("x2"));
        double endY = Double.valueOf(element.getAttribute("y2"));

        String transform = element.getAttribute("transform");

        result = new Line(startX, startY, endX, endY);

        // apply rotation, if any
        if(transform != null && !transform.equals("") && transform.contains("rotate")) {
            List<Rotate> rotates = getRotate(transform);
            if(rotates.size() > 0) {
                result.getTransforms().addAll(rotates);
            }
        }

        // set stroke color and stroke width
        String styles = element.getAttribute("style");
        result.setStrokeWidth(getStrokeWidth(styles));
        result.setStroke(getStrokeColor(styles));
        return result;
    }

    /**
     * Converts a SVG ellipse to an Ellipse shape
     * @param element to convert
     * @return Returns an Ellipse shape
     */
    private Shape convertEllipse(Element element) {
        Shape result;
        // get dimensions
        double centerX = Double.valueOf(element.getAttribute("cx"));
        double centerY = Double.valueOf(element.getAttribute("cy"));
        double radianX = Double.valueOf(element.getAttribute("rx"));
        double radianY = Double.valueOf(element.getAttribute("ry"));

        String transform = element.getAttribute("transform");

        result = new Ellipse(centerX, centerY, radianX, radianY);

        // apply rotation, if any
        if(transform != null && !transform.equals("") && transform.contains("rotate")) {
            List<Rotate> rotates = getRotate(transform);
            if(rotates.size() > 0) {
                result.getTransforms().addAll(rotates);
            }
        }

        // set color
        String styles = element.getAttribute("style");
        result.setFill(getFillColor(styles));
        result.setStroke(getStrokeColor(styles));
        result.setStrokeWidth(getStrokeWidth(styles));

        return result;
    }

    /**
     * Converts a SVG rect to a Rectangle or Parallelogram Shape
     * @param element to convert
     * @return Returns a Rectangle or Parallelogram
     */
    private Shape convertRect(Element element) {

        Shape result = null;

        // get dimensions
        double x = Double.valueOf(element.getAttribute("x"));
        double y = Double.valueOf(element.getAttribute("y"));
        double width = Double.valueOf(element.getAttribute("width"));
        double height = Double.valueOf(element.getAttribute("height"));

        // if it has skew transform attribute, it is a parallelogram
        String transform = element.getAttribute("transform");
        if(transform != null && !transform.equals("")) {
            if (transform.contains("skewX")) {
                double skewAngle = 0;

                Pattern pattern = Pattern.compile(".*skewX[(]([^)]*).*");

                Matcher matcher = pattern.matcher(transform);
                if (matcher.matches()) {
                    skewAngle = Double.valueOf(matcher.group(1));
                }
                // convert skewX angle to shearX
                double shearX = (skewAngle / 360) * 8;

                // apply skewX with pivots
                Shear shear = new Shear(shearX, 0, (x+(width/2)), (y+(height/2)));

                result = new Parallelogram(x, y, width, height);

                result.getTransforms().add(shear);

            }
        }

        // if shape hasn't been created, it must be a Rectangle
        if(result == null) {
            result = new Rectangle(x, y, width, height);
        }

        // apply rotation transforms, if any
        if(transform != null && !transform.equals("") && transform.contains("rotate")) {

            List<Rotate> rotates = getRotate(transform);
            if(rotates.size() > 0) {
                result.getTransforms().addAll(rotates);
            }

        }

        // set color
        String styles = element.getAttribute("style");
        result.setFill(getFillColor(styles));
        result.setStroke(getStrokeColor(styles));
        result.setStrokeWidth(getStrokeWidth(styles));

        return result;
    }

    /**
     * Extracts the stroke width from SVG styles attribute
     * @param styles to extract stroke width from
     * @return Returns double representing stroke width
     */
    private double getStrokeWidth(String styles) {
        double strokeWidth = 1.0;
        String[] parts = styles.split(";");

        // loop through all styles and extract width using regex
        for(String part : parts) {
            if(part.trim().startsWith("stroke-width")) {
                Pattern pattern = Pattern.compile(".*stroke-width:([^[.0-9]]*).*");
                Matcher matcher = pattern.matcher(part);
                if(matcher.matches()) {
                    strokeWidth = Double.valueOf(matcher.group(1));
                }
            }
        }

        return strokeWidth;
    }

    /**
     * Extracts stroke color and opacity from SVG styles attribute
     * @param styles to extract color from
     * @return Returns Color object
     */
    private Color getStrokeColor(String styles) {

        int r = -1;
        int g = -1;
        int b = -1;
        double opacity = 1.0;

        String[] parts = styles.split(";");

        for(String part : parts) {
            if(part.trim().startsWith("stroke:rgb")) {
                // extract color
                Pattern pattern = Pattern.compile(".*stroke:rgb[(]([^)]*).*");
                Matcher matcher = pattern.matcher(part);
                if(matcher.matches()) {
                    String rgb = matcher.group(1);
                    String[] rgbs = rgb.split(",");
                    r = Integer.valueOf(rgbs[0].trim());
                    g = Integer.valueOf(rgbs[1].trim());
                    b = Integer.valueOf(rgbs[2].trim());
                }
            } else if(part.trim().startsWith("stroke-opacity")) {
                // extract opacity
                Pattern pattern = Pattern.compile(".*stroke-opacity:([^[.0-9]]*).*");
                Matcher matcher = pattern.matcher(part);
                if(matcher.matches()) {
                    opacity = Double.valueOf(matcher.group(1));
                }
            }
        }

        return new Color(r/255.0, g/255.0, b/255.0, opacity);
    }

    /**
     * Extracts Rotation transform from SVG transform String
     * @param transformString to extract Rotation from
     * @return Rotation object
     */
    private List<Rotate> getRotate(String transformString) {
        List<Rotate> result = new ArrayList<>();

        // matches for example: rotate(1.3453 245.0 236.0)
        Pattern pattern = Pattern.compile("rotate[(]([^)]*)[)]");
        Matcher matcher = pattern.matcher(transformString);
        while(matcher.find()) {
            Rotate rotate = new Rotate();
            String match = matcher.group();
            match = match.replace("rotate(", "").replace(")", "");

            // split on space
            String[] parts = match.split(" ");
            rotate.setAngle(Double.parseDouble(parts[0]));
            rotate.setPivotX(Double.parseDouble(parts[1]));
            rotate.setPivotY(Double.parseDouble(parts[2]));

            result.add(rotate);
        }

        return result;
    }

    /**
     * Extracts fill color and opacity from SVG styles String
     * @param styles to extract fill color from
     * @return Returs Color object
     */
    private Color getFillColor(String styles) {

        int r = -1;
        int g = -1;
        int b = -1;
        double opacity = 1.0;

        String[] parts = styles.split(";");

        for(String part : parts) {
            // extract fill color
            if(part.trim().startsWith("fill:rgb")) {
                Pattern pattern = Pattern.compile(".*fill:rgb[(]([^)]*).*");
                Matcher matcher = pattern.matcher(part);
                if(matcher.matches()) {
                    String rgb = matcher.group(1);
                    String[] rgbs = rgb.split(",");
                    r = Integer.valueOf(rgbs[0].trim());
                    g = Integer.valueOf(rgbs[1].trim());
                    b = Integer.valueOf(rgbs[2].trim());
                }
            }
            // extract fill opacity
            else if(part.trim().startsWith("fill-opacity")) {
                Pattern pattern = Pattern.compile(".*fill-opacity:([^[.0-9]]*).*");
                Matcher matcher = pattern.matcher(part);
                if(matcher.matches()) {
                    opacity = Double.valueOf(matcher.group(1));
                }
            }
        }

        return new Color(r/255.0, g/255.0, b/255.0, opacity);
    }

}
