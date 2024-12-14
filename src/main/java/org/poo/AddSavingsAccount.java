package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class AddSavingsAccount extends AddAccount{
    public AddSavingsAccount(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Account account = new SavingsAccount();
        account.setAccountType(accountType)
                .setCurrency(currency)
                .setIBAN(IBAN);
        bank.addAccount(email, account);
    }
}
