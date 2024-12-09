package org.poo;

import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;


public class BankVisitor implements Visitor {
    public JsonObject visit(DatabaseEntry user) {
        JsonObject userObj = new JsonObject();
        userObj.add("firstName", user.getUser().getFirstName());
        userObj.add("lastName", user.getUser().getLastName());
        userObj.add("email", user.getUser().getEmail());
        JsonArray accounts = new JsonArray();
        for (Account account : user.getAccounts()) {
            accounts.add(account.acceptJsonObject(this));
        }
        userObj.add("accounts", accounts);
        return userObj;
    }

    public JsonObject visit(Account account) {
        JsonObject accountObj = new JsonObject();
        accountObj.add("IBAN", account.getIBAN());
        accountObj.add("balance", account.getBalance());
        accountObj.add("currency", account.getCurrency());
        accountObj.add("type", account.getAccountType());
        JsonArray cardsArray = new JsonArray();
        for (Card card : account.getCards()) {
            cardsArray.add(card.acceptJsonObject(this));
        }
        accountObj.add("cards", cardsArray);

        return accountObj;
    }

    public JsonObject visit(Card card) {
        JsonObject cardObj = new JsonObject();
        cardObj.add("cardNumber", card.getNumber());
        cardObj.add("status", card.getStatus());

        return cardObj;
    }

    public JsonArray visit(Bank bank) {
        JsonArray usersArray = new JsonArray();
        for (DatabaseEntry entry : bank.getDatabase().getEntries()) {
            usersArray.add(entry.acceptJsonObject(this));
        }
        return usersArray;
    }
}
