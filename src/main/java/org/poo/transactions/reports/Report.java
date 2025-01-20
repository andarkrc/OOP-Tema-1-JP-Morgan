package org.poo.transactions.reports;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

import java.util.List;

public class Report extends DefaultTransaction {
    protected String account;
    protected int startTimestamp;
    protected int endTimestamp;

    public Report(final CommandInput input, final Bank bank) {
        super(input, bank);
        account = input.getAccount();
        startTimestamp = input.getStartTimestamp();
        endTimestamp = input.getEndTimestamp();
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
        if (!bank.databaseHas(account)) {
            result.add("description", "Account not found");
            return "Account not found";
        }

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
    @Override
    public boolean hasLoggableError() {
        if (verify().equals("Account not found")) {
            return true;
        }

        return false;
    }

    /**
     * Executes the command if there are no errors that could cause crashes.
     */
    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        JsonObject reportData = new JsonObject();
        reportData.add("command", commandName);
        reportData.add("timestamp", timestamp);
        Account acc = bank.getAccountWithIBAN(account);
        JsonObject accountData = new JsonObject();
        accountData.add("IBAN", acc.getIban());
        accountData.add("balance", acc.getBalance());
        accountData.add("currency", acc.getCurrency());
        List<DefaultTransaction> transactionsList =
        bank.getEntryWithIBAN(account).getTransactionHistory().stream()
                .filter(e -> e.getAccount().equals(account))
                .filter(e -> e.getTimestamp() >= startTimestamp)
                .filter(e -> e.getTimestamp() <= endTimestamp)
                .filter(e -> !e.getCommandName().equals("addFunds")) // bruuh
                .toList();

        JsonArray transactions = new JsonArray();
        for (DefaultTransaction transaction : transactionsList) {
            transactions.add(transaction.getDetails());
        }
        accountData.add("transactions", transactions);
        reportData.add("output", accountData);
        bank.getOutput().add(reportData);
    }
}
