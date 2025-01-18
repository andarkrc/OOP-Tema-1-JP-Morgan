package org.poo.bank.database;

import lombok.Getter;
import org.poo.transactions.DefaultTransaction;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public final class Database {
    private List<DatabaseEntry> entries;
    private Map<String, DatabaseEntry> entriesMap;

    public Database() {
        entries = new ArrayList<>();
        entriesMap = new HashMap<>();
    }

    /**
     * Returns whether the database contains an entry at the specified key.
     *
     * @param key
     * @return
     */
    public boolean hasKey(final String key) {
        return entriesMap.containsKey(key);
    }

    /**
     * Adds the provided transaction to the transaction history of the
     * user with the specified email.
     *
     * @param email
     * @param transaction
     */
    public void addTransaction(final String email, final DefaultTransaction transaction) {
        entriesMap.get(email).addTransaction(transaction);
    }

    /**
     * Creates a new database entry for the provided user.
     *
     * @param user
     */
    public void addUser(final User user) {
        DatabaseEntry entry = new DatabaseEntry(user);
        entries.add(entry);
        entriesMap.put(user.getEmail(), entry);
    }

    /**
     * Adds the provided account to the user with the specified email.
     *
     * @param email
     * @param account
     */
    public void addAccount(final String email, final Account account) {
        DatabaseEntry entry = entriesMap.get(email);
        entry.addAccount(account);
        entriesMap.put(account.getIban(), entry);
    }

    /**
     * Removes the account with the specified IBAN.
     *
     * @param iban
     */
    public void removeAccount(final String iban) {
        DatabaseEntry entry = entriesMap.get(iban);

        while (!entry.getAccount(iban).getCards().isEmpty()) {
            removeCard(entry.getAccount(iban).getCards().getFirst().getNumber());
        }

        entry.removeAccount(iban);
        entriesMap.remove(iban);
    }

    /**
     * Adds the provided card to the account with the specified IBAN.
     *
     * @param iban
     * @param card
     */
    public void addCard(final String iban, final Card card) {
        DatabaseEntry entry = entriesMap.get(iban);
        entry.addCard(iban, card);
        entriesMap.put(card.getNumber(), entry);
    }

    /**
     * Removes the card with the specified number.
     *
     * @param cardNumber
     */
    public void removeCard(final String cardNumber) {
        DatabaseEntry entry = entriesMap.get(cardNumber);
        entry.removeCard(cardNumber);
        entriesMap.remove(cardNumber);
    }
}
