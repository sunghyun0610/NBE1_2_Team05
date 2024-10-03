package org.socialculture.platform.member.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.socialculture.platform.global.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
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

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberCategoryEntity> memberCategoryList = new ArrayList<>();

    public void changeName(String newName) {
        this.name = newName;
    }

}