package com.travel.enums;

public enum SubscriptionType {
    STANDARD(0d),
    GOLD(10d),
    PREMIUM(100d);

    private final double discountPercentage;

    SubscriptionType(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getDiscountPercentage() {
        return discountPercentage/100;
    }

}
