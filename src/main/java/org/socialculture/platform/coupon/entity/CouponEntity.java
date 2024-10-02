package org.socialculture.platform.coupon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.socialculture.platform.global.entity.BaseEntity;
import org.socialculture.platform.member.entity.MemberEntity;

import java.time.LocalDateTime;

/**
 * 쿠폰 엔티티
 *
 * @author ycjung
 */
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon")
public class CouponEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "percent", nullable = false)
    private int percent;

    @Column(name = "is_used", nullable = false)
    private boolean isUsed;

    @Column(name = "expire_time", nullable = false)
    private LocalDateTime expireTime;

    public void setUsed(boolean used) {
        this.isUsed = used;
    }
}
