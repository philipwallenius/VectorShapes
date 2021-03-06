package com.vectorshapes.view.event.factory;

import com.vectorshapes.controller.Controller;
import com.vectorshapes.view.View;
import com.vectorshapes.view.event.handlers.*;
import com.vectorshapes.view.menu.side.Tool;

/**
 *
 * This class manages creation of mouse event handlers
 *
 * Created by philipwallenius on 04/11/15.
 */
public class EventHandlerFactory {

    private final RectangleEventHandler rectangleEventHandler;
    private final ParallelogramEventHandler parallelogramEventHandler;
    private final EllipseEventHandler ellipseEventHandler;
    private final SelectEventHandler selectEventHandler;
    private final LineEventHandler lineEventHandler;

    public EventHandlerFactory(View view, Controller controller, MouseEventHandler mouseEventHandler) {
        rectangleEventHandler = new RectangleEventHandler(view, controller, mouseEventHandler);
        parallelogramEventHandler = new ParallelogramEventHandler(view, controller, mouseEventHandler);
        ellipseEventHandler = new EllipseEventHandler(view, controller, mouseEventHandler);
        selectEventHandler = new SelectEventHandler(view, mouseEventHandler);
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
