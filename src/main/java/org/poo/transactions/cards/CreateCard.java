package org.poo.transactions.cards;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;
import org.poo.utils.Constants;
import org.poo.utils.Utils;

public abstract class CreateCard extends DefaultTransaction {
    protected String iban;
    protected String email;
    protected String number;

    public CreateCard(final CommandInput input, final Bank bank) {
        super(input, bank);
        iban = input.getAccount();
        email = input.getEmail();

    }

    protected CreateCard() {

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
            result.add("description", "User does not exist");
            return "User does not exist";
        }

        if (!bank.databaseHas(iban)) {
            result.add("description", "Account does not exist");
            return "Account does not exist";
        }

        Account acc = bank.getAccountWithIBAN(iban);
        if (acc.getPermission(email) < Constants.BASIC_LEVEL) {
            result.add("description", "Account does not have permission");
            return "Account does not have permission";
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
     * Makes a copy of the transaction's details so it's not affected by future
     * transactions.
     */
    @Override
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
