package com.saintshape.view.menu.top;

import com.saintshape.controller.Controller;
import com.saintshape.view.dialog.NewDialog;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 *
 * Creates a TopMenu with menu items.
 *
 * Created by 150019538 on 02/11/15.
 */
public class TopMenu extends MenuBar {

    private Controller controller;
    private Menu menuFile, menuEdit;
    private MenuItem menuFileItemNew, menuFileItemSave, menuFileItemSaveAs, menuFileItemQuit;
    private MenuItem menuEditUndo, menuEditRedo;

    /**
     * Constructor that initializes the menus
     * @param controller to send commands to
     */
    public TopMenu(Controller controller) {
        this.controller = controller;
        initialize();
    }

    /**
     * Initializes the menus and their items
     */
    private void initialize() {
        menuFile = createFileMenu();
        menuEdit = createEditMenu();
        getMenus().addAll(menuFile, menuEdit);
        addEventHandlers();
    }

    /**
     * Creates the File menu and adds items to it
     * @return Returns a File menu
     */
    private Menu createFileMenu() {
        Menu menu = new Menu("File");
        menuFileItemNew = new MenuItem("New...");
        menuFileItemSave = new MenuItem("Save");
        menuFileItemSaveAs = new MenuItem("Save As...");
        menuFileItemQuit = new MenuItem("Quit Saintshape");
        menu.getItems().addAll(menuFileItemNew, menuFileItemSave, menuFileItemSaveAs, new SeparatorMenuItem(), menuFileItemQuit);
        return menu;
    }

    /**
     * Creates the Edit menu and adds items to it
     * @return Returns am Edit menu
     */
    private Menu createEditMenu() {
        Menu menu = new Menu("Edit");
        menuEditUndo = new MenuItem("Undo");
        menuEditRedo = new MenuItem("Redo");
        menu.getItems().addAll(menuEditUndo, menuEditRedo);
        return menu;
    }

    /**
     * Handles menu item events
     */
    private void addEventHandlers() {

        // File menu

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

        menuFileItemNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        menuFileItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        menuFileItemSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        menuFileItemQuit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

        // Edit menu
        menuEditUndo.setOnAction(event -> controller.undo());
        menuEditRedo.setOnAction(event -> controller.redo());
        menuEditUndo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.META_DOWN));
        menuEditRedo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN));
    }

}
