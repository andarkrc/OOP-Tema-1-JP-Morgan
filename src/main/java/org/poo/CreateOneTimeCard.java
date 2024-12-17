package org.poo;

import org.poo.fileio.CommandInput;

public class CreateOneTimeCard extends CreateCard {
    public CreateOneTimeCard(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public CreateOneTimeCard(DefaultTransaction transaction, String account) {
        commandName = "createOneTimeCard";
        bank = transaction.getBank();
        timestamp = transaction.getTimestamp();
        iban = account;
        email = bank.getEntryWithIBAN(account).getUser().getEmail();
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Card card = new OneTimeCard(number);
        bank.addCard(iban, card);
    }
}
