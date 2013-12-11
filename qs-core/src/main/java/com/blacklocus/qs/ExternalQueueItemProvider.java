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
package com.blacklocus.qs;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Jason Dunkelberger (dirkraft)
 */
public class ExternalQueueItemProvider<Q> implements QueueItemProvider<Q>, Closeable {

    private final SynchronousQueue<Q> q = new SynchronousQueue<Q>(true);
    private final List<Q> empty = Collections.emptyList();

    private final AtomicBoolean alive = new AtomicBoolean(true);

    @Override
    public Iterator<Collection<Q>> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        return alive.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Q> next() {
        Q q = this.q.poll();
        return q == null ? empty : Arrays.asList(q); // meh, no batching
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void put(Q q) throws InterruptedException {
        this.q.put(q);
    }

    @Override
    public void close() throws IOException {
        alive.set(false);
    }
}
