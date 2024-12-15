package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.utils.Utils;

public abstract class CreateCard extends DefaultTransaction{
    protected String IBAN;
    protected String email;
    protected String number;

    public CreateCard(CommandInput input, Bank bank) {
        super(input, bank);
        IBAN = input.getAccount();
        email = input.getEmail();
        number = Utils.generateCardNumber();
    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "User does not exist");
            return "User does not exist";
        }

        if (!bank.databaseHas(IBAN)) {
            result.add("description", "Account does not exist");
            return "Account does not exist";
        }

        if (bank.getEntryWithEmail(email) != bank.getEntryWithIBAN(IBAN))  {
            result.add("description", "User does not own account");
            return "User does not own account";
        }

        result.add("description", "ok");
        return "ok";
    }

    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        bank.addTransaction(email, this);
    }

    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }
        details = new JsonObject();
        details.add("timestamp", timestamp);
        details.add("account", IBAN);
        details.add("cardHolder", email);
        details.add("card", number);
        details.add("description", "New card created");
    }

    public String getAccount() {
        return IBAN;
    }
}
