package org.socialculture.platform.comment.dto.response;

public record CommentCreateResponse(long commentId, String content,long performanceId) {
    public static CommentCreateResponse of(long commentId, String content, long performanceId){
        return new CommentCreateResponse(commentId,content,performanceId);
    }
}
//getter 대신 그냥 변수명을 쓰면 된다 .
// getter 메서드처럼 자동 생성되는 해당 필드의 이름을 통해 값을 확인해야 합니다.
// CommentCreateResponse의 경우 commentId(), content(), performanceId() 메서드를 사용하여 필드 값을 가져와야 합니다.


