package org.socialculture.platform.comment.dto.response;

public record CommentDeleteResponse(long performanceId) {
    public static CommentDeleteResponse from(long performanceId){
        return new CommentDeleteResponse(performanceId);
    }
}
