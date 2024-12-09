package org.poo;

import lombok.Getter;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;


@Getter
public abstract class Card implements Visitable{
    protected String number;
    protected String status;

    public Card(String number) {
        this.number = number;
        this.status = "active";
    }

    public Card setStatus(String status) {
        this.status = status;
        return this;
    }

    public Card setNumber(String number) {
        this.number = number;
        return this;
    }

    public JsonArray acceptJsonArray(Visitor visitor) {
        return null;
    }

    public JsonObject acceptJsonObject(Visitor visitor) {
        return null;
    }
}
