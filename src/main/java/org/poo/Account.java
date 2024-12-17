package org.poo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public abstract class Account implements Visitable{
    protected String currency;
    protected String accountType;
    protected double balance;
    protected String iban;
    protected ArrayList<Card> cards;
    protected HashMap<String, Card> cardsMap;
    protected double minBalance;

    public Account() {
        balance = 0;
        cards = new ArrayList<>();
        cardsMap = new HashMap<>();
        minBalance = 0;
    }

    public boolean isSavings() {
        return false;
    }

    public String accept(Visitor visitor) {
        return visitor.visit(this);
    }

    public void addFunds(double amount) {
        balance += amount;
    }

    public void addCard(Card card) {
        cards.add(card);
        cardsMap.put(card.getNumber(), card);
    }

    public void removeCard(String cardNumber) {
        cards.remove(cardsMap.get(cardNumber));
        cardsMap.remove(cardNumber);
    }

    public Account setCurrency(String currency) {
        this.currency = currency;
        return this;
    }

    public Account setInterestRate(double interestRate) {
        return this;
    }

    public double getInterestRate() {
        return 0;
    }

    public Account setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public Account setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public Account setIban(String iban) {
        this.iban = iban;
        return this;
    }
}
