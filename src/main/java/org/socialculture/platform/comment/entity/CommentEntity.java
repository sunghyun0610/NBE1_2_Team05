package org.socialculture.platform.comment.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.socialculture.platform.global.entity.BaseEntity;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.performance.entity.PerformanceEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//Jpa는 리플렉션을 사용해 객체를 생성하기 때문에 기본생성자가 Protected이상으로 선언되어야함.
@Table(name = "comment")
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId; // id값은 원시형이 아닌 wrapper로 사용하자

    @Column(name = "comment", unique = true, nullable = false)
    private String content;

//    @Column(name = "parent_id", nullable = true)//default null
//    private Long parentId;

    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)//Enum값을 문자열로 저장
    private CommentStatus commentStatus=CommentStatus.ACTIVE;//기본값 설정

    @ManyToOne
    @JoinColumn(name = "performance_id", nullable = false)
    private PerformanceEntity performance;


    @ManyToOne
    @JoinColumn(name = "member_id" ,nullable = false)
    private MemberEntity member;

    @OneToMany(mappedBy = "parentComment")
    @Builder.Default
    private List<CommentEntity> replies = new ArrayList<>(); //부모댓글이 가진 대댓글 리스트 가져오기 ->이를 통해 부모댓글이 자식댓글들 가져올 수 있게함

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CommentEntity parentComment;//부모 댓글참조, 대댓글이 어느 댓글의 자식인지 나타내는 필드


    // Update content and updatedAt fields
    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public void changeCommentStatus(CommentStatus commentStatus) {
        this.commentStatus = commentStatus;
    }


}

