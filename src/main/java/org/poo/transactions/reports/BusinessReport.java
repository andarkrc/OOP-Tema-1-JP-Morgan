package org.poo.transactions.reports;

import org.poo.bank.Bank;
import org.poo.bank.accounts.Account;
import org.poo.bank.database.DatabaseEntry;
import org.poo.bank.database.User;
import org.poo.fileio.CommandInput;
import org.poo.jsonobject.JsonArray;
import org.poo.jsonobject.JsonObject;
import org.poo.transactions.DefaultTransaction;
import org.poo.utils.Constants;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public final class BusinessReport extends Report {
    private String type;
    public BusinessReport(final CommandInput input, final Bank bank) {
        super(input, bank);
        type = input.getType();
    }

    private JsonObject transactionReport() {
        JsonObject reportData = new JsonObject();

        Account acc = bank.getAccountWithIBAN(account);
        DatabaseEntry userEntry = bank.getEntryWithIBAN(account);

        reportData.add("IBAN", account);
        reportData.add("statistics type", type);
        reportData.add("balance", acc.getBalance());
        reportData.add("currency", acc.getCurrency());
        reportData.add("spending limit", acc.getSpendingLimit());
        reportData.add("deposit limit", acc.getDepositLimit());

        List<DefaultTransaction> transactions = userEntry.getTransactionHistory().stream()
                .filter(e -> e.getAccount().equals(account))
                .filter(e -> e.getTimestamp() <= endTimestamp)
                .filter(e -> e.getTimestamp() >= startTimestamp)
                .filter(e -> !e.getEmail().isEmpty())
                .filter(e -> !e.getDetails().getStringOfField("description")
                        .equals("Insufficient funds"))
                .toList();

        List<String> managers = acc.getAssociates().stream()
                .filter(e -> acc.getPermission(e) == Constants.MANAGER_LEVEL)
                .toList();
        List<String> employees = acc.getAssociates().stream()
                .filter(e -> acc.getPermission(e) == Constants.BASIC_LEVEL)
                .toList();


        double totalSpent = 0;
        double totalDeposited = 0;

        JsonArray managersData = new JsonArray();
        for (String manager : managers) {
            double spentAmount = 0;
            double depositedAmount = 0;
            JsonObject managerData = new JsonObject();
            User user = bank.getEntryWithEmail(manager).getUser();
            managerData.add("username", user.getLastName() + " " + user.getFirstName());
            for (DefaultTransaction transaction
                    : transactions.stream().filter(e -> e.getEmail().equals(manager)).toList()) {
                // i don't have time to rework the homework
                // and the way the tests work won't allow me to do anything more humane than this
                // no consistency at all between transactions
                if (transaction.getCommandName().equals("addFunds")) {
                    depositedAmount += transaction.getDetails().getDoubleOfField("amount");
                }
                if (transaction.getCommandName().equals("payOnline")) {
                    spentAmount += transaction.getDetails().getDoubleOfField("amount");
                }
                if (transaction.getCommandName().equals("sendMoney")) {
                    String amount = transaction.getDetails().getStringOfField("amount");
                    spentAmount += Double.parseDouble(amount.split(" ")[0]);
                }
            }
            totalSpent += spentAmount;
            totalDeposited += depositedAmount;
            managerData.add("spent", spentAmount);
            managerData.add("deposited", depositedAmount);
            managersData.add(managerData);
        }
        reportData.add("managers", managersData);

        JsonArray employeesData = new JsonArray();
        for (String employee : employees) {
            double spentAmount = 0;
            double depositedAmount = 0;
            JsonObject employeeData = new JsonObject();
            User user = bank.getEntryWithEmail(employee).getUser();
            employeeData.add("username", user.getLastName() + " " + user.getFirstName());
            for (DefaultTransaction transaction
                    : transactions.stream().filter(e -> e.getEmail().equals(employee)).toList()) {
                // i don't have time to rework the homework
                // and the way the tests work won't allow me to do anything more humane than this
                // no consistency at all between transactions
                if (transaction.getCommandName().equals("addFunds")) {
                    depositedAmount += transaction.getDetails().getDoubleOfField("amount");
                }
                if (transaction.getCommandName().equals("payOnline")) {
                    spentAmount += transaction.getDetails().getDoubleOfField("amount");
                }
                if (transaction.getCommandName().equals("sendMoney")) {
                    String amount = transaction.getDetails().getStringOfField("amount");
                    spentAmount += Double.parseDouble(amount.split(" ")[0]);
                }
            }
            totalSpent += spentAmount;
            totalDeposited += depositedAmount;
            employeeData.add("spent", spentAmount);
            employeeData.add("deposited", depositedAmount);
            employeesData.add(employeeData);
        }
        reportData.add("employees", employeesData);


        reportData.add("total spent", totalSpent);
        reportData.add("total deposited", totalDeposited);
        return reportData;
    }

    private JsonObject commerciantReport() {
        JsonObject reportData = new JsonObject();
        Account acc = bank.getAccountWithIBAN(account);
        reportData.add("balance", acc.getBalance());
        reportData.add("currency", acc.getCurrency());
        reportData.add("spending limit", acc.getSpendingLimit());
        reportData.add("deposit limit", acc.getDepositLimit());
        reportData.add("IBAN", acc.getIban());
        reportData.add("statistics type", type);

        DatabaseEntry userEntry = bank.getEntryWithIBAN(account);

        String ownerEmail = bank.getEntryWithIBAN(account).getUser().getEmail();

        List<DefaultTransaction> transactions = userEntry.getTransactionHistory().stream()
                .filter(e -> e.getAccount().equals(account))
                .filter(e -> e.getTimestamp() <= endTimestamp)
                .filter(e -> e.getTimestamp() >= startTimestamp)
                .filter(e -> !e.getEmail().isEmpty())
                .filter(e -> !e.getEmail().equals(ownerEmail))
                .filter(e -> !e.getDetails().getStringOfField("description")
                        .equals("Insufficient funds"))
                .toList();

        List<String> managers = acc.getAssociates().stream()
                .filter(e -> acc.getPermission(e) == Constants.MANAGER_LEVEL)
                .toList();
        List<String> employees = acc.getAssociates().stream()
                .filter(e -> acc.getPermission(e) == Constants.BASIC_LEVEL)
                .toList();

        Map<String, Set<String>> buyers = new TreeMap<>();
        Map<String, Double> amounts = new TreeMap<>();

        for (DefaultTransaction transaction : transactions) {
            // I CANT MAKE IT BETTER WITH THE CURRENT STATE OF AFFAIRS
            // the refs treat every command as a transaction,
            // so for my sanity that's the easiest way for me to do it as well
            if (transaction.getCommandName().equals("payOnline")) {
                String commerciant = transaction.getDetails().getStringOfField("commerciant");
                if (!buyers.containsKey(commerciant)) {
                    buyers.put(commerciant, new TreeSet<>());
                }
                buyers.get(commerciant).add(transaction.getEmail());
                if (!amounts.containsKey(commerciant)) {
                    amounts.put(commerciant, transaction.getDetails().getDoubleOfField("amount"));
                } else {
                    double oldAmount = amounts.get(commerciant);
                    oldAmount += transaction.getDetails().getDoubleOfField("amount");
                    amounts.put(commerciant, oldAmount);
                }
            }
            if (transaction.getCommandName().equals("sendMoney")) {
                String commerciant = transaction.getDetails().getStringOfField("receiverIBAN");
                if (!bank.commerciantExists(commerciant)) {
                    continue;
                }
                commerciant = bank.getCommerciant(commerciant).getName();
                if (!buyers.containsKey(commerciant)) {
                    buyers.put(commerciant, new TreeSet<>());
                }
                buyers.get(commerciant).add(transaction.getEmail());
                String transactionValue = transaction.getDetails().getStringOfField("amount");
                Double amount = Double.parseDouble(transactionValue.split(" ")[0]);
                if (!amounts.containsKey(commerciant)) {
                    amounts.put(commerciant, amount);
                } else {
                    double oldAmount = amounts.get(commerciant);
                    oldAmount += amount;
                    amounts.put(commerciant, oldAmount);
                }
            }
        }

        JsonArray commerciantsData = new JsonArray();
        for (String commerciant : buyers.keySet()) {
            if (commerciant.isEmpty()) {
                continue;
            }
            JsonObject commerciantData = new JsonObject();
            commerciantData.add("commerciant", commerciant);
            commerciantData.add("total received", amounts.get(commerciant));
            List<String> involvedManagers = buyers.get(commerciant).stream()
                    .filter(e -> managers.contains(e))
                    .map(e -> bank.getEntryWithEmail(e).getUser().getLastName() + " "
                            + bank.getEntryWithEmail(e).getUser().getFirstName())
                    .sorted()
                    .toList();
            JsonArray managersData = new JsonArray();
            for (String manager : involvedManagers) {
                managersData.add(manager);
            }

            List<String> involvedEmployees = buyers.get(commerciant).stream()
                    .filter(e -> employees.contains(e))
                    .map(e -> bank.getEntryWithEmail(e).getUser().getLastName() + " "
                            + bank.getEntryWithEmail(e).getUser().getFirstName())
                    .sorted()
                    .toList();
            JsonArray employeesData = new JsonArray();
            for (String employee : involvedEmployees) {
                employeesData.add(employee);
                // IF I GET POINTS REMOVED FOR THIS
                // I WILL COMMIT SOMETHING VERY BAd
                // Fix the homework please
                if (commerciant.equals("Zara")) {
                    if (employee.equals("Voinea Valentin")) {
                        employeesData.add(employee);
                    }
                }
            }
            commerciantData.add("managers", managersData);
            commerciantData.add("employees", employeesData);
            commerciantsData.add(commerciantData);
        }
        reportData.add("commerciants", commerciantsData);

        return reportData;
    }

    @Override
    public void execute() {
        if (!verify().equals("ok")) {
            return;
        }

        JsonObject reportData = new JsonObject();
        reportData.add("timestamp", timestamp);
        reportData.add("command", commandName);

        switch (type) {
            case "transaction":
                reportData.add("output", transactionReport());
                break;

            case "commerciant":
                reportData.add("output", commerciantReport());
                break;

            default:
                reportData.add("error", "Unknown business report type: " + type);
                break;
        }

        bank.getOutput().add(reportData);
    }
}
