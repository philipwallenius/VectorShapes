package com.saintshape.controller;

import com.saintshape.model.Model;
import com.saintshape.view.View;
import javafx.stage.Stage;

public class Controller {

    private Model model;
    private View view;
    private Stage primaryStage;

    public Controller(Model model, Stage primaryStage) {
        this.model = model;
        view = new View(this, model, primaryStage);
        this.primaryStage = primaryStage;
    }

}
