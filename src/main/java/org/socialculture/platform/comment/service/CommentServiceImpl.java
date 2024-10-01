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
import org.socialculture.platform.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
//    private PerformanceRepository performanceRepository;// performanceRepo도 필요하겠지?
    private final MemberRepository memberRepository;

    public CommentServiceImpl(CommentRepository commentRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.memberRepository= memberRepository;
//        this.performanceRepository=performanceRepository;
    }//생성자 의존성 주입


    /**
     * 댓글 전체 조회 로직
     * @author sunghyun0610
     * @param performanceId
     * @return 공통 Response사용하여 commentReadResponse list 반환
     *
     */

    @Override
    public List<CommentReadDto> getAllComment(long performanceId) {


        // 주어진 id값으로 댓글 테이블 조회하믄댐
        List<CommentEntity> commentEntityList = commentRepository.findAllByPerformance_PerformanceId(performanceId);

        if (commentEntityList.isEmpty()) {
            throw new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND);
        }
        List<CommentReadDto> commentReadDtos = new ArrayList<>();

        for (CommentEntity commentEntity : commentEntityList) {
            CommentReadDto commentReadDto= DtoConverter.fromCommentReadDto(commentEntity);
            commentReadDtos.add(commentReadDto);
        }
        return commentReadDtos;
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

//        PerformanceEntity performance = performanceRepository.findById(performanceId)
//                .orElseThrow(() -> new GeneralException(ErrorStatus.PERFORMANCE_NOT_FOUND));
        CommentEntity commentEntity = CommentEntity.builder()
                .content(commentCreateRequest.content())
                .parentId(commentCreateRequest.parentId())
                .commentStatus(CommentStatus.ACTIVE)
//                .performance(performance)
                .build();
        System.out.println(commentCreateRequest.content() + "입니다");
        commentRepository.save(commentEntity);

        CommentCreateResponse commentCreateResponse= CommentCreateResponse.from(commentEntity.getCommentId(),commentEntity.getContent(),commentEntity.getPerformance().getPerformanceId());

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

        commentEntity.builder()
                .content(commentUpdateRequest.content())
                .updatedAt(LocalDateTime.now())//이거는 불필요한 코드일 수도?
                .build();

        commentRepository.save(commentEntity);

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

        CommentDeleteResponse commentDeleteResponse = CommentDeleteResponse.of(commentEntity.getPerformance().getPerformanceId());
        commentEntity.setDeletedAt(LocalDateTime.now());
        commentRepository.delete(commentEntity);

        return  commentDeleteResponse;
    }

}
