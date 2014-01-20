package com.blacklocus.qs.realm.web;

import com.blacklocus.qs.realm.FindLogs;
import com.blacklocus.qs.realm.FindTasks;
import com.blacklocus.qs.realm.FindWorkers;
import com.blacklocus.qs.realm.QSInfoService;
import com.blacklocus.qs.worker.model.QSLogTaskModel;
import com.blacklocus.qs.worker.model.QSLogTickModel;
import com.blacklocus.qs.worker.model.QSLogWorkerModel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author Jason Dunkelberger (dirkraft)
 */
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class QSInfoWeb {

    private final QSInfoService infoService;

    public QSInfoWeb(QSInfoService infoService) {
        this.infoService = infoService;
    }

    @GET
    @Path("tasks")
    public List<QSLogTaskModel> findTasks() {
        return infoService.findTasks(new FindTasks());
    }

    @GET
    @Path("logs")
    public List<QSLogTickModel> findLogs() {
        return infoService.findLogs(new FindLogs());
    }

    @GET
    @Path("workers")
    public List<QSLogWorkerModel> findWorkers() {
        return infoService.findWorkers(new FindWorkers());
    }
}
