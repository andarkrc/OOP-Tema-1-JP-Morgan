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

    private boolean verify() {
        if (!bank.databaseHas(email)) {
            System.out.println("User does not exist");
            return false;
        }
        if (!bank.databaseHas(IBAN)) {
            System.out.println("Account does not exist");
            return false;
        }
        if (bank.getEntryWithEmail(email) != bank.getEntryWithIBAN(IBAN)) {
            System.out.println("User does not own account");
            return false;
        }
        return true;
    }

    public void execute() {
        if (!verify()) {
            return;
        }

        bank.removeAccount(IBAN);

        JsonObject status = new JsonObject();
        JsonObject output = new JsonObject();
        output.add("success", "Account deleted");
        output.add("timestamp", timestamp);
        status.add("command", "deleteAccount");
        status.add("output", output);
        status.add("timestamp", timestamp);
        bank.getOutput().add(status);
    }

    public void remember() {
        if (!verify()) {
            return;
        }

        bank.addTransaction(email, this);
    }
}
