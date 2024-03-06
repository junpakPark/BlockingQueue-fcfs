package com.example.coupon.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.coupon.domain.CouponRepository;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponRepository couponRepository;

    @AfterEach
    void tearDown() {
        couponService.resetCount();
        couponRepository.deleteAll();
    }

    @Test
    void apply() {
        //given
        long userId = 1L;

        // when
        couponService.issue(userId);

        // then
        long count = couponRepository.count();
        assertThat(count).isEqualTo(1L);
    }

    @Test
    void apply_concurrent() throws InterruptedException {
        // given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    couponService.issue(userId);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        // then
        long count = couponRepository.count();

        assertThat(count).isEqualTo(100L);
    }
}
