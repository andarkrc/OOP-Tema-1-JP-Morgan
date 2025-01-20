package org.poo.bank.commerciants;

import lombok.Getter;

@Getter
public abstract class DiscountCoupon {
    private double discount;

    protected DiscountCoupon(final double discount) {
        this.discount = discount;
    }

    /**
     * Returns the type of the discount coupon
     *
     * @return
     */
    public abstract String getType();
}
