package com.example.coupon.infrastructure;

import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.CouponQueueHandler;
import com.example.coupon.domain.CouponRepository;
import jakarta.annotation.PostConstruct;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.stereotype.Component;

@Component
public class DefaultCouponQueueHandler implements CouponQueueHandler {

    private final BlockingQueue<Coupon> queue = new LinkedBlockingQueue<>();
    private final CouponRepository couponRepository;

    public DefaultCouponQueueHandler(final CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @PostConstruct
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

    public void produce(Coupon coupon) {
        try {
            queue.put(coupon);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
