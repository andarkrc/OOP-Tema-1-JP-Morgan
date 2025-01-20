package org.poo.transactions.accounts;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class UpgradePlan extends DefaultTransaction {
    private String account;
    private String newPlanType;

    public UpgradePlan(CommandInput input, Bank bank) {
        super(input, bank);
        account = input.getAccount();
        newPlanType = input.getNewPlanType();
    }

    @Override
    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }
        details = new JsonObject();
        details.add("timestamp", timestamp);
        Account acc = bank.getAccountWithIBAN(account);
        String currentPlan = bank.getEntryWithIBAN(account).getPlan();
        if (!bank.getUpgradeMap().containsKey(currentPlan + "-" + newPlanType)) {
            if (newPlanType.equals(currentPlan)) {
                details.add("description", "The user already has the " + newPlanType + " plan.");
            }
            return;
        }
        double amount = bank.getUpgradeMap().get(currentPlan + "-" + newPlanType)
                        * bank.getExchangeRate("RON", acc.getCurrency());
        double totalAmount = bank.getTotalPrice(amount, acc.getCurrency(), bank.getEntryWithIBAN(account).getUser().getEmail());
        if (acc.getBalance() <amount) {
            details.add("description", "Insufficient funds");
            return;
        }
        details.add("description", "Upgrade plan");
        details.add("accountIBAN", account);
        details.add("newPlanType", newPlanType);
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(account)) {
            result.add("description", "Account not found");
            return "Account not found";
        }

        return "ok";
    }

    @Override
    public boolean hasLoggableError() {
        if (verify().equals("Account not found")) {
            return true;
        }

        return false;
    }

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }
        bank.getEntryWithIBAN(account).addTransaction(this);
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }
        Account acc = bank.getAccountWithIBAN(account);
        String currentPlan = bank.getEntryWithIBAN(account).getPlan();
        if (!bank.getUpgradeMap().containsKey(currentPlan + "-" + newPlanType)) {
            // Can't upgrade
            return;
        }
        double amount = bank.getUpgradeMap().get(currentPlan + "-" + newPlanType)
                        * bank.getExchangeRate("RON", acc.getCurrency());
        double totalAmount = bank.getTotalPrice(amount, acc.getCurrency(), bank.getEntryWithIBAN(account).getUser().getEmail());
        if (acc.getBalance() < amount) {
            // not enough funds
            return;
        }
        bank.getEntryWithIBAN(account).setPlan(newPlanType);
        acc.setBalance(acc.getBalance() - amount);
        //System.out.println("Upgraded from " + currentPlan + " to " + newPlanType + " for " + totalAmount);
    }

    @Override
    public String getAccount() {
        return account;
    }
}
