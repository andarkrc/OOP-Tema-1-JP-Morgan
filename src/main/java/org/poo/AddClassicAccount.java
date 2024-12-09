package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.utils.Utils;

public class AddClassicAccount extends AddAccount{
    public AddClassicAccount(CommandInput input, Bank bank) {
        super(input, bank);
    }

    private boolean verify() {
        if (!bank.databaseHas(email)) {
            System.out.println("No such user");
            return false;
        }

        return true;
    }

    public void execute() {
        if (!verify()) {
            return;
        }

        Account account = new ClassicAccount();
        account.setAccountType(accountType)
                .setCurrency(currency)
                .setIBAN(Utils.generateIBAN());
        bank.addAccount(email, account);
    }

    public void remember() {
        if (!verify()) {
            return;
        }

        bank.addTransaction(email, this);
    }
}
