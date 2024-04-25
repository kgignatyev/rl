package com.kgi.services.ratelimiter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this is not a thread safe class, should be used carefully
 */
class UsageBucket {
    private final int limit;
    int frameSize = 0;
    List<Long> requests = new ArrayList<>();

    public UsageBucket(int frameSizeInSeconds, int limit) {
        this.frameSize = frameSizeInSeconds;
        this.limit = limit;
    }

    public boolean allowRequest() {
        long currentTime = System.currentTimeMillis();
        boolean allow;
        long startFrameTimestamp = currentTime - frameSize * 1000L;
        //let's optimize a bit
        if (requests.isEmpty() ||  requests.getLast() < startFrameTimestamp) {
            requests.clear();
            allow = true;
        } else {
            requests = requests.stream().filter((ts) -> ts > startFrameTimestamp).collect(Collectors.toList());
            allow = requests.size() +1 <= limit;
        }
        if( allow ){
            requests.add( currentTime);
        }
        return allow;
    }
}
