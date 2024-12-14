package org.poo;

import org.poo.fileio.CommandInput;

public abstract class TransactionFactory {
    public static DefaultTransaction transactionBuild(CommandInput input, Bank bank) {
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

            default -> {
                return null;
            }
        }
    }
}
