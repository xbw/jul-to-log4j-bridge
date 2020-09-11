/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.logging.julbridge;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

public class MockAppender implements Appender {
    
    public List observedLoggingEvents = new ArrayList();
    
    public void addFilter(Filter newFilter) {

    }

    public void clearFilters() {
    }

    public void close() {
    }

    public void doAppend(LoggingEvent event) {
        observedLoggingEvents.add(event);
    }

    public ErrorHandler getErrorHandler() {
        return null;
    }

    public Filter getFilter() {
        return null;
    }

    public Layout getLayout() {
        return null;
    }

    public String getName() {
        return null;
    }

    public boolean requiresLayout() {
        return false;
    }

    public void setErrorHandler(ErrorHandler errorHandler) {

    }

    public void setLayout(Layout layout) {
    }

    public void setName(String name) {
        // TODO Auto-generated method stub

    }

}