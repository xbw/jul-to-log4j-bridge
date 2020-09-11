/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable
 * law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 * for the specific language governing permissions and limitations under the License.
 */
package org.apache.logging.julbridge;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

/**
 * <p>
 * {@link Filter} implementation that posts any {@link LogRecord} received to a specified
 * {@link Logger}, using the {@link JuliLevelConverter} class to map to a {@link Level}.
 * </p>
 * <p>
 * Once a {@link LogRecord} is received, it is 'appended' to all Appenders.
 * </p>
 * <p>
 * If a non-null existing Filter is provided to this class, then this instance is used to return the
 * true/false value the {@link Filter#isLoggable(LogRecord)} interface requires. If a null
 * value is provided, <code>true</code> is returned to mimic the default
 * behaviour of a Logger not having Filter configured.
 * </p>
 * 
 * @author psmith
 */
class JULBridgeFilter implements Filter {

    private final Logger loggerToPostTo;
    private final JULLog4jEventConverter converter;
    private final Filter existingJuliFilter;

    public JULBridgeFilter(final Filter existingJuliFilter, Logger loggerToPostTo,
            JULLog4jEventConverter converter) {
        this.existingJuliFilter = existingJuliFilter;
        this.loggerToPostTo = loggerToPostTo;
        this.converter = converter;
    }

    public boolean isLoggable(LogRecord record) {
        LoggingEvent event = converter.convert(record);
        loggerToPostTo.callAppenders(event);
        return (existingJuliFilter != null ? existingJuliFilter.isLoggable(record) : true);
    }

}
