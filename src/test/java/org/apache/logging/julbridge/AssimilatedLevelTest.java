package org.apache.logging.julbridge;

import java.util.logging.Logger;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.jul.JULBridgeTestUtils;

/**
 * Ensures that logging events arriving into log4j obey the configured levels
 * for that logger.
 * 
 * For example, Finer and Finest level in JUL should not be sent to appenders if
 * the matching log4j Logger is set to WARN level.
 * 
 * @author Joshua ChaitinPollack, modified to fit unit test structure by Paul
 *         Smith
 * 
 */
public class AssimilatedLevelTest extends TestCase {

    private MockAppender mockAppender;

    protected void setUp() throws Exception {
        super.setUp();
        LogManager.resetConfiguration();
        JULLog4jBridge.assimilate();
        mockAppender = new MockAppender();
        org.apache.log4j.Logger.getRootLogger().addAppender(mockAppender);

    }

    protected void tearDown() throws Exception {
        super.tearDown();
        JULLog4jBridge.repatriate();
        mockAppender.close();
        mockAppender = null;
    }

    public void testExpectedLevels() {

        org.apache.log4j.Logger.getRootLogger().setLevel(Level.WARN);
        Logger logger = Logger.getLogger("test.logger");

        logger.finest("Finest");
        logger.finer("Finer");
        logger.fine("Fine");
        logger.warning("Warning");
        logger.severe("Severe");
        assertEquals("Mismatch on expected # events: " +
                JULBridgeTestUtils
                        .convertToString(mockAppender.observedLoggingEvents),
                2, mockAppender.observedLoggingEvents.size());
    }

}
