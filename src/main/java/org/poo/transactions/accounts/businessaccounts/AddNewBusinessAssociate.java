package org.poo.transactions.accounts.businessaccounts;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;
import org.poo.utils.Constants;

public final class AddNewBusinessAssociate extends DefaultTransaction {
    private String account;
    private String email;
    private String role;

    public AddNewBusinessAssociate(CommandInput input, Bank bank) {
        super(input, bank);
        account = input.getAccount();
        email = input.getEmail();
        role = input.getRole();
    }

    protected String verify() {
        result = new JsonObject();
        result.add("timestamp", timestamp);

        if (!bank.databaseHas(account)) {
            result.add("description", "Account not found");
            return "Account not found";
        }

        if (!bank.databaseHas(email)) {
            result.add("description", "User not found");
            return "User not found";
        }

        result.add("description", "ok");
        return "ok";
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        Account acc = bank.getAccountWithIBAN(account);
        int permissionLevel = Constants.NO_LEVEL;
        switch (role) {
            case "manager":
                permissionLevel = Constants.MANAGER_LEVEL;
                break;

            case "employee":
                permissionLevel = Constants.BASIC_LEVEL;
                break;

            default:
                break;
        }
        acc.addPermission(email, permissionLevel);
    }
}
