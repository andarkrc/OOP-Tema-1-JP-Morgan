package org.poo.transactions.accounts;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;
import org.poo.utils.Constants;

public final class AddFunds extends DefaultTransaction {
    private String iban;
    private double amount;
    private String email;

    public AddFunds(final CommandInput input, final Bank bank) {
        super(input, bank);
        iban = input.getAccount();
        amount = input.getAmount();
        email = input.getEmail();
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(iban)) {
            result.add("description", "Account not found");
            return "Account not found";
        }
        Account acc = bank.getAccountWithIBAN(iban);
        if (acc.getPermission(email) < Constants.BASIC_LEVEL) {
            result.add("description", "Permission denied");
            return "Permission denied";
        }

        if (acc.getPermission(email) == Constants.BASIC_LEVEL) {
            if (amount > acc.getDepositLimit()) {
                result.add("description", "Deposit limit exceeded");
                return "Deposit limit exceeded";
            }
        }

        result.add("description", "ok");
        return "ok";
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Account acc = bank.getAccountWithIBAN(iban);

        bank.getAccountWithIBAN(iban).addFunds(amount);
    }

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }

        String ownerEmail = bank.getEntryWithIBAN(iban).getUser().getEmail();
        bank.addTransaction(ownerEmail, this);
    }

    @Override
    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }

        Account acc = bank.getAccountWithIBAN(iban);

        details = new JsonObject();
        details.add("timestamp", timestamp);

        details.add("amount", amount);
    }

    @Override
    public String getAccount() {
        return iban;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
