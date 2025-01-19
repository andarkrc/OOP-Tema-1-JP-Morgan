package org.poo.transactions.payments.splitpayments;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;

import java.util.HashMap;

public final class SplitPaymentCustom extends SplitPayment {
    public SplitPaymentCustom(final CommandInput input, final Bank bank) {
        super(input, bank);
        for (int i = 0; i < accounts.size(); i++) {
            amounts.put(accounts.get(i), input.getAmountForUsers().get(i));
        }
    }

    private SplitPaymentCustom(final SplitPayment splitPayment, final String targetAccount) {
        super(splitPayment, targetAccount);
        account = targetAccount;
        amounts = splitPayment.amounts;
        currency = splitPayment.currency;
        accounts = splitPayment.accounts;
        bank = splitPayment.getBank();
        commandName = splitPayment.getCommandName();
        timestamp = splitPayment.getTimestamp();
        amount = splitPayment.amount;
        type = splitPayment.type;
    }

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }

        for (String iban : accounts) {
            SplitPayment specificTransaction = new SplitPaymentCustom(this, iban);
            specificTransaction.burnDetails();
            bank.addTransaction(bank.getEntryWithIBAN(iban).getUser().getEmail(),
                    specificTransaction);
        }
    }

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
            double actualAmount = amounts.get(iban) * bank.getExchangeRate(currency, acc.getCurrency());
            double totalAmount = bank.getTotalPrice(actualAmount, currency, email);
            if (acc.getBalance() < totalAmount) {
                firstAccount = iban;
                break;
            }
            if (acc.getMinBalance() > totalAmount) {
                firstAccount = iban;
                break;
            }
        }
        if (!firstAccount.isEmpty()) {
            details.add("error", "Account " + firstAccount + " has insufficient funds for a split payment.");
        }
        details.add("description", String.format("Split payment of %.2f %s", amount, currency));
        details.add("splitPaymentType", type);
        details.add("currency", currency);
        JsonArray accountsArray = new JsonArray();
        for (String iban : accounts) {
            Account acc = bank.getAccountWithIBAN(iban);
            accountsArray.add(iban);
        }
        details.add("involvedAccounts", accountsArray);
        JsonArray amountsArray = new JsonArray();
        for (String iban : accounts) {
            amountsArray.add(amounts.get(iban));
        }
        details.add("amountForUsers", amountsArray);
    }
}
