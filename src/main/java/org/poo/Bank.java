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

    public boolean databaseHas(String key) {
        return database.hasKey(key);
    }

    public void addUser(UserInput input) {
        database.addUser(new User(input));
    }

    public void addAccount(String email, Account account) {
        database.addAccount(email, account);
    }

    public void removeAccount(String IBAN) {
        database.removeAccount(IBAN);
    }

    public void addCard(String IBAN, Card card) {
        database.addCard(IBAN, card);
    }

    public void removeCard(String cardNumber) {
        database.removeCard(cardNumber);
    }

    public void addTransaction(String email, Transaction transaction) {
        database.addTransaction(email, transaction);
    }

    public DatabaseEntry getEntryWithEmail(String email) {
        return database.getEntriesMap().get(email);
    }

    public DatabaseEntry getEntryWithIBAN(String IBAN) {
        return database.getEntriesMap().get(IBAN);
    }

    public DatabaseEntry getEntryWithCard(String cardNumber) {
        return database.getEntriesMap().get(cardNumber);
    }

    public Account getAccount(String IBAN) {
        if (database.getEntriesMap().containsKey(IBAN)) {
            return database.getEntriesMap().get(IBAN).getAccount(IBAN);
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
