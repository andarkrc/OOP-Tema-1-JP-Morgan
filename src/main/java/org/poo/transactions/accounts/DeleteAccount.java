package org.poo.transactions.accounts;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class DeleteAccount extends DefaultTransaction {
    private String iban;
    private String email;

    public DeleteAccount(final CommandInput input, final Bank bank) {
        super(input, bank);
        iban = input.getAccount();
        email = input.getEmail();
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "User does not exists");
            return "User does not exist";
        }
        if (!bank.databaseHas(iban)) {
            result.add("description", "Account does not exist");
            return "Account does not exist";
        }
        if (bank.getEntryWithEmail(email) != bank.getEntryWithIBAN(iban)) {
            result.add("description", "User does not exist");
            return "User does not own account";
        }

        result.add("description", "ok");
        return "ok";
    }

    @Override
    public void burnDetails() {
        details = new JsonObject();
        details.add("timestamp", timestamp);
        if (bank.getAccountWithIBAN(iban).getBalance() > 0) {
            details.add("description", "Account couldn't be deleted - there are funds remaining");
        } else {
            details.add("success", "Account deleted");
        }
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }


        JsonObject output = new JsonObject();
        output.add("timestamp", timestamp);
        if (bank.getAccountWithIBAN(iban).getBalance() > 0) {
            output.add("error", "Account couldn't be deleted - "
                                    + "see org.poo.transactions for details");
        } else {
            output.add("success", "Account deleted");
            bank.removeAccount(iban);
        }

        JsonObject status = new JsonObject();
        status.add("command", "deleteAccount");
        status.add("output", output);
        status.add("timestamp", timestamp);
        bank.getOutput().add(status);
    }

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        bank.addTransaction(email, this);
    }
}
