package ru.tomsknipineft.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.tomsknipineft.entities.EntityProject;
import ru.tomsknipineft.entities.areaObjects.BackfillSite;
import ru.tomsknipineft.entities.areaObjects.Vvp;
import ru.tomsknipineft.entities.linearObjects.CableRack;
import ru.tomsknipineft.entities.linearObjects.Line;
import ru.tomsknipineft.entities.linearObjects.Road;
import ru.tomsknipineft.entities.oilPad.BackfillWell;
import ru.tomsknipineft.services.entityService.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс с бизнес-логикой расчета сроков календарного плана договора из входных данных
 */

@Service
@RequiredArgsConstructor
public class BackfillWellGroupCalendarServiceImpl implements GroupObjectCalendarService{

    private final BackfillWellService backfillWellService;

    private final BackfillSiteService backfillSiteService;

    private final VvpService vvpService;

    private final RoadService roadService;

    private final LineService lineService;

    private final CableRackService cableRackService;

    private static final Logger logger = LogManager.getLogger(BackfillWellGroupCalendarServiceImpl.class);

    @Override
    public Integer resourceForEngGeodeticSurveyStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForEngGeodeticSurvey();
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectBackfillWell).getResourceForEngGeodeticSurvey() +
                    (((Road) entityProjectBackfillWell).getCount() - 1)/2;
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectBackfillWell).getResourceForEngGeodeticSurvey();
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getFindBackfillSiteFromRequest((BackfillSite) entityProjectBackfillWell).getResourceForEngGeodeticSurvey();
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectBackfillWell).getResourceForEngGeodeticSurvey() *
                    ((Vvp) entityProjectBackfillWell).getCount();
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectBackfillWell).getResourceForEngGeodeticSurvey();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEngGeologicalSurveyStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForEngGeologicalSurvey();
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectBackfillWell).getResourceForEngGeologicalSurvey() +
                    (((Road) entityProjectBackfillWell).getCount() - 1)/2;
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectBackfillWell).getResourceForEngGeologicalSurvey();
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getFindBackfillSiteFromRequest((BackfillSite) entityProjectBackfillWell).getResourceForEngGeologicalSurvey();
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectBackfillWell).getResourceForEngGeologicalSurvey() *
                    ((Vvp) entityProjectBackfillWell).getCount();
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectBackfillWell).getResourceForEngGeologicalSurvey();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForLabResearchStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForLabResearch();
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectBackfillWell).getResourceForLabResearch();
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectBackfillWell).getResourceForLabResearch();
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getFindBackfillSiteFromRequest((BackfillSite) entityProjectBackfillWell).getResourceForLabResearch();
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectBackfillWell).getResourceForLabResearch() +
                    ((Vvp) entityProjectBackfillWell).getCount();
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectBackfillWell).getResourceForLabResearch();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEngSurveyReportStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForEngSurveyReport();
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectBackfillWell).getResourceForEngSurveyReport();
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectBackfillWell).getResourceForEngSurveyReport();
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getFindBackfillSiteFromRequest((BackfillSite) entityProjectBackfillWell).getResourceForEngSurveyReport();
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectBackfillWell).getResourceForEngSurveyReport() +
                    ((Vvp) entityProjectBackfillWell).getCount();
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectBackfillWell).getResourceForEngSurveyReport();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForWorkDocStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForWorkDoc();
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectBackfillWell).getResourceForWorkDoc() +
                    (((Road) entityProjectBackfillWell).getCount() - 1);
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectBackfillWell).getResourceForWorkDoc();
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getFindBackfillSiteFromRequest((BackfillSite) entityProjectBackfillWell).getResourceForWorkDoc();
       } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectBackfillWell).getResourceForWorkDoc() *
                    ((Vvp) entityProjectBackfillWell).getCount();
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectBackfillWell).getResourceForWorkDoc();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForProjDocStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForProjDoc();
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectBackfillWell).getResourceForProjDoc();
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectBackfillWell).getResourceForProjDoc();
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getFindBackfillSiteFromRequest((BackfillSite) entityProjectBackfillWell).getResourceForProjDoc();
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectBackfillWell).getResourceForProjDoc();
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectBackfillWell).getResourceForProjDoc();
        }
        return resourceForStage;
    }

    @Override
    public Integer resourceForEstDocStage(EntityProject entityProjectBackfillWell) {
        int resourceForStage = 0;
        if (entityProjectBackfillWell.getClass() == BackfillWell.class) {
            resourceForStage += backfillWellService.getFindBackfillWellFromRequest((BackfillWell) entityProjectBackfillWell).getResourceForEstDoc();
            backfillWellService.evictCacheCalendar();
        } else if (entityProjectBackfillWell.getClass() == Road.class) {
            resourceForStage += roadService.getFindRoadFromRequest((Road) entityProjectBackfillWell).getResourceForEstDoc() +
                    (((Road) entityProjectBackfillWell).getCount() - 1)/2;
            roadService.evictCacheCalendar();
        } else if (entityProjectBackfillWell.getClass() == Line.class) {
            resourceForStage += lineService.getFindLineFromRequest((Line) entityProjectBackfillWell).getResourceForEstDoc();
            lineService.evictCacheCalendar();
        } else if (entityProjectBackfillWell.getClass() == BackfillSite.class) {
            resourceForStage += backfillSiteService.getFindBackfillSiteFromRequest((BackfillSite) entityProjectBackfillWell).getResourceForEstDoc();
            backfillSiteService.evictCacheCalendar();
        } else if (entityProjectBackfillWell.getClass() == Vvp.class) {
            resourceForStage += vvpService.getFindVvpFromRequest((Vvp) entityProjectBackfillWell).getResourceForEstDoc() +
                    ((Vvp) entityProjectBackfillWell).getCount();
            vvpService.evictCacheCalendar();
        } else if (entityProjectBackfillWell.getClass() == CableRack.class) {
            resourceForStage += cableRackService.getFindCableRackFromRequest((CableRack) entityProjectBackfillWell).getResourceForEstDoc();
            cableRackService.evictCacheCalendar();
        }
        return resourceForStage;
    }

    @Override
    public List<EntityProject> listActiveEntityProject(List<EntityProject> entityProjectsBackfillWell) {
        List<EntityProject> objects = new ArrayList<>();
        for (EntityProject entity :
                entityProjectsBackfillWell) {
            if (entity.isActive()) {
                if (entity.getStage() == null) {
                    entity.setStage(1);
                }
                if (entity.getClass() == BackfillWell.class) {
                    entity.setObjectType(backfillWellService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Road.class) {
                    entity.setObjectType(roadService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Line.class) {
                    entity.setObjectType(lineService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == BackfillSite.class) {
                    entity.setObjectType(backfillSiteService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == Vvp.class) {
                    entity.setObjectType(vvpService.getFirst().getObjectType());
                    objects.add(entity);
                } else if (entity.getClass() == CableRack.class) {
                    entity.setObjectType(cableRackService.getFirst().getObjectType());
                    objects.add(entity);
                }
            }
        }
        return objects;
    }

}
