package org.poo.transactions.accounts;

import org.poo.bank.accounts.SavingsAccount;
import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;

public final class AddSavingsAccount extends AddAccount {
    private double interestRate;
    public AddSavingsAccount(final CommandInput input, final Bank bank) {
        super(input, bank);
        interestRate = input.getInterestRate();
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Account account = new SavingsAccount();
        account.setAccountType(accountType)
                .setCurrency(currency)
                .setIban(iban)
                .setInterestRate(interestRate);
        bank.addAccount(email, account);
    }
}
