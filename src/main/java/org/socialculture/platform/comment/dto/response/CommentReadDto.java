package org.socialculture.platform.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.socialculture.platform.comment.entity.CommentEntity;
import org.socialculture.platform.comment.entity.CommentStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
* 댓글 전체 조회
* */
@Getter
@Builder
public class CommentReadDto {

    private long commentId;
    private long memberId;
    private String memberName;// 댓글에 사용자 이름을 표시하려면 name도 넘겨줘야하더라구요
    private String email;
    private String content; //엔티티 상에서는 comment
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long parentId;
    private CommentStatus commentStatus;

    // 대댓글 리스트 추가
    @Builder.Default
    private List<CommentReadDto> replies = new ArrayList<>();


}

//이건 추후에 record로 바꾸겠슴다.