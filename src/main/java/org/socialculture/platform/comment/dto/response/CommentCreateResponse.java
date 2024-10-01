package org.socialculture.platform.comment.dto.response;

public record CommentCreateResponse(long commentId, String content,long performanceId) {
    public static CommentCreateResponse from(long commentId, String content, long performanceId){
        return new CommentCreateResponse(commentId,content,performanceId);
    }
}
