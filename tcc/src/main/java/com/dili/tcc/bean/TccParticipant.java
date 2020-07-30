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

package com.dili.tcc.bean;

import java.io.Serializable;


public class TccParticipant implements Serializable {

    private static final long serialVersionUID = -2590970715288987627L;

    private String transId;

    private Integer status;

    private Boolean isSuccess = false;

    private TccInvocation tccInvocation;

    public TccParticipant(String transId, Integer status, Boolean isSuccess, TccInvocation tccInvocation) {
        this.transId = transId;
        this.status = status;
        this.isSuccess = isSuccess;
        this.tccInvocation = tccInvocation;
    }

    public String getTransId() {
        return transId;
    }

    public Integer getStatus() {
        return status;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public TccInvocation getTccInvocation() {
        return tccInvocation;
    }
}
