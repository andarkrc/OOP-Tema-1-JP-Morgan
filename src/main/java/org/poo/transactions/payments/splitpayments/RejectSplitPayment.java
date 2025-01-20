package org.poo.transactions.payments.splitpayments;

import org.poo.bank.Bank;
import org.poo.bank.SplitPaymentProcess;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;

public final class RejectSplitPayment extends DefaultTransaction {
    private String email;
    private String type;

    public RejectSplitPayment(final CommandInput input, final Bank bank) {
        super(input, bank);
        email = input.getEmail();
        type = input.getSplitPaymentType();
    }

    @Override
    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);
        if (!bank.isValidUser(email)) {
            result.add("description", "User not found");
            return "User not found";
        }
        SplitPaymentProcess process = bank.getSplitPayments().getFirstProcess(type, email);
        if (process == null) {
            result.add("description", "No payment to accept");
            return "No payment to accept";
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

        SplitPaymentProcess process = bank.getSplitPayments().getFirstProcess(type, email);

        //reject the payment
        process.reject(email);
        process.proceed();
        bank.getSplitPayments().removeFirstProcess(type, email);
    }
}
