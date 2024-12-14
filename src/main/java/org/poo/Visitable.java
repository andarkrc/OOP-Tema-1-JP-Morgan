package org.poo;

import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

public interface Visitable {
    public String accept(Visitor visitor);
}
