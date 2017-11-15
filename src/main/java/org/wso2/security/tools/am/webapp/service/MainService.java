package org.wso2.security.tools.am.webapp.service;/*
*  Copyright (c) ${date}, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
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

import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.wso2.security.tools.am.webapp.handlers.HttpsRequestHandler;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("classpath:global.properties")
public class MainService {

    @Value("${automation.manager.host}")
    private String automationManagerHost;

    @Value("${automation.manager.port}")
    private int automationManagerPort;

    @Value("${automation.manager.https-port}")
    private int automationManagerHttpsPort;

    @Value("${get.static.scanners}")
    private String getStaticScanners;

    @Value("${get.dynamic.scanners}")
    private String getDynamicScanners;


    public JSONArray[] getMyScanners(String userId) {
        try {
            JSONArray[] scannersArray = new JSONArray[2];
            URI uriToGetStaticScanners = (new URIBuilder()).setHost(automationManagerHost).setPort(automationManagerHttpsPort)
                    .setScheme("https").setPath(getStaticScanners)
                    .addParameter("userId", userId)
                    .build();

            URI uriToGetDynamicScanners = (new URIBuilder()).setHost(automationManagerHost).setPort(automationManagerHttpsPort)
                    .setScheme("https").setPath(getDynamicScanners)
                    .addParameter("userId", userId)
                    .build();

            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer 50e156bc-a28e-34b7-9921-f5eaa944920d");

            HttpsURLConnection httpsURLConnection = HttpsRequestHandler.sendRequest(uriToGetStaticScanners.toString(), headers, null, "GET");

            String jsonString = HttpsRequestHandler.getResponseAsString(httpsURLConnection);

            if (jsonString != null) {
                scannersArray[0] = new JSONArray(jsonString);
            }

            httpsURLConnection = HttpsRequestHandler.sendRequest(uriToGetDynamicScanners.toString(), headers, null, "GET");

            jsonString = HttpsRequestHandler.getResponseAsString(httpsURLConnection);

            if (jsonString != null) {
                scannersArray[1] = new JSONArray(jsonString);
            }

            return scannersArray;

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
