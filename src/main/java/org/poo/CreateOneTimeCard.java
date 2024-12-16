package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class CreateOneTimeCard extends CreateCard {
    public CreateOneTimeCard(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public CreateOneTimeCard(DefaultTransaction transaction, String account) {
        commandName = "createOneTimeCard";
        bank = transaction.getBank();
        timestamp = transaction.getTimestamp();
        IBAN = account;
        email = bank.getEntryWithIBAN(account).getUser().getEmail();
        number = Utils.generateCardNumber();
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Card card = new OneTimeCard(number);
        bank.addCard(IBAN, card);
    }
}
