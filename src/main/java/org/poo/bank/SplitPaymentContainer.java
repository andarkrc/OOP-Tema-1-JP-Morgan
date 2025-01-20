package org.poo.bank;

import org.poo.transactions.payments.splitpayments.SplitPayment;

import java.util.ArrayList;
import java.util.List;

public class SplitPaymentContainer {
    List<SplitPaymentProcess> processes;

    public SplitPaymentContainer() {
        processes = new ArrayList<>();
    }

    public void addProcess(SplitPayment transaction) {
        processes.add(new SplitPaymentProcess(transaction));
    }

    public SplitPaymentProcess getFirstProcess(String type, String email) {
        // Normally, you would accept a specific payment
        // but, it isn't the case
        if (processes.isEmpty()) {
            return null;
        }
        for (SplitPaymentProcess process : processes) {
            if (process.getPayment().getType().equals(type)
                && process.getWaitingList().contains(email)) {
                return process;
            }
        }
        return null;
    }

    public void removeFirstProcess(String type, String email) {
        // Again, you would target a specific payment
        if (!processes.isEmpty()) {
            for (SplitPaymentProcess process : processes) {
                if (process.getPayment().getType().equals(type)
                    && process.getInvolvedUsers().contains(email)) {
                    processes.remove(process);
                    return;
                }
            }
        }
    }
}
