package org.poo;

import lombok.Getter;
import lombok.Setter;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

@Setter
@Getter
public class DatabaseEntry implements Visitable {
    private User user;
    private ArrayList<Account> accounts;
    private ArrayList<Transaction> transactionHistory;
    private HashMap<String, Account> accountMap;

    public Account getAccount(String IBAN) {
        return accountMap.get(IBAN);
    }

    public JsonObject acceptJsonObject(Visitor visitor) {
        return visitor.visit(this);
    }

    public JsonArray acceptJsonArray(Visitor visitor) {
        return null;
    }

    public DatabaseEntry(User user) {
        this.user = user;
        accounts = new ArrayList<>();
        transactionHistory = new ArrayList<>();
        accountMap = new HashMap<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
        accountMap.put(account.getIBAN(), account);
    }

    public void removeAccount(String IBAN) {
        accounts.remove(accountMap.get(IBAN));
        accountMap.remove(IBAN);
    }

    public void addCard(String IBAN, Card card) {
        Account account = getAccount(IBAN);
        account.addCard(card);
        accountMap.put(card.getNumber(), account);
    }

    public void removeCard(String cardNumber) {
        Account account = getAccount(cardNumber);
        account.removeCard(cardNumber);
        accountMap.remove(cardNumber);

    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
}
