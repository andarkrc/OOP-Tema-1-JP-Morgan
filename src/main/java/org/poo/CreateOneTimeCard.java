package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class CreateOneTimeCard extends CreateCard {
    public CreateOneTimeCard(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Card card = new OneTimeCard(number);
        bank.addCard(IBAN, card);
    }
}
