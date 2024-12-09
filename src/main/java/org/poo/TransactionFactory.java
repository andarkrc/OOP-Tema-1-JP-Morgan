package org.poo;

import org.poo.fileio.CommandInput;

public abstract class TransactionFactory {
    public static Transaction transactionBuild(CommandInput input, Bank bank) {
        switch (input.getCommand()) {
            case "addAccount" -> {
                switch (input.getAccountType()) {
                    case "classic" -> {
                        return new AddClassicAccount(input, bank);
                    }

                    default -> {
                        return null;
                    }
                }
            }

            case "createCard" -> {
                return new CreateNormalCard(input, bank);
            }

            case "printUsers" -> {
                return new PrintUsers(input, bank);
            }

            case "addFunds" -> {
                return new AddFunds(input, bank);
            }

            case "deleteAccount" -> {
                return new DeleteAccount(input, bank);
            }

            default -> {
                return null;
            }
        }
    }
}
