package org.poo.bank.commerciants;

import org.poo.utils.Constants;

public final class ClothesCoupon extends DiscountCoupon {
    public ClothesCoupon() {
        super(Constants.CLOTHES_DISCOUNT);
    }

    @Override
    public String getType() {
        return "Clothes";
    }
}
