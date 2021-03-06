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
package com.blacklocus.qs.worker;

import com.blacklocus.qs.worker.model.QSTaskModel;
import com.blacklocus.qs.worker.simple.BlockingQueueQSTaskService;
import com.blacklocus.qs.worker.simple.HostNameQSWorkerIdService;
import com.blacklocus.qs.worker.simple.SystemOutQSLogService;
import com.fasterxml.jackson.databind.node.IntNode;
import org.apache.commons.lang.math.RandomUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

/**
 * @author Jason Dunkelberger (dirkraft)
 */
public class TestApplication implements Runnable {

    BlockingQueue<QSTaskModel> numbersMan = new SynchronousQueue<QSTaskModel>();

    @Override
    public void run() {
        // simulated work
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new TaskGenerator());

        // actual qs-worker API
        QSAssembly.newBuilder()
                .taskServices(new BlockingQueueQSTaskService(numbersMan))
                .logService(new SystemOutQSLogService())
                .workerIdService(new HostNameQSWorkerIdService())
                .workers(
                        new TestWorkerPrintIdentity(),
                        new TestWorkerPrintSquare(),
                        new TestWorkerPrintZero(),
                        new TestWorkerUnmotivated()
                )
                .build()
                .run();
    }

    public static void main(String[] args) {
        new TestApplication().run();
    }

    class TaskGenerator implements Callable<Void> {
        @Override
        public Void call() throws Exception {
            while (!Thread.interrupted()) {
                numbersMan.put(new QSTaskModel(null, "" + System.currentTimeMillis(), randomHandler(), 1, new IntNode(2)));
                Thread.sleep(100L);
            }
            return null;
        }

        String[] WORKER_NAMES = {
                TestWorkerPrintIdentity.HANDLER_NAME,
                TestWorkerPrintSquare.HANDLER_NAME,
                TestWorkerPrintZero.HANDLER_NAME,
                TestWorkerUnmotivated.HANDLER_NAME
        };

        String randomHandler() {
            return WORKER_NAMES[RandomUtils.nextInt(WORKER_NAMES.length)];
        }
    }
}

class TestWorkerPrintIdentity extends AbstractQSWorker<Integer> {

    public static final String HANDLER_NAME = "identity";

    @Override
    public String getHandlerName() {
        return HANDLER_NAME;
    }

    @Override
    public TaskKit<Integer> convert(TaskKitFactory<Integer> factory) throws Exception {
        return factory.newTaskKit(factory.paramsJson().asInt());
    }

    @Override
    public Object process(TaskKit<Integer> kit) throws Exception {
        System.out.println(kit.params());
        Thread.sleep(1000L);
        return null;
    }
}

class TestWorkerPrintSquare extends AbstractQSWorker<Integer> {

    public static final String HANDLER_NAME = "square";

    @Override
    public String getHandlerName() {
        return HANDLER_NAME;
    }

    @Override
    public TaskKit<Integer> convert(TaskKitFactory<Integer> factory) throws Exception {
        return factory.newTaskKit(factory.paramsJson().asInt());
    }

    @Override
    public Object process(TaskKit<Integer> kit) throws Exception {
        System.out.println(Math.pow(kit.params(), 2));
        Thread.sleep(1000L);
        return null;
    }
}

class TestWorkerPrintZero extends AbstractQSWorker<Integer> {

    public static final String HANDLER_NAME = "zero";

    @Override
    public String getHandlerName() {
        return HANDLER_NAME;
    }

    @Override
    public TaskKit<Integer> convert(TaskKitFactory<Integer> factory) throws Exception {
        return factory.newTaskKit(factory.paramsJson().asInt());
    }

    @Override
    public Object process(TaskKit<Integer> kit) throws Exception {
        System.out.println(0);
        Thread.sleep(1000L);
        return null;
    }
}

class TestWorkerUnmotivated extends AbstractQSWorker<Integer> {

    public static final String HANDLER_NAME = "unmotivated";

    @Override
    public String getHandlerName() {
        return HANDLER_NAME;
    }

    @Override
    public TaskKit<Integer> convert(TaskKitFactory<Integer> factory) throws Exception {
        return factory.newTaskKit(factory.paramsJson().asInt());
    }

    @Override
    public Object process(TaskKit<Integer> kit) throws Exception {
        kit.log("This is dum.");
        Thread.sleep(1000L);
        return null;
    }
}