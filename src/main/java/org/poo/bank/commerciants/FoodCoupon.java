package org.poo.bank.commerciants;

public class FoodCoupon extends DiscountCoupon{
    public FoodCoupon() {
        super(0.02);
    }

    @Override
    public String getType() {
        return "food";
    }
}
