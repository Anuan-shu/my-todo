package com.hamza.mytodo.controller;

import com.hamza.mytodo.dto.TodoDto;
import com.hamza.mytodo.entity.Todo;
import com.hamza.mytodo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoController {
    
    private final TodoService todoService;
    
    @GetMapping
    public ResponseEntity<List<TodoDto.Response>> getAllTodos() {
        List<TodoDto.Response> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TodoDto.Response>> getTodosByStatus(@PathVariable Todo.TodoStatus status) {
        List<TodoDto.Response> todos = todoService.getTodosByStatus(status);
        return ResponseEntity.ok(todos);
    }
    
    @PostMapping
    public ResponseEntity<TodoDto.Response> createTodo(@RequestBody TodoDto.CreateRequest request) {
        TodoDto.Response todo = todoService.createTodo(request);
        return ResponseEntity.ok(todo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto.Response> updateTodo(@PathVariable Long id, @RequestBody TodoDto.UpdateRequest request) {
        TodoDto.Response todo = todoService.updateTodo(id, request);
        return ResponseEntity.ok(todo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<TodoDto.Response>> getUpcomingTodos() {
        List<TodoDto.Response> todos = todoService.getUpcomingTodos();
        return ResponseEntity.ok(todos);
    }
} 