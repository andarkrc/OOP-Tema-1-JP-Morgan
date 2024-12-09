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

    public void execute() {
        DatabaseEntry entry = bank.getEntryOfEmail(email);
        Account account = bank.getAccount(IBAN);
        entry.getAccounts().remove(account);
        entry.getAccountMap().remove(IBAN);
        bank.getDatabase().getIBANDatabase().remove(IBAN);

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
        bank.getEntryOfEmail(email).addTransaction(this);
    }
}
