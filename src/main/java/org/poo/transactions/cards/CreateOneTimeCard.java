package org.poo.transactions.cards;

import org.poo.bank.cards.OneTimeCard;
import org.poo.bank.Bank;
import org.poo.bank.cards.Card;
import org.poo.fileio.CommandInput;
import org.poo.transactions.DefaultTransaction;

public final class CreateOneTimeCard extends CreateCard {
    public CreateOneTimeCard(final CommandInput input, final Bank bank) {
        super(input, bank);
    }

    public CreateOneTimeCard(final DefaultTransaction transaction, final String account) {
        commandName = "createOneTimeCard";
        bank = transaction.getBank();
        timestamp = transaction.getTimestamp();
        iban = account;
        email = bank.getEntryWithIBAN(account).getUser().getEmail();
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Card card = new OneTimeCard(number);
        bank.addCard(iban, card);
    }
}
