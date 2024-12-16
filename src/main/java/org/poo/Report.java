package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Report extends DefaultTransaction {
    protected String account;
    protected int startTimestamp;
    protected int endTimestamp;

    public Report(CommandInput input, Bank bank) {
        super(input, bank);
        account = input.getAccount();
        startTimestamp = input.getStartTimestamp();
        endTimestamp = input.getEndTimestamp();
    }

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

    public boolean hasLoggableError() {
        if (verify().equals("Account not found")) {
            return true;
        }

        return false;
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        JsonObject reportData = new JsonObject();
        reportData.add("command", commandName);
        reportData.add("timestamp", timestamp);
        Account acc = bank.getAccountWithIBAN(account);
        JsonObject accountData = new JsonObject();
        accountData.add("IBAN", acc.getIBAN());
        accountData.add("balance", acc.getBalance());
        accountData.add("currency", acc.getCurrency());
        List<DefaultTransaction> transactionsList =
        bank.getEntryWithIBAN(account).getTransactionHistory().stream()
                .filter(e -> e.getAccount().equals(account))
                .filter(e -> e.getTimestamp() >= startTimestamp)
                .filter(e -> e.getTimestamp() <= endTimestamp)
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
