package com.hamza.mytodo.service;

import com.hamza.mytodo.dto.TodoDto;
import com.hamza.mytodo.entity.Todo;
import com.hamza.mytodo.entity.User;
import com.hamza.mytodo.repository.TodoRepository;
import com.hamza.mytodo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {
    
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    
    public List<TodoDto.Response> getAllTodos() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        
        List<Todo> todos = todoRepository.findByUserIdOrderByPriorityDescCreatedAtDesc(user.getId());
        return todos.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    public List<TodoDto.Response> getTodosByStatus(Todo.TodoStatus status) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        
        List<Todo> todos = todoRepository.findByUserIdAndStatusOrderByPriorityDescCreatedAtDesc(user.getId(), status);
        return todos.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    public TodoDto.Response createTodo(TodoDto.CreateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setContent(request.getContent());
        todo.setDeadline(request.getDeadline());
        todo.setPriority(request.getPriority() != null ? request.getPriority() : Todo.Priority.MEDIUM);
        todo.setUser(user);
        
        Todo savedTodo = todoRepository.save(todo);
        return convertToDto(savedTodo);
    }
    
    public TodoDto.Response updateTodo(Long id, TodoDto.UpdateRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        if (!todo.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        if (request.getTitle() != null) todo.setTitle(request.getTitle());
        if (request.getContent() != null) todo.setContent(request.getContent());
        if (request.getDeadline() != null) todo.setDeadline(request.getDeadline());
        if (request.getPriority() != null) todo.setPriority(request.getPriority());
        if (request.getStatus() != null) todo.setStatus(request.getStatus());
        
        Todo updatedTodo = todoRepository.save(todo);
        return convertToDto(updatedTodo);
    }
    
    public void deleteTodo(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found"));
        
        if (!todo.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        
        todoRepository.delete(todo);
    }
    
    public List<TodoDto.Response> getUpcomingTodos() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusHours(24);
        
        List<Todo> todos = todoRepository.findUpcomingTodos(user.getId(), now, deadline);
        return todos.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private TodoDto.Response convertToDto(Todo todo) {
        TodoDto.Response dto = new TodoDto.Response();
        dto.setId(todo.getId());
        dto.setTitle(todo.getTitle());
        dto.setContent(todo.getContent());
        dto.setDeadline(todo.getDeadline());
        dto.setStatus(todo.getStatus());
        dto.setPriority(todo.getPriority());
        dto.setCreatedAt(todo.getCreatedAt());
        dto.setUpdatedAt(todo.getUpdatedAt());
        return dto;
    }
} 