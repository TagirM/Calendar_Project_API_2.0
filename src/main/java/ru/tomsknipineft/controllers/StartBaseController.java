package ru.tomsknipineft.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.tomsknipineft.services.CalendarService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin_data_base")
public class StartBaseController {

    private final CalendarService calendarService;

    private static final Logger logger = LogManager.getLogger(StartBaseController.class);

    /**
     * Первоначальная страница модуля работы с БД с выбором типа объекта проектирования
     */

    /**
     * Страница с выбором объекта проектирования кустовой площадки
     */
    @GetMapping("/oil_pad_object")
    public String oilPadObject() {
        return "oil-pad-object-base";
    }

    /**
     * Страница с выбором объекта проектирования технологической площадки
     */
    @GetMapping("/tech_object")
    public String techObject() {
        return "tech-object-base";
    }

    /**
     * Страница с выбором объекта проектирования инфраструктурного объекта
     */
    @GetMapping("/areaObject")
    public String areaObject() {
        return "area-object-base";
    }

    /**
     * Страница с выбором объекта проектирования линейного объекта
     */
    @GetMapping("/linear_object")
    public String linearObject() {
        return "linear-object-base";
    }

    /**
     * Страница с выбором объекта проектирования объекта энергетики
     */
    @GetMapping("/energy_facility")
    public String energyFacility() {
        return "energy-facility-base";
    }
}
