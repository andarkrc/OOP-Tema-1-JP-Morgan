package org.poo;

import org.poo.jsonobject.JsonObject;

public interface Transaction {
    void execute();
    void remember();

    private boolean verify() {
        return true;
    }
}
