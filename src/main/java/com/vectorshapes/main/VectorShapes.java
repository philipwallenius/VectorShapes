package com.vectorshapes.main;

import com.vectorshapes.controller.Controller;
import com.vectorshapes.model.Model;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * This class starts the application.
 *
 * Created by philipwallenius on 01/11/15.
 */
public class VectorShapes extends Application {

    private Controller controller;
    private Model model;

    /**
     * Starts the application
     * @param primaryStage derived from JavaFX
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        model = new Model();
        controller = new Controller(model, primaryStage);
    }

    /**
     * Main method
     * @param args representing program input
     */
    public static void main(String[] args) {
        launch(args);
    }
}
