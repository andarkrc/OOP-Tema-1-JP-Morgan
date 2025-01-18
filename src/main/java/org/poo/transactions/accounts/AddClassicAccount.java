package org.poo.transactions.accounts;

import org.poo.bank.accounts.ClassicAccount;
import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;

public final class AddClassicAccount extends AddAccount {
    public AddClassicAccount(final CommandInput input, final Bank bank) {
        super(input, bank);
    }

    @Override
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
