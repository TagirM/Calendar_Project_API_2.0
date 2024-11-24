package ru.tomsknipineft.services.baseService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.WorkloadDepartments;
import ru.tomsknipineft.repositories.WorkloadDepartmentsRepository;

@Service
@RequiredArgsConstructor
public class WorkloadDepartmentsService {

    private final WorkloadDepartmentsRepository workloadDepartmentsRepository;

    public void saveWorkloadDepartments(WorkloadDepartments workloadDepartments){
        workloadDepartmentsRepository.save(workloadDepartments);
    }
}
