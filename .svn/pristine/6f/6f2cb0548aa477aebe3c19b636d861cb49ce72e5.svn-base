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


import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.logging.julbridge.JULLog4jBridge;

/** JulAppender test case
 *
 * @author Sagi Mann (sagimann@gmail.com)
 * @author psmith
 */
public class JULAppenderTest extends TestCase {
    
    
    private java.util.logging.Logger julLogger;
    private Logger log;
    private AssertionHandler testHandler;
    
    
    public JULAppenderTest(String testName) {
        super(testName);
    }
    
    
    
    protected void setUp() throws Exception {
        // Prepare the JUL logger to simulate a JUL environment
        // The logger is set to log only INFO-level events or higher.
        // During testing, a JUL handler will verify the activity of the logger.
        LogManager.resetConfiguration();
        JULLog4jBridge.repatriate();
        String julLoggerName = JULAppenderTest.class.getName();
        julLogger = java.util.logging.Logger.getLogger(julLoggerName);
        julLogger.setLevel(java.util.logging.Level.INFO);
        //julLogger.setUseParentHandlers(false);
        testHandler = new AssertionHandler();
        julLogger.addHandler(testHandler);
        
        // Prepare the Log4j logger that will be tested using the JulAppender.
        // It is set to log ALL events on purpose, in order to test JUL-Log4j
        // level inconsistencies.
        log = Logger.getLogger(JULAppenderTest.class);
        if(log.getAllAppenders().hasMoreElements()) {
            throw new IllegalStateException("Huh, there should be no appenders connected to this logger");
        }
        log.setLevel(Level.ALL);
        JULAppender appender = new JULAppender();
        appender.activateOptions();
        log.addAppender(appender);
    }
    
    
    
    protected void tearDown() throws Exception {
        JULLog4jBridge.repatriate();
    }
    
    
    
    public void testAllLevels() {
        String message;
        
        message = "this is a trace message";
        log.trace(message);
        assertFalse("log4j should not have received an event yet, handler should still be inactive: lastObservedEvent:" + testHandler.getLastObservedLogRecordAsString(), testHandler.activated());
        
        message = "this is a debug message";
        testHandler.setExpectedMessage(message);
        log.debug(message);
        assertFalse(testHandler.activated());
        
        message = "this is an info message";
        testHandler.setExpectedMessage(message);
        log.info(message);
        assertTrue(testHandler.activated() && testHandler.passed());
        
        message = "this is a warn message";
        testHandler.setExpectedMessage(message);
        log.warn(message);
        assertTrue(testHandler.activated() && testHandler.passed());
        
        message = "this is an error message";
        testHandler.setExpectedMessage(message);
        log.error(message);
        assertTrue(testHandler.activated() && testHandler.passed());
        
        message = "this is a fatal message";
        testHandler.setExpectedMessage(message);
        log.fatal(message);
        assertTrue(testHandler.activated() && testHandler.passed());
    }
}
