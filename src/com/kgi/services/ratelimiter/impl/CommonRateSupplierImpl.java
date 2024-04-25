package com.kgi.services.ratelimiter.impl;


import com.kgi.services.ratelimiter.RateSupplier;

/**
 * all users have the same rate
 */
public class CommonRateSupplierImpl implements RateSupplier {
    private int rate = 10;

    public CommonRateSupplierImpl(int rate) {
        this.rate = rate;
    }

    @Override
    public int getRate(String userId) {
        return rate;
    }
}
