package com.kgi.services.ratelimiter;

import com.kgi.services.ratelimiter.impl.CommonRateSupplierImpl;
import com.kgi.services.ratelimiter.impl.RateLimiterLocalImpl;

public class RateLimiterTest {

    public static void main(String[] args) {
        RateSupplier rateSupplier = new CommonRateSupplierImpl(10);
        RateLimiter rateLimiter = new RateLimiterLocalImpl(rateSupplier, 10);
        int expectedCalls = 30;

        CallerSimulator caller1 = new CallerSimulator(rateLimiter, "user1");
        CallerSimulator caller2 = new CallerSimulator(rateLimiter, "user2");

        caller1.start();
        caller2.start();

        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("User 1 called " + caller1.count + " times");
        System.out.println("User 2 called " + caller2.count + " times");
        if( caller1.count != expectedCalls) {
            throw new RuntimeException("User 1 called " + caller1.count + " times, expected " + expectedCalls);
        }
        if( caller2.count != expectedCalls) {
            throw new RuntimeException("User 2 called " + caller2.count + " times, expected " + expectedCalls);
        }

    }

    private static class CallerSimulator extends Thread {

        private RateLimiter rateLimiter;
        private String userId;
        int count = 0;
        public CallerSimulator(RateLimiter rateLimiter, String userId) {
            setDaemon(true);
            this.rateLimiter = rateLimiter;
            this.userId = userId;
        }

        @Override
        public void run() {

            while (true) {
                if (rateLimiter.isBelowLimit(userId)) {
                    System.out.println("User " + userId + " is below limit, call is allowed");
                    count++;
                } else {
                    System.out.println("User " + userId + " is above limit, sleeping");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }

    }
}
