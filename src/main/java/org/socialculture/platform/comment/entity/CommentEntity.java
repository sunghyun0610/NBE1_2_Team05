package org.socialculture.platform.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.socialculture.platform.global.entity.BaseEntity;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//Jpa는 리플렉션을 사용해 객체를 생성하기 때문에 기본생성자가 Protected이상으로 선언되어야함.
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private long commentId;

    @Column(name = "comment", unique = true, nullable = false)
    private String content;

    @Column(name = "parent_id", nullable = true)//default null
    private String parentId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)//Enum값을 문자열로 저장
    private CommentStatus commentStatus=CommentStatus.ACTIVE;//기본값 설정

//    @ManyToOne
//    @JoinColumn(name = "performance_id" ,nullable = false)
//    private Performance performanceId; 아직 Performance 엔티티 없어서 보류
//
//    @ManyToOne
//    @JoinColumn(name = "user_id" ,nullable = false)
//    private User userId;  아직 User 엔티티 없어서 보류


}
