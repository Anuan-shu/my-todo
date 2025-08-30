package com.hamza.mytodo.dto.todo;

import com.hamza.mytodo.entity.Todo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateRequest {
    private String title;
    private String content;
    private LocalDateTime deadline;
    private Todo.Priority priority;
}
