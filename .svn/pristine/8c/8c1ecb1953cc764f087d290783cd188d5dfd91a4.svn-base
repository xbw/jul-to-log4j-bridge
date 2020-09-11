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

import java.util.Enumeration;
import java.util.logging.Filter;
import java.util.logging.Level;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerRepository;

/**
 * <p>
 * Helper class that creates bridge filters between java.util.logging.Loggers
 * and an equivalent log4j Logger.
 * </p>
 * <p>
 * The <code>bridge</code>-style method calls use a {@link Filter}
 * implementation that tries to minimise interference with the existing
 * <code>java.util.logging</code> configuration, but can only bridge specific
 * named loggers, and is not able to bridge child loggers automatically. Any
 * Logger that is bridged that contains an existing Filter instance is delegated
 * to when deciding if the LogRecord should continue in the java.util.logging
 * framework, even though the bridge posts the LogRecord into log4j regardless.
 * If there was no existing Filter defined for the bridged Logger, then the
 * bridging Filter returns 'true' to mimic the default behaviour of not having a
 * Filter defined.
 * </p>
 * <p>
 * The <code>assimilate</code>-style methods are much more dramatic, in that
 * they are designed to completely take over the JUL-logging, and route all
 * events into log4j. This mechanism resets the java.util.logging framework
 * configuration to it's default state, which has default {@link Level}
 * configured at INFO, and then configures the root JUL Logger with the matching
 * Level that the provided LoggerRepository has been configured. You should,
 * therefore, assimilate the JUL logging system after the log4j system has been
 * properly configured. You should consider whether the default
 * {@link JULLevelConverter} is appropriate to your needs, you may optionally
 * provide your own conversion instance to map the 2 Level classes.
 * </p>
 * <p>
 * The assimilate approach is very useful for the general case where you wish to
 * forget about JUL logging and use the flexibility of the log4j system. There
 * is an ever so slightly higher cost in routing the event into log4j using this
 * approach because each LogRecord received must lookup a named {@link Logger}.
 * The <code>bridge</code>-style approach is slightly faster because the
 * specifically requested logger name is looked-up on construction and re-used.
 * Generally though, the slight performance hit is not worth the inflexibility
 * of having to specify each and every Logger you want to bridge.
 * </p>
 * 
 * @see JULLevelConverter
 * @see JULLog4jEventConverter#DEFAULT_LEVEL_CONVERTER
 * @author psmith
 */
public class JULLog4jBridge {

    /**
     * Convenience method that gathers the default LoggerRepository and uses a
     * default Level Converter implemenation.
     * 
     * @param JULLoggerName
     */
    public static void bridgeJULLogger(String JULLoggerName) {
        bridgeJULLogger(JULLoggerName, LogManager.getLoggerRepository(),
                JULLog4jEventConverter.DEFAULT_LEVEL_CONVERTER);
    }

    public static void bridgeJULLogger(String JULLoggerName,
            LoggerRepository repository) {
        bridgeJULLogger(JULLoggerName, repository,
                JULLog4jEventConverter.DEFAULT_LEVEL_CONVERTER);
    }

    /**
     * Creates a bridge between the named java.util.logging Logger name, and an
     * equivalent inside the log4j LoggerRepository, using the provided level
     * converter class. If the JUL Logger already has a configured Filter, then
     * it is replaced by this bridge, and a WARN level event is triggered on the
     * JULLog4jBridge own Logger
     * 
     * @param JULLoggerName
     * @param repository
     */
    public static void bridgeJULLogger(String JULLoggerName,
            LoggerRepository repository, JULLevelConverter levelConverter) {
        Logger log4jLogger = repository.getLogger(JULLoggerName);
        java.util.logging.Logger JULLogger = java.util.logging.Logger
                .getLogger(JULLoggerName);

        JULLogger.setFilter(new JULBridgeFilter(JULLogger.getFilter(),
                log4jLogger, new JULLog4jEventConverter(repository,
                        levelConverter)));
    }

    /**
     * Completely take over the underlying java.logging sub-system. Any existing
     * java.logging configuration is reset, and a top level Handler is
     * registered that gathers all LogRecords and converts them into log4j
     * LoggingEvents.
     */
    public static void assimilate(LoggerRepository repository,
            JULLevelConverter levelConverter) {

        java.util.logging.LogManager.getLogManager().reset();

        // clearAllFilters(java.util.logging.LogManager.getLogManager());

        java.util.logging.Logger.getLogger("").setLevel(
                levelConverter.convertLog4jLevel(repository.getThreshold()));
        java.util.logging.Logger.getLogger("").addHandler(
                new JULBridgeHandler(repository, new JULLog4jEventConverter(
                        repository, levelConverter)));
    }

    private static void clearAllFilters(java.util.logging.LogManager logManager) {
        Enumeration enumeration = logManager.getLoggerNames();
        while (enumeration.hasMoreElements()) {
            java.util.logging.Logger logger = logManager.getLogger(enumeration
                    .nextElement().toString());
            logger.setFilter(null);
        }
    }

    public static void assimilate(LoggerRepository repository) {
        assimilate(repository, JULLog4jEventConverter.DEFAULT_LEVEL_CONVERTER);
    }

    public static void assimilate() {
        assimilate(LogManager.getLoggerRepository());
    }

    /**
     * Reverse of {@link #assimilate()}, disconnects the bridge from the
     * java.util.logging subsystem by resetting that framework back to it's
     * default configuration (removes all Handlers and Filters)
     */
    public static void repatriate() {

        java.util.logging.LogManager logManager = java.util.logging.LogManager
                .getLogManager();
        Enumeration loggerNames = logManager.getLoggerNames();
        while (loggerNames.hasMoreElements()) {
            logManager.getLogger(loggerNames.nextElement().toString())
                    .setFilter(null);
        }
        logManager.reset();

    }

}
