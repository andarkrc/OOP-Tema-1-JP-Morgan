package org.poo.transactions.accounts.businessaccounts;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;
import org.poo.utils.Constants;

public final class ChangeSpendingLimit extends DefaultTransaction {
    private String email;
    private String account;
    private double amount;

    public ChangeSpendingLimit(CommandInput input, Bank bank) {
        super(input, bank);
        email = input.getEmail();
        account = input.getAccount();
        amount = input.getAmount();
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);

        if (!bank.databaseHas(account)) {
            result.add("description", "Account not found");
            return "Account not found";
        }

        if (!bank.databaseHas(email)) {
            result.add("description", "User not found");
            return "User not found";
        }
        Account acc = bank.getAccountWithIBAN(account);
        if (acc.getPermission(email) != Constants.OWNER_LEVEL) {
            result.add("description", "You must be owner in order to change spending limit.");
            return "Permission denied";
        }

        result.add("description", "ok");
        return "ok";
    }

    @Override
    public boolean hasLoggableError() {
        if (verify().equals("Permission denied")) {
            return true;
        }

        return false;
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Account acc = bank.getAccountWithIBAN(account);

        acc.setSpendingLimit(amount);
    }
}
