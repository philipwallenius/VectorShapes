package com.vectorshapes.view.menu.top;

import com.vectorshapes.controller.Controller;
import com.vectorshapes.model.util.HistoryUtil;
import com.vectorshapes.view.View;
import com.vectorshapes.view.dialog.NewDialog;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;

import java.io.File;

/**
 *
 * Creates a TopMenu with menu items.
 *
 * Created by philipwallenius on 02/11/15.
 */
public class TopMenu extends MenuBar {

    private final View view;
    private final Controller controller;
    private Menu menuFile, menuEdit, menuHelp;
    private MenuItem menuFileItemNew, menuFileItemOpen, menuFileItemSave, menuFileItemSaveAs, menuFileItemQuit;
    private MenuItem menuEditUndo, menuEditRedo, menuEditImportImage;
    private MenuItem menuAbout;

    /**
     * Constructor that initializes the menus
     * @param controller to send commands to
     */
    public TopMenu(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
        initialize();
    }

    /**
     * Initializes the menus and their items
     */
    private void initialize() {
        menuFile = createFileMenu();
        menuEdit = createEditMenu();
        menuHelp = createHelpMenu();
        getMenus().addAll(menuFile, menuEdit, menuHelp);
        addEventHandlers();
    }

    /**
     * Creates the File menu and adds items to it
     * @return Returns a File menu
     */
    private Menu createFileMenu() {
        Menu menu = new Menu("File");
        menuFileItemNew = new MenuItem("New...");
        menuFileItemOpen = new MenuItem("Open...");
        menuFileItemSave = new MenuItem("Save");
        menuFileItemSaveAs = new MenuItem("Save As...");
        menuFileItemQuit = new MenuItem("Quit VectorShapes");

        // only enable buttons if project open
        menuFileItemSave.disableProperty().bind(controller.getWorkSpaceEmpty().or(controller.getModel().getFileProperty()));
        menuFileItemSaveAs.disableProperty().bind(controller.getWorkSpaceEmpty());

        menu.getItems().addAll(menuFileItemNew, menuFileItemOpen, menuFileItemSave, menuFileItemSaveAs, new SeparatorMenuItem(), menuFileItemQuit);
        return menu;
    }

    /**
     * Creates the Edit menu and adds items to it
     * @return Returns a Edit menu
     */
    private Menu createEditMenu() {
        Menu menu = new Menu("Edit");
        menuEditUndo = new MenuItem("Undo");
        menuEditRedo = new MenuItem("Redo");

        menuEditUndo.disableProperty().bind(HistoryUtil.getInstance().disableUndoProperty());
        menuEditRedo.disableProperty().bind(HistoryUtil.getInstance().disableRedoProperty());

        menuEditImportImage = new MenuItem("Import Image");
        menuEditImportImage.disableProperty().bind(controller.getWorkSpaceEmpty());
        menu.getItems().addAll(menuEditUndo, menuEditRedo, menuEditImportImage);
        return menu;
    }

    /**
     * Creates the Help menu and adds items to it
     * @return Returns a Help menu
     */
    private Menu createHelpMenu() {
        Menu menu = new Menu("Help");

        menuAbout = new MenuItem("About");
        menu.getItems().add(menuAbout);

        return menu;
    }

    /**
     * Handles menu item events
     */
    private void addEventHandlers() {

        // File menu

        menuFileItemNew.setOnAction(t -> new NewDialog(view, controller, controller.getPrimaryStage()));
        menuFileItemOpen.setOnAction(event -> openDialog());
        menuFileItemQuit.setOnAction(event -> controller.quit());
        menuFileItemSave.setOnAction(event -> view.save());
        menuFileItemSaveAs.setOnAction(event -> view.saveAsDialog());

        menuFileItemNew.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        menuFileItemOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        menuFileItemSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        menuFileItemSaveAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        menuFileItemQuit.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));

        // Edit menu
        menuEditUndo.setOnAction(event -> controller.undo());
        menuEditRedo.setOnAction(event -> controller.redo());
        menuEditUndo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        menuEditRedo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN, KeyCombination.ALT_DOWN));

        menuEditImportImage.setOnAction(event -> view.showImportImageDialog());

        // Help menu
        menuAbout.setOnAction(t -> new Alert(Alert.AlertType.INFORMATION, "VectorShapes by Philip Wallenius").showAndWait());

    }

    private void openDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open...");

        // filter file types
        FileChooser.ExtensionFilter extFilterSvg = new FileChooser.ExtensionFilter("SVG files (*.svg)", "*.svg", "*.SVG");
        fileChooser.getExtensionFilters().addAll(extFilterSvg);

        File file = fileChooser.showOpenDialog(controller.getPrimaryStage());

        if (file != null) {
            try {
                // import image to canvas
                controller.open(file);
            } catch(Exception exception) {
                exception.printStackTrace();
                view.showError("Error", "Error", "Failed to open image: " + file.getName());
            }
        }
    }

}
