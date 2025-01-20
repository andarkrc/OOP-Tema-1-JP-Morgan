package org.poo.bank.commerciants;

public class TechCoupon extends DiscountCoupon {
    public TechCoupon() {
        super(0.1);
    }

    @Override
    public String getType() {
        return "Tech";
    }
}
