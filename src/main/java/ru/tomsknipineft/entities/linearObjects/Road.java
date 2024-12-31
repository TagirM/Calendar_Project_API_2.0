package ru.tomsknipineft.entities.linearObjects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.TaskSchedule;
import ru.tomsknipineft.entities.WorkloadDepartments;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.OnActiveBridgeRoad;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;
import ru.tomsknipineft.utils.entityValidator.RoadGroupSequenceProvider;

import java.io.Serializable;
import java.util.*;

/**
 * Автомобильная дорога
 */
@GroupSequenceProvider(RoadGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roads")
public class Road implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // наименование объекта
    @Column(name = "name")
    private String name = "Автомобильная дорога";

    private boolean active;

    // основные проектные отделы этого сооружения
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Column(name = "main_design_departments")
    private Set<String> mainDesignDepartments = new HashSet<>(List.of("Отдел автомобильных дорог"));

    // дополнительные проектные отделы этого сооружения
    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @Column(name = "additional_design_departments")
    private Set<String> additionalDesignDepartments = new HashSet<>(List.of("Отдел тепловодоснабжения и пожаротушения",
            "Отдел землеустройства", "Отдел смет", "Отдел ПОС", "Отдел оценки воздействия на окружающую среду",
            "Департамент экологии", "ГИП"));

    // тип объекта
    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    // наличие мостов у дороги
    @Column(name = "bridge_exist")
    private boolean bridgeExist;

    // количество мостов
    @Column(name = "bridge_road_count")
    @NotNull(message = "Количество не заполнено", groups = OnActiveBridgeRoad.class)
    @Positive(message = "Количество не может быть 0 или отрицательным", groups = OnActiveBridgeRoad.class)
    @Max(value = 5, message = "Не может быть больше 5", groups = OnActiveBridgeRoad.class)
    private Integer bridgeRoadCount;

    // общая длина мостов, м
    @Column(name = "bridge_road_length")
    @NotNull(message = "Длина моста не заполнена", groups = OnActiveBridgeRoad.class)
    @Positive(message = "Длина моста не может быть 0 или отрицательной", groups = OnActiveBridgeRoad.class)
    @Max(value = 1000, message = "Не может быть больше 1000", groups = OnActiveBridgeRoad.class)
    private Integer bridgeRoadLength;

    //    категория дорог
    @NotNull(message = "Категория не заполнена", groups = OnActiveCheck.class)
    @Min(value = 3, message = "Неверно указана категория", groups = OnActiveCheck.class)
    @Max(value = 4, message = "Неверно указана категория", groups = OnActiveCheck.class)
    private Integer category;

    //    общая протяженность дорог, км
    @NotNull(message = "Длина не заполнена", groups = OnActiveCheck.class)
    @Positive(message = "Длина не может быть 0 или отрицательной", groups = OnActiveCheck.class)
    @Max(value = 60, message = "Не может быть больше 60 км", groups = OnActiveCheck.class)
    private Double length;

    //    общее количество дорог, км
    @NotNull(message = "Количество не заполнено", groups = OnActiveCheck.class)
    @Positive(message = "Количество не может быть 0 или отрицательным", groups = OnActiveCheck.class)
    @Max(value = 20, message = "Не может быть больше 20", groups = OnActiveCheck.class)
    private Integer count = 1;

    //    этап строительства
    @NotNull(message = "Этап не заполнен", groups = OnActiveCheck.class)
    @Min(value = 1, message = "Не может быть меньше 1", groups = OnActiveCheck.class)
    private Integer stage;

    //    необходимые ресурсы для выполнения геодезических полевых ИИ, чел/дней
    @Column(name = "resource_for_eng_geodetic_survey")
    private Integer resourceForEngGeodeticSurvey;

    //    необходимые ресурсы для выполнения геологических полевых ИИ, чел/дней
    @Column(name = "resource_for_eng_geological_survey")
    private Integer resourceForEngGeologicalSurvey;

    //    необходимые ресурсы для выполнения ЛИ, чел/дней
    @Column(name = "resource_for_lab_research")
    private Integer resourceForLabResearch;

    //    необходимые ресурсы для выполнения отчета ИИ, чел/дней
    @Column(name = "resource_for_eng_survey_report")
    private Integer resourceForEngSurveyReport;

    //    необходимые ресурсы для разработки РД, чел/дней
    @Column(name = "resource_for_work_doc")
    private Integer resourceForWorkDoc;

    //    необходимые ресурсы для разработки ПД, чел/дней
    @Column(name = "resource_for_proj_doc")
    private Integer resourceForProjDoc;

    //    необходимые ресурсы для разработки СД, чел/дней
    @Column(name = "resource_for_est_doc")
    private Integer resourceForEstDoc;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<WorkloadDepartments>  workloadDepartments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TaskSchedule> taskMainDesignDepartmentsSchedules = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TaskSchedule> taskAdditionalDesignDepartmentsSchedules = new ArrayList<>();

    public Road(Integer category, Double length, Integer resourceForWorkDoc) {
        this.category = category;
        this.length = length;
        this.resourceForWorkDoc = resourceForWorkDoc;
    }

    // расчет необходимых ресурсов для проектирования дорожного моста
    public Integer getResourceBridge(){
        return (bridgeRoadLength/20)*bridgeRoadCount;
    }

}
