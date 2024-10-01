package org.socialculture.platform.comment.converter;

import lombok.Builder;
import org.socialculture.platform.comment.dto.response.CommentReadDto;
import org.socialculture.platform.comment.entity.CommentEntity;


public class DtoConverter{
    public static CommentReadDto fromCommentReadDto(CommentEntity commentEntity){
        CommentReadDto commentReadDto = CommentReadDto.builder()
                .commentId(commentEntity.getCommentId())
                .memberId(1)//아직은 테스트
                .content(commentEntity.getContent())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .parentId(commentEntity.getParentId())
                .commentStatus(commentEntity.getCommentStatus())
                .build();
        return commentReadDto;
    }//엔티티 -> CommentReadDto로 변환해주는 컨버터이다.

}
