package com.example.coupon.infrastructure;

import com.example.coupon.domain.Coupon;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CouponBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public CouponBatchRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveAll(final List<Coupon> coupons) {
        String sql = "INSERT INTO coupon (user_id) VALUES (?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                Coupon coupon = coupons.get(i);
                ps.setLong(1, coupon.getUserId());
            }

            @Override
            public int getBatchSize() {
                return coupons.size();
            }
        });

    }
}
