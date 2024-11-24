package ru.tomsknipineft.entities.linearObjects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.enumEntities.BridgeType;
import ru.tomsknipineft.entities.enumEntities.ObjectType;
import ru.tomsknipineft.entities.oilPad.OilPad;
import ru.tomsknipineft.utils.entityValidator.BridgeGroupSequenceProvider;
import ru.tomsknipineft.utils.entityValidator.OnActiveBridgeRoad;
import ru.tomsknipineft.utils.entityValidator.OnActiveCheck;

import java.io.Serializable;

@GroupSequenceProvider(BridgeGroupSequenceProvider.class)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bridge")
public class Bridge implements OilPad, EntityProject, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private boolean active;

    @Column(name = "object_type")
    @Enumerated(EnumType.STRING)
    private ObjectType objectType;

    // тип моста
    @NotNull(message = "Тип не заполнен", groups = OnActiveCheck.class)
    @Column(name = "bridge_type")
    @Enumerated(EnumType.STRING)
    private BridgeType bridgeType;

    // количество мостов
    @Column(name = "bridge_road_count")
    @NotNull(message = "Количество не заполнено", groups = OnActiveBridgeRoad.class)
    @Positive(message = "Количество не может быть 0 или отрицательным", groups = OnActiveBridgeRoad.class)
    private Integer bridgeRoadCount;

    // общая длина мостов, м
    @Column(name = "bridge_road_length")
    @NotNull(message = "Длина моста не заполнена", groups = OnActiveBridgeRoad.class)
    @Positive(message = "Длина моста не может быть 0 или отрицательной", groups = OnActiveBridgeRoad.class)
    private Integer bridgeRoadLength;

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
}

