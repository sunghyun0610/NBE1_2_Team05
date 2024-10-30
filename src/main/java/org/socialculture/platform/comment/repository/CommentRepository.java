package org.socialculture.platform.comment.repository;

import org.socialculture.platform.comment.entity.CommentEntity;
import org.socialculture.platform.performance.entity.PerformanceEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

    @Query("SELECT c FROM CommentEntity c WHERE c.performance.performanceId = :performanceId AND c.parentComment IS NULL")
    List<CommentEntity> findParentCommentsByPerformanceId(@Param("performanceId") Long performanceId, Pageable pageable);


//
//    @Query("SELECT c FROM CommentEntity c LEFT JOIN FETCH c.replies WHERE c.performance.performanceId = :performanceId AND c.parentComment IS NULL")
//    List<CommentEntity> findParentCommentsByPerformanceIdWithReplies(@Param("performanceId") Long performanceId, Pageable pageable);
//    //지연 로딩 적용해서 query한번에 조회해오기


}
