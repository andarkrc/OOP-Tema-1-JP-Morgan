package org.poo.bank.commerciants;

public class ClothesCoupon extends DiscountCoupon{
    public ClothesCoupon() {
        super(0.05);
    }

    @Override
    public String getType() {
        return "clothes";
    }
}
