package org.socialculture.platform.member.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.socialculture.platform.global.entity.BaseEntity;

@Entity
@Table(name = "member")
@Getter
@Setter
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private SocialProvider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role;

    public enum MemberRole {
        USER, PADMIN, ADMIN
    }

    public enum SocialProvider {
        LOCAL, NAVER, KAKAO
    }
}