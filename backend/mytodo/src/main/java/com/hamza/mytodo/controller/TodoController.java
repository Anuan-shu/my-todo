package com.hamza.mytodo.controller;

import com.hamza.mytodo.dto.todo.CreateRequest;
import com.hamza.mytodo.dto.todo.Response;
import com.hamza.mytodo.dto.todo.UpdateRequest;
import com.hamza.mytodo.entity.Todo;
import com.hamza.mytodo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoController {

    private final TodoService todoService;
    
    @GetMapping
    public ResponseEntity<List<Response>> getAllTodos(@RequestHeader("X-User-ID") Long userId) {
        List<Response> todos = todoService.getAllTodos(userId);
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Response>> getTodosByStatus(@PathVariable Todo.TodoStatus status, @RequestHeader("X-User-ID") Long userId) {
        List<Response> todos = todoService.getTodosByStatus(status, userId);
        return ResponseEntity.ok(todos);
    }
    
    @PostMapping
    public ResponseEntity<Response> createTodo(@RequestBody CreateRequest request, @RequestHeader("X-User-ID") Long userId) {
        Response todo = todoService.createTodo(request, userId);
        return ResponseEntity.ok(todo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTodo(@PathVariable Long id, @RequestBody UpdateRequest request, @RequestHeader("X-User-ID") Long userId) {
        Response todo = todoService.updateTodo(id, request, userId);
        return ResponseEntity.ok(todo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, @RequestHeader("X-User-ID") Long userId) {
        todoService.deleteTodo(id, userId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<Response>> getUpcomingTodos(@RequestHeader("X-User-ID") Long userId) {
        List<Response> todos = todoService.getUpcomingTodos(userId);
        return ResponseEntity.ok(todos);
    }
} 