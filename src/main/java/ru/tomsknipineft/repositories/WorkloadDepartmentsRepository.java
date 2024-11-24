package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tomsknipineft.entities.WorkloadDepartments;

import java.util.Optional;

public interface WorkloadDepartmentsRepository extends JpaRepository<WorkloadDepartments, Long> {
    Optional<WorkloadDepartments> findWorkloadDepartmentsByDepartment(String nameDepartment);

}
