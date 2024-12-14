package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.utils.Utils;

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
                .setIBAN(IBAN);
        bank.addAccount(email, account);
    }
}
