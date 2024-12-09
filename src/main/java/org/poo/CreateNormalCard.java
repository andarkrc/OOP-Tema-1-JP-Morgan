package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class CreateNormalCard extends CreateCard {
    public CreateNormalCard(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public void execute() {
        if (!verify()) {
            return;
        }

        Card card = new NormalCard(Utils.generateCardNumber());

        bank.addCard(IBAN, card);
    }

    public void remember() {
        if (!verify()) {
            return;
        }

        bank.addTransaction(email, this);
    }
}
