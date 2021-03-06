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
package com.blacklocus.qs.worker.api;

import com.blacklocus.qs.worker.model.QSTaskModel;

/**
 * @author Jason Dunkelberger (dirkraft)
 */
public interface QSTaskService {

    void putTask(QSTaskModel task);

    QSTaskModel getAvailableTask();

    void resetTask(QSTaskModel task);

    void closeTask(QSTaskModel task);

}
