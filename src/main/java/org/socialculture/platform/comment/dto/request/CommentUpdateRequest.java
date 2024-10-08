package org.socialculture.platform.comment.dto.request;

public record CommentUpdateRequest(String content) {

    public static CommentUpdateRequest from(String content){
        return new CommentUpdateRequest(content);
    }//정적 팩토리 메소드 적용해보기, 매개변수 1개니까 from
}
