package org.poo.bank.commerciants;

import org.poo.fileio.CommerciantInput;

public class Commerciant {
    private String name;
    private int id;
    private String account;
    private String type;
    private String cashbackStrategy;

    public Commerciant(CommerciantInput input) {
        name = input.getCommerciant();
        id = input.getId();
        account = input.getAccount();
        type = input.getType();
        cashbackStrategy = input.getCashbackStrategy();
    }
}
