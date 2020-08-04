/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dili.tcc.disruptor.event;


import com.dili.tcc.disruptor.DisruptorEventType;

/**
 * DataEvent.
 * disruptor data carrier .
 *
 * @author chenbin sixh
 */
public class DataEvent<T> {

    private T t;

    private DisruptorEventType eventType;

    public void setT(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public DisruptorEventType getEventType() {
        return eventType;
    }

    public void setEventType(DisruptorEventType eventType) {
        this.eventType = eventType;
    }
}
