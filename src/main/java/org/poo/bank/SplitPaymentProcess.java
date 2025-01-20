package org.poo.bank;

import lombok.Getter;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.database.User;
import org.poo.transactions.payments.splitpayments.SplitPayment;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SplitPaymentProcess {
    private List<String> involvedUsers;
    private List<String> waitingList;
    private SplitPayment payment;

    public SplitPaymentProcess(final SplitPayment transaction) {
        payment = transaction;
        waitingList = new ArrayList<>();
        involvedUsers = new ArrayList<>();
        List<String> accounts = transaction.getAccounts();
        List<String> emails = accounts.stream().map(payment.getBank()::getEntryWithIBAN)
                                                .map(DatabaseEntry::getUser)
                                                .map(User::getEmail).toList();
        for (int i = 0; i < accounts.size(); i++) {
            waitingList.add(emails.get(i));
            involvedUsers.add(emails.get(i));
        }
    }

    /**
     * Proceeds to do the split payment.
     */
    public void proceed() {
        payment.burnDetails();
        payment.remember();
        payment.execute();
    }

    /**
     * Returns true if the split payment can proceed(all users have rejected/accepted).
     *
     * @return
     */
    public boolean canProceed() {
        return waitingList.isEmpty();
    }

    /**
     * Accepts the split payment (for the user).
     *
     * @param email
     */
    public void accept(final String email) {
        waitingList.removeAll(List.of(email));
    }

    /**
     * Rejects the split payment (for the user).
     *
     * @param email
     */
    public void reject(final String email) {
        waitingList.removeAll(List.of(email));
        payment.setRejected(true);
    }
}
