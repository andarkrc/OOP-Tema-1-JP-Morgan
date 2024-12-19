package org.poo.transactions;

import lombok.Getter;
import lombok.Setter;
import org.poo.visitors.Visitable;
import org.poo.visitors.Visitor;
import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

@Getter
@Setter
public abstract class DefaultTransaction implements Visitable {
    protected String commandName;
    protected int timestamp;
    protected Bank bank;
    protected JsonObject result;
    protected JsonObject details;

    protected DefaultTransaction() {

    }

    public DefaultTransaction(final CommandInput input, final Bank bank) {
        commandName = input.getCommand();
        timestamp = input.getTimestamp();
        this.bank = bank;
    }

    /**
     * Accepts a visitor so it can retrieve data.
     *
     * @param visitor
     * @return
     */
    public String accept(final Visitor visitor) {
        return visitor.visit(this);
    }

    /**
     * Executes the command if there are no errors that could cause crashes.
     */
    public void execute() {

    }

    /**
     * Keeps track of the transaction in some way (usually in a transaction history).
     */
    public void remember() {

    }

    /**
     * Verifies if there are any errors that could cause crashes.
     *
     * @return      error message of the verifying process
     */
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        result.add("description", "ok");
        return "ok";
    }

    /**
     * Returns whether the verifying process returned a loggable error.
     * In an ideal world, you wouldn't need this function. You would just
     * log everything to a log file.
     *
     * @return
     */
    public boolean hasLoggableError() {
        return false;
    }

    /**
     * Makes a copy of the transaction's details so it's not affected by future
     * transactions.
     */
    public void burnDetails() {
        details = new JsonObject();
        details.add("command", commandName);
        details.add("timestamp", timestamp);
    }

    /**
     * Returns the associated account of the transaction (if there is one).
     *
     * @return          the account that the transaction was made on
     */
    public String getAccount() {
        return "";
    }

    /**
     * Returns whether the transaction should appear in a spendings report.
     * In an ideal world, this should be always true.
     *
     * @return
     */
    public boolean appearsInSpendingsReport() {
        return false;
    }

    /**
     * Returns whether the transaction is a payment type transaction.
     *
     * @return
     */
    public boolean isPayment() {
        return false;
    }
}
