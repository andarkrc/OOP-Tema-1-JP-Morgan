package org.poo.bank;

import lombok.Getter;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.database.User;
import org.poo.transactions.payments.splitpayments.SplitPayment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SplitPaymentProcess {
    private List<String> waitingList;
    private SplitPayment payment;

    public SplitPaymentProcess(SplitPayment transaction) {
        payment = transaction;
        waitingList = new ArrayList<>();
        List<String> accounts = transaction.getAccounts();
        List<String> emails = accounts.stream().map(payment.getBank()::getEntryWithIBAN)
                                                .map(DatabaseEntry::getUser)
                                                .map(User::getEmail).toList();
        for (int i = 0; i < accounts.size(); i++) {
            waitingList.add(emails.get(i));
        }
    }

    public void proceed() {
        payment.burnDetails();
        payment.remember();
        payment.execute();
    }

    public boolean canProceed() {
        return waitingList.isEmpty();
    }

    public void accept(String email) {
        waitingList.removeAll(List.of(email));
    }
}
