package com.saintshape.view.event.factory;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.event.handlers.*;
import com.saintshape.view.menu.side.Tool;

/**
 *
 * This class manages creation of mouse event handlers
 *
 * Created by 150019538 on 04/11/15.
 */
public class EventHandlerFactory {

    private RectangleEventHandler rectangleEventHandler;
    private ParallelogramEventHandler parallelogramEventHandler;
    private EllipseEventHandler ellipseEventHandler;
    private SelectEventHandler selectEventHandler;
    private LineEventHandler lineEventHandler;

    public EventHandlerFactory(View view, Controller controller, MouseEventHandler mouseEventHandler) {
        rectangleEventHandler = new RectangleEventHandler(view, controller, mouseEventHandler);
        parallelogramEventHandler = new ParallelogramEventHandler(view, controller, mouseEventHandler);
        ellipseEventHandler = new EllipseEventHandler(view, controller, mouseEventHandler);
        selectEventHandler = new SelectEventHandler(view, controller, mouseEventHandler);
        lineEventHandler = new LineEventHandler(view, controller, mouseEventHandler);
    }

    /**
     * Returns appropriate event handler based on the tool passed to it
     * @param tool to get mouse event handler for
     * @return Returns an event handler appropriate for the tool
     */
    public ToolEventHandler getEventHandler(Tool tool) {
        ToolEventHandler eventHandler;
        switch (tool) {
            case RECTANGLE : {
                eventHandler = rectangleEventHandler;
                break;
            }
            case PARALLELOGRAM: {
                eventHandler = parallelogramEventHandler;
                break;
            }
            case ELLIPSE : {
                eventHandler = ellipseEventHandler;
                break;
            }
            case SELECT : {
                eventHandler = selectEventHandler;
                break;
            }
            case LINE : {
                eventHandler = lineEventHandler;
                break;
            }
            default : {
                throw new RuntimeException(String.format("Invalid tool: %s", tool));
            }
        }
        return eventHandler;
    }

}
