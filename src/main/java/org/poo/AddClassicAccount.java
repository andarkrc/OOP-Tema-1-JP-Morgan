package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.utils.Utils;

public class AddClassicAccount extends AddAccount{
    public AddClassicAccount(CommandInput input, Bank bank) {
        super(input, bank);
    }

    public void execute() {
        Account account = new ClassicAccount();
        account.setAccountType(accountType)
                .setCurrency(currency)
                .setIBAN(Utils.generateIBAN());
        bank.getEntryOfEmail(email).addAccount(account);
        bank.getDatabase().getIBANDatabase().put(account.getIBAN(), bank.getEntryOfEmail(email));
    }

    public void remember() {
        bank.getEntryOfEmail(email).addTransaction(this);
    }
}
