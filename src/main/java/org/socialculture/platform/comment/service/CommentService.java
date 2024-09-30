package org.socialculture.platform.comment.service;


import org.socialculture.platform.comment.dto.request.CommentCreateRequest;
import org.socialculture.platform.comment.dto.response.CommentReadDto;

import java.util.List;

public interface CommentService {
    List<CommentReadDto> getAllComment(long performanceId);// 댓글 전체조회

    boolean createComment(long performanceId, CommentCreateRequest commentCreateRequest);
}
