package ru.tomsknipineft.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "engineering_surveys")
public class EngineeringSurveys {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "facility")
    private String facility;

    @Column(name = "unit_measurement")
    private String unitMeasurement;

    @Column(name = "resource_for_relocation_specialists")
    private Double resourceForRelocationSpecialists;

    @Column(name = "resource_for_geodesy")
    private Double resourceForGeodesy;

    @Column(name = "resource_for_fixing_geodesy")
    private Double resourceForFixingGeodesy;

    @Column(name = "resource_for_soil_drilling")
    private Double resourceForSoilDrilling;

    @Column(name = "resource_for_lab_research")
    private Integer resourceForLabResearch;

    @Column(name = "resource_for_eng_survey_report")
    private Integer resourceForEngSurveyReport;
}
