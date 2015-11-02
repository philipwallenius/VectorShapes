package com.saintshape.view;

import com.saintshape.controller.Controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * Created by 150019538 on 02/11/15.
 */
public class TopMenu extends MenuBar {

    private Controller controller;
    private Menu menuFile;
    private MenuItem menuFileItemNew, menuFileItemSave, menuFileItemSaveAs, menuFileItemQuit;

    public TopMenu(Controller controller) {
        this.controller = controller;
        initialize();
    }

    public void initialize() {
        menuFile = createFileMenu();
        getMenus().addAll(menuFile);
        addEventHandlers();
    }

    public Menu createFileMenu() {
        Menu menu = new Menu("File");
        menuFileItemNew = new MenuItem("New...");
        menuFileItemSave = new MenuItem("Save");
        menuFileItemSaveAs = new MenuItem("Save As...");
        menuFileItemQuit = new MenuItem("Quit Saintshape");
        menu.getItems().addAll(menuFileItemNew, menuFileItemSave, menuFileItemSaveAs, menuFileItemQuit);
        return menu;
    }

    private void addEventHandlers() {
        menuFileItemNew.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                new NewDialog(controller, controller.getPrimaryStage());
            }
        });

        menuFileItemQuit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
    }

}
