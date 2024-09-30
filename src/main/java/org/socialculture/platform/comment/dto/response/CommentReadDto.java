package org.socialculture.platform.comment.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.socialculture.platform.comment.entity.CommentEntity;
import org.socialculture.platform.comment.entity.CommentStatus;

import java.time.LocalDateTime;

/*
* 댓글 전체 조회
* */
@Getter
@Builder
public class CommentReadDto {

    private long commentId;
    private long memberId;
    private String content; //엔티티 상에서는 comment
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private long parentId;
    private CommentStatus commentStatus;



}

//이건 추후에 record로 바꾸겠슴다.