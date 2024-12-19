package org.poo.transactions.accounts;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class AddInterest extends DefaultTransaction {
    private String account;

    public AddInterest(final CommandInput input, final Bank bank) {
        super(input, bank);
        account = input.getAccount();
    }

    @Override
    public boolean hasLoggableError() {
        if (verify().equals("Account is not savings")) {
            return true;
        }
        return false;
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(account)) {
            result.add("description", "Account does not exist");
            return "Account does not exist";
        }
        if (!bank.getAccountWithIBAN(account).isSavings()) {
            result.add("description", "This is not a savings account");
            return "Account is not savings";
        }

        result.add("description", "ok");
        return "ok";
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Account acc = bank.getAccountWithIBAN(this.account);
        acc.setBalance(acc.getBalance() + acc.getBalance() * acc.getInterestRate());
    }

    @Override
    public String getAccount() {
        return account;
    }
}
