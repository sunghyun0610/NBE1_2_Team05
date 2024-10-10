package org.socialculture.platform.comment.converter;

import lombok.Builder;
import org.socialculture.platform.comment.dto.response.CommentReadDto;
import org.socialculture.platform.comment.entity.CommentEntity;

import java.util.stream.Collectors;


public class DtoConverter{
    public static CommentReadDto fromCommentEntity(CommentEntity commentEntity) {
        return CommentReadDto.builder()
                .commentId(commentEntity.getCommentId())
                .memberId(commentEntity.getMember().getMemberId()) // 실제 멤버 ID로 수정
                .content(commentEntity.getContent())
                .createdAt(commentEntity.getCreatedAt())
                .updatedAt(commentEntity.getUpdatedAt())
                .memberName(commentEntity.getMember().getName())
                .parentId(commentEntity.getParentComment() != null ? commentEntity.getParentComment().getCommentId() : null) // 부모 댓글 ID 설정
                .commentStatus(commentEntity.getCommentStatus())
                .replies(commentEntity.getReplies().stream() // 대댓글 리스트 매핑
                        .map(DtoConverter::fromCommentEntity)
                        .collect(Collectors.toList()))
                .email(commentEntity.getMember().getEmail())
                .build();
    }

}//엔티티 -> CommentReadDto로 변환해주는 컨버터이다.


