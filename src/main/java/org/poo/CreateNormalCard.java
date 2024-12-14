package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class CreateNormalCard extends CreateCard {
    public CreateNormalCard(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Card card = new NormalCard(number);
        bank.addCard(IBAN, card);
    }
}
