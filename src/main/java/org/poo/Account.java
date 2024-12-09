package org.poo;

import lombok.Getter;
import lombok.Setter;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

@Getter
@Setter
public abstract class Account implements Visitable{
    protected String currency;
    protected String accountType;
    protected double balance;
    protected String IBAN;
    protected ArrayList<Card> cards;
    protected HashMap<String, Card> cardsMap;

    public Account() {
        balance = 0;
        cards = new ArrayList<>();
        cardsMap = new HashMap<>();
    }

    public JsonArray acceptJsonArray(Visitor visitor) {
        return null;
    }

    public JsonObject acceptJsonObject(Visitor visitor) {
        return null;
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

    public Account setAccountType(String accountType) {
        this.accountType = accountType;
        return this;
    }

    public Account setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public Account setIBAN(String IBAN) {
        this.IBAN = IBAN;
        return this;
    }
}
