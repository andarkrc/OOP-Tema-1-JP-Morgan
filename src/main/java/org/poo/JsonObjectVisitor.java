package org.poo;

import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

public class JsonObjectVisitor implements Visitor{
    public String visit(DefaultTransaction transaction) {
        JsonObject output = new JsonObject();
        output.add("command", transaction.getCommandName());
        output.add("timestamp", transaction.getTimestamp());
        output.add("output", transaction.getResult());
        return output.finalizeData();
    }

    public String visit(DatabaseEntry dbEntry) {
        JsonObject userObj = new JsonObject();
        userObj.add("firstName", dbEntry.getUser().getFirstName());
        userObj.add("lastName", dbEntry.getUser().getLastName());
        userObj.add("email", dbEntry.getUser().getEmail());
        JsonArray accounts = new JsonArray();
        for (Account account : dbEntry.getAccounts()) {
            accounts.addStringNoQuotes(account.accept(this));
        }
        userObj.add("accounts", accounts);
        return userObj.finalizeData();
    }

    public String visit(Account account) {
        JsonObject accountObj = new JsonObject();
        accountObj.add("IBAN", account.getIban());
        accountObj.add("balance", account.getBalance());
        accountObj.add("currency", account.getCurrency());
        accountObj.add("type", account.getAccountType());
        JsonArray cardsArray = new JsonArray();
        for (Card card : account.getCards()) {
            cardsArray.addStringNoQuotes(card.accept(this));
        }
        accountObj.add("cards", cardsArray);

        return accountObj.finalizeData();
    }

    public String visit(Card card) {
        JsonObject cardObj = new JsonObject();
        cardObj.add("cardNumber", card.getNumber());
        cardObj.add("status", card.getStatus());
        return cardObj.finalizeData();
    }
}
