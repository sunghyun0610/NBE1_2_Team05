package org.socialculture.platform.comment.service;

import org.socialculture.platform.comment.converter.DtoConverter;
import org.socialculture.platform.comment.dto.request.CommentCreateRequest;
import org.socialculture.platform.comment.dto.request.CommentUpdateRequest;
import org.socialculture.platform.comment.dto.response.CommentCreateResponse;
import org.socialculture.platform.comment.dto.response.CommentDeleteResponse;
import org.socialculture.platform.comment.dto.response.CommentReadDto;
import org.socialculture.platform.comment.dto.response.CommentUpdateResponse;
import org.socialculture.platform.comment.entity.CommentEntity;
import org.socialculture.platform.comment.entity.CommentStatus;
import org.socialculture.platform.comment.repository.CommentRepository;
import org.socialculture.platform.global.apiResponse.exception.ErrorStatus;
import org.socialculture.platform.global.apiResponse.exception.GeneralException;
import org.socialculture.platform.member.entity.MemberEntity;
import org.socialculture.platform.member.repository.MemberRepository;
import org.socialculture.platform.performance.entity.PerformanceEntity;
import org.socialculture.platform.performance.repository.PerformanceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private PerformanceRepository performanceRepository;// performanceRepo도 필요하겠지?
    private final MemberRepository memberRepository;

    public CommentServiceImpl(CommentRepository commentRepository, MemberRepository memberRepository, PerformanceRepository performanceRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository= memberRepository;
        this.performanceRepository=performanceRepository;
    }//생성자 의존성 주입


    /**
     * 댓글 전체 조회 로직
     * @author sunghyun0610
     * @param performanceId
     * @return 공통 Response사용하여 commentReadResponse list 반환
     *
     */

    @Override
    public List<CommentReadDto> getAllComment(long performanceId, int page, int size) {

        Pageable pageable= PageRequest.of(page, size);

        // 주어진 id값으로 댓글 테이블 조회하믄댐
        List<CommentEntity> commentEntityList = commentRepository.findAllByPerformance_PerformanceId(performanceId, pageable);

        if (commentEntityList.isEmpty()) {
            throw new GeneralException(ErrorStatus.COMMENT_NOT_FOUND);
        }
        return commentEntityList.stream()
                .map(DtoConverter::fromCommentReadDto)
                .collect(Collectors.toList());
    }

    /**
     * 댓글 생성 로직
     * @author sunghyun0610
     * @param performanceId
     * @return 공통 Response사용하여 commentCreateResponse 반환
     *
     */
    @Override
    public CommentCreateResponse createComment(long performanceId, CommentCreateRequest commentCreateRequest) {



        PerformanceEntity performance = performanceRepository.findById(performanceId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));// performanceEntity객체를 저장하는 것이 아니라 performance 외래키 저장

        MemberEntity member= memberRepository.findById(1L)
                .orElseThrow(()->new GeneralException(ErrorStatus._INTERNAL_SERVER_ERROR));//임의로 넣었음 나중에 jwt에서 추출할 예정

        //대댓글 기능 로직
        Long parentId= commentCreateRequest.parentId();
        CommentEntity parentComment=null;
        if(parentId!=null){//즉 parentId가 있다면
            parentComment=commentRepository.findById(parentId)
                    .orElseThrow(()->new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));
        }


        CommentEntity commentEntity = CommentEntity.builder()
                .content(commentCreateRequest.content())
                .parentId(parentId != null ? parentComment.getCommentId() : null) // 부모 댓글 ID 설정
                .commentStatus(CommentStatus.ACTIVE)
                .performance(performance)
                .member(member)
                .build();
        commentRepository.save(commentEntity);

        CommentCreateResponse commentCreateResponse= CommentCreateResponse.of(commentEntity.getCommentId(),commentEntity.getContent(),commentEntity.getPerformance().getPerformanceId());

        return commentCreateResponse;
    }//일단 userid도 없어서 보류 ->jwt로 이메일 가져오고싶음!


    /**
     * 댓글 전체 조회
     * @author sunghyun0610
     * @param commentId
     * @return 공통 Response사용하여 CommentUpdateResponse 반환
     *
     */
    @Override
    public CommentUpdateResponse updateComment(long commentId, CommentUpdateRequest commentUpdateRequest) {
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));//comment 객체찾아주고

        commentEntity.updateContent(commentUpdateRequest.content());

        commentRepository.save(commentEntity);

        System.out.println("수정성공?");
        CommentUpdateResponse commentUpdateResponse = CommentUpdateResponse.from(commentEntity.getPerformance().getPerformanceId());

        return commentUpdateResponse;
    }

    /**
     * 댓글 전체 조회
     * @author sunghyun0610
     * @param commentId
     * @return 공통 Response사용하여 deleteComment반환
     *
     */

    @Override
    public CommentDeleteResponse deleteComment(long commentId) {
        // 댓글이 존재하지 않을 경우 예외를 던지고, 존재할 경우 바로 삭제
        CommentEntity commentEntity = commentRepository.findById(commentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        CommentDeleteResponse commentDeleteResponse = CommentDeleteResponse.from(commentEntity.getPerformance().getPerformanceId());
        commentEntity.recordDeletedAt(LocalDateTime.now());
        commentEntity.changeCommentStatus(CommentStatus.DELETED);

        commentRepository.save(commentEntity);
        return  commentDeleteResponse;
    }
    //DB에서 삭제하는 것이 아닌 상태만 Deleted로 바꿔주는 것

}
