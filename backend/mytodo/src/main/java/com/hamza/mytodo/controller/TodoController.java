package com.hamza.mytodo.controller;

import com.hamza.mytodo.dto.todo.CreateRequest;
import com.hamza.mytodo.dto.todo.Response;
import com.hamza.mytodo.dto.todo.UpdateRequest;
import com.hamza.mytodo.entity.Todo;
import com.hamza.mytodo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TodoController {

    @Autowired
    private TodoService todoService;
    
    @GetMapping
    public ResponseEntity<List<Response>> getAllTodos(HttpSession session) {
        List<Response> todos = todoService.getAllTodos(session);
        return ResponseEntity.ok(todos);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Response>> getTodosByStatus(@PathVariable Todo.TodoStatus status, HttpSession session) {
        List<Response> todos = todoService.getTodosByStatus(status, session);
        return ResponseEntity.ok(todos);
    }
    
    @PostMapping
    public ResponseEntity<Response> createTodo(@RequestBody CreateRequest request, HttpSession session) {
        Response todo = todoService.createTodo(request, session);
        return ResponseEntity.ok(todo);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateTodo(@PathVariable Long id, @RequestBody UpdateRequest request, HttpSession session) {
        Response todo = todoService.updateTodo(id, request, session);
        return ResponseEntity.ok(todo);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, HttpSession session) {
        todoService.deleteTodo(id, session);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<Response>> getUpcomingTodos(HttpSession session) {
        List<Response> todos = todoService.getUpcomingTodos(session);
        return ResponseEntity.ok(todos);
    }
} 