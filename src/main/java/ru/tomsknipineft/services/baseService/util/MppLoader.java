// Класс для загрузки графиков в базу данных согласно модели

package ru.tomsknipineft.services.baseService.util;

import net.sf.mpxj.*;
import net.sf.mpxj.reader.UniversalProjectReader;
import ru.tomsknipineft.entities.TaskSchedule;
import ru.tomsknipineft.entities.WorkloadDepartments;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.utils.exceptions.NoResourceException;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

public class MppLoader {

    /**
     * Метод создания Project-файла
     *
     * @param filePath путь, где сохраняются графики
     * @return Project-файл
     */
    public ProjectFile createProjectFile(String filePath) {
        try {
            return new UniversalProjectReader().read(new File(filePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод чтения задач ведущего отдела из графика
     *
     * @param filePath путь, где находится график
     * @param road     сущность - сооружение
     * @return список задач с параметрами
     */
    public List<TaskSchedule> mppReaderForScheduleMainDesignDepartments(String filePath, Road road) {
        Set<String> mainDesignDepartments = road.getMainDesignDepartments(); // получаем набор ведущих отделов
        return separateAndLoadTasksFromSchedule(filePath, mainDesignDepartments);
    }

    /**
     * Формирование списка задач из графика в модель данных из БД
     * @param filePath путь, где находится график
     * @param designDepartments список проектных отделов
     * @return список задач
     */
    private List<TaskSchedule> separateAndLoadTasksFromSchedule(String filePath, Set<String> designDepartments){
        List<TaskSchedule> taskSchedules = new ArrayList<>();
        ProjectFile projectFile = createProjectFile(filePath);
        for (Task task : projectFile.getTasks()) {
            if (task != null && designDepartments.contains(task.getName()) && task.getActive()) {
                List<Task> subTasks = task.getChildTasks();
                for (Task t :
                        subTasks) {
                    // Создается новая задача
                    createTaskSchedule(t, taskSchedules);
//                    TaskSchedule taskSchedule = new TaskSchedule();
//                    // Считываем данные
//                    String departmentName = "";
//                    List<ResourceAssignment> assignments = t.getResourceAssignments();
//                    if (!assignments.isEmpty()) {
//                        departmentName = assignments.get(0).getResource().getName(); //.get(TaskField.TEXT3)
//                        LocalDateTime start = t.getStart();
//                        LocalDateTime finish = t.getFinish();
//                        List<Relation> predecessors = t.getPredecessors();
//                        List<String> predecessorsName = predecessors.stream().map(Relation::getPredecessorTask).map(Task::getName).toList();
//                        List<Relation> successors = t.getSuccessors();
//                        List<String> successorsName = successors.stream().map(Relation::getSuccessorTask).map(Task::getName).toList();
//                        String documentType = (String) t.get(TaskField.TEXT10);
//                        boolean isActiveTask = t.getActive();
//                        Double percentComplete = (Double) t.get(TaskField.PERCENT_COMPLETE);
//                        Duration durationTask = t.getDuration();
//                        double durationTaskInDay = t.getDuration().getDuration();
//                        if (durationTask.toString().contains("h")) {
//                            durationTaskInDay = durationTaskInDay / 8;
//
//                        }
//                        if (departmentName != null) {
//                            taskSchedule.setTaskName(t.getName().trim()).setActive(isActiveTask).setDepartment(departmentName).setTaskStart(start)
//                                    .setTasksFinish(finish).setTaskDuration(durationTaskInDay)
//                                    .setPredecessors(predecessorsName).setSuccessors(successorsName).setTypeDocumentation(documentType)
//                                    .setPercentageCompletion(percentComplete);
//                            taskSchedules.add(taskSchedule);
//                        }
//                    }
                    if (!t.getChildTasks().isEmpty()) {
                        for (Task greatChild :
                                t.getChildTasks()) {
                            // Создается новая подзадача
                            createTaskSchedule(greatChild, taskSchedules);
//                            TaskSchedule taskSchedule1 = new TaskSchedule();
//                            // Считываем данные
//                            String departmentName1 = "";
//                            List<ResourceAssignment> assignments1 = greatChild.getResourceAssignments();
//                            if (!assignments1.isEmpty()) {
//                                departmentName1 = assignments1.get(0).getResource().getName(); //.get(TaskField.TEXT3)
//                                LocalDateTime start1 = greatChild.getStart();
//                                LocalDateTime finish1 = greatChild.getFinish();
//                                List<Relation> predecessors1 = greatChild.getPredecessors();
//                                List<String> predecessorsName1 = predecessors1.stream().map(Relation::getPredecessorTask).map(Task::getName).toList();
//                                List<Relation> successors1 = greatChild.getSuccessors();
//                                List<String> successorsName1 = successors1.stream().map(Relation::getSuccessorTask).map(Task::getName).toList();
//                                String documentType1 = (String) greatChild.get(TaskField.TEXT10);
//                                boolean isActiveTask1 = greatChild.getActive();
//                                Double percentComplete1 = (Double) greatChild.get(TaskField.PERCENT_COMPLETE);
//                                Duration durationTask1 = greatChild.getDuration();
//                                double durationTaskInDay1 = greatChild.getDuration().getDuration();
//                                if (durationTask1.toString().contains("h")) {
//                                    durationTaskInDay1 = durationTaskInDay1 / 8;
//                                }
//                                if (departmentName1 != null) {
//                                    taskSchedule1.setTaskName(greatChild.getName().trim()).setActive(isActiveTask1).setDepartment(departmentName1).setTaskStart(start1)
//                                            .setTasksFinish(finish1).setTaskDuration(durationTaskInDay1)
//                                            .setPredecessors(predecessorsName1).setSuccessors(successorsName1).setTypeDocumentation(documentType1)
//                                            .setPercentageCompletion(percentComplete1);
//                                    taskSchedules.add(taskSchedule1);
//                                }
//                            }
                        }
                    }
                }
            }
        }
        return taskSchedules;
    }

    /**
     * Метод оцифровки одной задачи из графика и формирования Entity задача
     * @param t задача графика
     * @param taskSchedules формирующийся список задач
     */
    private void createTaskSchedule(Task t, List<TaskSchedule> taskSchedules){
        TaskSchedule taskSchedule = new TaskSchedule();
        // Считываем данные
        String departmentName = "";
        List<ResourceAssignment> assignments = t.getResourceAssignments();
        if (!assignments.isEmpty()) {
            if (assignments.get(0).getResource() == null){
                throw new NoResourceException("У задачи '" + t.getName() + "' не задан ресурс.");
            }
            departmentName = assignments.get(0).getResource().getName(); //.get(TaskField.TEXT3)
            LocalDateTime start = t.getStart();
            LocalDateTime finish = t.getFinish();
            List<Relation> predecessors = t.getPredecessors();
            List<String> predecessorsName = predecessors.stream().map(Relation::getPredecessorTask).map(Task::getName).toList();
            List<Relation> successors = t.getSuccessors();
            List<String> successorsName = successors.stream().map(Relation::getSuccessorTask).map(Task::getName).toList();
            String documentType = (String) t.get(TaskField.TEXT10);
            boolean isActiveTask = t.getActive();
            Double percentComplete = (Double) t.get(TaskField.PERCENT_COMPLETE);
            Duration durationTask = t.getDuration();
            double durationTaskInDay = t.getDuration().getDuration();
            if (durationTask.toString().contains("h")) {
                durationTaskInDay = durationTaskInDay / 8;

            }
            if (departmentName != null) {
                taskSchedule.setTaskName(t.getName().trim()).setActive(isActiveTask).setDepartment(departmentName).setTaskStart(start)
                        .setTasksFinish(finish).setTaskDuration(durationTaskInDay)
                        .setPredecessors(predecessorsName).setSuccessors(successorsName).setTypeDocumentation(documentType)
                        .setPercentageCompletion(percentComplete);
                taskSchedules.add(taskSchedule);
            }
        }
    }

    /**
     * Метод чтения задач неведущих отделов из графика
     *
     * @param filePath путь, где находится график
     * @param road     сущность - сооружение
     * @return список задач с параметрами
     */
    public List<TaskSchedule> mppReaderForScheduleAdditionalDesignDepartments(String filePath, Road road) {
//        List<TaskSchedule> taskSchedules = new ArrayList<>();
//        ProjectFile projectFile = createProjectFile(filePath);
        Set<String> additionalDesignDepartments = road.getAdditionalDesignDepartments(); // получаем набор неведущих отделов
//        for (Task task : projectFile.getTasks()) {
//            if (task != null && additionalDesignDepartments.contains(task.getName()) && task.getActive()) {
//                List<Task> subTasks = task.getChildTasks(); // получаем список подзадач (на самом деле задач под заголовком отдела)
//                for (Task t :
//                        subTasks) {
//                    // Создается новая задача
//                    TaskSchedule taskSchedule = new TaskSchedule();
//                    // Считываем данные
//                    String departmentName = "";
//                    List<ResourceAssignment> assignments = t.getResourceAssignments();
//                    if (!assignments.isEmpty()) {
//                        departmentName = assignments.get(0).getResource().getName(); // получаем наименование ресурса (отдела)
//                        LocalDateTime start = t.getStart();
//                        LocalDateTime finish = t.getFinish();
//                        List<Relation> predecessors = t.getPredecessors();
//                        List<String> predecessorsName = predecessors.stream().map(Relation::getPredecessorTask).map(Task::getName).toList();
//                        List<Relation> successors = t.getSuccessors();
//                        List<String> successorsName = successors.stream().map(Relation::getSuccessorTask).map(Task::getName).toList();
//                        String documentType = (String) t.get(TaskField.TEXT10);
//                        boolean isActiveTask = t.getActive();
//                        Double percentComplete = (Double) t.get(TaskField.PERCENT_COMPLETE);
//                        Duration durationTask = t.getDuration();
//                        double durationTaskInDay = t.getDuration().getDuration();
//                        if (durationTask.toString().contains("h")) {
//                            durationTaskInDay = durationTaskInDay / 8;
//                        }
//                        if (departmentName != null) {
//                            taskSchedule.setTaskName(t.getName().trim()).setActive(isActiveTask).setDepartment(departmentName).setTaskStart(start)
//                                    .setTasksFinish(finish).setTaskDuration(durationTaskInDay)
//                                    .setPredecessors(predecessorsName).setSuccessors(successorsName).setTypeDocumentation(documentType)
//                                    .setPercentageCompletion(percentComplete);
//                            taskSchedules.add(taskSchedule);
//                        }
//                    }
//                    if (!t.getChildTasks().isEmpty()) {
//                        for (Task greatChild :
//                                t.getChildTasks()) {
//                            TaskSchedule taskSchedule1 = new TaskSchedule();
//                            // Считываем данные
//                            String departmentName1 = "";
//                            List<ResourceAssignment> assignments1 = greatChild.getResourceAssignments();
//                            if (!assignments1.isEmpty()) {
//                                departmentName1 = assignments1.get(0).getResource().getName(); //.get(TaskField.TEXT3)
//                                LocalDateTime start1 = greatChild.getStart();
//                                LocalDateTime finish1 = greatChild.getFinish();
//                                List<Relation> predecessors1 = greatChild.getPredecessors();
//                                List<String> predecessorsName1 = predecessors1.stream().map(Relation::getPredecessorTask).map(Task::getName).toList();
//                                List<Relation> successors1 = greatChild.getSuccessors();
//                                List<String> successorsName1 = successors1.stream().map(Relation::getSuccessorTask).map(Task::getName).toList();
//                                String documentType1 = (String) greatChild.get(TaskField.TEXT10);
//                                boolean isActiveTask1 = greatChild.getActive();
//                                Double percentComplete1 = (Double) greatChild.get(TaskField.PERCENT_COMPLETE);
//                                Duration durationTask1 = greatChild.getDuration();
//                                double durationTaskInDay1 = greatChild.getDuration().getDuration();
//                                if (durationTask1.toString().contains("h")) {
//                                    durationTaskInDay1 = durationTaskInDay1 / 8;
//                                }
//                                if (departmentName1 != null) {
//                                    taskSchedule1.setTaskName(greatChild.getName().trim()).setActive(isActiveTask1).setDepartment(departmentName1).setTaskStart(start1)
//                                            .setTasksFinish(finish1).setTaskDuration(durationTaskInDay1)
//                                            .setPredecessors(predecessorsName1).setSuccessors(successorsName1).setTypeDocumentation(documentType1)
//                                            .setPercentageCompletion(percentComplete1);
//                                    taskSchedules.add(taskSchedule1);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
        return separateAndLoadTasksFromSchedule(filePath, additionalDesignDepartments);
    }

    private void taskParsing() {

    }

    /**
     * Метод чтения трудозатрат каждого отдела
     *
     * @param filePath путь, где находится график
     * @param road     сущность - сооружение
     * @return список отделов с трудозатратами
     */
    public List<WorkloadDepartments> mppReaderForWorkload(String filePath, Road road) {
        List<WorkloadDepartments> workloadDepartments = new ArrayList<>();
        ProjectFile projectFile = createProjectFile(filePath);
        Map<String, Double> departments = new HashMap<>();
        Set<String> mainDesignDepartments = road.getMainDesignDepartments(); // получаем набор ведущих отделов
        Set<String> additionalDesignDepartments = road.getAdditionalDesignDepartments(); // получаем набор неведущих отделов
        for (Task task : projectFile.getTasks()) {
            if (task != null && (additionalDesignDepartments.contains(task.getName().trim()) || mainDesignDepartments.contains(task.getName().trim()))) {
                List<Task> subTasks = task.getChildTasks();
                for (Task t :
                        subTasks) {
                // Считывание трудозатрат с одной задачи и записывание ее отделу
                    taskWorkLoad(t, departments);
//                    String departmentName = "";
//                    List<ResourceAssignment> assignments = t.getResourceAssignments();
//                    if (!assignments.isEmpty()) {
//                        departmentName = assignments.get(0).getResource().getName(); //.get(TaskField.TEXT3)
//                    }
//                    if (!departmentName.isEmpty()) {
//                        Duration durationTask = t.getDuration();
//                        System.out.println(durationTask.toString());
//                        double durationTaskInDay = durationTask.getDuration();
//                        if (durationTask.toString().contains("h")) {
//                            durationTaskInDay = durationTaskInDay / 8;
//                        }
//                        if (departments.containsKey(departmentName)) {
//                            departments.put(departmentName, departments.get(departmentName) + durationTaskInDay);
//                        } else {
//                            departments.put(departmentName, durationTaskInDay);
//                        }
//                    }
                    if (!t.getChildTasks().isEmpty()) {
                        for (Task greatChild :
                                t.getChildTasks()) {
                            // Считывание трудозатрат с одной подзадачи и записывание ее отделу
                            taskWorkLoad(greatChild, departments);
//                            String departmentName1 = "";
//                            List<ResourceAssignment> assignments1 = greatChild.getResourceAssignments();
//                            if (!assignments1.isEmpty()) {
//                                departmentName1 = assignments1.get(0).getResource().getName(); //.get(TaskField.TEXT3)
//                            }
//                            if (!departmentName1.isEmpty()) {
//                                Duration durationTask1 = greatChild.getDuration();
//                                System.out.println(durationTask1.toString());
//                                double durationTaskInDay1 = durationTask1.getDuration();
//                                if (durationTask1.toString().contains("h")) {
//                                    durationTaskInDay1 = durationTaskInDay1 / 8;
//                                }
//                                if (departments.containsKey(departmentName1)) {
//                                    departments.put(departmentName1, departments.get(departmentName1) + durationTaskInDay1);
//                                } else {
//                                    departments.put(departmentName1, durationTaskInDay1);
//                                }
//                            }
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

    /**
     * Метод считывания трудозатрат с одной задачи и записывание ее отделу
     * @param t задача
     * @param departments map с отделами и их трудозатратами
     */
    private void taskWorkLoad(Task t, Map<String, Double> departments){
        String departmentName = "";
        List<ResourceAssignment> assignments = t.getResourceAssignments();
        if (!assignments.isEmpty()) {
            if (assignments.get(0).getResource() == null){
                throw new NoResourceException("У задачи '" + t.getName() + "' не задан ресурс.");
            }
            departmentName = assignments.get(0).getResource().getName(); //.get(TaskField.TEXT3)
        }
        if (!departmentName.isEmpty()) {
            Duration durationTask = t.getDuration();
            double durationTaskInDay = durationTask.getDuration();
            if (durationTask.toString().contains("h")) {
                durationTaskInDay = durationTaskInDay / 8;
            }
            if (departments.containsKey(departmentName)) {
                departments.put(departmentName, departments.get(departmentName) + durationTaskInDay);
            } else {
                departments.put(departmentName, durationTaskInDay);
            }
        }
    }
}
