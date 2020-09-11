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

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Helper class - a JUL handler that will verify the logging activity. It is
 * provided with an expected message to verify. Once the logger logs an event,
 * it may test the handler using the activated() and passed() methods:<br>
 * activated() : did the logger attempt to log anything?<br>
 * passed(): did the logger log the correct message?<br>
 */
public class AssertionHandler extends Handler {

    private String expectedMessage;
    private Level expectedLevel = null;

    private boolean passed = false;
    private boolean activated = false;
    private LogRecord lastObservedLogRecord;

    public AssertionHandler() {
        this.activated = false;
        this.passed = false;
    }

    public final Level getExpectedLevel() {
        return expectedLevel;
    }

    public final void setExpectedLevel(Level expectedLevel) {
        this.expectedLevel = expectedLevel;
    }

    public void close() throws SecurityException {
    }

    public void flush() {
    }

    public void publish(LogRecord record) {
        lastObservedLogRecord = record;
        activated = true;
        if(expectedMessage != null) {
            passed = expectedMessage.equals(record.getMessage());
        }
        if(expectedLevel != null) {
            passed = passed && (expectedLevel.intValue() == record.getLevel().intValue());
        }
    }

    public void setExpectedMessage(String expectedMessage) {
        this.expectedMessage = expectedMessage;
    }

    public boolean passed() {
        return passed;
    }

    public boolean activated() {
        return activated;
    }

    public final LogRecord getLastObservedLogRecord() {
        return lastObservedLogRecord;
    }

    public String getLastObservedLogRecordAsString() {
        
        LogRecord record = getLastObservedLogRecord();
        if(record == null ) {
            return "{no LogRecord seen}";
        }
        StringBuffer buf = new StringBuffer();
        buf.append(record.getLoggerName()).append(",").append(record.getMessage()).append("level:").append(record.getLevel());
        return buf.toString();
    }

}