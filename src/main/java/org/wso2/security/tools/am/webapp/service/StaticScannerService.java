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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wso2.security.tools.am.webapp.entity.StaticScanner;
import org.wso2.security.tools.am.webapp.handlers.HttpRequestHandler;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("classpath:global.properties")
public class StaticScannerService {


    @Value("${AUTOMATION_MANAGER_HOST}")
    private String automationManagerHost;

    @Value("${AUTOMATION_MANGER_PORT}")
    private int automationManagerPort;

    @Value("${START_STATIC_SCAN}")
    private String startScan;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public String startScan(StaticScanner staticScanner, boolean isFileUpload, MultipartFile zipFile,
                            String url, String branch, String tag) {
        try {
            URI uri = (new URIBuilder()).setHost(automationManagerHost).setPort(automationManagerPort).setScheme("http").setPath(startScan)
                    .addParameter("userId", staticScanner.getUserId())
                    .addParameter("name", staticScanner.getName())
                    .addParameter("ipAddress", staticScanner.getIpAddress())
                    .addParameter("isFileUpload", String.valueOf(isFileUpload))
                    .addParameter("url", url)
                    .addParameter("branch", branch)
                    .addParameter("tag", tag)
                    .addParameter("isFindSecBugs", String.valueOf(staticScanner.isFindSecBugs()))
                    .addParameter("isDependencyCheck", String.valueOf(staticScanner.isDependencyCheck()))
                    .build();

            Map<String, MultipartFile> files = new HashMap<>();

            if (zipFile != null) {
                files.put("zipFile", zipFile);
            }
            HttpResponse response = HttpRequestHandler.sendMultipartRequest(uri, files, null);
            LOGGER.info("Sending request to Automation Manager to start static scan");

            if (response != null) {
                return HttpRequestHandler.printResponse(response);
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();

        }
        return "Failed to execute start static scanner request";

    }

}
