package org.poo.bank.accounts;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.cards.Card;
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

    public Account() {
        balance = 0;
        cards = new ArrayList<>();
        cardsMap = new HashMap<>();
        minBalance = 0;
    }

    /**
     * Returns whether the account is a savings account or not.
     *
     * @return
     */
    public boolean isSavings() {
        return false;
    }

    public boolean isClassic() {
        return false;
    }

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
