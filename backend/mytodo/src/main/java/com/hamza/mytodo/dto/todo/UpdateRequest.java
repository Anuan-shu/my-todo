package com.hamza.mytodo.dto.todo;

import com.hamza.mytodo.entity.Todo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateRequest {
    private String title;
    private String content;
    private LocalDateTime deadline;
    private Todo.Priority priority;
    private Todo.TodoStatus status;
}
