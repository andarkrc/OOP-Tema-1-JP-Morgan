package org.poo;

import org.poo.jsonobject.JsonObject;

public class NormalCard extends Card{
    public NormalCard(String cardNumber) {
        super(cardNumber);
        type = "normal";
    }

    @Override
    public JsonObject acceptJsonObject(Visitor visitor) {
        return visitor.visit(this);
    }
}
