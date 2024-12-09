package org.poo;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
public class Database {
    private ArrayList<DatabaseEntry> entries;
    private HashMap<String, DatabaseEntry> entriesMap;

    public Database() {
        entries = new ArrayList<>();
        entriesMap = new HashMap<>();
    }

    public boolean hasKey(String key) {
        return entriesMap.containsKey(key);
    }

    public void addTransaction(String email, Transaction transaction) {
        entriesMap.get(email).addTransaction(transaction);
    }

    public void addUser(User user) {
        DatabaseEntry entry = new DatabaseEntry(user);
        entries.add(entry);
        entriesMap.put(user.getEmail(), entry);
    }

    public void addAccount(String email, Account account) {
        DatabaseEntry entry = entriesMap.get(email);
        entry.addAccount(account);
        entriesMap.put(account.getIBAN(), entry);
    }

    public void removeAccount(String IBAN) {
        DatabaseEntry entry = entriesMap.get(IBAN);

        while (!entry.getAccount(IBAN).getCards().isEmpty()) {
            removeCard(entry.getAccount(IBAN).getCards().getFirst().getNumber());
        }

        entry.removeAccount(IBAN);
        entriesMap.remove(IBAN);
    }

    public void addCard(String IBAN, Card card) {
        DatabaseEntry entry = entriesMap.get(IBAN);
        entry.addCard(IBAN, card);
        entriesMap.put(card.getNumber(), entry);
    }

    public void removeCard(String cardNumber) {
        DatabaseEntry entry = entriesMap.get(cardNumber);
        entry.removeCard(cardNumber);
        entriesMap.remove(cardNumber);
    }
}
