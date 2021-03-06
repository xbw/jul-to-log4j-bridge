/*
 * Copyright 1999-2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.log4j.jul;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.logging.julbridge.JULLevelConverter;
import org.apache.logging.julbridge.JULLog4jEventConverter;

/**
 * A Log4j appender for Java Logging API (aka JUL). This appender allows
 * existing log4j-enabled applications running inside a JUL-enabled environment
 * (like an application server) to correctly log events in the proper log level
 * and JUL format.<br>
 * <br>
 * The need for this appender comes from several facts:<br>
 * <br>
 * 
 * 1. JUL alone does not provide features like layouts, patterns, NDC, etc; so
 * using JUL takes away important features from developers.<br>
 * <br>
 * 
 * 2. Log4j alone does not correctly map Log4j log levels to JUL levels. The
 * best one can do at the moment is use the all-purpose ConsoleAppender, which
 * causes all log4j events to appear as either INFO or WARNING, depending on the
 * appender's target (out/err) and NOT depending on the logging method
 * (debug/trace/error/etc). Proper mapping to JUL levels (INFO, FINE, FINEST,
 * etc) is expected to depend on the logging method being called.<br>
 * <br>
 * 
 * 3. The fact that there is no appropriate log4j-JUL level mapping also means
 * that the JUL environment mistreats events generated by the application; for
 * example, the administrator's level-dependent rules may mistreat log4j events,
 * or a simple query for all FATAL-level events using standard JUL interfaces
 * would not show log4j events, making maintenance and troubleshooting
 * difficult.<br>
 * <br>
 * 
 * This appender effectively satisfies those needs:<br>
 * <br>
 * 
 * 1. It fully supports layouts like ConsoleAppender<br>
 * <br>
 * 
 * 2. It appropriately maps Log4j-levels to JUL levels, depending on the logging
 * method being called. The mapping is as follows (log4j --> JUL):<br>
 * all --> ALL<br>
 * trace --> FINER<br>
 * debug --> FINE<br>
 * info --> INFO<br>
 * warn --> WARNING<br>
 * error --> SEVERE<br>
 * fatal --> SEVERE<br>
 * <br>
 * Note: there is no *standard* JUL level between WARNING and SEVERE, so both
 * error and fatal log4j events are mapped to SEVERE.<br>
 * <br>
 * 
 * Usage and configuration is identical to using the {@link ConsoleAppener}.
 * Any application currently configured to use the ConsoleAppender, and wishes
 * to switch to JulAppender simply has to replace "ConsoleAppender" with
 * "JulAppender" in its log4j configuration; for example:<br>
 * <br>
 * <tt>
 * log4j.appender.julAppender=org.apache.log4j.JulAppender
 * log4j.appender.julAppender.layout=org.apache.log4j.PatternLayout
 * log4j.appender.julAppender.layout.ConversionPattern=%d %-5p %c - %m%n
 * </tt><br>
 * <br>
 * It may even be sensible to reduce the number of elements in the pattern since
 * JUL usually provides some context information for each logged event (JUL
 * implementation dependent).<br>
 * <br>
 * 
 * Other features:<br>
 * <br>
 * 
 * 1. Automatic mapping between log4j logger hierarchy and JUL logger hierarchy;
 * For example, a log4j logger for class com.mycompany.service.MyWebService will
 * automatically log JUL events as if it is a JUL logger by the same name, using
 * the respective JUL configuration and rules for that logger. If such a JUL
 * logger is not available by the JUL environment, it will attempt to log as if
 * it is the JUL logger com.mycompany.service, and so on.<br>
 * <br>
 * 
 * 2. If a log4j event is being dropped by the JUL environment because its level
 * is too fine, a warning will be sent to LogLog, indicating a mismatch between
 * log4j configuration and JUL configuration.<br>
 * <br>
 * 
 * 3. If, for some reason, the appropriate JUL logger cannot be obtained
 * (usually because of a JUL-environment issue), a warning will be sent to
 * LogLog.<br>
 * <br>
 * 
 * @author Sagi Mann (sagimann@gmail.com)
 * @author psmith
 */
public class JULAppender extends AppenderSkeleton {

    private JULLevelConverter levelConverter = JULLog4jEventConverter.DEFAULT_LEVEL_CONVERTER;
    private String customLevelConverterClass = null;

    /** Creates a new appender with no special layout */
    public JULAppender() {
    }

    /** Creates a new appender with the specified layout */
    public JULAppender(Layout layout) {
        setLayout(layout);
    }

    public void activateOptions() {

        if (customLevelConverterClass != null) {
            try {
                Class clazz = Class.forName(customLevelConverterClass);
                levelConverter = (JULLevelConverter) clazz.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(
                        "Failed to create custom Level Converter class '" +
                                customLevelConverterClass + "'", e);
            }
        }
        
        super.activateOptions();
    }

    /**
     * This appender requires a layout. However, if the appender is created
     * programatically without a layout, it will simply log the event as-is.
     */
    public boolean requiresLayout() {
        return true;
    }

    public void close() {
    }

    /**
     * Append a log event at the appropriate JUL level, depending on the log4j
     * level.
     */
    protected void append(LoggingEvent loggingEvent) {
        java.util.logging.Logger logger = java.util.logging.Logger
                .getLogger(loggingEvent.getLoggerName());
        if (logger == null) {
            LogLog
                    .warn("Cannot obtain JUL " +
                            loggingEvent.getLoggerName() +
                            ". " +
                            "Verify that this appender is used while an appropriate LogManager " +
                            "is active.");
            return;
        }

        // Layout is optional. No layout --> the message is written without
        // formatting.
        // In practice, since the requiresLayout method of this appender returns
        // true,
        // layout should never be null at this point, however, it is possible
        // that
        // the logging service will support optional layouts in the future...

        String msg;
        if (layout != null) {
            msg = layout.format(loggingEvent);
        } else {
            msg = loggingEvent.getRenderedMessage();
        }

        // Split the level mapping logic into 2 sections for performance reasons
        // -
        // All low-level messages (debug, trace, etc) are separated from the
        // more common-level messages (a complete binary tree is an
        // overkill...):

        Level level = loggingEvent.getLevel();

        java.util.logging.Level jullevel = levelConverter
                .convertLog4jLevel(level);
        logger.log(jullevel, msg);
    }

    /**
     * Returns the customized {@link JULLevelConverter} implementation class
     * that will be used in place of the default.
     * 
     * @return
     */
    public final String getCustomLevelConverterClass() {
        return customLevelConverterClass;
    }

    /**
     * Sets the name of the customized {@link JULLevelConverter} class
     * implementation that will be used to map log4j and JUL Logging Levels.
     * 
     * A non-null value will indicate a custom implementation, otherwise a
     * default is chosen (see {@link JULLog4jEventConverter}.
     * 
     * @param customLevelConverterClass
     */
    public final void setCustomLevelConverterClass(
            String customLevelConverterClass) {
        this.customLevelConverterClass = customLevelConverterClass;
    }
}
