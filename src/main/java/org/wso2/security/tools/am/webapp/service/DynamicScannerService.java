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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wso2.security.tools.am.webapp.entity.DynamicScanner;
import org.wso2.security.tools.am.webapp.handlers.HttpRequestHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("classpath:global.properties")
public class DynamicScannerService {

    @Value("${AUTOMATION_MANAGER_HOST}")
    private String automationManagerHost;

    @Value("${AUTOMATION_MANAGER_PORT}")
    private int automationManagerPort;

    @Value("${START_DYNAMIC_SCAN}")
    private String startScan;

    private final String HTTP = "http";
    private final String HTTPS = "https";

    public String startScan(DynamicScanner dynamicScanner, MultipartFile urlListFile, boolean isFileUpload, MultipartFile zipFile,
                            String wso2ServerHost, int wso2ServerPort, boolean isAuthenticatedScan) {
        try {
            URI uri = (new URIBuilder()).setHost(automationManagerHost).setPort(automationManagerPort).setScheme(HTTP).setPath(startScan)
                    .addParameter("userId", dynamicScanner.getUserId())
                    .addParameter("name", dynamicScanner.getName())
                    .addParameter("ipAddress", dynamicScanner.getIpAddress())
                    .addParameter("isFileUpload", String.valueOf(isFileUpload))
                    .addParameter("wso2ServerHost", wso2ServerHost)
                    .addParameter("wso2ServerPort", String.valueOf(wso2ServerPort))
                    .addParameter("isAuthenticatedScan", String.valueOf(isAuthenticatedScan))
                    .build();

            Map<String, MultipartFile> files = new HashMap<>();
            if (zipFile != null) {
                files.put("zipFile", zipFile);
            }
            files.put("urlListFile", urlListFile);

            HttpResponse response = HttpRequestHandler.sendMultipartRequest(uri, files, null);
            if (response != null) {
                return HttpRequestHandler.printResponse(response);
            } else {
                return "Cannot execute ZAP scan";
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
