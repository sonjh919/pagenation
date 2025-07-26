package example.pagenation;

import java.time.LocalDateTime;
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
    @GetMapping("/todos/cursor") // localhost:8080/todos/cursor?cursorId=20&size=5
    public Slice<Todo> getTodosByCursor(
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") int size) {
        return todoService.getTodosByCursor(cursorId, size);
    }

    /**
     * 복합 커서 기반 Todo 조회
     * @param cursorCreatedAt 이전 응답 마지막 createdAt (nullable: 첫 페이지일 때는 전송 X)
     * @param cursorId 이전 응답 마지막 id (nullable: 첫 페이지일 때는 전송 X)
     * @param size 페이지 크기
     */
    @GetMapping("/todos/composite-cursor") // localhost:8080/todos/composite-cursor?cursorCreatedAt=2025-07-30T10:00:00&cursorId=30&size=10
    public Slice<Todo> getTodosByCompositeCursor(
            @RequestParam(required = false) LocalDateTime cursorCreatedAt,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") int size
    ) {
        return todoService.getTodosByCompositeCursor(cursorCreatedAt, cursorId, size);
    }

}

/*
일반 커서 vs 복합 커서
일반 커서(Cursor) 방식는 보통 한 개의 단일 키(예: id)만을 기준으로 페이지네이션 커서를 사용합니다. 복합 커서(Composite Cursor) 방식은 2개 이상의 필드(예: createdAt + id)를 커서로 사용하는 방식입니다.

복합 커서 방식의 특징과 장점
1. 정렬 기준 중복 데이터도 안전하게 처리
하나의 커서 필드(id 등)만 사용할 경우, 동일한 값이 여러 개 존재할 때(예: createdAt이 같은 데이터가 여러 개인 경우) 페이지네이션 도중 중복·누락이 발생할 수 있습니다.

복합 커서는 대표적으로 (createdAt DESC, id DESC)처럼 정렬 필드 전체를 커서로 삼아 "같은 시간에 생성된 다수 데이터"까지도 안전하게, 정확하게 다음 페이지로 연결할 수 있습니다.

2. 완전한 정렬 일관성 보장
예를 들어, ORDER BY createdAt DESC, id DESC 조건이면,

커서도 (createdAt, id) 쌍으로 구성해야 "createdAt이 같은 행"들에서 안정적으로 다음/이전 페이지를 가져올 수 있습니다.

단일 커서는 정렬 기준이 여러 개인 경우 일관성을 보장하지 못할 수 있음에 비해, 복합 커서는 정렬 순서 그대로 이어집니다.

3. 중복/누락 없이 전부 순회 가능
다수 유저 또는 이벤트에서 "동일 시각 데이터"가 대량으로 쌓여도, 정확하게 빠짐없이 모든 행을 순회할 수 있다는 점이 대규모 시스템에서 특히 중요합니다.

단일 커서 기반에서는 "정렬 키가 겹칠 때" 데이터가 건너뛰어질 위험이 있습니다.

4. 유연한 커서 페이징 확장성
createdAt 외에도, 여러 개의 정렬 키(예: price, id 등)로 복합 커서를 만들 수 있어 다양한 데이터 특성에 최적화된 페이징이 가능합니다.

비교 요약
방식	예시 커서	주요 장점
일반 커서	id	단순, 구현이 쉬움. 단일 기준으로 충분히 정렬이 되면 OK
복합 커서	createdAt + id	다중 정렬·동일값 대량 존재 상황에서도 안전, 정렬 일관성 탁월
결론:
복합 커서 방식은 데이터를 여러 필드로 정렬하거나, 동일한 값이 대량으로 생성될 수 있는 시스템에서 특히 신뢰성 높고 안전한 페이지네이션을 제공합니다. 단일 키 커서 방식보다 코드가 살짝 복잡할 수는 있지만, 대규모 환경·동시 이벤트·정렬 완전성이 중요한 서비스에서는 복합 커서가 명확한 장점이 있습니다.
 */