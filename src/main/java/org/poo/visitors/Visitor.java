package org.poo.visitors;

import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.database.DatabaseEntry;
import org.poo.transactions.DefaultTransaction;

/**
 * Visitor class capable of retrieving data from various other classes.
 */
public interface Visitor {
    /**
     * Retrieves information from a database entry.
     * @param dbEntry
     * @return
     */
    String visit(DatabaseEntry dbEntry);

    /**
     * Retrieves information from an account.
     *
     * @param account
     * @return
     */
    String visit(Account account);

    /**
     * Retrieves information from a card.
     *
     * @param card
     * @return
     */
    String visit(Card card);

    /**
     * Retrieves information from a transaction.
     *
     * @param transaction
     * @return
     */
    String visit(DefaultTransaction transaction);
}
