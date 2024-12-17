package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.utils.Utils;

public abstract class CreateCard extends DefaultTransaction{
    protected String iban;
    protected String email;
    protected String number;

    public CreateCard(CommandInput input, Bank bank) {
        super(input, bank);
        iban = input.getAccount();
        email = input.getEmail();

    }

    protected CreateCard() {

    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "User does not exist");
            return "User does not exist";
        }

        if (!bank.databaseHas(iban)) {
            result.add("description", "Account does not exist");
            return "Account does not exist";
        }

        if (bank.getEntryWithEmail(email) != bank.getEntryWithIBAN(iban))  {
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
        // I am sorry that I am doing this
        // there is no other way.
        // Can't generate the number in the constructor.
        // It needs to be generated only once, only if the transaction is valid.
        number = Utils.generateCardNumber();

        details = new JsonObject();
        details.add("timestamp", timestamp);
        details.add("account", iban);
        details.add("cardHolder", email);
        details.add("card", number);
        details.add("description", "New card created");
    }

    public String getAccount() {
        return iban;
    }
}
