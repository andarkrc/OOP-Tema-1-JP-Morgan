package org.poo.bank.commerciants;

import org.poo.utils.Constants;

public final class FoodCoupon extends DiscountCoupon {
    public FoodCoupon() {
        super(Constants.FOOD_DISCOUNT);
    }

    @Override
    public String getType() {
        return "Food";
    }
}
