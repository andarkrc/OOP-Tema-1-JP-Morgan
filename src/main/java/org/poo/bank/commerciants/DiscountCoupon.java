package org.poo.bank.commerciants;

import lombok.Getter;

@Getter
public abstract class DiscountCoupon {
    double discount;

    protected DiscountCoupon(double discount) {
        this.discount = discount;
    }

    public abstract String getType();
}
