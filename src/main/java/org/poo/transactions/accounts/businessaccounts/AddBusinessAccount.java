package org.poo.transactions.accounts.businessaccounts;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.bank.accounts.BusinessAccount;
import org.poo.fileio.CommandInput;
import org.poo.transactions.accounts.AddAccount;
import org.poo.utils.Constants;

public class AddBusinessAccount extends AddAccount {
    public AddBusinessAccount(CommandInput input, Bank bank) {
        super(input, bank);
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }
        double rate = bank.getExchangeRate("RON", currency);
        Account account = new BusinessAccount();
        account.setAccountType(accountType)
                .setCurrency(currency)
                .setIban(iban)
                .setSpendingLimit(Constants.DEFAULT_SPENDING_LIMIT * rate)
                .setDepositLimit(Constants.DEFAULT_DEPOSIT_LIMIT * rate);
        account.addPermission(email, Constants.OWNER_LEVEL);
        bank.addAccount(email, account);
    }
}
