package ru.tomsknipineft.services.baseService.util;

import net.sf.mpxj.*;
import net.sf.mpxj.reader.UniversalProjectReader;
import ru.tomsknipineft.entities.TaskSchedule;
import ru.tomsknipineft.entities.WorkloadDepartments;
import ru.tomsknipineft.entities.linearObjects.Road;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

public class MppLoader {

    public ProjectFile createProjectFile(String filePath) {
        try {
            return new UniversalProjectReader().read(new File(filePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<TaskSchedule> mppReaderForScheduleMainDesignDepartments(String filePath, Road road) {
        List<TaskSchedule> taskSchedules = new ArrayList<>();
        ProjectFile projectFile = createProjectFile(filePath);
        Set<String> mainDesignDepartments = road.getMainDesignDepartments();
        for (Task task : projectFile.getTasks()) {
            if (task != null && mainDesignDepartments.contains(task.getName())) {
                List<Task> subTasks = task.getChildTasks();
                for (Task t :
                        subTasks) {
                    // Создается новая задача
                    TaskSchedule taskSchedule = new TaskSchedule();
                    // Считываем данные
                    String departmentName = "";
                    List<ResourceAssignment> assignments = t.getResourceAssignments();
                    if (!assignments.isEmpty()) {
                        departmentName = assignments.get(0).getResource().getName(); //.get(TaskField.TEXT3)
                    }
                    LocalDateTime start = t.getStart();
                    LocalDateTime finish = t.getFinish();
                    List<Relation> predecessors = t.getPredecessors();
                    List<String> predecessorsName = predecessors.stream().map(Relation::getPredecessorTask).map(Task::getName).toList();
                    List<Relation> successors = t.getSuccessors();
                    List<String> successorsName = successors.stream().map(Relation::getSuccessorTask).map(Task::getName).toList();
                    String documentType = (String) t.get(TaskField.TEXT10);
                    Double percentComplete = (Double) t.get(TaskField.PERCENT_COMPLETE);
                    Duration durationTask = t.getDuration();
                    double durationTaskInDay = t.getDuration().getDuration();
                    if (durationTask.toString().contains("h")) {
                        durationTaskInDay = durationTaskInDay / 8;

                    }
                    if (departmentName != null) {
                        taskSchedule.setTaskName(t.getName()).setDepartment(departmentName).setTaskStart(start)
                                .setTasksFinish(finish).setTaskDuration(durationTaskInDay)
                                .setPredecessors(predecessorsName).setSuccessors(successorsName).setTypeDocumentation(documentType)
                                .setPercentageCompletion(percentComplete);
                        taskSchedules.add(taskSchedule);
                    }
                    if (!t.getChildTasks().isEmpty()) {
                        for (Task greatChild :
                                t.getChildTasks()) {
                            TaskSchedule taskSchedule1 = new TaskSchedule();
                            // Считываем данные
                            String departmentName1 = "";
                            List<ResourceAssignment> assignments1 = greatChild.getResourceAssignments();
                            if (!assignments1.isEmpty()) {
                                departmentName1 = assignments1.get(0).getResource().getName(); //.get(TaskField.TEXT3)
                            }
                            LocalDateTime start1 = greatChild.getStart();
                            LocalDateTime finish1 = greatChild.getFinish();
                            List<Relation> predecessors1 = greatChild.getPredecessors();
                            List<String> predecessorsName1 = predecessors1.stream().map(Relation::getPredecessorTask).map(Task::getName).toList();
                            List<Relation> successors1 = greatChild.getSuccessors();
                            List<String> successorsName1 = successors1.stream().map(Relation::getSuccessorTask).map(Task::getName).toList();
                            String documentType1 = (String) greatChild.get(TaskField.TEXT10);
                            Double percentComplete1 = (Double) greatChild.get(TaskField.PERCENT_COMPLETE);
                            Duration durationTask1 = greatChild.getDuration();
                            double durationTaskInDay1 = greatChild.getDuration().getDuration();
                            if (durationTask1.toString().contains("h")) {
                                durationTaskInDay1 = durationTaskInDay1 / 8;
                            }
                            if (departmentName1 != null) {
                                taskSchedule1.setTaskName(greatChild.getName()).setDepartment(departmentName1).setTaskStart(start1)
                                        .setTasksFinish(finish1).setTaskDuration(durationTaskInDay1)
                                        .setPredecessors(predecessorsName1).setSuccessors(successorsName1).setTypeDocumentation(documentType1)
                                        .setPercentageCompletion(percentComplete1);
                                taskSchedules.add(taskSchedule1);
                            }
                        }
                    }
                }
            }
        }
        return taskSchedules;
    }

    public List<TaskSchedule> mppReaderForScheduleAdditionalDesignDepartments(String filePath, Road road) {
        List<TaskSchedule> taskSchedules = new ArrayList<>();
        ProjectFile projectFile = createProjectFile(filePath);
        Set<String> additionalDesignDepartments = road.getAdditionalDesignDepartments();
        for (Task task : projectFile.getTasks()) {
            if (task != null && additionalDesignDepartments.contains(task.getName())) {
                List<Task> subTasks = task.getChildTasks();
                for (Task t :
                        subTasks) {
                    // Создается новая задача
                    TaskSchedule taskSchedule = new TaskSchedule();
                    // Считываем данные
                    String departmentName = "";
                    List<ResourceAssignment> assignments = t.getResourceAssignments();
                    if (!assignments.isEmpty()) {
                        departmentName = assignments.get(0).getResource().getName(); //.get(TaskField.TEXT3)
                    }
                    LocalDateTime start = t.getStart();
                    LocalDateTime finish = t.getFinish();
                    List<Relation> predecessors = t.getPredecessors();
                    List<String> predecessorsName = predecessors.stream().map(Relation::getPredecessorTask).map(Task::getName).toList();
                    List<Relation> successors = t.getSuccessors();
                    List<String> successorsName = successors.stream().map(Relation::getSuccessorTask).map(Task::getName).toList();
                    String documentType = (String) t.get(TaskField.TEXT10);
                    Double percentComplete = (Double) t.get(TaskField.PERCENT_COMPLETE);
                    Duration durationTask = t.getDuration();
                    double durationTaskInDay = t.getDuration().getDuration();
                    if (durationTask.toString().contains("h")) {
                        durationTaskInDay = durationTaskInDay / 8;
                    }
                    if (departmentName != null) {
                        taskSchedule.setTaskName(t.getName()).setDepartment(departmentName).setTaskStart(start)
                                .setTasksFinish(finish).setTaskDuration(durationTaskInDay)
                                .setPredecessors(predecessorsName).setSuccessors(successorsName).setTypeDocumentation(documentType)
                                .setPercentageCompletion(percentComplete);
                        taskSchedules.add(taskSchedule);
                    }
                    if (!t.getChildTasks().isEmpty()) {
                        for (Task greatChild :
                                t.getChildTasks()) {
                            TaskSchedule taskSchedule1 = new TaskSchedule();
                            // Считываем данные
                            String departmentName1 = "";
                            List<ResourceAssignment> assignments1 = greatChild.getResourceAssignments();
                            if (!assignments1.isEmpty()) {
                                departmentName1 = assignments1.get(0).getResource().getName(); //.get(TaskField.TEXT3)
                            }
                            LocalDateTime start1 = greatChild.getStart();
                            LocalDateTime finish1 = greatChild.getFinish();
                            List<Relation> predecessors1 = greatChild.getPredecessors();
                            List<String> predecessorsName1 = predecessors1.stream().map(Relation::getPredecessorTask).map(Task::getName).toList();
                            List<Relation> successors1 = greatChild.getSuccessors();
                            List<String> successorsName1 = successors1.stream().map(Relation::getSuccessorTask).map(Task::getName).toList();
                            String documentType1 = (String) greatChild.get(TaskField.TEXT10);
                            Double percentComplete1 = (Double) greatChild.get(TaskField.PERCENT_COMPLETE);
                            Duration durationTask1 = greatChild.getDuration();
                            double durationTaskInDay1 = greatChild.getDuration().getDuration();
                            if (durationTask1.toString().contains("h")) {
                                durationTaskInDay1 = durationTaskInDay1 / 8;
                            }
                            if (departmentName1 != null) {
                                taskSchedule1.setTaskName(greatChild.getName()).setDepartment(departmentName1).setTaskStart(start1)
                                        .setTasksFinish(finish1).setTaskDuration(durationTaskInDay1)
                                        .setPredecessors(predecessorsName1).setSuccessors(successorsName1).setTypeDocumentation(documentType1)
                                        .setPercentageCompletion(percentComplete1);
                                taskSchedules.add(taskSchedule1);
                            }
                        }
                    }
                }
            }
        }
        return taskSchedules;
    }

    private void taskParsing() {

    }

    public List<WorkloadDepartments> mppReaderForWorkload(String filePath, Road road) {
        List<WorkloadDepartments> workloadDepartments = new ArrayList<>();
        ProjectFile projectFile = createProjectFile(filePath);
        Map<String, Double> departments = new HashMap<>();
        Set<String> mainDesignDepartments = road.getMainDesignDepartments();
        Set<String> additionalDesignDepartments = road.getAdditionalDesignDepartments();
        for (Task task : projectFile.getTasks()) {
            if (task != null && (additionalDesignDepartments.contains(task.getName()) || mainDesignDepartments.contains(task.getName()))) {
                List<Task> subTasks = task.getChildTasks();
                for (Task t :
                        subTasks) {
                    String departmentName = "";
                    List<ResourceAssignment> assignments = t.getResourceAssignments();
                    if (!assignments.isEmpty()) {
                        departmentName = assignments.get(0).getResource().getName(); //.get(TaskField.TEXT3)
                        System.out.println(departmentName);
                    }
                    if (!departmentName.isEmpty()) {
                        Duration durationTask = t.getDuration();
                        System.out.println(durationTask.toString());
                        double durationTaskInDay = durationTask.getDuration();
                        if (durationTask.toString().contains("h")) {
                            durationTaskInDay = durationTaskInDay / 8;
                        }
                        if (departments.containsKey(departmentName)) {
                            departments.put(departmentName, departments.get(departmentName) + durationTaskInDay);
                        } else {
                            departments.put(departmentName, durationTaskInDay);
                        }
                        System.out.println(departments);
                    }
                    if (!t.getChildTasks().isEmpty()) {
                        for (Task greatChild :
                                t.getChildTasks()) {
                            String departmentName1 = "";
                            List<ResourceAssignment> assignments1 = greatChild.getResourceAssignments();
                            if (!assignments1.isEmpty()) {
                                departmentName1 = assignments1.get(0).getResource().getName(); //.get(TaskField.TEXT3)
                                System.out.println(departmentName1);
                            }
                            if (!departmentName1.isEmpty()) {
                                Duration durationTask1 = greatChild.getDuration();
                                System.out.println(durationTask1.toString());
                                double durationTaskInDay1 = durationTask1.getDuration();
                                if (durationTask1.toString().contains("h")) {
                                    durationTaskInDay1 = durationTaskInDay1 / 8;
                                }
                                if (departments.containsKey(departmentName1)) {
                                    departments.put(departmentName1, departments.get(departmentName1) + durationTaskInDay1);
                                } else {
                                    departments.put(departmentName1, durationTaskInDay1);
                                }
                                System.out.println(departments);
                            }
                        }
                    }
                }
            }
        }
        for (String department :
                departments.keySet()) {
            WorkloadDepartments workloadDepartment = new WorkloadDepartments();
            workloadDepartment.setDepartment(department).setDepartmentResource(departments.get(department));
            workloadDepartments.add(workloadDepartment);
        }
        return workloadDepartments;
    }
}
