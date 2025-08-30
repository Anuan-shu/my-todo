package com.hamza.mytodo.repository;

import com.hamza.mytodo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findByUserIdOrderByPriorityDescCreatedAtDesc(Long userId);
    List<Todo> findByUserIdAndStatusOrderByPriorityDescCreatedAtDesc(Long userId, Todo.TodoStatus status);
    
    @Query("SELECT t FROM Todo t WHERE t.user.id = :userId AND t.deadline BETWEEN :now AND :deadline AND t.status = 'PENDING'")
    List<Todo> findUpcomingTodos(@Param("userId") Long userId, @Param("now") LocalDateTime now, @Param("deadline") LocalDateTime deadline);
} 