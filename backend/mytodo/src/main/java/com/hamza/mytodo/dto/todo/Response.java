package com.hamza.mytodo.dto.todo;

import com.hamza.mytodo.entity.Todo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Response {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime deadline;
    private Todo.TodoStatus status;
    private Todo.Priority priority;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
