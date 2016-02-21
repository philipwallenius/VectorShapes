package com.vectorshapes.controller.util;

import com.vectorshapes.model.Model;

import java.io.File;

/**
 *
 * Interface for saving and opening different file formats
 *
 * Created by 150019538 on 19/11/15.
 */
public interface OpenSaveInterface {

    Model importFile(File file) throws Exception;
    void exportFile(Model model) throws Exception;

}
