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

import org.apache.log4j.plugins.Plugin;
import org.apache.log4j.plugins.PluginSkeleton;
import org.apache.logging.julbridge.JULLevelConverter;
import org.apache.logging.julbridge.JULLog4jBridge;
import org.apache.logging.julbridge.JULLog4jEventConverter;

public class JULReceiver extends PluginSkeleton implements Plugin{

    private String levelConverterClassName;
    
    public void shutdown() {
        JULLog4jBridge.repatriate();
        
    }

    public void activateOptions() {
        JULLevelConverter converter = JULLog4jEventConverter.DEFAULT_LEVEL_CONVERTER;
        if (levelConverterClassName!=null) {
            Class clazz = null;
            try {
                clazz = Class.forName(levelConverterClassName);
                converter = (JULLevelConverter) clazz.newInstance();
            } catch (Exception e) {
                getLogger().error(
                        "Failed to create converter class " + levelConverterClassName + "'", e);
                return;
            }
        }
        JULLog4jBridge.assimilate(getLoggerRepository(), converter);
        active = true;
    }

}
