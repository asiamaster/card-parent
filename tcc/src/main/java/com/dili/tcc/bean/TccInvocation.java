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


public class TccInvocation implements Serializable {

    private Class<?> targetClass;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] args;

    private Object result;

    public TccInvocation(Class<?> targetClass, String methodName,
                         Class<?>[] parameterTypes,
                         Object[] args, Object result) {
        this.targetClass = targetClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
        this.result = result;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getResult() {
        return result;
    }
}
