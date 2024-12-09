package org.poo;

import org.poo.fileio.CommandInput;

import java.math.BigDecimal;

public class AddFunds extends DefaultTransaction {
    private String IBAN;
    private double amount;

    public AddFunds(CommandInput input, Bank bank) {
        super(input, bank);
        IBAN = input.getAccount();
        amount = input.getAmount();
    }

    public void execute() {
        if (bank.getAccount(IBAN) != null) {
            bank.getAccount(IBAN).addFunds(amount);
        } else {
            System.out.println("Account not found");
        }
    }

    public void remember() {
        if (bank.getEntryOfIBAN(IBAN) != null) {
            bank.getEntryOfIBAN(IBAN).addTransaction(this);
        } else {
            System.out.println("User not found");
        }
    }
}
