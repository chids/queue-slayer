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
package com.blacklocus.qs.worker.model;

import com.google.common.base.Objects;

import java.util.Map;

/**
 * The definition of a task which can be carried out by a worker.
 *
 * @author Jason Dunkelberger (dirkraft)
 */
public class QSTaskModel {

    public String batchId;
    public String taskId;
    public String handler;
    public volatile Integer remainingAttempts;
    public Map<String, Object> params;

    public QSTaskModel() {
    }

    public QSTaskModel(String batchId, String taskId, String handler, Integer remainingAttempts, Map<String, Object> params) {
        this.batchId = batchId;
        this.taskId = taskId;
        this.handler = handler;
        this.remainingAttempts = remainingAttempts;
        this.params = params;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("batchId", batchId)
                .add("taskId", taskId)
                .add("handler", handler)
                .add("params", params)
                .toString();
    }
}
