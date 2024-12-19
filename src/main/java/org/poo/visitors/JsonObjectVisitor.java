package org.poo.visitors;

import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.database.DatabaseEntry;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

/**
 * Visitor class used for the implementation of:
 * printUsers
 * printTransactions
 */
public final class JsonObjectVisitor implements Visitor {
    /**
     * Retrieves information from a transaction as a JsonObject.
     *
     * @param transaction
     * @return
     */
    public String visit(final DefaultTransaction transaction) {
        JsonObject output = new JsonObject();
        output.add("command", transaction.getCommandName());
        output.add("timestamp", transaction.getTimestamp());
        output.add("output", transaction.getResult());
        return output.finalizeData();
    }

    /**
     * Retrieves information from a database entry as a JsonObject.
     * @param dbEntry
     * @return
     */
    public String visit(final DatabaseEntry dbEntry) {
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

    /**
     * Retrieves information from an account as a JsonObject.
     *
     * @param account
     * @return
     */
    public String visit(final Account account) {
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

    /**
     * Retrieves information from a card as a JsonObject.
     *
     * @param card
     * @return
     */
    public String visit(final Card card) {
        JsonObject cardObj = new JsonObject();
        cardObj.add("cardNumber", card.getNumber());
        cardObj.add("status", card.getStatus());
        return cardObj.finalizeData();
    }
}
