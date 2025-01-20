package org.poo.bank;

import org.poo.transactions.payments.splitpayments.SplitPayment;

import java.util.ArrayList;
import java.util.List;

public final class SplitPaymentContainer {
    private List<SplitPaymentProcess> processes;

    public SplitPaymentContainer() {
        processes = new ArrayList<>();
    }

    /**
     * Adds a new split payment process to the list.
     *
     * @param transaction
     */
    public void addProcess(final SplitPayment transaction) {
        processes.add(new SplitPaymentProcess(transaction));
    }

    /**
     * Returns the first split payment process of a certain type of the specified user.
     *
     * @param type
     * @param email
     * @return
     */
    public SplitPaymentProcess getFirstProcess(final String type, final String email) {
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

    /**
     * Removes the first process of a certain type of the specified user.
     *
     * @param type
     * @param email
     */
    public void removeFirstProcess(final String type, final String email) {
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
