package org.poo;

import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

public interface Visitor {
    public String visit(DatabaseEntry dbEntry);
    public String visit(Account account);
    public String visit(Card card);
    public String visit(DefaultTransaction transaction);
}
