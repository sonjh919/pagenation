package example.pagenation;

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
    

}
