package org.poo;

import org.poo.jsonobject.JsonObject;

public class OneTimeCard extends Card {
    OneTimeCard(String number) {
        super(number);
    }

    public boolean isOneTime() {
        return true;
    }
}
