package com.saintshape.view;

import com.saintshape.controller.Controller;
import com.saintshape.model.Model;
import javafx.stage.Stage;

/**
 * Created by philipwallenius on 01/11/15.
 */
public class View {

    private Controller controller;
    private Model model;
    private Stage primaryStage;

    public View(Controller controller, Model model, Stage primaryStage) {
        this.controller = controller;
        this.model = model;
        this.primaryStage = primaryStage;
    }

}
