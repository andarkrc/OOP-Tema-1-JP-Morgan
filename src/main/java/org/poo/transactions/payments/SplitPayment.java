package org.poo.transactions.payments;

import lombok.Getter;
import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

import java.util.List;

public final class SplitPayment extends DefaultTransaction {
    private List<String> accounts;
    private double amount;
    private String currency;
    @Getter
    private String account;

    public SplitPayment(final CommandInput input, final Bank bank) {
        super(input, bank);
        accounts = input.getAccounts();
        amount = input.getAmount();
        currency = input.getCurrency();
    }

    private SplitPayment(final SplitPayment splitPayment, final String targetAccount) {
        this.account = targetAccount;
        amount = splitPayment.amount;
        currency = splitPayment.currency;
        accounts = splitPayment.accounts;
        bank = splitPayment.bank;
        commandName = splitPayment.commandName;
        timestamp = splitPayment.timestamp;
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        for (String iban : accounts) {
            if (!bank.databaseHas(iban)) {
                result.add("description", "Account " + iban + " does not exist");
                return "Account does not exist";
            }
        }
        result.add("description", "ok");
        return "ok";
    }

    @Override
    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }

        double splitAmount = amount / accounts.size();
        boolean canPay = true;
        String lastAccount = "";
        for (String iban : accounts) {
            Account acc = bank.getAccountWithIBAN(iban);
            double accAmount = splitAmount * bank.getExchangeRate(currency, acc.getCurrency());
            if (acc.getBalance() < accAmount) {
                canPay = false;
                lastAccount = iban;
            }
        }
        details = new JsonObject();
        details.add("timestamp", timestamp);
        details.add("description", String.format("Split payment of %.2f %s", amount, currency));
        details.add("amount", splitAmount);
        details.add("currency", currency);
        JsonArray involvedAccounts = new JsonArray();
        for (String iban : accounts) {
            involvedAccounts.add(iban);
        }
        details.add("involvedAccounts", involvedAccounts);
        if (!canPay) {
            details.add("error", "Account " + lastAccount
                        + " has insufficient funds for a split payment.");
        }
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        double splitAmount = amount / accounts.size();
        boolean canPay = true;
        for (String iban : accounts) {
            Account acc = bank.getAccountWithIBAN(iban);
            double accAmount = splitAmount * bank.getExchangeRate(currency, acc.getCurrency());
            if (acc.getBalance() < accAmount) {
                canPay = false;
            }
        }
        if (canPay) {
            for (String iban : accounts) {
                Account acc = bank.getAccountWithIBAN(iban);
                double accAmount = splitAmount * bank.getExchangeRate(currency, acc.getCurrency());
                acc.setBalance(acc.getBalance() - accAmount);
            }
        }
    }

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }

        for (String iban : accounts) {
            SplitPayment specificTransaction = new SplitPayment(this, iban);
            specificTransaction.burnDetails();
            bank.addTransaction(bank.getEntryWithIBAN(iban).getUser().getEmail(),
                                specificTransaction);
        }
    }
}
