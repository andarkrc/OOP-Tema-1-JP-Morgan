package org.poo;

import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.UserInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

public class Bank {
    private static Bank instance = null;
    @Getter
    private Database database;
    @Setter
    @Getter
    private JsonArray output;
    private CurrencyExchange exchange;

    private Bank() {

    }

    public double getExchangeRate(String from, String to) {
        return exchange.getRate(from, to);
    }

    public boolean databaseHas(String key) {
        return database.hasKey(key);
    }

    public boolean databaseHas(String key, String email) {
        if (!database.hasKey(key)) {
            DatabaseEntry entry = getEntryWithEmail(email);
            if (entry != null) {
                if (entry.hasAlias(key)) {
                    return databaseHas(entry.getAlias(key));
                }
            }
            return false;
        }
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

    public void addTransaction(String email, DefaultTransaction transaction) {
        database.addTransaction(email, transaction);
    }

    public DatabaseEntry getEntryWithEmail(String email) {
        return database.getEntriesMap().get(email);
    }

    public DatabaseEntry getEntryWithIBAN(String IBAN) {
        return database.getEntriesMap().get(IBAN);
    }

    /**
     *
     * Verify if an IBAN exists in the database, or the provided email has
     * an alias set as the IBAN (if the IBAN is an alias from that account)
     * @param IBAN
     * @param email
     * @return
     */
    public DatabaseEntry getEntryWithIBAN(String IBAN, String email) {
        if (!database.getEntriesMap().containsKey(IBAN)) {
            DatabaseEntry entry = getEntryWithEmail(email);
            if (entry != null) {
                if (entry.hasAlias(IBAN)) {
                    return getEntryWithIBAN(entry.getAlias(IBAN));
                }
            }
            return null;
        }
        return database.getEntriesMap().get(IBAN);
    }

    public DatabaseEntry getEntryWithCard(String cardNumber) {
        return database.getEntriesMap().get(cardNumber);
    }

    public Account getAccountWithIBAN(String IBAN) {
        if (database.getEntriesMap().containsKey(IBAN)) {
            return database.getEntriesMap().get(IBAN).getAccount(IBAN);
        }
        return null;
    }

    /**
     * Verify if there is an account with the provided IBAN, else
     * search for an account with the alias IBAN from the user with
     * the provided email.
     *
     * @param IBAN
     * @param email
     * @return
     */
    public Account getAccountWithIBAN(String IBAN, String email) {
        if (!database.getEntriesMap().containsKey(IBAN)) {
            DatabaseEntry entry = getEntryWithEmail(email);
            if (entry != null) {
                if (entry.hasAlias(IBAN)) {
                    return getAccountWithIBAN(entry.getAlias(IBAN));
                }
            }
            return null;
        }

        return database.getEntriesMap().get(IBAN).getAccount(IBAN);
    }

    public Account getAccountWithCard(String cardNumber) {
        if (database.getEntriesMap().containsKey(cardNumber)) {
            return database.getEntriesMap().get(cardNumber).getAccountMap().get(cardNumber);
        }
        return null;
    }

    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    public void init(ExchangeInput[] input) {
        database = new Database();
        exchange = CurrencyExchange.getInstance();
        exchange.init(input);
    }
}
