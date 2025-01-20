package org.poo.bank.commerciants;

import org.poo.utils.Constants;

public final class TechCoupon extends DiscountCoupon {
    public TechCoupon() {
        super(Constants.TECH_DISCOUNT);
    }

    @Override
    public String getType() {
        return "Tech";
    }
}
