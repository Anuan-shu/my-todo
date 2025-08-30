package com.hamza.mytodo.service;

import com.hamza.mytodo.dto.todo.CreateRequest;
import com.hamza.mytodo.dto.todo.Response;
import com.hamza.mytodo.dto.todo.UpdateRequest;
import com.hamza.mytodo.entity.Todo;
import com.hamza.mytodo.entity.User;
import com.hamza.mytodo.repository.TodoRepository;
import com.hamza.mytodo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    
    public List<Response> getAllTodos(Long userId) {
        List<Todo> todos = todoRepository.findByUserIdOrderByPriorityDescCreatedAtDesc(userId);
        return todos.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    public List<Response> getTodosByStatus(Todo.TodoStatus status, Long userId) {
        List<Todo> todos = todoRepository.findByUserIdAndStatusOrderByPriorityDescCreatedAtDesc(userId, status);
        return todos.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    public Response createTodo(CreateRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        
        Todo todo = new Todo();
        todo.setTitle(request.getTitle());
        todo.setContent(request.getContent());
        todo.setDeadline(request.getDeadline());
        todo.setPriority(request.getPriority() != null ? request.getPriority() : Todo.Priority.MEDIUM);
        todo.setUser(user);
        
        Todo savedTodo = todoRepository.save(todo);
        return convertToDto(savedTodo);
    }
    
    public Response updateTodo(Long id, UpdateRequest request, Long userId) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("任务不存在"));
        
        if (!todo.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限");
        }
        
        if (request.getTitle() != null) todo.setTitle(request.getTitle());
        if (request.getContent() != null) todo.setContent(request.getContent());
        if (request.getDeadline() != null) todo.setDeadline(request.getDeadline());
        if (request.getPriority() != null) todo.setPriority(request.getPriority());
        if (request.getStatus() != null) todo.setStatus(request.getStatus());
        
        Todo updatedTodo = todoRepository.save(todo);
        return convertToDto(updatedTodo);
    }
    
    public void deleteTodo(Long id, Long userId) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("任务不存在"));
        
        if (!todo.getUser().getId().equals(userId)) {
            throw new RuntimeException("无权限");
        }
        
        todoRepository.delete(todo);
    }
    
    public List<Response> getUpcomingTodos(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusHours(24);
        
        List<Todo> todos = todoRepository.findUpcomingTodos(userId, now, deadline);
        return todos.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    
    private Response convertToDto(Todo todo) {
        Response dto = new Response();
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