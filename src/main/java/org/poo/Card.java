package org.poo;

import lombok.Getter;
import lombok.Setter;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;


@Getter
@Setter
public abstract class Card implements Visitable{
    protected String number;
    protected String status;

    public Card(String number) {
        this.number = number;
        this.status = "active";
    }

    public String accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public boolean isOneTime() {
        return false;
    }
}
