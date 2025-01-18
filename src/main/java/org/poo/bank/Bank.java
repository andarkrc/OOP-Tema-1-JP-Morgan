package org.poo.bank;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.commerciants.Commerciant;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.database.User;
import org.poo.fileio.CommerciantInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.UserInput;
import org.poo.jsonobject.JsonArray;
import org.poo.transactions.DefaultTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Bank {
    private static Bank instance = null;
    @Getter
    private Database database;
    @Setter
    @Getter
    private JsonArray output;
    private CurrencyExchange exchange;
    private Map<String, Commerciant> commerciants;

    private Bank() {

    }

    /**
     * Returns the exchange rate from currency1 to currency2.
     *
     * @param from      currency1
     * @param to        currency2
     * @return          exchange rate
     */
    public double getExchangeRate(final String from, final String to) {
        return exchange.getRate(from, to);
    }

    /**
     * Checks if the database contains the provided key.
     *
     * @param key
     * @return
     */
    public boolean databaseHas(final String key) {
        return database.hasKey(key);
    }

    /**
     * Checks if the database contains the provided key, or
     * if the provided key is an alias set by the user with the provided email.
     *
     * @param key
     * @param email
     * @return
     */
    public boolean databaseHas(final String key, final String email) {
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

    /**
     * Adds a new user to the database based on the provided UserInput object.
     *
     * @param input
     */
    public void addUser(final UserInput input) {
        database.addUser(new User(input));
    }

    /**
     * Adds the provided account to the user with the specified email.
     *
     * @param email
     * @param account
     */
    public void addAccount(final String email, final Account account) {
        database.addAccount(email, account);
    }

    /**
     * Removes the account with the specified IBAN.
     *
     * @param iban
     */
    public void removeAccount(final String iban) {
        database.removeAccount(iban);
    }

    /**
     * Adds the provided card to the account with the specified IBAN.
     *
     * @param iban
     * @param card
     */
    public void addCard(final String iban, final Card card) {
        database.addCard(iban, card);
    }

    /**
     * Removes the card with the specified number.
     *
     * @param cardNumber
     */
    public void removeCard(final String cardNumber) {
        database.removeCard(cardNumber);
    }

    /**
     * Adds the provided transaction to the transaction history of the
     * user with the specified email.
     *
     * @param email
     * @param transaction
     */
    public void addTransaction(final String email, final DefaultTransaction transaction) {
        database.addTransaction(email, transaction);
    }

    /**
     * Returns the database entry of the user with the specified email.
     *
     * @param email
     * @return
     */
    public DatabaseEntry getEntryWithEmail(final String email) {
        return database.getEntriesMap().get(email);
    }

    /**
     * Returns the database entry of the user that owns the specified account.
     *
     * @param iban
     * @return
     */
    public DatabaseEntry getEntryWithIBAN(final String iban) {
        return database.getEntriesMap().get(iban);
    }

    /**
     * Returns the database entry of the user with the specified account.
     * If there is none, it will search the aliases of the provided user.
     *
     * @param iban
     * @param email
     * @return
     */
    public DatabaseEntry getEntryWithIBAN(final String iban, final String email) {
        if (!database.getEntriesMap().containsKey(iban)) {
            DatabaseEntry entry = getEntryWithEmail(email);
            if (entry != null) {
                if (entry.hasAlias(iban)) {
                    return getEntryWithIBAN(entry.getAlias(iban));
                }
            }
            return null;
        }
        return database.getEntriesMap().get(iban);
    }

    /**
     * Returns the database entry of the user that owns the specified card.
     *
     * @param cardNumber
     * @return
     */
    public DatabaseEntry getEntryWithCard(final String cardNumber) {
        return database.getEntriesMap().get(cardNumber);
    }

    /**
     * Returns the account that has the specified IBAN.
     *
     * @param iban
     * @return
     */
    public Account getAccountWithIBAN(final String iban) {
        if (database.getEntriesMap().containsKey(iban)) {
            return database.getEntriesMap().get(iban).getAccount(iban);
        }
        return null;
    }

    /**
     * Verify if there is an account with the provided IBAN, else
     * search for an account with the alias IBAN from the user with
     * the provided email.
     *
     * @param iban
     * @param email
     * @return
     */
    public Account getAccountWithIBAN(final String iban, final String email) {
        if (!database.getEntriesMap().containsKey(iban)) {
            DatabaseEntry entry = getEntryWithEmail(email);
            if (entry != null) {
                if (entry.hasAlias(iban)) {
                    return getAccountWithIBAN(entry.getAlias(iban));
                }
            }
            return null;
        }

        return database.getEntriesMap().get(iban).getAccount(iban);
    }

    /**
     * Returns the account that has the specified card.
     *
     * @param cardNumber
     * @return
     */
    public Account getAccountWithCard(final String cardNumber) {
        if (database.getEntriesMap().containsKey(cardNumber)) {
            return database.getEntriesMap().get(cardNumber).getAccountMap().get(cardNumber);
        }
        return null;
    }

    /**
     * Returns the card with the specified number.
     *
     * @param cardNumber
     * @return
     */
    public Card getCard(final String cardNumber) {
        if (databaseHas(cardNumber)) {
            return getAccountWithCard(cardNumber).getCardsMap().get(cardNumber);
        }
        return null;
    }

    public Commerciant getCommerciant(String commerciant) {
        return commerciants.get(commerciant);
    }

    public boolean commerciantExists(String account) {
        return commerciants.containsKey(account);
    }

    public Map<String, Double> getUpgradeMap() {
        Map<String, Double> map = new HashMap<>();
        map.put("student-silver", 100.0);
        map.put("standard-silver", 100.0);
        map.put("silver-gold", 250.0);
        map.put("student-gold", 350.0);
        map.put("standard-gold", 350.0);
        return map;
    }

    public double getTotalPrice(double price, String currency, String email) {
        String plan = getEntryWithEmail(email).getPlan();
        double local_price = price * exchange.getRate(currency, "RON");
        switch (plan) {
            case "student", "gold" -> {
                return price;
            }
            case "silver" -> {
                return (local_price < 500) ? price : price + price * 0.001;
            }
            default -> {
                return price + price * 0.002;
            }
        }
    }

    private Map<String, Double> getCashbacksMap() {
        Map<String, Double> cashbacks = new HashMap<>();
        cashbacks.put("100student", 0.001);
        cashbacks.put("100standard", 0.001);
        cashbacks.put("100silver", 0.003);
        cashbacks.put("100gold", 0.005);

        cashbacks.put("300student", 0.002);
        cashbacks.put("300standard", 0.002);
        cashbacks.put("300silver", 0.004);
        cashbacks.put("300gold", 0.0055);

        cashbacks.put("500student", 0.0025);
        cashbacks.put("500standard", 0.0025);
        cashbacks.put("500silver", 0.005);
        cashbacks.put("500gold", 0.007);
        return cashbacks;
    }

    private double spendingThresholdCashback(double price, String currency, String account, String commerciant) {
        List<DefaultTransaction> transactions =
                getEntryWithIBAN(account).getTransactionHistory().stream()
                        .filter(e -> e.getAccount().equals(account)).toList();
        double totalSum = 0.0;
        String accountPlan = getEntryWithIBAN(account).getPlan();
        String commerciantType = commerciants.get(commerciant).getType();
        for (DefaultTransaction transaction : transactions) {
            String comm = transaction.getDetails().getStringOfField("commerciant");
            if (!comm.isEmpty()) {
                if (commerciants.get(comm).getType().equals(commerciantType)) {
                    String curr = getAccountWithIBAN(account).getCurrency();
                    double amount = transaction.getDetails().getDoubleOfField("amount");
                    totalSum += amount * exchange.getRate(curr, "RON");
                }
            } else {
                String rec = transaction.getDetails().getStringOfField("receiver");
                if (!rec.isEmpty()) {
                    if (commerciants.containsKey(rec)) {
                        if (commerciants.get(rec).getType().equals(commerciantType)) {
                        double amount = transaction.getDetails().getDoubleOfField("amount");
                        totalSum += amount;
                        }
                    }
                }
            }
        }

        Map<String, Double> cashbacks = getCashbacksMap();
        double accountPrice = price * exchange.getRate(currency, getAccountWithIBAN(account).getCurrency());

        if (totalSum >= 500) {
            return accountPrice * cashbacks.get("500" + accountPlan);
        }

        if (totalSum >= 300) {
            return accountPrice * cashbacks.get("300" + accountPlan);
        }

        if (totalSum >= 100) {
            return accountPrice * cashbacks.get("100" + accountPlan);
        }

        return 0.0;
    }

    public double getCashBack(double price, String currency, String account, String commerciant) {
        switch (commerciants.get(commerciant).getCashbackStrategy()) {
            case "spendingThreshold" -> {
                return spendingThresholdCashback(price, currency, account, commerciant);
            }

            default -> {
                return 0.0;
            }
        }
    }

    /**
     * Returns the instance of the bank.
     * If there is no instance, it will create a new instance and return it.
     *
     * @return
     */
    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    /**
     * Clears the bank's database and initializes new exchange rates.
     * To be used with caution.
     *
     * @param rates
     */
    public void init(final ExchangeInput[] rates, final CommerciantInput[] commerciantInput) {
        database = new Database();
        commerciants = new HashMap<>();
        exchange = CurrencyExchange.getInstance();
        exchange.init(rates);
        for (CommerciantInput commerciant : commerciantInput) {
            Commerciant com = new Commerciant(commerciant);
            commerciants.put(com.getAccount(), com);
            commerciants.put(com.getName(), com);
        }
    }
}
