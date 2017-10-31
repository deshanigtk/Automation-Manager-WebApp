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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wso2.security.tools.am.webapp.entity.StaticScanner;
import org.wso2.security.tools.am.webapp.handlers.MultipartUtility;

import java.io.IOException;
import java.net.*;
import java.util.List;

@Service
@PropertySource("classpath:global.properties")
public class StaticScannerService {

    @Value("${AUTOMATION_MANAGER_HOST}")
    private String automationManagerHost;

    @Value("${AUTOMATION_MANAGER_HTTPS_PORT}")
    private int automationManagerPort;

    @Value("${START_STATIC_SCAN}")
    private String startScan;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public String startScan(StaticScanner staticScanner, boolean isFileUpload, MultipartFile zipFile,
                            String url, String branch, String tag) {
        try {

            if (isFileUpload) {
                if (zipFile == null || !zipFile.getOriginalFilename().endsWith(".zip")) {
                    return "Please upload a zip file";
                }
            } else {
                if (url == null) {
                    return "Please enter a URL to clone";
                }
            }

            URI uri = (new URIBuilder()).setHost(automationManagerHost).setPort(automationManagerPort).setScheme("https").setPath(startScan)
                    .build();

            String charset = "UTF-8";

            MultipartUtility multipartRequest = new MultipartUtility(uri.toString(), charset);

            multipartRequest.addFormField("userId", staticScanner.getUserId());
            multipartRequest.addFormField("name", staticScanner.getName());
            multipartRequest.addFormField("ipAddress", staticScanner.getIpAddress());
            multipartRequest.addFormField("isFileUpload", String.valueOf(isFileUpload));
            multipartRequest.addFormField("isFindSecBugs", String.valueOf(staticScanner.isFindSecBugs()));
            multipartRequest.addFormField("isDependencyCheck", String.valueOf(staticScanner.isDependencyCheck()));

            if (isFileUpload) {
                multipartRequest.addFilePart("zipFile", zipFile.getInputStream(), zipFile.getOriginalFilename());

            } else {
                multipartRequest.addFormField("url", url);
                if (branch != null) {
                    multipartRequest.addFormField("branch", branch);
                }
                if (tag != null) {
                    multipartRequest.addFormField("tag", tag);
                }
            }

            List<String> response = multipartRequest.finish();
            LOGGER.info("SERVER REPLIED:");

            for (String line : response) {
               LOGGER.info(line);
            }
            return "Ok";

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return "Failed to execute start static scanner request";
        }

    }

}
