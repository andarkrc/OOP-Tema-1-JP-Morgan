package org.poo.transactions.payments.splitpayments;

import org.poo.bank.Bank;
import org.poo.bank.SplitPaymentProcess;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class AcceptSplitPayment extends DefaultTransaction {
    private String email;
    private String type;

    public AcceptSplitPayment(CommandInput input, Bank bank) {
        super(input, bank);
        email = input.getEmail();
        type = input.getSplitPaymentType();
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.databaseHas(email)) {
            result.add("description", "User not found");
            return "User not found";
        }
        SplitPaymentProcess process = bank.getSplitPayments().getFirstProcess(type);
        if (process == null) {
            result.add("description", "No payment to accept");
            return "No payment to accept";
        }

        result.add("description", "ok");
        return "ok";
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        // Why do you accept using the email, not the iban????
        // Who am I, the guy who splits among his own accounts??
        SplitPaymentProcess process = bank.getSplitPayments().getFirstProcess(type);
        process.accept(email);

        if (process.canProceed()) {
            process.proceed();
            bank.getSplitPayments().removeFirstProcess(type);
        }
    }
}
