// Класс с контроллером для работы с базой данных автодорог

package ru.tomsknipineft.controllers;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tomsknipineft.entities.TaskSchedule;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.services.baseService.RoadCreateDataBaseService;
import ru.tomsknipineft.services.entityService.RoadService;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin_data_base/linear_object/linear_road")
public class BaseLinearRoadController {
    //    private final LinearPipelineGroupCalendarServiceImpl linearObjectGroupCalendarService;
    private final RoadCreateDataBaseService roadCreateDataBaseService;

    private final RoadService roadService;

    private static final Logger logger = LogManager.getLogger(BaseLinearRoadController.class);

    /**
     * Страница с вводом данных по автомобильной дороге для формирования базы данных
     */
    @GetMapping
    public String linearRoadInputPage(Model model) {
        model.addAttribute("dataRoad", new Road());
        return "input_base_page/linear-road-base";
    }

    /**
     * Получение характеристик сооружения из страницы ввода данных для формирования базы данных
     *
     * @param road класс, содержащий основные данные по сооружению автомобильная дорога
     * @return перенаправление на страницу успешного заполнения базы данных
     */
    @PostMapping("/createBase")
    public String createCalendar(@RequestParam("file") MultipartFile file, Model model, @Valid @ModelAttribute("dataRoad") Road road, BindingResult bindingResult,
                                 HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "input_base_page/linear-road-base";
        }
        if (file.isEmpty()) {
            model.addAttribute("message", "Пожалуйста, выберите файл для загрузки.");
            return "input_base_page/linear-road-base"; // Вернуться к форме, если файл не выбран
        }
        try {
            roadCreateDataBaseService.loadRoadInBase(road, file);
            model.addAttribute("message", "Файл загружен: " + file.getOriginalFilename());
        } catch (IOException e) {
            model.addAttribute("message", "Ошибка при загрузке файла: " + e.getMessage());
        }
        return "redirect:/admin_data_base/linear_object/linear_road/success_base";
    }

    /**
     * Страница с информацией об успешной загрузке автомобильной дороги в базу данных
     */
    @GetMapping("/success_base")
    public String resultBase(Model model, HttpSession session) {
//        String codeContract = (String) session.getAttribute("codeContract");
//        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
//        DataFormOilPad dataFormOilPad = (DataFormOilPad) calendarService.getDataFormProject(calendars);
//        logger.info("Календарь по шифру " + codeContract + " выведен - " + calendars);
//        model.addAttribute("calendars", calendars);
//        model.addAttribute("codeContract", codeContract);
//        model.addAttribute("dataFormOilPad", dataFormOilPad);
//        model.addAttribute("fieldEngineeringSurvey", dataFormOilPad.isFieldEngineeringSurvey());
//        model.addAttribute("engineeringSurveyReport", dataFormOilPad.isEngineeringSurveyReport());
//        model.addAttribute("mmg", dataFormOilPad.isMmg());
        return "result_base/road-success-base";
    }

    /**
     * Вывод веб-страницы с таблицей
     */
    @GetMapping("/success_base/table")
    public String tableFromBase(Model model) {
        List<Road> roads = roadCreateDataBaseService.dataFromRoadInBase();
        List<TaskSchedule> tasks = roads.get(0).getTaskMainDesignDepartmentsSchedules();
        model.addAttribute("roads", roads);
        model.addAttribute("tasks", tasks);
        return "result_base/road-table-base";
    }

    /**
     * Вывод веб-страницы с графиком
     */
    @GetMapping("/success_base/graph")
    public String graphFromBase() {
        return "result_base/road-graph-base";
    }

    /**
     * Вывод графика на веб-страницу
     */
    @GetMapping("/success_base/graph/chart")
    public void graphFromBaseChart1(HttpServletResponse response) {
        roadCreateDataBaseService.createGraphFromBase(response);
    }
}
