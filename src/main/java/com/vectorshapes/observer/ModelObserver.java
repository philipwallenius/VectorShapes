package com.vectorshapes.observer;

import com.vectorshapes.model.Model;

/**
 *
 * Interface for model observers
 *
 * Created by philipwallenius on 01/11/15.
 */
public interface ModelObserver {

    void update();
    void update(Model model);

}
