package com.example.coupon.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.coupon.domain.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CouponServiceTest {

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponRepository couponRepository;


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
}
