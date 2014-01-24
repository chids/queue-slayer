/**
 * Copyright 2013 BlackLocus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blacklocus.qs.worker.es;

import com.blacklocus.jres.Jres;
import com.blacklocus.jres.request.index.JresIndexDocument;
import com.blacklocus.qs.worker.QSLogService;
import com.blacklocus.qs.worker.model.QSLogTaskModel;
import com.blacklocus.qs.worker.model.QSLogTickModel;
import com.blacklocus.qs.worker.model.QSLogWorkerModel;
import com.blacklocus.qs.worker.util.IdSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ElasticSearchQSLogService implements QSLogService {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchQSLogService.class);

    public static final String INDEX_TYPE_TASK = "task";
    public static final String INDEX_TYPE_TASK_LOG = "taskLog";
    public static final String INDEX_TYPE_WORKER = "worker";

    private final String index;
    private final Jres jres;

    private final Map<QSLogTaskModel, String> logTaskIds = new ConcurrentHashMap<QSLogTaskModel, String>();

    public ElasticSearchQSLogService(String index, Jres jres) {
        this(index, new ElasticSearchInitializer(index, jres), jres);
    }

    public ElasticSearchQSLogService(String index, ElasticSearchInitializer initializer, Jres jres) {
        this.index = index;
        this.jres = jres;
        initializer.verifyElasticSearchMappings();
    }

    @Override
    public void startedTask(QSLogTaskModel logTask) {
        String documentId = IdSupplier.newId();
        logTaskIds.put(logTask, documentId);
        // true - createOnly besides its literal assurance, add that if for some reason the finishedTask submission gets
        // there first, we won't overwrite it with the startedTask which would be missing 'finished' information (say,
        // due to parallelized batching submissions or some such).
        jres.quest(new JresIndexDocument(index, INDEX_TYPE_TASK, documentId, new QSLogTaskElasticSearchModel(logTask), true));
    }

    @Override
    public void logTask(QSLogTickModel logTick) {
        // Append-only, hence generated ID.
        jres.quest(new JresIndexDocument(index, INDEX_TYPE_TASK_LOG, null, new QSLogTickElasticSearchModel(logTick)));
    }

    @Override
    public void completedTask(QSLogTaskModel logTask) {
        // Possibly updates the document submitted by startedTask, if it has been received by ElasticSearch
        String documentId = logTaskIds.remove(logTask);
        if (documentId == null) {
            LOG.warn("Could not find original LogTask document id, which means I can't update the original entry. " +
                    "Writing the result anyhow.");
        }
        jres.quest(new JresIndexDocument(index, INDEX_TYPE_TASK, documentId, new QSLogTaskElasticSearchModel(logTask)));
    }

    @Override
    public void workerHeartbeat(QSLogWorkerModel logWorker) {
        jres.quest(new JresIndexDocument(index, INDEX_TYPE_WORKER, logWorker.workerId, logWorker));
    }

}
