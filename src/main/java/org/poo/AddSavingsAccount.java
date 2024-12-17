package org.poo;

import org.poo.fileio.CommandInput;

public class AddSavingsAccount extends AddAccount{
    private double interestRate;
    public AddSavingsAccount(CommandInput input, Bank bank) {
        super(input, bank);
        interestRate = input.getInterestRate();
    }

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
