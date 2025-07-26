package example.pagenation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping("/todos") // localhost:8080/todos?page=1&size=10
    public Page<Todo> getTodos(@RequestParam int page, @RequestParam int size) {
        return todoService.getTodosByPage(page, size);
    }

}
