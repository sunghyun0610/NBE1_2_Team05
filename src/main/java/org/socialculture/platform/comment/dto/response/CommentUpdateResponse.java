package org.socialculture.platform.comment.dto.response;

import org.socialculture.platform.comment.dto.request.CommentUpdateRequest;
import org.socialculture.platform.performance.entity.PerformanceEntity;

public record CommentUpdateResponse(long performanceId) {

    public static CommentUpdateResponse from(long performanceId){
        return new CommentUpdateResponse(performanceId);
    }//정적 팩토리 메소드 적용해보기, 매개변수 1개니까 from
}
