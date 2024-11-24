package ru.tomsknipineft.controllers;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.tomsknipineft.entities.Calendar;
import ru.tomsknipineft.entities.DataFormProject;
import ru.tomsknipineft.entities.linearObjects.DataFormLinearObjects;
import ru.tomsknipineft.entities.oilPad.DataFormOilPad;
import ru.tomsknipineft.services.CalendarService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/calendar_application")
public class StartCalendarController {

    private final CalendarService calendarService;

    private static final Logger logger = LogManager.getLogger(StartCalendarController.class);

    /**
     * Первоначальная страница приложения с выбором типа объекта проектирования
     */

    /**
     * Страница с выбором объекта проектирования кустовой площадки
     */
    @GetMapping("/oil_pad_object")
    public String oilPadObject(){
        return "oil-pad-object";
    }

    /**
     * Страница с выбором объекта проектирования технологической площадки
     */
    @GetMapping("/tech_object")
    public String techObject(){
        return "tech-object";
    }

    /**
     * Страница с выбором объекта проектирования инфраструктурного объекта
     */
    @GetMapping("/areaObject")
    public String areaObject(){
        return "area-object";
    }

    /**
     * Страница с выбором объекта проектирования линейного объекта
     */
    @GetMapping("/linear_object")
    public String linearObject(){
        return "linear-object";
    }

    /**
     * Страница с выбором объекта проектирования объекта энергетики
     */
    @GetMapping("/energy_facility")
    public String energyFacility(){
        return "energy-facility";
    }

    /**
     * Получение шифра договора из страницы ввода данных для формирования календарного плана и проверка его наличия в БД
     * @param codeContract искомый шифр договора для вывода календаря
     * @return перенаправление на страницу вывода календарного плана договора
     */
    @Transactional
    @GetMapping("/codeContract")
    public String outputCalendar(@RequestParam String codeContract, HttpSession session){
        List<Calendar> calendars = calendarService.getCalendarByCode(codeContract);
        DataFormProject dataFormProject = calendarService.getDataFormProject(calendars);
        session.setAttribute("codeContract", codeContract);
        if (dataFormProject.getClass() == DataFormOilPad.class){
            return "redirect:/calendar_application/oil_pad_object/backfill_well/calendar";
        }
        else if (dataFormProject.getClass() == DataFormLinearObjects.class){
            return "redirect:/calendar_application/linear_object/field_pipeline/calendar";
        }
        return null;
    }

    /**
     * Вывод всех проектов из базы данных
     */
    @GetMapping("/allProjects")
    public String allProjects(Model model){
        logger.info("Выведены все проекты из базы данных");
        model.addAttribute("calendarsName", calendarService.getAllCalendars());
        return "all-projects";
    }
}
