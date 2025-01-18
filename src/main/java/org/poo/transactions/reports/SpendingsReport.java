package org.poo.transactions.reports;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class SpendingsReport extends Report {
    public SpendingsReport(final CommandInput input, final Bank bank) {
        super(input, bank);
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        if (!bank.databaseHas(account)) {
            result.add("description", "Account not found");
            result.add("timestamp", timestamp);
            return "Account not found";
        }
        if (bank.getAccountWithIBAN(account).isSavings()) {
            result.add("error", "This kind of report is not supported for a saving account");
            return "Account is savings";
        }

        result.add("description", "ok");
        result.add("timestamp", timestamp);
        return "ok";
    }

    @Override
    public boolean hasLoggableError() {
        if (verify().equals("Account not found")) {
            return true;
        }
        if (verify().equals("Account is savings")) {
            return true;
        }

        return false;
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }
        Account acc = bank.getAccountWithIBAN(account);
        JsonObject reportData = new JsonObject();
        reportData.add("command", commandName);
        reportData.add("timestamp", timestamp);
        JsonObject accountData = new JsonObject();
        accountData.add("IBAN", account);
        accountData.add("currency", acc.getCurrency());
        accountData.add("balance", acc.getBalance());

        Map<String, Double> commerciantData = new TreeMap<>();
        JsonArray transactionsData = new JsonArray();

        List<DefaultTransaction> transactions =
        bank.getEntryWithIBAN(account).getTransactionHistory().stream()
                .filter(e -> e.getAccount().equals(account))
                .filter(e -> e.getTimestamp() >= startTimestamp)
                .filter(e -> e.getTimestamp() <= endTimestamp)
                .filter(DefaultTransaction::isPayment)
                .filter(DefaultTransaction::appearsInSpendingsReport).toList();

        for (DefaultTransaction transaction : transactions) {
            transactionsData.add(transaction.getDetails());
            String commerciant = transaction.getDetails().getStringOfField("commerciant");
            double amount = transaction.getDetails().getDoubleOfField("amount");
            if (commerciantData.containsKey(commerciant)) {
                amount += commerciantData.get(commerciant);
                commerciantData.put(commerciant, amount);
            } else {
                commerciantData.put(commerciant, amount);
            }
        }
        accountData.add("transactions", transactionsData);
        JsonArray commerciantsData = new JsonArray();
        for (String commerciant : commerciantData.keySet()) {
            // IDK why, but intellij wants this to be named commercit, and I am not complaining
            JsonObject commercit = new JsonObject();
            commercit.add("commerciant", commerciant);

            commercit.add("total", commerciantData.get(commerciant));
            commerciantsData.add(commercit);
        }
        accountData.add("commerciants", commerciantsData);
        reportData.add("output", accountData);
        bank.getOutput().add(reportData);
    }
}
