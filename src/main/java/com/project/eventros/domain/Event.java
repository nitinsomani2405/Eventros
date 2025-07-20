package com.project.eventros.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id",nullable = false,updatable = false)
    private UUID id;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "venue",nullable = false)
    private String venue;

    @Column(name = "sales_start")
    private LocalDateTime salesStart;

    @Column(name = "sales_end")
    private LocalDateTime salesEnd;

    @Column(name = "status",nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatusEnum status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private User organizer;


    @ManyToMany(mappedBy = "attendingEvents")
    private List<User>attendees=new ArrayList<>();

    @ManyToMany(mappedBy = "staffingEvents")
    private List<User>staff=new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at",nullable=false,updatable=false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="updated_at",nullable=false)
    private LocalDateTime updatedAt;
}
