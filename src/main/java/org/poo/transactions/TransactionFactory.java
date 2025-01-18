package org.poo.transactions;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.accounts.AddClassicAccount;
import org.poo.transactions.accounts.AddFunds;
import org.poo.transactions.accounts.AddInterest;
import org.poo.transactions.accounts.AddSavingsAccount;
import org.poo.transactions.accounts.ChangeInterestRate;
import org.poo.transactions.accounts.DeleteAccount;
import org.poo.transactions.accounts.SetAlias;
import org.poo.transactions.accounts.SetMinimumBalance;
import org.poo.transactions.cards.CheckCardStatus;
import org.poo.transactions.cards.CreateNormalCard;
import org.poo.transactions.cards.CreateOneTimeCard;
import org.poo.transactions.cards.DeleteCard;
import org.poo.transactions.payments.PayOnline;
import org.poo.transactions.payments.SendMoney;
import org.poo.transactions.payments.SplitPayment;
import org.poo.transactions.reports.PrintTransactions;
import org.poo.transactions.reports.PrintUsers;
import org.poo.transactions.reports.Report;
import org.poo.transactions.reports.SpendingsReport;

/**
 * Class that creates a new transaction based on the provided input.
 */
public abstract class TransactionFactory {
    /**
     * Creates a new transaction based on the provided input for the provided bank.
     *
     * @param input
     * @param bank
     * @return
     */
    public static DefaultTransaction transactionBuild(final CommandInput input, final Bank bank) {
        switch (input.getCommand()) {
            case "addAccount" -> {
                switch (input.getAccountType()) {
                    case "classic" -> {
                        return new AddClassicAccount(input, bank);
                    }

                    case "savings" -> {
                        return new AddSavingsAccount(input, bank);
                    }

                    default -> {
                        return null;
                    }
                }
            }

            case "createCard" -> {
                return new CreateNormalCard(input, bank);
            }

            case "createOneTimeCard" -> {
                return new CreateOneTimeCard(input, bank);
            }

            case "deleteCard" -> {
                return new DeleteCard(input, bank);
            }

            case "printUsers" -> {
                return new PrintUsers(input, bank);
            }

            case "addFunds" -> {
                return new AddFunds(input, bank);
            }

            case "payOnline" -> {
                return new PayOnline(input, bank);
            }

            case "deleteAccount" -> {
                return new DeleteAccount(input, bank);
            }

            case "sendMoney" -> {
                return new SendMoney(input, bank);
            }

            case "setAlias" -> {
                return new SetAlias(input, bank);
            }

            case "printTransactions" -> {
                return new PrintTransactions(input, bank);
            }

            case "setMinimumBalance" -> {
                return new SetMinimumBalance(input, bank);
            }

            case "checkCardStatus" -> {
                return new CheckCardStatus(input, bank);
            }

            case "changeInterestRate" -> {
                return new ChangeInterestRate(input, bank);
            }

            case "splitPayment" -> {
                return new SplitPayment(input, bank);
            }

            case "report" -> {
                return new Report(input, bank);
            }

            case "addInterest" -> {
                return new AddInterest(input, bank);
            }

            case "spendingsReport" -> {
                return new SpendingsReport(input, bank);
            }

            default -> {
                return null;
            }
        }
    }
}
