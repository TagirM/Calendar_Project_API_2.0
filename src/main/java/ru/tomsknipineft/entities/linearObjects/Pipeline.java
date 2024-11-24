package ru.tomsknipineft.entities.linearObjects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.enumEntities.PipelineLayingMethod;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;
import ru.tomsknipineft.utils.entityValidator.PipelineGroupSequenceProvider;

import java.io.Serializable;

/**
 * Линейный трубопровод
 */
@GroupSequenceProvider(PipelineGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pipelines")
public class Pipeline  implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active  = true;

    // тип объекта проектирования
    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    // способ прокладки трубопровода
    @Column(name = "pipeline_laying_method")
    @Enumerated(EnumType.STRING)
    private PipelineLayingMethod pipelineLayingMethod = PipelineLayingMethod.ABOVEGROUND;

    //    протяженность трубпровода, км
    @NotNull(message = "Длина не заполнена", groups = OnActiveCheck.class)
    @Positive(message = "Длина не может быть 0 или отрицательной", groups = OnActiveCheck.class)
    @Max(value = 60, message = "Не может быть больше 60 км", groups = OnActiveCheck.class)
    private Double length;

    // Наличие узлов
    @Column(name = "unit_exist")
    private boolean unitExist;

    // Количество узлов запорной арматуры
    @Min(value = 0, message = "Не может быть меньше 0", groups = OnActiveCheck.class)
    @NotNull(message = "Количество узлов запорной арматуры не указано", groups = OnActiveCheck.class)
    @Max(value = 20, message = "Не может быть больше 20", groups = OnActiveCheck.class)
    @Column(name = "units_valve")
    private Integer unitsValve = 0;

    // Количество узлов средств очистки и диагностики (СОД)
    @Min(value = 0, message = "Не может быть меньше 0", groups = OnActiveCheck.class)
    @NotNull(message = "Количество узлов средств очистки и диагностики не указано", groups = OnActiveCheck.class)
    @Max(value = 10, message = "Не может быть больше 10", groups = OnActiveCheck.class)
    @Column(name = "units_SOD")
    private Integer unitsSOD = 0;

    //    этап строительства
    @NotNull(message = "Данное поле не может быть пустым")
    @Min(value = 1, message = "Не может быть меньше 1", groups = OnActiveCheck.class)
    private Integer stage = 1;

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
}
