package org.poo.bank.cards;

public final class OneTimeCard extends Card {
    public OneTimeCard(final String number) {
        super(number);
    }

    @Override
    public boolean isOneTime() {
        return true;
    }
}
