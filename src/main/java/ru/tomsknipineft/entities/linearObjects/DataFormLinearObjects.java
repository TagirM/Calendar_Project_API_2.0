package ru.tomsknipineft.entities.linearObjects;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.group.GroupSequenceProvider;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.Mps;
import ru.tomsknipineft.entities.areaObjects.Sikn;
import ru.tomsknipineft.entities.areaObjects.Vec;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.enumEntities.ComplexityOfGeology;
import ru.tomsknipineft.entities.enumEntities.GeodeticTeamType;
import ru.tomsknipineft.utils.entityValidator.EngineeringSurveyLinearObjectsGroupSequenceProvider;
import ru.tomsknipineft.utils.entityValidator.OnActiveEngineeringSurvey;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@GroupSequenceProvider(EngineeringSurveyLinearObjectsGroupSequenceProvider.class)
@Data
public class DataFormLinearObjects implements DataFormProject, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //    шифр договора
    @NotNull(message = "Шифр не может быть пустым")
    @Size(min = 4, max = 10, message = "Шифр должен иметь количество символов от 4 до 10")
    String codeContract;

    //    наименование проекта
    @NotNull(message = "Наименование проекта не может быть пустым")
    @Size(min = 10, max = 200, message = "Наименование проекта должно иметь количество символов более 10")
    private String projectName;

    //    начало выполнения работ по договору
    @NotNull(message = "Дата не может быть пустой")
    @FutureOrPresent(message = "Указана прошедшая дата")
    LocalDate startContract;

    @Valid
    private Pipeline pipeline = new Pipeline();

    @Valid
    private Road road = new Road();

    @Valid
    private Bridge bridge = new Bridge();

    @Valid
    private Line line = new Line();

    @Valid
    private Sikn sikn = new Sikn();

    @Valid
    private Mps mps = new Mps();

    @Valid
    private Vec vec = new Vec();

    @Valid
    private Ktplp ktplp = new Ktplp();

    @Valid
    private Vvp vvp = new Vvp();

    @Valid
    private CableRack cableRack = new CableRack();

    // есть ли полевые ИИ
    private boolean fieldEngineeringSurvey = true;

    // есть ли камеральные ИИ
    private boolean engineeringSurveyReport;

    // размер геодезической бригады
    @NotNull(message = "Размер не заполнен", groups = OnActiveEngineeringSurvey.class)
    @Enumerated(EnumType.STRING)
    private GeodeticTeamType geodeticTeamType = GeodeticTeamType.STANDARD;

    // количество геодезических бригад для выполнения ИИ
    @NotNull(message = "Заполните количество геодезических бригад", groups = OnActiveEngineeringSurvey.class)
    @Min(value = 1, message = "Количество геодезических бригад не может быть меньше 1", groups = OnActiveEngineeringSurvey.class)
    @Max(value = 10, message = "Количество геодезических бригад не должно быть больше 10", groups = OnActiveEngineeringSurvey.class)
    private Integer geodeticTeam = 1;

    // количество буровых бригад для выполнения ИИ
    @NotNull(message = "Заполните количество буровых бригад", groups = OnActiveEngineeringSurvey.class)
    @Min(value = 1, message = "Количество буровых бригад не может быть меньше 1", groups = OnActiveEngineeringSurvey.class)
    @Max(value = 10, message = "Количество буровых бригад не должно быть больше 10", groups = OnActiveEngineeringSurvey.class)
    private Integer drillingRig = 1;

    // местность распространения ММГ
    private boolean mmg = true;

    //    сложность геологии
    @Enumerated(EnumType.STRING)
    private ComplexityOfGeology complexityOfGeology = ComplexityOfGeology.EASY;;

    // не нужен РХР
    private boolean rhrDoc = true;

    // не нужен СЗЗ
    private boolean notSzzDoc;

    // человеческий фактор, непредвиденные обстоятельства, форс-мажор, какие-либо неопределенности в выполнении работ
    @NotNull(message = "Заполните человеческий фактор")
    @Min(value = 0, message = "Человеческий фактор не может быть меньше 0")
    @Max(value = 20, message = "Человеческий фактор не должен быть больше 20")
    private Integer humanFactor = 5;

    /**
     * Метод получения всех сущностей (сооружений) из DataFormOilPad
     *
     * @return список сущностей (сооружений)
     */
    public List<EntityProject> getEntityProjects() {
        return new ArrayList<>(List.of(getPipeline(), getRoad(), getBridge(), getLine(),
                getSikn(), getMps(), getKtplp(), getVvp(), getCableRack()));
    }
}
