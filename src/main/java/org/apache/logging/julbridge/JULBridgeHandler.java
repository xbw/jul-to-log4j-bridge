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

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.LoggingEvent;

/**
 * An implementation of {@link Handler} that converts the {@link LogRecord} into
 * a {@link LoggingEvent} and posts it to a duplicately named {@link Logger} in
 * the log4j system.
 * 
 * @author psmith
 * 
 */
class JULBridgeHandler extends Handler {
    private static final String UNKNOWN_LOGGER_NAME = "unknown.jul.logger";

    private final LoggerRepository repository;
    private final JULLog4jEventConverter converter;

    public JULBridgeHandler(LoggerRepository repository,
            JULLog4jEventConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public void close() throws SecurityException {

    }

    public void flush() {

    }

    public void publish(LogRecord record) {
        LoggingEvent event = converter.convert(record);

        String loggerName = record.getLoggerName();
        if (loggerName == null) {
    	    loggerName = UNKNOWN_LOGGER_NAME;
        }

        Category localLogger = repository.getLogger(loggerName);
        if (event.getLevel().isGreaterOrEqual(localLogger.getEffectiveLevel())) {
            localLogger.callAppenders(event);
        }
    }

}
