package ru.tomsknipineft.services.baseService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.TaskSchedule;
import ru.tomsknipineft.repositories.TasksScheduleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TasksScheduleService {
    private final TasksScheduleRepository tasksScheduleRepository;

    public void saveTaskSchedule(TaskSchedule taskSchedule){
        tasksScheduleRepository.save(taskSchedule);
    }
}
