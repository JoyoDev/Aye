package com.joyodev.aye.activescanner;

public interface Client {

    /**
     * Check if Anchore Engine can be reached
     * @return true if Anchore can be reached, else false
     */
    boolean checkEngineHealth();
}
