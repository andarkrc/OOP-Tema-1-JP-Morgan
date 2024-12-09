package org.poo;

import org.poo.jsonobject.JsonObject;

public class SavingsAccount extends Account {
    @Override
    public JsonObject acceptJsonObject(Visitor visitor) {
        return visitor.visit(this);
    }
}
