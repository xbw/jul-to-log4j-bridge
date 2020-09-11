package org.apache.log4j.jul;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.spi.LoggingEvent;

public class JULBridgeTestUtils {

    public static String convertToString(List observedLoggingEvents) {
        StringBuffer buf = new StringBuffer();
        for (Iterator iterator = observedLoggingEvents.iterator(); iterator
                .hasNext();) {
            LoggingEvent event = (LoggingEvent) iterator.next();
            buf.append("message:'").append(event.getMessage()).append("', ");
            buf.append("timeStamp: ").append(event.getTimeStamp());
            buf.append("logger:").append(event.getLevel());
        }
        return buf.toString();
    }
}
