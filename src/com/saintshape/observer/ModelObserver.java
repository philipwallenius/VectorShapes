package com.saintshape.observer;

import com.saintshape.model.Model;

/**
 * Created by philipwallenius on 01/11/15.
 */
public interface ModelObserver {

    void update();
    void update(Model model);

}
