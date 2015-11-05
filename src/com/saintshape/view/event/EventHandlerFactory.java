package com.saintshape.view.event;

import com.saintshape.controller.Controller;
import com.saintshape.view.View;
import com.saintshape.view.menu.side.Tool;

/**
 * Created by 150019538 on 04/11/15.
 */
public class EventHandlerFactory {

    private RectangleEventHandler rectangleEventHandler;
    private EllipseEventHandler ellipseEventHandler;
    private SelectEventHandler selectEventHandler;
    private LineEventHandler lineEventHandler;

    public EventHandlerFactory(View view, Controller controller, MouseEventHandler mouseEventHandler) {
        rectangleEventHandler = new RectangleEventHandler(view, controller, mouseEventHandler);
        ellipseEventHandler = new EllipseEventHandler(view, controller, mouseEventHandler);
        selectEventHandler = new SelectEventHandler(view, controller, mouseEventHandler);
        lineEventHandler = new LineEventHandler(view, controller, mouseEventHandler);
    }

    public ToolEventHandler getEventHandler(Tool tool) {
        ToolEventHandler eventHandler;
        switch (tool) {
            case RECTANGLE : {
                eventHandler = rectangleEventHandler;
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
