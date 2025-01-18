package org.poo.transactions.payments;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class SendMoney extends DefaultTransaction {
    private String account;
    private String receiver;
    private double amount;
    private String email;
    private String description;
    private String status;

    public SendMoney(final CommandInput input, final Bank bank) {
        super(input, bank);
        account = input.getAccount();
        receiver = input.getReceiver();
        amount = input.getAmount();
        email = input.getEmail();
        description = input.getDescription();
        status = "sent";
    }

    private SendMoney(final SendMoney copy) {
        commandName = copy.getCommandName();
        timestamp = copy.getTimestamp();
        bank = copy.getBank();
        account = copy.account;
        receiver = copy.receiver;
        amount = copy.amount;
        email = copy.email;
        description = copy.description;
        status = "received";
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "User not found");
            return "User not found";
        }
        if (!bank.databaseHas(account)) {
            result.add("description", "User not found");
            return "User not found";
        }
        if (!bank.databaseHas(receiver, email) && !bank.commerciantExists(receiver)) {
            result.add("description", "User not found");
            return "User not found";
        }
        if (bank.getEntryWithIBAN(account, email) != bank.getEntryWithEmail(email)) {
            result.add("description", "Sender account does not belong to this account");
            return "Sender account does not belong to this account";
        }
        result.add("description", "ok");
        return "ok";
    }

    @Override
    public boolean hasLoggableError() {
        if (verify().equals("User not found")) {
            return true;
        }

        return false;
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }
        Account sender = bank.getAccountWithIBAN(account);
        Account receiverAcc = bank.getAccountWithIBAN(this.receiver, email);
        double totalAmount = bank.getTotalPrice(amount, sender.getCurrency(), email);
        if (sender.getBalance() >= totalAmount) {
            String receiverCurrency = "RON";
            if (!bank.commerciantExists(receiver)) {
                receiverCurrency = receiverAcc.getCurrency();
            }
            double exchangeRate = bank.getExchangeRate(sender.getCurrency(),
                                                    receiverCurrency);
            double receivedAmount = amount * exchangeRate;
            sender.setBalance(sender.getBalance() - totalAmount);
            if (!bank.commerciantExists(receiver)) {
                receiverAcc.setBalance(receiverAcc.getBalance() + receivedAmount);
            } else {
                //cash back
                String commerciantName = bank.getCommerciant(receiver).getName();
                sender.addFunds(bank.getCashBack(amount, sender.getCurrency(), sender.getIban(), commerciantName));
            }
        }
    }

    @Override
    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.addTransaction(email, this);
        Account sender = bank.getAccountWithIBAN(account);
        double totalAmount = bank.getTotalPrice(amount, sender.getCurrency(), email);
        if (bank.getAccountWithIBAN(account).getBalance() < totalAmount) {
            return;
        }

        if (!bank.commerciantExists(receiver)) {
            DefaultTransaction receivedMoney = new SendMoney(this);
            receivedMoney.burnDetails();
            bank.addTransaction(bank.getEntryWithIBAN(receiver, email).getUser().getEmail(),
                    receivedMoney);
        }
    }

    @Override
    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }
        details = new JsonObject();
        details.add("timestamp", timestamp);
        Account sender = bank.getAccountWithIBAN(account);
        double totalAmount = bank.getTotalPrice(amount, sender.getCurrency(), email);
        if (sender.getBalance() >= totalAmount) {
            details.add("description", description);
            if (!bank.commerciantExists(receiver)) {
                details.add("receiverIBAN", bank.getAccountWithIBAN(receiver, email).getIban());
            } else {
                details.add("receiverIBAN", receiver);
            }
            details.add("senderIBAN", bank.getAccountWithIBAN(account, email).getIban());
            String transferedAmount = Double.toString(this.amount);
            if (status.equals("sent")) {
                transferedAmount += " " + bank.getAccountWithIBAN(account, email).getCurrency();
            }
            if (status.equals("received")) {
                String senCurrency = bank.getAccountWithIBAN(account, email).getCurrency();
                String recCurrency = bank.getAccountWithIBAN(receiver, email).getCurrency();
                double exchangeRate = bank.getExchangeRate(senCurrency, recCurrency);
                transferedAmount = Double.toString(this.amount * exchangeRate);
                transferedAmount += " " + recCurrency;
            }
            details.add("amount", transferedAmount);
            details.add("transferType", status);
        } else {
            details.add("description", "Insufficient funds");
        }
    }

    @Override
    public String getAccount() {
        if (status.equals("sent")) {
            return account;
        } else {
            return receiver;
        }
    }

    @Override
    public boolean isPayment() {
        if (status.equals("sent")) {
            return true;
        }
        return false;
    }
}
