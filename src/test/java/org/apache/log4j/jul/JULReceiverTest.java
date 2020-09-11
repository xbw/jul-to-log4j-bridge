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
package org.apache.log4j.jul;

import java.net.URL;

import junit.framework.TestCase;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.LoggerRepositoryExImpl;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.log4j.spi.RepositorySelector;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.logging.julbridge.JULLog4jBridge;
import org.apache.logging.julbridge.MockAppender;

public class JULReceiverTest extends TestCase {

    private static final Object repositorySelectorGuard = new Object();
    final LoggerRepository repositoryExImpl = new LoggerRepositoryExImpl(
            LogManager.getLoggerRepository());
    private MockAppender mockAppender;

    protected void setUp() throws Exception {
        super.setUp();
        java.util.logging.LogManager.getLogManager().reset();
        mockAppender = new MockAppender();
        LogManager.resetConfiguration();
        LogManager.setRepositorySelector(new RepositorySelector() {

            public LoggerRepository getLoggerRepository() {
                return repositoryExImpl;
            }
        }, repositorySelectorGuard);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        JULLog4jBridge.repatriate();
    }

    public void testJULReceiverPluginConfiguration() {
        URL resource = this.getClass().getResource("JULReceiverPluginTest.xml");
        DOMConfigurator.configure(resource);

        Logger.getRootLogger().addAppender(mockAppender);
        java.util.logging.Logger juliLogger = java.util.logging.Logger
                .getLogger("foo");
        juliLogger.info("boo");
        assertEquals("Should have received 1 event: " +
                JULBridgeTestUtils
                        .convertToString(mockAppender.observedLoggingEvents),
                1, mockAppender.observedLoggingEvents.size());

    }

}
