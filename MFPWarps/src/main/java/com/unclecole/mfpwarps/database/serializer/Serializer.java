package com.unclecole.mfpwarps.database.serializer;

import com.unclecole.mfpwarps.MFPWarps;

public class Serializer {


    /**
     * Saves your class to a .json file.
     */
    public void save(Object instance) {
        MFPWarps.getPersist().save(instance);
    }

    /**
     * Loads your class from a json file
     *
   */
    public <T> T load(T def, Class<T> clazz, String name) {
        return MFPWarps.getPersist().loadOrSaveDefault(def, clazz, name);
    }



}
