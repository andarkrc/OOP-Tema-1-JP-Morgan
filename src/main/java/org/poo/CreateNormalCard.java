package org.poo;

import org.poo.fileio.CommandInput;

public class CreateNormalCard extends CreateCard {
    public CreateNormalCard(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Card card = new NormalCard(number);
        bank.addCard(iban, card);
    }
}
