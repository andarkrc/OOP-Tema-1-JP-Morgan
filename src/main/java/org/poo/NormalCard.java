package org.poo;

import org.poo.jsonobject.JsonObject;

public class NormalCard extends Card{
    public NormalCard(String cardNumber) {
        super(cardNumber);
    }

    public JsonObject acceptJsonObject(Visitor visitor) {
        return visitor.visit(this);
    }
}
