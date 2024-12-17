package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class DeleteAccount extends DefaultTransaction {
    private String iban;
    private String email;

    public DeleteAccount(CommandInput input, Bank bank) {
        super(input, bank);
        iban = input.getAccount();
        email = input.getEmail();
    }

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

    public void burnDetails() {
        details = new JsonObject();
        details.add("timestamp", timestamp);
        if (bank.getAccountWithIBAN(iban).getBalance() > 0) {
            details.add("description", "Account couldn't be deleted - there are funds remaining");
        } else {
            details.add("success", "Account deleted");
        }
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }


        JsonObject output = new JsonObject();
        output.add("timestamp", timestamp);
        if (bank.getAccountWithIBAN(iban).getBalance() > 0) {
            output.add("error", "Account couldn't be deleted - see org.poo.transactions for details");
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

    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        bank.addTransaction(email, this);
    }
}
