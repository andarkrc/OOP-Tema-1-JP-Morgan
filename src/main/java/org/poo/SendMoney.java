package org.poo;

import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;

public class SendMoney extends DefaultTransaction{
    private String account;
    private String receiver;
    private double amount;
    private String email;
    private String description;
    private String status;

    public SendMoney(CommandInput input, Bank bank) {
        super(input, bank);
        account = input.getAccount();
        receiver = input.getReceiver();
        amount = input.getAmount();
        email = input.getEmail();
        description = input.getDescription();
        status = "sent";
    }

    private SendMoney(SendMoney copy) {
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

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(account)) {
            result.add("description", "Sender account does not exist");
            return "Sender account does not exist";
        }
        if (!bank.databaseHas(receiver, email)) {
            result.add("description", "Receiver account does not exist");
            return "Receiver account does not exist";
        }
        if (bank.getEntryWithIBAN(account, email) != bank.getEntryWithEmail(email)) {
            result.add("description", "Sender account does not belong to this account");
            return "Sender account does not belong to this account";
        }
        result.add("description", "ok");
        return "ok";
    }

    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }
        Account sender = bank.getAccountWithIBAN(account);
        Account receiver = bank.getAccountWithIBAN(this.receiver, email);
        if (sender.getBalance() >= amount) {
            double receivedAmount = amount * bank.getExchangeRate(sender.getCurrency(), receiver.getCurrency());
            sender.setBalance(sender.getBalance() - amount);
            receiver.setBalance(receiver.getBalance() + receivedAmount);
        }
    }

    public void remember() {
        if (!verify().equals("ok")) {
            return;
        }

        bank.addTransaction(email, this);

        if (bank.getAccountWithIBAN(account).getBalance() < amount) {
            return;
        }

        DefaultTransaction receivedMoney = new SendMoney(this);
        receivedMoney.burnDetails();
        bank.addTransaction(bank.getEntryWithIBAN(receiver, email).getUser().getEmail(), receivedMoney);
    }

    public void burnDetails() {
        if (!verify().equals("ok")) {
            return;
        }
        details = new JsonObject();
        details.add("timestamp", timestamp);
        Account sender = bank.getAccountWithIBAN(account);
        if (sender.getBalance() >= amount) {
            details.add("description", description);
            details.add("receiverIBAN", bank.getAccountWithIBAN(receiver, email).getIban());
            details.add("senderIBAN", bank.getAccountWithIBAN(account, email).getIban());
            String amount = Double.toString(this.amount);
            if (status.equals("sent")) {
                amount += " " + bank.getAccountWithIBAN(account, email).getCurrency();
            }
            if (status.equals("received")) {
                String senCurrency = bank.getAccountWithIBAN(account, email).getCurrency();
                String recCurrency = bank.getAccountWithIBAN(receiver, email).getCurrency();
                amount = Double.toString(this.amount * bank.getExchangeRate(senCurrency, recCurrency));
                amount += " " + recCurrency;
            }
            details.add("amount", amount);
            details.add("transferType", status);
        } else {
            details.add("description", "Insufficient funds");
        }
    }

    public String getAccount() {
        if (status == "sent") {
            return account;
        } else {
            return receiver;
        }
    }
}
