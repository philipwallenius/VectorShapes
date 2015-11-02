package com.saintshape.main;

import com.saintshape.controller.Controller;
import com.saintshape.model.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class Saintshape extends Application {

    private Controller controller;
    private Model model;

    @Override
    public void start(Stage primaryStage) throws Exception{
        model = new Model();
        controller = new Controller(model, primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
