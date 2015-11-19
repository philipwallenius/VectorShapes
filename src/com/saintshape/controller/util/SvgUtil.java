package com.saintshape.controller.util;

import com.saintshape.model.Model;
import com.saintshape.model.shape.Ellipse;
import com.saintshape.model.shape.Line;
import com.saintshape.model.shape.Parallelogram;
import com.saintshape.model.shape.Rectangle;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import org.w3c.dom.Document;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * This singleton class is responsible for exporting and importing SVG documents.
 *
 * Created by 150019538 on 18/11/15.
 */
public class SvgUtil {

    private static SvgUtil instance;
    private DocumentBuilderFactory documentBuilderFactory;
    private TransformerFactory transformerFactory;

    /**
     * Singleton constructor
     */
    private SvgUtil() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        transformerFactory = TransformerFactory.newInstance();
    }

    /**
     * Get the singleton instance of this class or creates a new one if it hasn't been created
     * @return Returns the instance of this class
     */
    public static SvgUtil getInstance() {
        if(instance == null) {
            instance = new SvgUtil();
        }
        return instance;
    }

    /**
     * Exports drawing into a SVG file
     * @param file to save drawing in
     * @param width of the drawing
     * @param height of the drawing
     * @param nodes to be included in the drawing
     * @throws Exception
     */
    public void exportSvg(File file, double width, double height, List<Node> nodes) throws Exception {

        // build XML document
        Document document = createXmlDocument(width, height, nodes);

        // save it to the file
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

    /**
     * Builds an SVG XML document with given width, height and nodes
     * @param width of the SVG drawing
     * @param height of the SVG drawing
     * @param nodes to be included in the SVG drawing
     * @return Returns a SVG XML document
     * @throws ParserConfigurationException
     */
    private Document createXmlDocument(double width, double height, List<Node> nodes) throws ParserConfigurationException {

        // build document
        DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.newDocument();

        // create svg root element, set attributes and append to XML document
        Element rootElement = document.createElement("svg");
        rootElement.setAttribute("width", String.valueOf(width));
        rootElement.setAttribute("height", String.valueOf(height));
        rootElement.setAttribute("viewbox", "0 0 " + String.valueOf(width) + " " + String.valueOf(height));
        rootElement.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        rootElement.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        rootElement.setAttribute("version", "1.1");
        document.appendChild(rootElement);

        // convert all shapes into SVG elements
        for(Node node : nodes) {
            if(node instanceof Parallelogram){
                rootElement.appendChild(convertParallelogram((Parallelogram) node, document));
            } else if(node instanceof Rectangle) {
                rootElement.appendChild(convertRectangle((Rectangle) node, document));
            } else if(node instanceof Ellipse) {
                rootElement.appendChild(convertEllipse((Ellipse) node, document));
            } else if(node instanceof Line) {
                rootElement.appendChild(convertLine((Line) node, document));
            } else if(node instanceof ImageView) {
                rootElement.appendChild(convertImageView((ImageView) node, document));
            }

        }

        return document;
    }

    /**
     * Converts an ImageView into a SVG image
     * @param imageView to convert
     * @param document that SVG image will be part of
     * @return Returns a XML element with the SVG image
     */
    private Element convertImageView(ImageView imageView, Document document) {

        // create XML element and set attributes
        Element result = document.createElement("image");
        result.setAttribute("width", String.valueOf(imageView.getFitWidth()));
        result.setAttribute("height", String.valueOf(imageView.getFitHeight()));
        result.setAttribute("x", String.valueOf(imageView.getX()));
        result.setAttribute("y", String.valueOf(imageView.getY()));
        result.setAttribute("preserveAspectRatio", "none");

        // convert the imageView image into a base64 representation
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(imageView.getImage(), null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] res  = byteArrayOutputStream.toByteArray();
        String base64Encoded = new String(Base64.getEncoder().encode(res));

        // set base64 data as an attribute in the SVG image element
        result.setAttribute("xlink:href", "data:image/png;base64," + base64Encoded);

        // apply rotation transforms, if any
        String rotateTransformation = convertRotations(imageView, (imageView.getX() + (imageView.getFitWidth() / 2)), (imageView.getY() + (imageView.getFitHeight() / 2)));
        if(rotateTransformation != null) {
            result.setAttribute("transform", rotateTransformation);
        }

        return result;
    }

    /**
     * Converts a Line into a SVG line
     * @param line to convert into SVG line
     * @param document that SVG line will be part of
     * @return Returns a XML element with the SVG line
     */
    private Element convertLine(Line line, Document document) {

        // create XML element and set attributes
        Element result = document.createElement("line");
        result.setAttribute("x1", String.valueOf(line.getStartX()));
        result.setAttribute("y1", String.valueOf(line.getStartY()));
        result.setAttribute("x2", String.valueOf(line.getEndX()));
        result.setAttribute("y2", String.valueOf(line.getEndY()));

        // set color
        Color color = (Color)line.getStroke();
        double opacity = color.getOpacity();
        double strokeWidth = line.getStrokeWidth();
        String fillString = "stroke:rgb("+(int)(color.getRed()*255)+","+(int)(color.getGreen()*255)+","+(int)(color.getBlue()*255)+");stroke-width:"+strokeWidth+";stroke-opacity:"+opacity;
        result.setAttribute("style", fillString);

        // apply rotation, if any
        String rotateTransformation = convertRotations(line, (line.getStartX()+((line.getEndX() - line.getStartX())/2)), (line.getStartY()+((line.getEndY()-line.getStartY())/2)));
        if(rotateTransformation != null) {
            result.setAttribute("transform", rotateTransformation);
        }

        return result;
    }

    /**
     * Converts an Ellipse into a SVG Ellipse
     * @param ellipse to convert into SVG Ellipse
     * @param document that SVG Ellipse will be part of
     * @return Returns a XML element with the SVG Ellipse
     */
    private Element convertEllipse(Ellipse ellipse, Document document) {

        // create XML element and set attributes
        Element result = document.createElement("ellipse");
        result.setAttribute("rx", String.valueOf(ellipse.getRadiusX()));
        result.setAttribute("ry", String.valueOf(ellipse.getRadiusY()));
        result.setAttribute("cx", String.valueOf(ellipse.getCenterX()));
        result.setAttribute("cy", String.valueOf(ellipse.getCenterY()));

        // set color
        Color color = (Color)ellipse.getFill();
        double opacity = color.getOpacity();
        String fillString = "fill:rgb("+(int)(color.getRed()*255)+","+(int)(color.getGreen()*255)+","+(int)(color.getBlue()*255)+");fill-opacity:"+opacity;
        result.setAttribute("style", fillString);

        // apply rotation, if any
        String rotateTransformation = convertRotations(ellipse, ellipse.getCenterX(), ellipse.getCenterY());
        if(rotateTransformation != null) {
            result.setAttribute("transform", rotateTransformation);
        }

        return result;
    }

    /**
     * Converts Parallelogram into SVG Rectangle with a skewX attribute
     * @param parallelogram to convert into SVG Rectangle
     * @param document that SVG Rectangle will be part of
     * @return Returns a XML element with the SVG Rectangle
     */
    private Element convertParallelogram(Parallelogram parallelogram, Document document) {

        // create XML element and set attributes
        Element result = document.createElement("rect");
        result.setAttribute("width", String.valueOf(parallelogram.getWidth()));
        result.setAttribute("height", String.valueOf(parallelogram.getHeight()));
        result.setAttribute("x", String.valueOf(parallelogram.getX()));
        result.setAttribute("y", String.valueOf(parallelogram.getY()));

        // set color
        Color color = (Color)parallelogram.getFill();
        double opacity = color.getOpacity();
        String fillString = "fill:rgb("+(int)(color.getRed()*255)+","+(int)(color.getGreen()*255)+","+(int)(color.getBlue()*255)+");fill-opacity:"+opacity;
        result.setAttribute("style", fillString);

        // apply rotation and shear
        String transformString = "";

        // get rotate transforms
        String rotateTransformation = convertRotations(parallelogram, (parallelogram.getX() + (parallelogram.getWidth() / 2)), (parallelogram.getY() + (parallelogram.getHeight() / 2)));
        if(rotateTransformation != null) {
            transformString += rotateTransformation;
        }

        // combine shear transforms
        double totalShearX = 0;
        for(Transform transform : parallelogram.getTransforms()) {
            if(transform instanceof Shear) {
                Shear shear = (Shear)transform;
                totalShearX += shear.getX();
            }
        }

        // add shear transforms
        if(totalShearX != 0) {
            if(!transformString.equals("")) {
                transformString += " ";
            }
            // svg skewX must be in degrees so convert it
            double angles = (totalShearX/8)*360;
            String shearTransformation = "skewX("+angles+")";
            transformString += shearTransformation;
        }

        if(!transformString.equals("")) {
            result.setAttribute("transform", transformString);
        }


        return result;
    }

    /**
     * Converts Rectangle into SVG rectangle
     * @param rectangle to convert into SVG Rectangle
     * @param document that SVG Rectangle will be part of
     * @return Returns a XML element with the SVG Rectangle
     */
    public Element convertRectangle(Rectangle rectangle, Document document) {

        // create XML element and set attributes
        Element result = document.createElement("rect");
        result.setAttribute("width", String.valueOf(rectangle.getWidth()));
        result.setAttribute("height", String.valueOf(rectangle.getHeight()));
        result.setAttribute("x", String.valueOf(rectangle.getX()));
        result.setAttribute("y", String.valueOf(rectangle.getY()));

        // set color
        Color color = (Color)rectangle.getFill();
        double opacity = color.getOpacity();
        String fillString = "fill:rgb("+(int)(color.getRed()*255)+","+(int)(color.getGreen()*255)+","+(int)(color.getBlue()*255)+");fill-opacity:"+opacity;
        result.setAttribute("style", fillString);

        String transformations = convertRotations(rectangle, (rectangle.getX() + (rectangle.getWidth() / 2)), (rectangle.getY() + (rectangle.getHeight() / 2)));

        if(transformations != null) {
            result.setAttribute("transform", transformations);
        }

        return result;
    }

    /**
     * Converts node rotations to SVG rotation String
     * @param node to convert rotations for
     * @param pivotX of node rotation
     * @param pivotY of node rotation
     * @return Returns SVG string with rotation
     */
    private String convertRotations(Node node, double pivotX, double pivotY) {
        String result = null;
        double totalRotation = 0;
        for(Transform transform : node.getTransforms()) {
            if(transform instanceof Rotate) {
                Rotate rotate = (Rotate)transform;
                totalRotation += rotate.getAngle();
            }
        }
        if(totalRotation > 0) {
            result = "rotate(" + totalRotation + " " + pivotX + " " + pivotY + ")";
        }
        return result;
    }

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

                Shape shape = null;

                // create shapes based on their SVG type
                switch(nodeName) {

                    // this one creates Rectangles and Parallelograms
                    case "rect" : {

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

                                Shear shear = new Shear(shearX, 0);

                                shape = new Parallelogram(x, y, width, height);

                                shape.getTransforms().add(shear);

                            }
                        }

                        // if shape hasn't been created, it must be a Rectangle
                        if(shape == null) {
                            shape = new Rectangle(x, y, width, height);
                        }

                        // apply rotation transforms, if any
                        if(transform != null && !transform.equals("") && transform.contains("rotate")) {

                            Rotate rotate = getRotate(transform);
                            if(rotate != null) {
                                shape.getTransforms().add(rotate);
                            }

                        }

                        // set color
                        String styles = element.getAttribute("style");
                        Color color = getFillColor(styles);
                        shape.setFill(color);

                        // add shape to model
                        model.getNodes().add(shape);

                        break;
                    }
                    case "ellipse" : {
                        // get dimensions
                        double centerX = Double.valueOf(element.getAttribute("cx"));
                        double centerY = Double.valueOf(element.getAttribute("cy"));
                        double radianX = Double.valueOf(element.getAttribute("rx"));
                        double radianY = Double.valueOf(element.getAttribute("ry"));

                        String transform = element.getAttribute("transform");

                        shape = new Ellipse(centerX, centerY, radianX, radianY);

                        // apply rotation, if any
                        if(transform != null && !transform.equals("") && transform.contains("rotate")) {
                            Rotate rotate = getRotate(transform);
                            if(rotate != null) {
                                shape.getTransforms().add(rotate);
                            }
                        }

                        // set color
                        String styles = element.getAttribute("style");
                        Color color = getFillColor(styles);
                        shape.setFill(color);

                        // add shape to model
                        model.getNodes().add(shape);

                        break;
                    }
                    case "line" : {
                        // get dimensions
                        double startX = Double.valueOf(element.getAttribute("x1"));
                        double startY = Double.valueOf(element.getAttribute("y1"));
                        double endX = Double.valueOf(element.getAttribute("x2"));
                        double endY = Double.valueOf(element.getAttribute("y2"));

                        String transform = element.getAttribute("transform");

                        shape = new Line(startX, startY, endX, endY);

                        // apply rotation, if any
                        if(transform != null && !transform.equals("") && transform.contains("rotate")) {
                            Rotate rotate = getRotate(transform);
                            if(rotate != null) {
                                shape.getTransforms().add(rotate);
                            }
                        }

                        // set stroke color and stroke width
                        String styles = element.getAttribute("style");
                        Color color = getStrokeColor(styles);
                        double strokeWidth = getStrokeWidth(styles);
                        shape.setStrokeWidth(strokeWidth);
                        shape.setStroke(color);

                        // add shape to model
                        model.getNodes().add(shape);

                        break;
                    }
                    case "image" : {

                        ImageView imageView = new ImageView();

                        // get dimensions of image
                        double x = Double.valueOf(element.getAttribute("x"));
                        double y = Double.valueOf(element.getAttribute("y"));
                        double width = Double.valueOf(element.getAttribute("width"));
                        double height = Double.valueOf(element.getAttribute("height"));

                        String transform = element.getAttribute("transform");

                        // set x, y, width and height of image
                        imageView.setX(x);
                        imageView.setY(y);
                        imageView.setFitWidth(width);
                        imageView.setFitHeight(height);

                        // add rotation transforms, if any
                        if(transform != null && !transform.equals("") && transform.contains("rotate")) {
                            Rotate rotate = getRotate(transform);
                            if(rotate != null) {
                                imageView.getTransforms().add(rotate);
                            }
                        }

                        // decode base64 data and convert into a Java FX image
                        String data = element.getAttribute("xlink:href");
                        String base64 = data.replace("data:image/png;base64,", "");

                        byte[] imageBytes = Base64.getDecoder().decode(base64);
                        ByteArrayInputStream in = new ByteArrayInputStream(imageBytes);

                        Image image = new Image(in);
                        imageView.setImage(image);

                        // add image to model
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

        Color color = new Color(r/255.0, g/255.0, b/255.0, opacity);

        return color;
    }

    /**
     * Extracts Rotation transform from SVG transform String
     * @param transformString to extract Rotation from
     * @return Rotation object
     */
    private Rotate getRotate(String transformString) {
        Rotate rotate = null;
        double rotateAngle = 0;
        String rotateString = "";

        // extract using regex
        Pattern pattern = Pattern.compile(".*rotate[(]([^)]*).*");

        Matcher matcher = pattern.matcher(transformString);
        if(matcher.matches()) {
            rotateString = matcher.group(1);
            String[] parts = rotateString.split(" ");
            rotateAngle = Double.valueOf(parts[0]);
            rotate = new Rotate(rotateAngle);
            rotate.setPivotX(Double.parseDouble(parts[1]));
            rotate.setPivotY(Double.parseDouble(parts[2]));
        }

        return rotate;
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

        Color color = new Color(r/255.0, g/255.0, b/255.0, opacity);

        return color;
    }

}
