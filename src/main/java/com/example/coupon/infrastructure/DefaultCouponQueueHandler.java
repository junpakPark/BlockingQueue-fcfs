package com.example.coupon.infrastructure;

import com.example.coupon.domain.Coupon;
import com.example.coupon.domain.CouponQueueHandler;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.springframework.stereotype.Component;

@Component
public class DefaultCouponQueueHandler implements CouponQueueHandler {

    private final BlockingQueue<Coupon> queue = new LinkedBlockingQueue<>();
    private final CouponBatchRepository couponBatchRepository;

    public DefaultCouponQueueHandler(final CouponBatchRepository couponBatchRepository) {
        this.couponBatchRepository = couponBatchRepository;
    }

    @PostConstruct
    public void consume() {
        new Thread(() -> {
            while (true) {
                try {
                    if (queue.isEmpty()) {
                        Thread.sleep(50);
                        continue;
                    }

                    List<Coupon> coupons = extractUniqueCoupons();
                    couponBatchRepository.saveAll(coupons);

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

    private List<Coupon> extractUniqueCoupons() {
        List<Coupon> allCoupons = new ArrayList<>();
        queue.drainTo(allCoupons);

        return allCoupons.stream().
                distinct()
                .limit(100L)
                .toList();
    }

}
