package org.socialculture.platform.comment.dto.response;

import org.socialculture.platform.comment.entity.CommentStatus;

public record CommentDeleteResponse(long performanceId, CommentStatus commentStatus) {
    public static CommentDeleteResponse of(long performanceId,CommentStatus commentStatus){
        return new CommentDeleteResponse(performanceId, commentStatus);
    }
}
