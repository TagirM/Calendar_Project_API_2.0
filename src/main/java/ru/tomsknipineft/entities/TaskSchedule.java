package ru.tomsknipineft.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "tasks_schedule")
public class TaskSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "task_name")
    private String taskName;

    @Column(name = "department")
    private String department;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "task_start")
    private LocalDateTime taskStart;

    @Column(name = "task_finish")
    private LocalDateTime tasksFinish;

    @Column(name = "task_duration")
    private Double taskDuration;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Column(name = "predecessors")
    private List<String> predecessors;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Column(name = "successors")
    private List<String> successors;

    @Column(name = "type_documentation")
    private String typeDocumentation;

    @Column(name = "percentage_completion")
    private Double percentageCompletion;

    @Column(name = "share_total_workload_department")
    private Double shareTotalWorkloadDepartment;
}
