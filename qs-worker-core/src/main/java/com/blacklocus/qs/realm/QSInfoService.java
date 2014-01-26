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
package com.blacklocus.qs.realm;

import com.blacklocus.qs.worker.model.QSLogModel;
import com.blacklocus.qs.worker.model.QSTaskModel;
import com.blacklocus.qs.worker.model.QSWorkerModel;
import com.google.common.annotations.Beta;

import java.util.List;

/**
 * @author Jason Dunkelberger (dirkraft)
 */
@Beta
public interface QSInfoService {

    List<QSTaskModel> findTasks(FindTasks findTasks);

    List<QSLogModel> findLogs(FindLogs findLogs);

    List<QSWorkerModel> findWorkers(FindWorkers findWorkers);

}
