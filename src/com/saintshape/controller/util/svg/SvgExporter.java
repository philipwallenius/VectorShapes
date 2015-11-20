package com.saintshape.controller.util.svg;

import com.saintshape.model.shape.Ellipse;
import com.saintshape.model.shape.Line;
import com.saintshape.model.shape.Parallelogram;
import com.saintshape.model.shape.Rectangle;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Shear;
import javafx.scene.transform.Transform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 *
 * Class responsible for exporting to SVG files
 *
 * Created by 150019538 on 19/11/15.
 */
class SvgExporter {


    private final DocumentBuilderFactory documentBuilderFactory;
    private final TransformerFactory transformerFactory;

    public SvgExporter() {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        transformerFactory = TransformerFactory.newInstance();
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
     * @throws javax.xml.parsers.ParserConfigurationException
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
        String rotateTransformation = convertRotations(imageView);
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
        double strokeOpacity = color.getOpacity();
        double strokeWidth = line.getStrokeWidth();
        String strokeString = "stroke:rgb("+(int)(color.getRed()*255)+","+(int)(color.getGreen()*255)+","+(int)(color.getBlue()*255)+");stroke-width:"+strokeWidth+";stroke-opacity:"+strokeOpacity;
        result.setAttribute("style", strokeString);

        // apply rotation, if any
        String rotateTransformation = convertRotations(line);
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
        Color fillColor = (Color)ellipse.getFill();
        double fillOpacity = fillColor.getOpacity();
        Color strokeColor = (Color)ellipse.getStroke();
        double strokeOpacity = strokeColor.getOpacity();
        double strokeWidth = ellipse.getStrokeWidth();
        String fillString = "fill:rgb("+(int)(fillColor.getRed()*255)+","+(int)(fillColor.getGreen()*255)+","+(int)(fillColor.getBlue()*255)+");fill-opacity:"+fillOpacity+";stroke:rgb("+(int)(strokeColor.getRed()*255)+","+(int)(strokeColor.getGreen()*255)+","+(int)(strokeColor.getBlue()*255)+");stroke-opacity:"+strokeOpacity+";stroke-width:"+strokeWidth;
        result.setAttribute("style", fillString);

        // apply rotation, if any
        String rotateTransformation = convertRotations(ellipse);
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
        Color fillColor = (Color)parallelogram.getFill();
        double fillOpacity = fillColor.getOpacity();
        Color strokeColor = (Color)parallelogram.getStroke();
        double strokeOpacity = fillColor.getStrokeOpacity();

        String fillString = "fill:rgb("+(int)(fillColor.getRed()*255)+","+(int)(fillColor.getGreen()*255)+","+(int)(fillColor.getBlue()*255)+");fill-opacity:"+fillOpacity;
        result.setAttribute("style", fillString);

        // apply rotation and shear
        String transformString = "";

        // get rotate transforms
        String rotateTransformation = convertRotations(parallelogram);
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
    Element convertRectangle(Rectangle rectangle, Document document) {

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

        String transformations = convertRotations(rectangle);

        if(transformations != null) {
            result.setAttribute("transform", transformations);
        }

        return result;
    }

    /**
     * Converts node rotations to SVG rotation String
     * @param node to convert rotations for
     * @return Returns SVG string with rotation
     */
    private String convertRotations(Node node) {
        String result = "";
        for(Transform transform : node.getTransforms()) {
            if(transform instanceof Rotate) {
                Rotate rotate = (Rotate)transform;
                result += "rotate(" + rotate.getAngle() + " " + rotate.getPivotX() + " " + rotate.getPivotY() + "),";
            }
        }

        if(!result.equals("")) {
            if(result.endsWith(",")) {
                result = result.substring(0, result.lastIndexOf(","));
            }
        }

        return result;
    }

}
