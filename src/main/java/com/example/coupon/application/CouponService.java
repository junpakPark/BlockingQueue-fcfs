package com.example.coupon.application;

import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.CouponRepository;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final BlockingQueue<Coupon> queue = new LinkedBlockingQueue<>();

    private final AtomicInteger couponCount = new AtomicInteger();

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @PostConstruct
    @Transactional
    public void consume() {
        new Thread(() -> {
            while (true) {
                try {
                    Coupon coupon = queue.take();
                    couponRepository.save(coupon);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }


    public void issue(Long userId) {
        int count = couponCount.incrementAndGet();

        if (count > 100) {
            throw new IllegalArgumentException("쿠폰 발행갯수를 초과하였습니다.");
        }

        try {
            queue.put(new Coupon(userId));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public void resetCount() {
        couponCount.set(0);
    }
}
