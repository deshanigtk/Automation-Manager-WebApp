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
import org.wso2.security.tools.am.webapp.entity.DynamicScanner;
import org.wso2.security.tools.am.webapp.handlers.MultipartRequestHandler;
import org.wso2.security.tools.am.webapp.handlers.TokenHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Deshani Geethika
 */
@Service
@PropertySource("classpath:global.properties")
public class DynamicScannerService {

    @Value("${automation.manager.host}")
    private String automationManagerHost;

    @Value("${automation.manager.https-port}")
    private int automationManagerPort;

    @Value("${dynamic-scanner.start-scan}")
    private String startScan;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    public String startScan(DynamicScanner dynamicScanner, MultipartFile urlListFile, boolean isFileUpload, MultipartFile zipFile,
                            String wso2ServerHost, int wso2ServerPort, boolean isAuthenticatedScan) {

        String accessToken = TokenHandler.getAccessToken();
        int i = 0;
        while (i < 10) {
            try {
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

                MultipartRequestHandler multipartRequest = new MultipartRequestHandler(uri.toString(), charset, accessToken);
                multipartRequest.addFormField("userId", dynamicScanner.getUserId());
                multipartRequest.addFormField("testName", dynamicScanner.getTestName());
                multipartRequest.addFormField("ipAddress", dynamicScanner.getIpAddress());
                multipartRequest.addFormField("productName", dynamicScanner.getProductName());
                multipartRequest.addFormField("wumLevel", dynamicScanner.getWumLevel());
                multipartRequest.addFormField("isFileUpload", String.valueOf(isFileUpload));
                multipartRequest.addFormField("isAuthenticatedScan", String.valueOf(isAuthenticatedScan));
                multipartRequest.addFilePart("urlListFile", urlListFile.getInputStream(), urlListFile.getOriginalFilename());

                if (isFileUpload) {
                    multipartRequest.addFilePart("zipFile", zipFile.getInputStream(), zipFile.getOriginalFilename());

                } else {
                    multipartRequest.addFormField("wso2ServerHost", wso2ServerHost);
                    multipartRequest.addFormField("wso2ServerPort", String.valueOf(wso2ServerPort));
                }
                multipartRequest.finish();
                LOGGER.info("SERVER REPLIED:");

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
