package org.spring.teamproject.repository;

import org.spring.teamproject.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FileRepository extends JpaRepository<BoardFileEntity, Long> {

//	@Query("")
//	Optional<BoardFileEntity> findByBoardId(Long no);
//
//	@Query("")
//	void delete(Optional<BoardFileEntity> optionalBoardFile);

}
