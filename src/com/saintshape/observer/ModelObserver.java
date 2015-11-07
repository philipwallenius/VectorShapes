package com.saintshape.observer;

import com.saintshape.model.Model;

/**
 *
 * Interface for model observers
 *
 * Created by 150019538 on 01/11/15.
 */
public interface ModelObserver {

    void update();
    void update(Model model);

}
