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

import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.wso2.security.tools.am.webapp.entity.StaticScanner;
import org.wso2.security.tools.am.webapp.handlers.HttpRequestHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

@Service
@PropertySource("classpath:global.properties")
public class MainService {

    @Value("${AUTOMATION_MANAGER_HOST}")
    private String automationManagerHost;

    @Value("${AUTOMATION_MANGER_PORT}")
    private int automationManagerPort;

    @Value("${GET_STATIC_SCANNERS}")
    private String getStaticScanners;

    @Value("${GET_DYNAMIC_SCANNERS}")
    private String getDynamicScanners;


    public JSONArray getMyScanners(String userId) {
        try {
            URI uriToGetStaticScanners = (new URIBuilder()).setHost(automationManagerHost).setPort(automationManagerPort).setScheme("http").setPath(getStaticScanners)
                    .addParameter("userId", userId)
                    .build();

            HttpResponse response = HttpRequestHandler.sendGetRequest(uriToGetStaticScanners);

            if (response != null) {
                String json_string = EntityUtils.toString(response.getEntity());
                JSONArray temp1 = new JSONArray(json_string);
                return temp1;
            }

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
