package com.hamza.mytodo.dto;

import com.hamza.mytodo.entity.Todo;
import lombok.Data;

import java.time.LocalDateTime;

public class TodoDto {
    
    @Data
    public static class CreateRequest {
        private String title;
        private String content;
        private LocalDateTime deadline;
        private Todo.Priority priority;
    }
    
    @Data
    public static class UpdateRequest {
        private String title;
        private String content;
        private LocalDateTime deadline;
        private Todo.Priority priority;
        private Todo.TodoStatus status;
    }
    
    @Data
    public static class Response {
        private Long id;
        private String title;
        private String content;
        private LocalDateTime deadline;
        private Todo.TodoStatus status;
        private Todo.Priority priority;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
} 