package com.jasper.dungeontrackerbackend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "subclasses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassEntity classEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt;
}
