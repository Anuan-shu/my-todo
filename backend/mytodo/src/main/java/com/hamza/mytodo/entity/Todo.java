package com.hamza.mytodo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "todos")
@Data
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    private LocalDateTime deadline;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING', 'COMPLETED') DEFAULT 'PENDING'")
    private TodoStatus status = TodoStatus.PENDING;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM'")
    private Priority priority = Priority.MEDIUM;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum TodoStatus {
        PENDING, COMPLETED
    }
    
    public enum Priority {
        LOW, MEDIUM, HIGH
    }
} 