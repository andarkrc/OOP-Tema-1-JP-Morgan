package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class DeleteAccount extends DefaultTransaction {
    private String IBAN;
    private String email;

    public DeleteAccount(CommandInput input, Bank bank) {
        super(input, bank);
        IBAN = input.getAccount();
        email = input.getEmail();
    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "User does not exists");
            return "User does not exist";
        }
        if (!bank.databaseHas(IBAN)) {
            result.add("description", "Account does not exist");
            return "Account does not exist";
        }
        if (bank.getEntryWithEmail(email) != bank.getEntryWithIBAN(IBAN)) {
            result.add("description", "User does not exist");
            return "User does not own account";
        }

        result.add("description", "ok");
        return "ok";
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }


        JsonObject output = new JsonObject();
        output.add("timestamp", timestamp);
        if (Double.compare(0, bank.getAccountWithIBAN(IBAN).getBalance()) == 0) {
            output.add("success", "Account deleted");
            bank.removeAccount(IBAN);
        } else {
            output.add("error", "Account couldn't be deleted - see org.poo.transactions for details");
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
