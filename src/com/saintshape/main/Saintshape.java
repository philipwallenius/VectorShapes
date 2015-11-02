package com.saintshape.main;

import com.saintshape.controller.Controller;
import com.saintshape.model.Model;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * This class starts the application.
 *
 * Created by 150019538 on 01/11/15.
 */
public class Saintshape extends Application {

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
