package example.pagenation;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    public Page<Todo> getTodosByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size); // page는 0부터 시작
        return todoRepository.findAll(pageable);        // Page<Todo> 반환
    }

    public Slice<Todo> getTodosBySlice(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return todoRepository.findSliceBy(pageable);
    }

    /**
     * 커서 기반 페이지네이션 조회
     * @param cursorId 마지막 조회된 Todo의 id, 처음에는 null
     * @param size 페이지 크기
     * @return Slice<Todo> - 다음 페이지 존재 여부 확인 가능
     */
    public Slice<Todo> getTodosByCursor(Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);  // page 번호 무시, 항상 첫 페이지 요청
        return todoRepository.findByIdLessThan(cursorId, pageable);
    }

    /**
     * 복합 커서 기반 페이지네이션
     * @param cursorCreatedAt 마지막 항목의 createdAt (null이면 첫 페이지)
     * @param cursorId 마지막 항목의 id (null이면 첫 페이지)
     * @param size 페이지 크기
     * @return Slice<Todo>
     */
    public Slice<Todo> getTodosByCompositeCursor(LocalDateTime cursorCreatedAt, Long cursorId, int size) {
        Pageable pageable = PageRequest.of(0, size);
        return todoRepository.findByCreatedAtAndIdLessThan(cursorCreatedAt, cursorId, pageable);
    }


}
