package org.poo.bank;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.accounts.Account;
import org.poo.bank.cards.Card;
import org.poo.bank.commerciants.Commerciant;
import org.poo.bank.commerciants.DiscountCoupon;
import org.poo.bank.database.Database;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.database.User;
import org.poo.fileio.CommerciantInput;
import org.poo.fileio.ExchangeInput;
import org.poo.fileio.UserInput;
import org.poo.jsonobject.JsonArray;
import org.poo.transactions.DefaultTransaction;
import org.poo.utils.Constants;

import java.util.ArrayList;
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
    @Getter
    private SplitPaymentContainer splitPayments;
    private List<String> validUsers;

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
     * Checks if the provided email is of a valid user.
     *
     * @param email     email to check
     * @return
     */
    public boolean isValidUser(final String email) {
        return validUsers.contains(email);
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
        validUsers.add(input.getEmail());
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

    /**
     * Returns the commerciant with name or iban commerciant.
     *
     * @param commerciant
     * @return
     */
    public Commerciant getCommerciant(final String commerciant) {
        return commerciants.get(commerciant);
    }

    /**
     * Verifies if a specified commerciant exists.
     *
     * @param account
     * @return
     */
    public boolean commerciantExists(final String account) {
        return commerciants.containsKey(account);
    }

    /**
     * Returns whether the specified user should auto update its plan
     * after the specified purchase.
     *
     * @param email
     * @param amount
     * @param currency
     * @return
     */
    public boolean updateUserPlan(final String email, final double amount, final String currency) {
        DatabaseEntry entry = getEntryWithEmail(email);
        String plan = entry.getPlan();
        if (!plan.equals("silver")) {
            return false;
        }
        if (getExchangeRate(currency, "RON") * amount >= Constants.SILVER_PAYMENT) {
            entry.setSilverPayments(entry.getSilverPayments() + 1);
        }
        if (entry.getSilverPayments() == Constants.SILVER_PAYMENTS_REQUIRED) {
            return true;
        }
        return false;
    }

    /**
     * Generates the amount of purchases necessary to create a type of
     * discount coupon.
     *
     * @return
     */
    private Map<Integer, String> numberToCouponTypeMap() {
        Map<Integer, String> couponTypeMap = new HashMap<>();
        couponTypeMap.put(Constants.FOOD_TRANSACTIONS, "Food");
        couponTypeMap.put(Constants.CLOTHES_TRANSACTIONS, "Clothes");
        couponTypeMap.put(Constants.TECH_TRANSACTIONS, "Tech");

        return couponTypeMap;
    }

    /**
     * Checks and adds a coupon to the specified account from the specified commerciant.
     *
     * @param account
     * @param commerciant
     */
    public void receiveCoupon(final String account, final String commerciant) {
        Account acc = getAccountWithIBAN(account);
        Commerciant com = getCommerciant(commerciant);
        if (!com.getCashbackStrategy().equals("nrOfTransactions")) {
            return;
        }
        DatabaseEntry entry = getEntryWithIBAN(account);
        List<DefaultTransaction> transactions = entry.getTransactionHistory().stream()
                .filter(e -> e.getAccount().equals(account))
                .filter(e -> !e.getDetails().getStringOfField("description")
                        .equals("Insufficient funds"))
                .filter(e -> getCommerciant(e.getDetails().getStringOfField("commerciant")) == com
                        || getCommerciant(e.getDetails().getStringOfField("receiverIBAN")) == com)
                .toList();



        Map<Integer, String> map = numberToCouponTypeMap();
        if (map.get(transactions.size()) == null) {
            return;
        }

        for (DiscountCoupon coupon : acc.getAvailableCoupons()) {
            if (coupon.getType().equals(map.get(transactions.size()))) {
                acc.getUsableCoupons().add(coupon);
                acc.getAvailableCoupons().remove(coupon);
                break;
            }
        }
    }

    /**
     * Returns the prices to upgrade from and to different plans.
     *
     * @return
     */
    public Map<String, Double> getUpgradeMap() {
        Map<String, Double> map = new HashMap<>();
        map.put("student-silver", Constants.BASIC_SILVER);
        map.put("standard-silver", Constants.BASIC_SILVER);
        map.put("silver-gold", Constants.SILVER_GOLD);
        map.put("student-gold", Constants.BASIC_GOLD);
        map.put("standard-gold", Constants.BASIC_GOLD);
        return map;
    }

    /**
     * Applies the corresponding commission to the specified purchase.
     *
     * @param price
     * @param currency
     * @param account
     * @return
     */
    public double getTotalPrice(final double price, final String currency, final String account) {
        String email = getEntryWithIBAN(account).getUser().getEmail();
        String plan = getEntryWithEmail(email).getPlan();
        double localPrice = price * exchange.getRate(currency, "RON");
        switch (plan) {
            case "student", "gold" -> {
                return price;
            }
            case "silver" -> {
                return (localPrice < Constants.SILVER_COMMISSION) ? price
                        : price + price * Constants.COMMISSION_01;
            }
            default -> {
                return price + price * Constants.COMMISSION_02;
            }
        }
    }

    /**
     * Returns the map of different cashbacks depending on threshold and plan.
     *
     * @return
     */
    private Map<String, Double> getCashbacksMap() {
        Map<String, Double> cashbacks = new HashMap<>();
        cashbacks.put("100student", Constants.COMMISSION_01);
        cashbacks.put("100standard", Constants.COMMISSION_01);
        cashbacks.put("100silver", Constants.COMMISSION_03);
        cashbacks.put("100gold", Constants.COMMISSION_05);

        cashbacks.put("300student", Constants.COMMISSION_02);
        cashbacks.put("300standard", Constants.COMMISSION_02);
        cashbacks.put("300silver", Constants.COMMISSION_04);
        cashbacks.put("300gold", Constants.COMMISSION_055);

        cashbacks.put("500student", Constants.COMMISSION_025);
        cashbacks.put("500standard", Constants.COMMISSION_025);
        cashbacks.put("500silver", Constants.COMMISSION_05);
        cashbacks.put("500gold", Constants.COMMISSION_07);
        return cashbacks;
    }

    /**
     * Calculates the corresponding cashback for a threshold type commerciant.
     *
     * @param price
     * @param currency
     * @param account
     * @param commerciant
     * @return
     */
    private double spendingThresholdCashback(final double price, final String currency,
                                             final String account, final String commerciant) {
        List<DefaultTransaction> transactions =
                getEntryWithIBAN(account).getTransactionHistory().stream()
                        .filter(e -> e.getAccount().equals(account))
                        .filter(e -> !e.getDetails().getStringOfField("description")
                                .equals("Insufficient funds"))
                        .toList();
        double totalSum = 0.0;
        String accountPlan = getEntryWithIBAN(account).getPlan();
        String commerciantType = commerciants.get(commerciant).getCashbackStrategy();
        for (DefaultTransaction transaction : transactions) {
            String comm = transaction.getDetails().getStringOfField("commerciant");
            if (!comm.isEmpty()) {
                if (commerciants.get(comm).getCashbackStrategy().equals(commerciantType)) {
                    String curr = getAccountWithIBAN(account).getCurrency();
                    double amount = transaction.getDetails().getDoubleOfField("amount");
                    totalSum += amount * exchange.getRate(curr, "RON");
                }
            } else {
                String rec = transaction.getDetails().getStringOfField("receiverIBAN");
                if (!rec.isEmpty()) {
                    if (commerciants.containsKey(rec)) {
                        if (commerciants.get(rec).getCashbackStrategy().equals(commerciantType)) {
                            String amount = transaction.getDetails().getStringOfField("amount");
                            totalSum += Double.parseDouble(amount.split(" ")[0]);
                        }
                    }
                }
            }
        }

        Map<String, Double> cashbacks = getCashbacksMap();
        double accountPrice = price * exchange.getRate(currency,
                getAccountWithIBAN(account).getCurrency());

        if (totalSum >= Constants.THRESHOLD_BIG) {
            return accountPrice * cashbacks.get("500" + accountPlan);
        }

        if (totalSum >= Constants.THRESHOLD_MEDIUM) {
            return accountPrice * cashbacks.get("300" + accountPlan);
        }

        if (totalSum >= Constants.THRESHOLD_SMALL) {
            return accountPrice * cashbacks.get("100" + accountPlan);
        }

        return 0.0;
    }

    /**
     * Calculates the corresponding cashback of a nrOfTransactions commerciant
     *
     * @param price
     * @param currency
     * @param account
     * @param commerciant
     * @return
     */
    private double applyCoupon(final double price, final String currency, final String account,
                               final String commerciant) {
        Account acc = getAccountWithIBAN(account);
        if (acc.getUsableCoupons().isEmpty()) {
            return 0.0;
        }

        Commerciant com = getCommerciant(commerciant);
        for (DiscountCoupon coupon : acc.getUsableCoupons()) {
            if (coupon.getType().equals(com.getType())) {
                double cashback = price * coupon.getDiscount();
                acc.getUsableCoupons().remove(coupon);
                return cashback * getExchangeRate(currency, acc.getCurrency());
            }
        }

        return 0.0;
    }

    /**
     * Returns the corresponding cashback for a specified purchase, depending
     * on the type of the specified commerciant.
     *
     * @param price
     * @param currency
     * @param account
     * @param commerciant
     * @return
     */
    public double getCashBack(final double price, final String currency,
                              final String account, final String commerciant) {
        switch (commerciants.get(commerciant).getCashbackStrategy()) {
            case "spendingThreshold" -> {
                return spendingThresholdCashback(price, currency, account, commerciant);
            }

            case "nrOfTransactions" -> {
                return applyCoupon(price, currency, account, commerciant);
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
        splitPayments = new SplitPaymentContainer();
        validUsers = new ArrayList<>();
    }
}
