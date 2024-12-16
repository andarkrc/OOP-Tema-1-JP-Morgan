package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

import java.util.List;

public class SplitPayment extends DefaultTransaction {
    private List<String> accounts;
    private double amount;
    private String currency;
    private String account;

    public SplitPayment(CommandInput input, Bank bank) {
        super(input, bank);
        accounts = input.getAccounts();
        amount = input.getAmount();
        currency = input.getCurrency();
    }

    private SplitPayment(SplitPayment splitPayment, String account) {
        this.account = account;
        amount = splitPayment.amount;
        currency = splitPayment.currency;
        accounts = splitPayment.accounts;
        bank = splitPayment.bank;
        commandName = splitPayment.commandName;
        timestamp = splitPayment.timestamp;
    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        for (String account : accounts) {
            if (!bank.databaseHas(account)) {
                result.add("description", "Account " + account + " does not exist");
                return "Account does not exist";
            }
        }
        result.add("description", "ok");
        return "ok";
    }

    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }

        double splitAmount = amount / accounts.size();
        boolean canPay = true;
        String lastAccount = "";
        for (String account : accounts) {
            Account acc = bank.getAccountWithIBAN(account);
            double accAmount = splitAmount * bank.getExchangeRate(currency, acc.getCurrency());
            if (acc.getBalance() < accAmount) {
                canPay = false;
                lastAccount = account;
            }
        }
        details = new JsonObject();
        details.add("timestamp", timestamp);
        details.add("description", String.format("Split payment of %.2f %s", amount, currency));
        details.add("amount", splitAmount);
        details.add("currency", currency);
        JsonArray involvedAccounts = new JsonArray();
        for (String account : accounts) {
            involvedAccounts.add(account);
        }
        details.add("involvedAccounts", involvedAccounts);
        if (!canPay) {
            details.add("error", "Account " + lastAccount + " has insufficient funds for a split payment.");
        }
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        double splitAmount = amount / accounts.size();
        boolean canPay = true;
        for (String account : accounts) {
            Account acc = bank.getAccountWithIBAN(account);
            double accAmount = splitAmount * bank.getExchangeRate(currency, acc.getCurrency());
            if (acc.getBalance() < accAmount) {
                canPay = false;
            }
        }
        if (canPay) {
            for (String account : accounts) {
                Account acc = bank.getAccountWithIBAN(account);
                double accAmount = splitAmount * bank.getExchangeRate(currency, acc.getCurrency());
                acc.setBalance(acc.getBalance() - accAmount);
            }
        }
    }

    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }

        for (String account : accounts) {
            SplitPayment specificTransaction = new SplitPayment(this, account);
            specificTransaction.burnDetails();
            bank.addTransaction(bank.getEntryWithIBAN(account).getUser().getEmail(), specificTransaction);
        }
    }

    public String getAccount() {
        return account;
    }
}
