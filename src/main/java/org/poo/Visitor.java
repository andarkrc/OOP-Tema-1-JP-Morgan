package org.poo;

import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

public interface Visitor {
    public JsonArray visit(Bank bank);
    public JsonObject visit(DatabaseEntry user);
    public JsonObject visit(Account account);
    public JsonObject visit(Card card);
}
