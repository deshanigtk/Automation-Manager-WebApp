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

import org.apache.http.HttpStatus;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wso2.security.tools.am.webapp.entity.StaticScanner;
import org.wso2.security.tools.am.webapp.handlers.MultipartUtility;
import org.wso2.security.tools.am.webapp.handlers.TokenHandler;

import java.io.IOException;
import java.net.*;

@Service
@PropertySource("classpath:global.properties")
public class StaticScannerService {

    @Value("${automation.manager.host}")
    private String automationManagerHost;

    @Value("${automation.manager.https-port}")
    private int automationManagerPort;

    @Value("${static-scanner.start-scan}")
    private String startScan;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public String startScan(StaticScanner staticScanner, boolean isFileUpload, MultipartFile zipFile,
                            String url, String branch, String tag) {
        String accessToken = TokenHandler.getAccessToken();
        int i = 0;
        while (i < 10) {
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

                MultipartUtility multipartRequest = new MultipartUtility(uri.toString(), charset, accessToken);

                multipartRequest.addFormField("userId", staticScanner.getUserId());
                multipartRequest.addFormField("testName", staticScanner.getTestName());
                multipartRequest.addFormField("ipAddress", staticScanner.getIpAddress());
                multipartRequest.addFormField("productName", staticScanner.getProductName());
                multipartRequest.addFormField("wumLevel", staticScanner.getWumLevel());
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
                multipartRequest.finish();
                if (multipartRequest.getResponseStatus() == HttpStatus.SC_OK) {
                    return "Ok";
                }
                Thread.sleep(1000);

            } catch (URISyntaxException | IOException | InterruptedException e) {
                e.printStackTrace();
                TokenHandler.generateAccessToken();
                accessToken = TokenHandler.getAccessToken();
                i += 1;
            }
        }
        return "Error response";
    }
}
