package org.wso2.security.tools.am.webapp.property;/*
*  Copyright (c) ${date}, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this propertiesFile to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this propertiesFile except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

import java.io.*;
import java.util.Properties;

public class GlobalProperty {

    private static Properties properties;
    private static String clientId;
    private static String clientSecret;
    private static String accessTokenUri;

    static {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(GlobalProperty.class.getClassLoader().getResource("global.properties").getFile())));
            clientId = properties.getProperty("webapp.client-id");
            clientSecret = properties.getProperty("webapp.client-secret");
            accessTokenUri = properties.getProperty("webapp.access-token-uri");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getClientId() {
        return clientId;
    }

    public static String getClientSecret() {
        return clientSecret;
    }

    public static String getAccessTokenUri() {
        return accessTokenUri;
    }

}
