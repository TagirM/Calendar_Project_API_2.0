package ru.tomsknipineft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tomsknipineft.entities.EngineeringSurveys;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;

import java.util.Optional;

@Repository
public interface EngineeringSurveysRepository extends JpaRepository<EngineeringSurveys, Long> {
    Optional<EngineeringSurveys> findEngineeringSurveysByFacility(String facility);
}
