package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class CreateNormalCard extends CreateCard {
    public CreateNormalCard(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public void execute() {
        Card card = new NormalCard(Utils.generateCardNumber());
        card.setStatus("active");

        if (bank.getAccount(IBAN) != null) {
            bank.getAccount(IBAN).addCard(card);
        } else {
            System.out.println("Account doesn't exist");
        }
    }

    public void remember() {
        if (bank.getEntryOfEmail(email) != null) {
            bank.getEntryOfEmail(email).addTransaction(this);
        } else {
            System.out.println("User doesn't exist");
        }
    }
}
