package org.poo.transactions.accounts;


import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;
import org.poo.utils.Utils;

public abstract class AddAccount extends DefaultTransaction {
    protected String email;
    protected String currency;
    protected String accountType;
    protected String iban;

    public AddAccount(final CommandInput input, final Bank bank) {
        super(input, bank);
        email = input.getEmail();
        currency = input.getCurrency();
        accountType = input.getAccountType();
        iban = Utils.generateIBAN();
    }

    /**
     * Makes a copy of the transaction's details so it's not affected by future
     * transactions.
     */
    @Override
    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }

        details = new JsonObject();
        details.add("timestamp", timestamp);
        details.add("description", "New account created");
    }

    /**
     * Verifies if there are any errors that could cause crashes.
     *
     * @return      error message of the verifying process
     */
    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "No such user");
            return "No such user";
        }
        result.add("description", "ok");
        return "ok";
    }

    /**
     * Keeps track of the transaction in some way (usually in a transaction history).
     */
    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        bank.addTransaction(email, this);
    }

    /**
     * Returns the associated account of the transaction (if there is one).
     *
     * @return          the account that the transaction was made on
     */
    @Override
    public String getAccount() {
        return iban;
    }
}
