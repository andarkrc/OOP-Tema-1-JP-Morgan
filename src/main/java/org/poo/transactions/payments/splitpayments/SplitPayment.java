package org.poo.transactions.payments.splitpayments;

import lombok.Getter;
import lombok.Setter;
import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplitPayment extends DefaultTransaction {
    @Getter
    protected List<String> accounts;
    protected Map<String, Double> amounts;
    @Getter
    protected double amount;
    protected String currency;
    @Getter
    protected String type;
    @Getter
    protected String account;
    @Setter
    protected boolean rejected;

    public SplitPayment(final CommandInput input, final Bank bank) {
        super(input, bank);
        accounts = input.getAccounts();
        currency = input.getCurrency();
        amount = input.getAmount();
        type = input.getSplitPaymentType();
        amounts = new HashMap<>();
        for (String acc : accounts) {
            amounts.put(acc, amount / accounts.size());
        }
        rejected = false;
    }

    protected SplitPayment(final SplitPayment splitPayment, final String targetAccount) {
        account = targetAccount;
        amounts = splitPayment.amounts;
        currency = splitPayment.currency;
        accounts = splitPayment.accounts;
        bank = splitPayment.bank;
        commandName = splitPayment.commandName;
        timestamp = splitPayment.timestamp;
        amount = splitPayment.amount;
        type = splitPayment.type;
        rejected = splitPayment.rejected;
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
        for (String iban : accounts) {
            if (!bank.databaseHas(iban)) {
                result.add("description", "Account " + iban + " does not exist");
                return "Account does not exist";
            }
        }
        result.add("description", "ok");
        return "ok";
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
        String firstAccount = "";
        for (String iban : accounts) {
            Account acc = bank.getAccountWithIBAN(iban);
            String email = bank.getEntryWithIBAN(iban).getUser().getEmail();
            double actualAmount = amounts.get(iban)
                    * bank.getExchangeRate(currency, acc.getCurrency());
            double totalAmount = bank.getTotalPrice(actualAmount, currency, iban);
            if (acc.isSavings()) {
                totalAmount = actualAmount;
            }
            if (acc.getBalance() < totalAmount) {
                firstAccount = iban;
                break;
            }
            if (acc.getMinBalance() > totalAmount) {
                firstAccount = iban;
                break;
            }
        }
        details.add("amount", amount / accounts.size());
        details.add("description", String.format("Split payment of %.2f %s", amount, currency));
        details.add("splitPaymentType", type);
        details.add("currency", currency);
        JsonArray accountsArray = new JsonArray();
        for (String iban : accounts) {
            accountsArray.add(iban);
        }
        details.add("involvedAccounts", accountsArray);
        if (rejected) {
            details.add("error", "One user rejected the payment.");
            return;
        }
        if (!firstAccount.isEmpty()) {
            details.add("error", "Account " + firstAccount
                    + " has insufficient funds for a split payment.");
        }
    }

    /**
     * Executes the command if there are no errors that could cause crashes.
     */
    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }
        if (rejected) {
            return;
        }
        for (String iban : accounts) {
            Account acc = bank.getAccountWithIBAN(iban);
            double actualAmount = amounts.get(iban) * bank.getExchangeRate(currency,
                    acc.getCurrency());
            double totalAmount = bank.getTotalPrice(actualAmount, currency, iban);
            if (acc.isSavings()) {
                totalAmount = actualAmount;
            }
            if (acc.getBalance() < totalAmount) {
                return;
            }
            if (acc.getMinBalance() > totalAmount) {
                return;
            }
        }

        for (String iban : accounts) {
            Account acc = bank.getAccountWithIBAN(iban);
            double actualAmount = amounts.get(iban) * bank.getExchangeRate(currency,
                    acc.getCurrency());
            double totalAmount = bank.getTotalPrice(actualAmount, currency, iban);
            if (acc.isSavings()) {
                totalAmount = actualAmount;
            }
            acc.setBalance(acc.getBalance() - totalAmount);
        }
    }

    /**
     * Keeps track of the transaction in some way (usually in a transaction history).
     */
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
