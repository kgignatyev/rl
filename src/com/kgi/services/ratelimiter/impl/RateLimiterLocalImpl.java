package com.kgi.services.ratelimiter.impl;

import com.kgi.services.ratelimiter.RateLimiter;
import com.kgi.services.ratelimiter.RateSupplier;

import java.util.HashMap;
import java.util.Map;

/**
 * this implementation is JVM local and if there
 * are multiple instances of the service running
 * then user can make more requests than the limit allows
 */
public class RateLimiterLocalImpl implements RateLimiter {


    private RateSupplier rateSupplier;

    private int frameSizeInSeconds = 10;

    private Map<String, UsageBucket> usageBuckets = new HashMap<>();

    private String bucketsLock = "BUCKETS_LOCK";

    public RateLimiterLocalImpl(RateSupplier rateSupplier, int frameSizeInSeconds) {
        this.rateSupplier = rateSupplier;
        this.frameSizeInSeconds = frameSizeInSeconds;
        //todo: implement periodic sweeper to remove buckets which are not actively used by users
    }

    @Override
    public boolean isBelowLimit(String userId) {
        String lockObject = getLock( userId);
        synchronized (lockObject){
            UsageBucket currentUsage = usageBuckets.get( userId);
            if( currentUsage == null){
                currentUsage = new UsageBucket(frameSizeInSeconds, rateSupplier.getRate(userId));
                usageBuckets.put(userId, currentUsage);
            }
            return currentUsage.allowRequest( );
        }

    }

    private String getLock(String userId) {
        return bucketsLock;//
    }


}
