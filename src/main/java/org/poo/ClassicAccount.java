package org.poo;


import org.poo.jsonobject.JsonObject;

public class ClassicAccount extends Account {
    public ClassicAccount() {
        super();
    }

    @Override
    public JsonObject acceptJsonObject(Visitor visitor) {
        return visitor.visit(this);
    }
}
