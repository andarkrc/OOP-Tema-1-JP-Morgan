package org.poo;

import org.poo.fileio.CommandInput;

public class AddClassicAccount extends AddAccount{
    public AddClassicAccount(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Account account = new ClassicAccount();
        account.setAccountType(accountType)
                .setCurrency(currency)
                .setIban(iban);
        bank.addAccount(email, account);
    }
}
