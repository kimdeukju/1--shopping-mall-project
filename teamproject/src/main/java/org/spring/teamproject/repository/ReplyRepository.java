package org.spring.teamproject.repository;

import org.spring.teamproject.entity.BoardEntity;
import org.spring.teamproject.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {

    List<ReplyEntity> findAllByBoardId(BoardEntity boardEntityNo);

}
