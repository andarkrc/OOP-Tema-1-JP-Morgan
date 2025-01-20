package org.poo.bank.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.cards.Card;
import org.poo.bank.commerciants.DiscountCoupon;
import org.poo.utils.Constants;
import org.poo.visitors.Visitable;
import org.poo.visitors.Visitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class Account implements Visitable {
    protected String currency;
    protected String accountType;
    protected double balance;
    protected String iban;
    protected List<Card> cards;
    protected Map<String, Card> cardsMap;
    protected double minBalance;
    protected double spendingLimit;
    protected double depositLimit;
    protected List<String> associates;
    protected Map<String, Integer> permissions;

    protected List<DiscountCoupon> availableCoupons;
    protected List<DiscountCoupon> usableCoupons;

    public Account() {
        balance = 0;
        cards = new ArrayList<>();
        cardsMap = new HashMap<>();
        associates = new ArrayList<>();
        permissions = new HashMap<>();
        availableCoupons = new ArrayList<>();
        usableCoupons = new ArrayList<>();
        minBalance = 0;
        spendingLimit = Constants.NO_SPENDING_LIMIT;
        depositLimit = Constants.NO_DEPOSIT_LIMIT;
    }

    /**
     * Returns whether the account is a savings account or not.
     *
     * @return
     */
    public boolean isSavings() {
        return false;
    }

    /**
     * Returns whether the account is a classic account or not.
     *
     * @return
     */
    public boolean isClassic() {
        return false;
    }

    /**
     * Returns whether the account is a business account or not.
     *
     * @return
     */
    public boolean isBusiness() {
        return false;
    }

    /**
     * Accepts a visitor so it can retrieve data.
     *
     * @param visitor
     * @return
     */
    public String accept(final Visitor visitor) {
        return visitor.visit(this);
    }

    /**
     * Adds the specified amount to the account balance.
     *
     * @param amount
     */
    public void addFunds(final double amount) {
        balance += amount;
    }

    /**
     * Adds a new card to the account's card list.
     *
     * @param card
     */
    public void addCard(final Card card) {
        cards.add(card);
        cardsMap.put(card.getNumber(), card);
    }

    /**
     * Removes the card with the specified number from the card list.
     *
     * @param cardNumber
     */
    public void removeCard(final String cardNumber) {
        cards.remove(cardsMap.get(cardNumber));
        cardsMap.remove(cardNumber);
    }

    /**
     * Adds the specified permission level for the user.
     *
     * @param email     the user who was granted the permission
     * @param level     the permission level granted
     */
    public void addPermission(final String email, final int level) {
        associates.add(email);
        permissions.put(email, level);
    }

    /**
     * Returns the permission level of the specified user.
     *
     * @param email     user
     * @return          permission level:
     *                  0 - no permission
     *                  1 - can spend and deposit within limit
     *                      creates cards
     *                  2 - can also delete cards
     *                  3 - can also set limits (every permission)
     */
    public Integer getPermission(final String email) {
        if (!permissions.containsKey(email)) {
            return Constants.NO_LEVEL;
        }
        return permissions.get(email);
    }

    /**
     * Removes the permission for the specified user.
     *
     * @param email
     */
    public void removePermission(final String email) {
        associates.remove(email);
        permissions.remove(email);
    }

    /**
     * Sets the account's currency to the specified currency.
     *
     * @param newCurrency
     * @return              the current account
     */
    public Account setCurrency(final String newCurrency) {
        this.currency = newCurrency;
        return this;
    }

    /**
     * Sets the account's spending limit to the specified limit.
     *
     * @param newSpendingLimit
     * @return                      the current account
     */
    public Account setSpendingLimit(final double newSpendingLimit) {
        this.spendingLimit = newSpendingLimit;
        return this;
    }

    /**
     * Sets the account's deposit limit to the specified limit.
     *
     * @param newDepositLimit
     * @return                  the current account
     */
    public Account setDepositLimit(final double newDepositLimit) {
        this.depositLimit = newDepositLimit;
        return this;
    }

    /**
     * Sets the interest rate of an account that benefits from it(ex: savings account).
     *
     * @param interestRate
     * @return              the current account
     */
    public Account setInterestRate(final double interestRate) {
        return this;
    }

    /**
     * Returns the interest rate of an account that benefits from it.
     *
     * @return
     */
    public double getInterestRate() {
        return 0;
    }

    /**
     * Sets the type of the account.
     *
     * @param newAccountType
     * @return                  the current account
     */
    public Account setAccountType(final String newAccountType) {
        this.accountType = newAccountType;
        return this;
    }

    /**
     * Sets the balance of the account.
     *
     * @param newBalance
     * @return                  the current account
     */
    public Account setBalance(final double newBalance) {
        this.balance = newBalance;
        return this;
    }

    /**
     * Sets the IBAN of the account.
     *
     * @param newIban
     * @return                  the current account
     */
    public Account setIban(final String newIban) {
        this.iban = newIban;
        return this;
    }
}
