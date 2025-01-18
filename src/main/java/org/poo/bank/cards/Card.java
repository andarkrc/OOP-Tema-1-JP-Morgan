package org.poo.bank.cards;

import lombok.Getter;
import lombok.Setter;
import org.poo.visitors.Visitable;
import org.poo.visitors.Visitor;

@Getter
@Setter
public abstract class Card implements Visitable {
    protected String number;
    protected String status;

    public Card(final String number) {
        this.number = number;
        this.status = "active";
    }

    /**
     * Accepts a visitor and returns the data collected by it.
     * @param visitor
     * @return
     */
    public String accept(final Visitor visitor) {
        return visitor.visit(this);
    }

    /**
     * Returns whether the card is a one time card or not.
     *
     * @return
     */
    public boolean isOneTime() {
        return false;
    }
}
