package com.saintshape.controller.util.svg;

import com.saintshape.controller.util.OpenSaveInterface;
import com.saintshape.model.Model;

import java.io.File;

/**
 *
 * This singleton class is responsible for exporting and importing SVG documents.
 *
 * Created by 150019538 on 18/11/15.
 */
public class SvgUtil implements OpenSaveInterface {

    private final SvgExporter svgExporter;
    private final SvgImporter svgImporter;
    private static SvgUtil instance;


    /**
     * Singleton constructor
     */
    private SvgUtil() {
        svgExporter = new SvgExporter();
        svgImporter = new SvgImporter();
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
     * Imports model from SVG file
     * @param file to import model from
     * @return Returns imported model
     * @throws Exception
     */
    public Model importFile(File file) throws Exception {
        return svgImporter.importSvg(file);
    }

    /**
     * Exports drawing to SVG file
     * @param model to export
     * @throws Exception
     */
    public void exportFile(Model model) throws Exception {
        svgExporter.exportSvg(model.getFile(), model.getRootCanvas().getWidth(), model.getRootCanvas().getHeight(), model.getNodes());
    }

}
