package com.saintshape.view.event;

import javafx.scene.input.MouseEvent;

/**
 * Created by 150019538 on 04/11/15.
 */
public interface ToolEventHandler {

    void handleMousePress(MouseEvent event);
    void handleMouseMove(MouseEvent event);
    void handleMouseDrag(MouseEvent event);
    void handleMouseRelease(MouseEvent event);

}
