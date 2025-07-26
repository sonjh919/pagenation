package example.pagenation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // offset 방식
    @GetMapping("/todos") // localhost:8080/todos?page=1&size=10
    public Page<Todo> getTodos(@RequestParam int page, @RequestParam int size) {
        return todoService.getTodosByPage(page, size);
    }

    // slice 방식
    @GetMapping("/todos/slice") // localhost:8080/todos/slice?page=1&size=10
    public Slice<Todo> getTodosBySlice(@RequestParam int page, @RequestParam int size) {
        return todoService.getTodosBySlice(page, size);
    }

    // cursor 방식
    // cursorId는 이전 응답에서 받은 마지막 todos의 id. 첫 요청은 생략하거나 null로 보냄.
    @GetMapping("/todos/cursor")
    public Slice<Todo> getTodosByCursor(
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") int size) {
        return todoService.getTodosByCursor(cursorId, size);
    }

}
