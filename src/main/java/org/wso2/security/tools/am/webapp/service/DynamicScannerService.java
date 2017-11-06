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
import org.wso2.security.tools.am.webapp.entity.DynamicScanner;
import org.wso2.security.tools.am.webapp.handlers.MultipartUtility;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
@PropertySource("classpath:global.properties")
public class DynamicScannerService {

    @Value("${AUTOMATION_MANAGER_HOST}")
    private String automationManagerHost;

    @Value("${AUTOMATION_MANAGER_HTTPS_PORT}")
    private int automationManagerPort;

    @Value("${START_DYNAMIC_SCAN}")
    private String startScan;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public String startScan(DynamicScanner dynamicScanner, MultipartFile urlListFile, boolean isFileUpload, MultipartFile zipFile,
                            String wso2ServerHost, int wso2ServerPort, boolean isAuthenticatedScan) {
        try {
            System.out.println(dynamicScanner.getIpAddress());
            System.out.println(dynamicScanner.getUserId());
            if (isFileUpload) {
                if (zipFile == null || !zipFile.getOriginalFilename().endsWith(".zip")) {
                    return "Please upload a zip file";
                }
            } else {
                if (wso2ServerHost == null || wso2ServerPort == -1) {
                    return "Please enter wso2 server host and port";
                }
            }

            URI uri = (new URIBuilder()).setHost(automationManagerHost).setPort(automationManagerPort).setScheme("https").setPath(startScan)
                    .build();

            String charset = "UTF-8";

            MultipartUtility multipartRequest = new MultipartUtility(uri.toString(), charset);

            multipartRequest.addFormField("userId", dynamicScanner.getUserId());
            multipartRequest.addFormField("name", dynamicScanner.getName());
            multipartRequest.addFormField("ipAddress", dynamicScanner.getIpAddress());
            multipartRequest.addFormField("isFileUpload", String.valueOf(isFileUpload));
            multipartRequest.addFormField("isAuthenticatedScan", String.valueOf(isAuthenticatedScan));
            multipartRequest.addFilePart("urlListFile", urlListFile.getInputStream(), urlListFile.getOriginalFilename());

            if (isFileUpload) {
                multipartRequest.addFilePart("zipFile", zipFile.getInputStream(), zipFile.getOriginalFilename());

            } else {
                multipartRequest.addFormField("wso2ServerHost", wso2ServerHost);
                multipartRequest.addFormField("wso2ServerPort", String.valueOf(wso2ServerPort));
            }

            List<String> response = multipartRequest.finish();
            LOGGER.info("SERVER REPLIED:");

            for (String line : response) {
                LOGGER.info(line);
            }
            return "Ok";
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
