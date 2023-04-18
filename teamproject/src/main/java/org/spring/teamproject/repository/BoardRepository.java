package org.spring.teamproject.repository;

import org.spring.teamproject.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

	@Modifying
	@Query("update BoardEntity b set b.view=view+1 where b.no=:no")
	void upViewCount(Long no);

	Page<BoardEntity> findByTitleContaining(Pageable pageable, String search);

}
