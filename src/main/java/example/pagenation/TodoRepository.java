package example.pagenation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
    // 기본 제공 offset 방식 Page 반환 메서드
    // Page<Todo> findAll(Pageable pageable); // 이미 존재함

    // Slice 방식 메서드
    Slice<Todo> findSliceBy(Pageable pageable);
}
