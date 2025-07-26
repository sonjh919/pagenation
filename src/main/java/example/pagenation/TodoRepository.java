package example.pagenation;

import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // 기본 제공 offset 방식 Page 반환 메서드
    // Page<Todo> findAll(Pageable pageable); // 이미 존재함

    // Slice 방식 메서드
    Slice<Todo> findSliceBy(Pageable pageable);

    // 커서(id) 기준으로 id < cursorId 인 데이터를 날짜 기준 내림차순으로 페이지 사이즈 만큼 조회
    @Query("SELECT t FROM Todo t WHERE (:cursorId IS NULL OR t.id < :cursorId) ORDER BY t.id DESC")
    Slice<Todo> findByIdLessThan(@Param("cursorId") Long cursorId, Pageable pageable);

    // id + createdAt을 사용하는 복합 키 cursor 방식
    @Query("SELECT t FROM Todo t WHERE (:cursorCreatedAt IS NULL OR t.createdAt < :cursorCreatedAt OR (t.createdAt = :cursorCreatedAt AND t.id < :cursorId)) ORDER BY t.createdAt DESC, t.id DESC")
    Slice<Todo> findByCreatedAtAndIdLessThan(@Param("cursorCreatedAt") LocalDateTime cursorCreatedAt, @Param("cursorId") Long cursorId, Pageable pageable);

}
