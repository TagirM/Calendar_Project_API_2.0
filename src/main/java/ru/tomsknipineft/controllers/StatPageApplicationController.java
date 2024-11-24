package ru.tomsknipineft.controllers;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class StatPageApplicationController {
    private static final Logger logger = LogManager.getLogger(StatPageApplicationController.class);

    /**
     * Первоначальная страница приложения с выбором типа объекта проектирования
     */
    @GetMapping
    public String start(){
        return "start-page";
    }

    @GetMapping("/admin_data_base")
    public String adminDataBase(){
        return "admin-data-base";
    }

    @GetMapping("/calendar_application")
    public String calendarApplication(){
        return "calendar-application";
    }
}
