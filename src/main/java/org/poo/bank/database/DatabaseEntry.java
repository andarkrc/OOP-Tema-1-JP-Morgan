package org.poo.bank.database;

import lombok.Getter;
import lombok.Setter;
import org.poo.transactions.DefaultTransaction;
import org.poo.visitors.Visitable;
import org.poo.visitors.Visitor;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public final class DatabaseEntry implements Visitable {
    private User user;
    private List<Account> accounts;
    private List<DefaultTransaction> transactionHistory;
    private Map<String, Account> accountMap;
    private Map<String, String> aliases;

    public DatabaseEntry(final User user) {
        this.user = user;
        accounts = new ArrayList<>();
        transactionHistory = new ArrayList<>();
        accountMap = new HashMap<>();
        aliases = new HashMap<>();
    }

    /**
     * Returns the account that has the specified IBAN.
     *
     * @param iban
     * @return
     */
    public Account getAccount(final String iban) {
        return accountMap.get(iban);
    }

    /**
     * Accepts a visitor and returns the data collected by it.
     * @param visitor
     * @return
     */
    public String accept(final Visitor visitor) {
        return visitor.visit(this);
    }

    /**
     * Creates a new alias for the provided account.
     * If the alias already existed, it gets replaced.
     *
     * @param account       iban of the account
     * @param alias         new alias
     */
    public void setAlias(final String account, final String alias) {
        aliases.put(alias, account);
    }

    /**
     * Removes the specified alias.
     *
     * @param alias
     */
    public void removeAlias(final String alias) {
        aliases.remove(alias);
    }

    /**
     * Checks if the provided alias was previously created.
     *
     * @param alias
     * @return
     */
    public boolean hasAlias(final String alias) {
        return aliases.containsKey(alias);
    }

    /**
     * Returns the IBAN of the account associated with the provided alias.
     *
     * @param alias
     * @return
     */
    public String getAlias(final String alias) {
        return aliases.get(alias);
    }

    /**
     * Adds the provided account to the account list.
     *
     * @param account
     */
    public void addAccount(final Account account) {
        accounts.add(account);
        accountMap.put(account.getIban(), account);
    }

    /**
     * Removes the account with the provided iban from the account list.
     *
     * @param iban
     */
    public void removeAccount(final String iban) {
        accounts.remove(accountMap.get(iban));
        accountMap.remove(iban);
    }

    /**
     * Adds the provided card to the account with the specified IBAN.
     *
     * @param iban
     * @param card
     */
    public void addCard(final String iban, final Card card) {
        Account account = getAccount(iban);
        account.addCard(card);
        accountMap.put(card.getNumber(), account);
    }

    /**
     * Removes the card with the specified number.
     *
     * @param cardNumber
     */
    public void removeCard(final String cardNumber) {
        Account account = getAccount(cardNumber);
        account.removeCard(cardNumber);
        accountMap.remove(cardNumber);

    }

    /**
     * Adds the provided transaction to the transaction history list.
     *
     * @param transaction
     */
    public void addTransaction(final DefaultTransaction transaction) {
        transactionHistory.add(transaction);
    }
}
