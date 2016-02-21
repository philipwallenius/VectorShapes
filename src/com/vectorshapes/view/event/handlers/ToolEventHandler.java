package com.vectorshapes.view.event.handlers;

import javafx.scene.input.MouseEvent;

/**
 *
 * Interface contract of mouse events handlers
 *
 * Created by 150019538 on 04/11/15.
 */
public interface ToolEventHandler {

    void handleMousePress(MouseEvent event);
    void handleMouseMove(MouseEvent event);
    void handleMouseDrag(MouseEvent event);
    void handleMouseRelease(MouseEvent event);

}
