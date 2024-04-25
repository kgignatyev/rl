package com.kgi.services.ratelimiter;

/**
 * allows per-user limits
 */
public interface RateSupplier {
    /**
     * @param userId
     * @return number of requests allowed per 'standard' frame
     */
    int getRate( String userId);
}
