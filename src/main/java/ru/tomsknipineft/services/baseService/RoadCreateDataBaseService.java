// Класс-сервис для работы с базой данных автодорог

package ru.tomsknipineft.services.baseService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tomsknipineft.entities.EngineeringSurveys;
import ru.tomsknipineft.entities.TaskSchedule;
import ru.tomsknipineft.entities.WorkloadDepartments;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.repositories.EngineeringSurveysRepository;
import ru.tomsknipineft.repositories.RoadRepository;
import ru.tomsknipineft.services.baseService.util.CreateGraph;
import ru.tomsknipineft.services.baseService.util.MppLoader;
import ru.tomsknipineft.services.entityService.RoadService;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoadCreateDataBaseService {
    private final RoadRepository roadRepository;

    private final RoadService roadService;

    private final EngineeringSurveysRepository engineeringSurveysRepository;

    /**
     * Константный список неменяющихся задач
     */
    final static List<String> constantTask = List.of("Выдача ГИПу", "Выдача тома", "Отработка разделов", "Отработка тома",
            "Выдача задания в ОТВиП на сбор поверхностных и талых вод с проезжей части", "Объем поверхностных стоков",
            "Объем поверхностных стоков в ВОЗ (ОАД)", "Разработка рабочих чертежей по сбору стоков",
            "Подготовка материалов к отводу по проекту", "Выдача площадей в ОГП", "Задание для ООВОС и ОПОС - потребности в землях",
            "Разработка предварительного сводного сметного расчета", "Сводный сметный расчет по стадии РД", "Задание в ОС на расчеты ССР",
            "На составление смет по рекультивации земель", "На составление смет по выдаче компенсационных платежей");

    /**
     * Константный список отделов с неменяющимися задачами
     */
    final static List<String> constantDepartments = List.of("ГИП", "СО 2", "ОВВ ПИР");

    /**
     * Загрузка графика ПСД по автодороге в БД
     *
     * @param road     какая-то дорога
     * @param fileName путь с названием графика в mpp-формате
     * @return сущность дорогу с загруженном в нее графиком ПСД
     */
    public Road loadRoadSchedule(Road road, String fileName) {
        MppLoader mppLoader = new MppLoader();
        List<WorkloadDepartments> workloadDepartments = mppLoader.mppReaderForWorkload(fileName, road);
        for (WorkloadDepartments workloadDepartment : workloadDepartments) {
            road.getWorkloadDepartments().add(workloadDepartment);
        }
        List<TaskSchedule> taskSchedulesMainDesignDepartments = mppLoader.mppReaderForScheduleMainDesignDepartments(fileName, road);
        for (TaskSchedule taskSchedule :
                taskSchedulesMainDesignDepartments) {
            for (WorkloadDepartments workloadDepartment : workloadDepartments) {
                if (workloadDepartment.getDepartment().equals(taskSchedule.getDepartment())) {
                    taskSchedule.setShareTotalWorkloadDepartment(taskSchedule.getTaskDuration() / workloadDepartment.getDepartmentResource());
                }
            }
            road.getTaskMainDesignDepartmentsSchedules().add(taskSchedule);
        }
        List<TaskSchedule> taskSchedulesAdditionalDesignDepartments = mppLoader.mppReaderForScheduleAdditionalDesignDepartments(fileName, road);
        for (TaskSchedule taskSchedule :
                taskSchedulesAdditionalDesignDepartments) {
            for (WorkloadDepartments workloadDepartment : workloadDepartments) {
                if (workloadDepartment.getDepartment().equals(taskSchedule.getDepartment())) {
                    taskSchedule.setShareTotalWorkloadDepartment(taskSchedule.getTaskDuration() / workloadDepartment.getDepartmentResource());
                }
            }
            road.getTaskAdditionalDesignDepartmentsSchedules().add(taskSchedule);
        }
        return road;
    }

    /**
     * Загрузки графика ИИ по автодороге в БД
     *
     * @param road какая-то дорога
     * @return сущность дорогу с загруженном в нее графиком ИИ
     */
    public Road loadEngineeringSurveys(Road road) {
        EngineeringSurveys engineeringSurveys = engineeringSurveysRepository.findEngineeringSurveysByFacility(road.getName()).orElseThrow(() ->
                new NoSuchEntityException("Сооружение в базе данных отсутствует"));
        double resourceForEngSurveyConstant = engineeringSurveys.getResourceEngineeringSurveyConstant(); // включить в модель

        double resourceForEngGeodeticSurvey = engineeringSurveys.getResourceForGeodesy() * road.getLength(); // уточнить расчет
        road.setResourceForEngGeodeticSurvey((int) resourceForEngGeodeticSurvey);
        double resourceForEngGeologicalSurvey = engineeringSurveys.getResourceForSoilDrilling() * road.getLength(); // уточнить расчет
        road.setResourceForEngGeologicalSurvey((int) resourceForEngGeologicalSurvey);
        double resourceForLabResearch = engineeringSurveys.getResourceForLabResearch() + road.getLength(); // уточнить расчет
        road.setResourceForLabResearch((int) resourceForLabResearch);
        double resourceForEngSurveyReport = engineeringSurveys.getResourceForEngSurveyReport() + road.getLength(); // уточнить расчет
        road.setResourceForEngSurveyReport((int) resourceForEngSurveyReport);
        return road;
    }


    /**
     * Загрузка сущности автодороги с определенными характеристиками и графиками в базу данных
     *
     * @param road какая-то дорога
     * @param file файл с графиком в mpp-формате
     * @throws IOException исключение
     */
    public void loadRoadInBase(Road road, MultipartFile file) throws IOException {
        String filePath = "E://MyJob/calendar_api/log-service/downloads_schedule/" + file.getOriginalFilename(); // уточнить адрес при размещении на сервере
        file.transferTo(new File(filePath)); // Сохранение файла
        road = loadRoadSchedule(road, filePath);
        if (roadRepository.findByCategoryAndLength(road.getCategory(), road.getLength()).isPresent()) {
            System.out.println(roadRepository.findByCategoryAndLength(road.getCategory(), road.getLength()).isPresent());
            roadRepository.deleteAll(roadRepository.findAllByCategoryAndLength(road.getCategory(), road.getLength()).orElseThrow());
        }
        roadRepository.save(road);
    }

    /**
     * Метод формирования данных из БД по автодорогам для их визуализации на странице (в виде таблицы, графиков)
     *
     * @return список автодорог с данными
     */
    public List<Road> dataFromRoadInBase() {
        List<Road> roads = roadService.getAll();
        List<Road> roadsWithVariableTask = new ArrayList<>();
        for (Road road :
                roads) {
            List<TaskSchedule> taskSchedules = road.getTaskMainDesignDepartmentsSchedules();
            taskSchedules.addAll(road.getTaskAdditionalDesignDepartmentsSchedules());
            List<TaskSchedule> variableTaskSchedules = taskSchedules.stream()
                    .filter(TaskSchedule::isActive)
                    .filter(taskSchedule -> !taskSchedule.getDepartment().equals(constantDepartments.get(0)))
                    .filter(taskSchedule -> !taskSchedule.getDepartment().equals(constantDepartments.get(1)))
                    .filter(taskSchedule -> !taskSchedule.getDepartment().equals(constantDepartments.get(2)))
                    .filter(taskSchedule -> !(taskSchedule.getTaskName().equals("На составление смет (стадия РД)")
                            && taskSchedule.getDepartment().equals("ОТВиП")))
                    .collect(Collectors.toList());
            for (String elem :
                    constantTask) {
                variableTaskSchedules = variableTaskSchedules.stream()
                        .filter(taskSchedule -> !taskSchedule.getTaskName().contains(elem))
                        .collect(Collectors.toList());
            }
            road.setTaskMainDesignDepartmentsSchedules(variableTaskSchedules);
            roadsWithVariableTask.add(road);
        }
        roadsWithVariableTask = roadsWithVariableTask.stream()
                .sorted(Comparator.comparing(Road::getLength)).collect(Collectors.toList());
        return roadsWithVariableTask;
    }

    /**
     * Визуализация данных из БД в виде графика, создание графика
     */
    public void createGraphFromBase(HttpServletResponse response) {
        CreateGraph graph = new CreateGraph();
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (Road road :
                dataFromRoadInBase()) {
            dataset = graph.createDataset(road.getTaskMainDesignDepartmentsSchedules(), road.getLength(),dataset);
//                dataset = graph.createDataset(road.getTaskMainDesignDepartmentsSchedules(), road.getLength(), dataset);
        }
        graph.getChart(response, "Графики по автомобильным дорогам"
                , "Протяженность автодороги, км", "Трудозатраты, чел/дн", dataset);
    }
}
