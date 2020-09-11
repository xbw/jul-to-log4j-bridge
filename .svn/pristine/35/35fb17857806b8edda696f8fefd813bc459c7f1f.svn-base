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

import java.io.IOException;

import org.apache.log4j.LogManager;

/**
 * Another way to bridge JUL->log4j is be configure custom JUL LogManager class by configuring the <code>java.util.logging.manager</code> system property, so
 * by specifing this classname as the value for that property you can immediately transfel all JUL events straight into log4j.
 * 
 * Just add this to your command-line JVM arguments:
 * 
 * <pre>-Djava.util.logging.manager=org.apache.logging.julbridge.JULBridgeLogManager</pre>
 * 
 * @author Brett Randall
 *
 */
public class JULBridgeLogManager extends java.util.logging.LogManager {

    public void readConfiguration() throws IOException, SecurityException {
        JULLog4jBridge.assimilate(LogManager.getLoggerRepository());
    }
}