# Автоматизация формирования календарного плана договора проектно-изыскательских работ по объектам обустройства нефтегазовых месторождений

## Описание
Данное приложение предназначено для автоматизации формирования календарного плана договора проектно-изыскательских работ по объектам 
обустройства нефтегазовых месторождений. 

С данным приложением можно будет сформировать календарный план проектирования с учетом этапов строительства на любой объект обустройства 
нефтегазового месторождения любой сложности (кустовая площадка, технологическая площадка, инфраструктурный объект, объект энергетики, 
линейный объект). Ввиду огромного перечня объектов и вариантов группировки сооружений разработана тестовая версия приложения с реализацией 
только объекта кустовой площадки - инженерная подготовка, и линейного промыслового трубопровода с вспомогательными сооружениями. 
Приложение доступно на сайте http://calendar.sytes.net.

Ввиду активной работы с кодом по расширению функционала и возможностей приложения в данной версии программы не предусмотрены модули Spring 
Cloud, Spring Boot Security, а также модули для мониторинга работы приложения.

Разработано на Spring Boot 3, Java 17 с использованием сборщика Maven.

Тестирование приложения предусмотрено с помощью unit-тестов методов классов, методы для тестирования взяты выборочно самые важные и характерные,
остальные методы либо входят в состав тестируемых методов и являются вспомогательными, либо аналогичны тестируемым, либо их фунциональность 
проверяется тестируемыми методами.

## Зависимости
- Spring Boot: validation, thymeleaf, thymeleaf-layout-dialect, web, log4j2, jpa, devtools
- Дополнительные зависимости: h2, lombok

## Структура проекта
- Entity классы: BackfillWell, Road, Line, Mupn, Vec, Vvp, Vjk, Mps, Sikn, Ktplp, CableRack, Pipeline, Calendar
- Репозитории и сервисы для каждого из вышеуказанных Entity классов
- Контроллер класса Calendar

## Инструкции по запуску
1. Убедитесь, что у вас установлен Java, Maven и PostgreSQL
2. Клонируйте репозиторий с приложением
3. Перейдите в директорию проекта
4. Запустите PostgreSQL, запустите приложение, выполнив команду `mvn spring-boot:run`
5. Откройте браузер и введите адрес `localhost:8080` для доступа к приложению

## Важно
Убедитесь, что все зависимости, указанные выше установлены перед запуском проекта. Если возникнут проблемы с запуском, обратитесь 
к документации Spring Boot для получения более подробной информации.

## Лицензия
Данный проект еще не лицензирован, так как находится еще в разработке.

## Авторы
- Мурзагалеев Тагир Муратович (@TagirMurzagaleev)

---