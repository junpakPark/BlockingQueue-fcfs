package com.example.coupon.application;

import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.CouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CouponService {

    private final CouponRepository couponRepository;

    public CouponService(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public void issue(Long userId) {
        couponRepository.save(new Coupon(userId));
    }

    @Transactional(readOnly = true)
    public Coupon getCoupon(Long couponId) {
        return couponRepository.findById(couponId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
