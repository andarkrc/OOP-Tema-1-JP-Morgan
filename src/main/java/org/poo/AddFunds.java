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

    private boolean verify() {
        if (!bank.databaseHas(IBAN)) {
            System.out.println("Account not found");
            return false;
        }
        return true;
    }

    public void execute() {
        if (!verify()) {
            return;
        }

        bank.getAccount(IBAN).addFunds(amount);
    }

    public void remember() {
        if (!verify()) {
            return;
        }

        DatabaseEntry entry = bank.getEntryWithIBAN(IBAN);
        bank.addTransaction(entry.getUser().getEmail(), this);
    }
}
