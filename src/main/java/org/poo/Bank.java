package org.poo;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.UserInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

public class Bank implements Visitable{
    private static Bank instance = null;
    @Getter
    private Database database;
    @Setter
    @Getter
    private JsonArray output;

    private Bank() {

    }

    public JsonArray acceptJsonArray(Visitor visitor) {
        return visitor.visit(this);
    }

    public JsonObject acceptJsonObject(Visitor visitor) {
        return null;
    }

    public void addUser(UserInput input) {
        database.addUser(new User(input));
    }

    public DatabaseEntry getEntryOfEmail(String email) {
        return database.getEmailDatabase().get(email);
    }

    public DatabaseEntry getEntryOfIBAN(String IBAN) {
        return database.getIBANDatabase().get(IBAN);
    }

    public Account getAccount(String IBAN) {
        if (database.getIBANDatabase().get(IBAN) != null) {
            return database.getIBANDatabase().get(IBAN).getAccount(IBAN);
        }
        return null;
    }

    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    public void init() {
        database = new Database();
    }
}
