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


import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import junit.framework.TestCase;

public class TestLogPosting extends TestCase {

    private Logger log;
    private java.util.logging.Logger juliLog;
    private MockAppender testAppender;
    
    public void setUp() {
        LogManager.resetConfiguration();
        java.util.logging.LogManager.getLogManager().reset();
    
        log = Logger.getLogger("foo");
        juliLog = java.util.logging.Logger.getLogger("foo");
        
        testAppender = new MockAppender();
        log.addAppender(testAppender);
    }
    
    public void testBasicObservedEvent() {
        juliLog.setFilter(new JULBridgeFilter(juliLog.getFilter(), log, new JULLog4jEventConverter()));
        String expectedMessage = "foo";
        
        juliLog.warning(expectedMessage);
        
        assertEquals("Should have observed 1 and only 1 event", 1, testAppender.observedLoggingEvents.size());
        LoggingEvent observedEvent = (LoggingEvent) testAppender.observedLoggingEvents.get(0);
        assertEquals(Level.WARN, observedEvent.getLevel());
        assertEquals(expectedMessage, observedEvent.getMessage());
        assertNull(observedEvent.getThrowableInformation());
        
    }
    
    public void tearDown() {
        JULLog4jBridge.repatriate();
    }
    
    
}
