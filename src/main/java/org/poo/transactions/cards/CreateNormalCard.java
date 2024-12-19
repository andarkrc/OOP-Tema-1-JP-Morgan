package org.poo.transactions.cards;

import org.poo.bank.cards.NormalCard;
import org.poo.bank.Bank;
import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;

public final class CreateNormalCard extends CreateCard {
    public CreateNormalCard(final CommandInput input, final Bank bank) {
        super(input, bank);
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Card card = new NormalCard(number);
        bank.addCard(iban, card);
    }
}
