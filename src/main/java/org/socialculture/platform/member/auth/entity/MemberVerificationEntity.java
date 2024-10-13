package org.socialculture.platform.member.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.socialculture.platform.member.entity.MemberEntity;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "member_verification")
public class MemberVerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_email", nullable = false)
    private String memberEmail;

    @Column(name = "verification_email", nullable = false, unique = true)
    private String verificationEmail;

    @OneToOne
    @JoinColumn(name = "member_email", referencedColumnName = "email", insertable = false, updatable = false)
    private MemberEntity member;
}
