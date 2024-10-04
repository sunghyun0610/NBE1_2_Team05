package org.socialculture.platform.comment.dto.request;

import lombok.Builder;

@Builder
public record CommentCreateRequest(String content, Long parentId) {
    public static CommentCreateRequest of(String content, Long parentId){
        return CommentCreateRequest.builder()
                .content(content)
                .parentId(parentId)//여기에 부모댓글의 commentId가 넘어와야 하는 것.
                .build();
    }
}
