package ru.tomsknipineft.services.baseService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tomsknipineft.entities.EngineeringSurveys;
import ru.tomsknipineft.entities.TaskSchedule;
import ru.tomsknipineft.entities.WorkloadDepartments;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.repositories.EngineeringSurveysRepository;
import ru.tomsknipineft.repositories.RoadRepository;
import ru.tomsknipineft.services.baseService.util.MppLoader;
import ru.tomsknipineft.utils.exceptions.NoSuchEntityException;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadCreateDataBaseService {
    private final RoadRepository roadRepository;
    private final EngineeringSurveysRepository engineeringSurveysRepository;

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
                if (workloadDepartment.getDepartment().equals(taskSchedule.getDepartment())){
                    taskSchedule.setShareTotalWorkloadDepartment(taskSchedule.getTaskDuration()/workloadDepartment.getDepartmentResource());
                }
            }
            road.getTaskMainDesignDepartmentsSchedules().add(taskSchedule);
        }
        List<TaskSchedule> taskSchedulesAdditionalDesignDepartments = mppLoader.mppReaderForScheduleAdditionalDesignDepartments(fileName, road);
        for (TaskSchedule taskSchedule :
                taskSchedulesAdditionalDesignDepartments) {
            for (WorkloadDepartments workloadDepartment : workloadDepartments) {
                if (workloadDepartment.getDepartment().equals(taskSchedule.getDepartment())){
                    taskSchedule.setShareTotalWorkloadDepartment(taskSchedule.getTaskDuration()/workloadDepartment.getDepartmentResource());
                }
            }
            road.getTaskAdditionalDesignDepartmentsSchedules().add(taskSchedule);
        }

        return road;
    }

    public Road loadEngineeringSurveys(Road road) {
        EngineeringSurveys engineeringSurveys = engineeringSurveysRepository.findEngineeringSurveysByFacility(road.getName()).orElseThrow(() ->
                new NoSuchEntityException("Сооружение в базе данных отсутствует"));
        double resourceForEngGeodeticSurvey = engineeringSurveys.getResourceForGeodesy() * road.getLength() +
                engineeringSurveys.getResourceForFixingGeodesy() + engineeringSurveys.getResourceForRelocationSpecialists(); // уточнить расчет
        road.setResourceForEngGeodeticSurvey((int) resourceForEngGeodeticSurvey);
        double resourceForEngGeologicalSurvey = engineeringSurveys.getResourceForSoilDrilling() * road.getLength() +
                +engineeringSurveys.getResourceForRelocationSpecialists(); // уточнить расчет
        road.setResourceForEngGeologicalSurvey((int) resourceForEngGeologicalSurvey);
        double resourceForLabResearch = engineeringSurveys.getResourceForLabResearch() + road.getLength(); // уточнить расчет
        road.setResourceForLabResearch((int) resourceForLabResearch);
        double resourceForEngSurveyReport = engineeringSurveys.getResourceForEngSurveyReport() + road.getLength(); // уточнить расчет
        road.setResourceForEngSurveyReport((int) resourceForEngSurveyReport);
        return road;
    }

    public void loadRoadInBase(Road road, MultipartFile file) throws IOException {
        String filePath = "E://MyJob/calendar_api/log-service/downloads_schedule/" + file.getOriginalFilename(); // уточнить адрес при размещении на сервере

        file.transferTo(new File(filePath)); // Сохранение файла
        road = loadEngineeringSurveys(loadRoadSchedule(road, filePath));
        roadRepository.save(road);
    }
}
