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

import java.util.logging.Level;

import junit.framework.TestCase;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LocationInfo;
import org.apache.log4j.spi.LoggingEvent;

public class JULLog4jBridgeTest extends TestCase {

    private static final String PARENT_LOGGER = "foo";
    private static final String LOGGER_NAME = PARENT_LOGGER + ".beep";
    private MockAppender mockAppender;
    private java.util.logging.Logger JULLogger;
    private java.util.logging.Logger parentLogger;
    private java.util.logging.Logger JULRootLogger;
    

    protected void setUp() throws Exception {
        LogManager.resetConfiguration();
        java.util.logging.LogManager.getLogManager().reset();
    
        mockAppender = new MockAppender();
        Logger.getRootLogger().addAppender(mockAppender);
        JULLogger = java.util.logging.Logger.getLogger(LOGGER_NAME);
        parentLogger = java.util.logging.Logger.getLogger(PARENT_LOGGER);
        JULRootLogger = java.util.logging.Logger.getLogger("");
        
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        mockAppender = null;
        /*
         * It's pretty dumb, but  java.util.logging.LogManager.getLogManager().reset();
         * does NOT remove any Filter set
         */
        JULLogger.setFilter(null);
        JULLog4jBridge.repatriate();
    }

    public void testBridgeJULLogger() {
        
        JULLogger.warning("boo");
        
        assertEquals("Shouldn't expect a bridged event", 0, mockAppender.observedLoggingEvents.size());
        JULLog4jBridge.bridgeJULLogger(LOGGER_NAME);
     
        JULLogger.warning("boo2");
        
        assertEquals(1, mockAppender.observedLoggingEvents.size());
        assertEquals("boo2", ((LoggingEvent)mockAppender.observedLoggingEvents.get(0)).getMessage());
        
    }
    
    public void testLocationInfo(){
        JULLog4jBridge.bridgeJULLogger(LOGGER_NAME);
        JULLogger.logp(Level.INFO, "com.mycompany.MyClass", "doBar", "something");

        assertEquals(1, mockAppender.observedLoggingEvents.size());
        LoggingEvent loggingEvent = ((LoggingEvent)mockAppender.observedLoggingEvents.get(0));
        assertEquals(org.apache.log4j.Level.INFO, loggingEvent.getLevel());
        assertEquals("something", loggingEvent.getMessage());

        LocationInfo locationInformation = loggingEvent.getLocationInformation();
        assertNotNull(locationInformation);
        assertNotNull(locationInformation.getClassName());
        assertNotNull(locationInformation.getMethodName());
        
        assertFalse("Class name should not be '?'","?".equals(locationInformation.getClassName()));
        assertFalse("Method name should not be '?'","?".equals(locationInformation.getMethodName()));
        
        
        
    }
    
    public void testAbsorbedIntoTheLog4jCollective(){
        JULLog4jBridge.assimilate();
        
        JULLogger.logp(Level.INFO, "com.mycompany.MyClass", "doBar", "something");

        assertEquals(1, mockAppender.observedLoggingEvents.size());
        LoggingEvent loggingEvent = ((LoggingEvent)mockAppender.observedLoggingEvents.get(0));
        assertEquals(org.apache.log4j.Level.INFO, loggingEvent.getLevel());
        assertEquals("something", loggingEvent.getMessage());

        LocationInfo locationInformation = loggingEvent.getLocationInformation();
        assertNotNull(locationInformation);
        assertNotNull(locationInformation.getClassName());
        assertNotNull(locationInformation.getMethodName());
        
        assertFalse("Class name should not be '?'","?".equals(locationInformation.getClassName()));
        assertFalse("Method name should not be '?'","?".equals(locationInformation.getMethodName()));
        
        parentLogger.log(Level.INFO, "Hello World");
        assertEquals(2, mockAppender.observedLoggingEvents.size());
        loggingEvent = ((LoggingEvent)mockAppender.observedLoggingEvents.get(1));
        assertEquals(org.apache.log4j.Level.INFO, loggingEvent.getLevel());
        assertEquals("Hello World", loggingEvent.getMessage());
        
    }
    public void testJULLevelConfigurationChanges(){
        assertEquals(Level.INFO, JULRootLogger.getLevel());
        
        LogManager.getLoggerRepository().setThreshold(org.apache.log4j.Level.WARN);
        
        JULLog4jBridge.assimilate();
        
        assertEquals(Level.WARNING, JULRootLogger.getLevel());
        
        
    }

}
