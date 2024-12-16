package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class AddInterest extends DefaultTransaction {
    private String account;

    public AddInterest(CommandInput input, Bank bank) {
        super(input, bank);
        account = input.getAccount();
    }

    public boolean hasLoggableError() {
        if (verify().equals("Account is not savings")) {
            return true;
        }
        return false;
    }

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

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Account account = bank.getAccountWithIBAN(this.account);
        account.setBalance(account.getBalance() + account.getBalance() * account.getInterestRate());
    }

    public String getAccount() {
        return account;
    }
}
