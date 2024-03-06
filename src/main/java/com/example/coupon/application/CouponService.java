package com.example.coupon.application;

import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.CouponQueueHandler;
import com.example.coupon.domain.CouponRepository;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponQueueHandler couponQueueHandler;

    private final AtomicInteger couponCount = new AtomicInteger();

    public CouponService(
            final CouponRepository couponRepository,
            final CouponQueueHandler couponQueueHandler
    ) {
        this.couponRepository = couponRepository;
        this.couponQueueHandler = couponQueueHandler;
    }


    public void issue(Long userId) {
        int count = couponCount.incrementAndGet();

        if (count > 100) {
            throw new IllegalArgumentException("쿠폰 발행갯수를 초과하였습니다.");
        }

        couponQueueHandler.produce(new Coupon(userId));
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
