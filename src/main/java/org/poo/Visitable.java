package org.poo;

import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

public interface Visitable {
    public JsonObject acceptJsonObject(Visitor visitor);
    public JsonArray acceptJsonArray(Visitor visitor);
}
