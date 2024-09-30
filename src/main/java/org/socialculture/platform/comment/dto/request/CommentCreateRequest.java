package org.socialculture.platform.comment.dto.request;

import lombok.Builder;

@Builder
public record CommentCreateRequest(String content, long parentId) {
    public static CommentCreateRequest Of(String content, long parentId){
        return CommentCreateRequest.builder()
                .content(content)
                .parentId(parentId)
                .build();
    }
}
