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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.logging.julbridge.JULLevelConverter;
import org.apache.logging.julbridge.JULLog4jBridge;

import junit.framework.TestCase;

public class JULAppenderCustomLevelTest extends TestCase{

    java.util.logging.Logger julLogger;
    Logger log;
    AssertionHandler testHandler;
    
    protected void setUp() throws Exception {
        // Prepare the JUL logger to simulate a JUL environment
        // The logger is set to log only INFO-level events or higher.
        // During testing, a JUL handler will verify the activity of the logger.
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
        log.setLevel(Level.ALL);
        JULAppender appender = new JULAppender();
        appender.setCustomLevelConverterClass(SimpleCustomLevelConverter.class.getName());
        appender.activateOptions();
        log.addAppender(appender);
        
        /*
         * We expect ALL levels to be INFO in this case.
         */
        testHandler.setExpectedLevel(java.util.logging.Level.INFO);
    }
    
    
    
    protected void tearDown() throws Exception {
        JULLog4jBridge.repatriate();
    }
    
    public void testCustomLevelTests() {
        String message;
        
        message = "this is a trace message";
        testHandler.setExpectedMessage(message);
        log.trace(message);
        
        assertTrue(testHandler.passed());
        
        message = "this is a debug message";
        testHandler.setExpectedMessage(message);
        log.debug(message);
        
        assertTrue(testHandler.passed());
    }
    
    public static class SimpleCustomLevelConverter implements JULLevelConverter{

        public Level convertJuliLevel(java.util.logging.Level juliLevel) {
            return Level.INFO;
        }

        public java.util.logging.Level convertLog4jLevel(Level log4jLevel) {
            return java.util.logging.Level.INFO;
        }
        
    }
    
}
