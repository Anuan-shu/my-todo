package com.hamza.mytodo.controller;

import com.hamza.mytodo.dto.TodoDto;
import com.hamza.mytodo.entity.Todo;
import com.hamza.mytodo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowCredentials = "true")
public class TodoController {
    
    private final TodoService todoService;
    
    @GetMapping
    public ResponseEntity<List<TodoDto.Response>> getAllTodos(HttpSession session) {
        List<TodoDto.Response> todos = todoService.getAllTodos(session);
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TodoDto.Response>> getTodosByStatus(@PathVariable Todo.TodoStatus status, HttpSession session) {
        List<TodoDto.Response> todos = todoService.getTodosByStatus(status, session);
        return ResponseEntity.ok(todos);
    }
    
    @PostMapping
    public ResponseEntity<TodoDto.Response> createTodo(@RequestBody TodoDto.CreateRequest request, HttpSession session) {
        TodoDto.Response todo = todoService.createTodo(request, session);
        return ResponseEntity.ok(todo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto.Response> updateTodo(@PathVariable Long id, @RequestBody TodoDto.UpdateRequest request, HttpSession session) {
        TodoDto.Response todo = todoService.updateTodo(id, request, session);
        return ResponseEntity.ok(todo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, HttpSession session) {
        todoService.deleteTodo(id, session);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<TodoDto.Response>> getUpcomingTodos(HttpSession session) {
        List<TodoDto.Response> todos = todoService.getUpcomingTodos(session);
        return ResponseEntity.ok(todos);
    }
} 