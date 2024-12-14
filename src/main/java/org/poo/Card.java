package org.poo;

import lombok.Getter;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;


@Getter
public abstract class Card implements Visitable{
    protected String number;
    protected String status;
    protected String type;

    public Card(String number) {
        this.number = number;
        this.status = "active";
    }

    public String accept(Visitor visitor) {
        return visitor.visit(this);
    }
}
