package org.poo.bank.accounts;

public final class ClassicAccount extends Account {
    public ClassicAccount() {
        super();
    }

    @Override
    public boolean isClassic() {
        return true;
    }
}
