package org.poo.transactions;

import org.poo.bank.Bank;
import org.poo.fileio.CommandInput;
import org.poo.transactions.accounts.*;
import org.poo.transactions.accounts.businessaccounts.AddBusinessAccount;
import org.poo.transactions.accounts.businessaccounts.AddNewBusinessAssociate;
import org.poo.transactions.accounts.businessaccounts.ChangeDepositLimit;
import org.poo.transactions.accounts.businessaccounts.ChangeSpendingLimit;
import org.poo.transactions.cards.CheckCardStatus;
import org.poo.transactions.cards.CreateNormalCard;
import org.poo.transactions.cards.CreateOneTimeCard;
import org.poo.transactions.cards.DeleteCard;
import org.poo.transactions.payments.*;
import org.poo.transactions.payments.splitpayments.AcceptSplitPayment;
import org.poo.transactions.payments.splitpayments.RejectSplitPayment;
import org.poo.transactions.payments.splitpayments.SplitPayment;
import org.poo.transactions.payments.splitpayments.SplitPaymentCustom;
import org.poo.transactions.reports.*;

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

                    case "business" -> {
                        return new AddBusinessAccount(input, bank);
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
                switch(input.getSplitPaymentType()) {
                    case "custom" -> {
                        return new SplitPaymentCustom(input, bank);
                    }

                    case "equal" -> {
                        return new SplitPayment(input, bank);
                    }

                    default -> {
                        System.out.println("Unknown type of split payment.");
                        return null;
                    }
                }
            }

            case "acceptSplitPayment" -> {
                return new AcceptSplitPayment(input, bank);
            }

            case "rejectSplitPayment" -> {
                return new RejectSplitPayment(input, bank);
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

            case "withdrawSavings" -> {
                return new WithdrawSavings(input, bank);
            }

            case "upgradePlan" -> {
                return new UpgradePlan(input, bank);
            }

            case "cashWithdrawal" -> {
                return new CashWithdrawal(input, bank);
            }

            case "addNewBusinessAssociate" -> {
                return new AddNewBusinessAssociate(input, bank);
            }

            case "changeSpendingLimit" -> {
                return new ChangeSpendingLimit(input, bank);
            }

            case "changeDepositLimit" -> {
                return new ChangeDepositLimit(input, bank);
            }

            case "businessReport" -> {
                return new BusinessReport(input, bank);
            }

            default -> {
                return null;
            }
        }
    }
}
