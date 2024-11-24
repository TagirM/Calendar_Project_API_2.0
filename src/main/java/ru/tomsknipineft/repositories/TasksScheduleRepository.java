package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tomsknipineft.entities.TaskSchedule;

import java.util.Optional;

public interface TasksScheduleRepository extends JpaRepository<TaskSchedule, Long> {
    Optional<TaskSchedule> findTasksScheduleByTaskName(String name);
}
