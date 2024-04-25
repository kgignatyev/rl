package com.kgi.services.ratelimiter;

public interface RateLimiter {

    boolean isBelowLimit(String userId);

}
