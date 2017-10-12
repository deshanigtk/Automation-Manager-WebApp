package org.wso2.security.tools.am.webapp.controller;/*
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wso2.security.tools.am.webapp.entity.StaticScanner;
import org.wso2.security.tools.am.webapp.handlers.HttpRequestHandler;

import java.net.URI;
import java.net.URISyntaxException;

@Controller
@PropertySource("classpath:global.properties")
@SessionAttributes("staticScanner")
@RequestMapping(value = "staticScanner")
public class StaticScannerController {

    @Value("${automation_manager_host}")
    private String automationManagerHost;

    @Value("${automation_manager_port}")
    private int automationManagerPort;

    private final String HTTP = "http";
    private final String HTTPS = "https";

    @ModelAttribute("staticScanner")
    public StaticScanner getStaticScanner() {
        return new StaticScanner();
    }

    @PostMapping(value = "scanners")
    public String scanners(@ModelAttribute("staticScanner") StaticScanner staticScanner,
                           @RequestParam String userId, @RequestParam String name, @RequestParam String ipAddress, @RequestParam int containerPort,
                           @RequestParam int hostPort) throws InterruptedException {
        staticScanner.setUserId(userId);
        staticScanner.setName(name);
        staticScanner.setIpAddress(ipAddress);
        staticScanner.setContainerPort(containerPort);
        staticScanner.setHostPort(hostPort);
        return "staticScanner/scanners";
    }

    @GetMapping(value = "scanners")
    public String scanners(@ModelAttribute("staticScanner") StaticScanner staticScanner) throws InterruptedException {
        return "staticScanner/scanners";
    }

    @PostMapping(value = "productUploader")
    public String productUploader(@ModelAttribute("staticScanner") StaticScanner staticScanner, @RequestParam boolean isFindSecBugs,
                                  @RequestParam boolean isDependencyCheck) {

        staticScanner.setDoFindSecBugs(isFindSecBugs);
        staticScanner.setDoDependencyCheck(isDependencyCheck);
        return "staticScanner/productUploader";
    }

    @GetMapping(value = "productUploader")
    public String productUploader(@ModelAttribute("staticScanner") StaticScanner staticScanner) {
        return "staticScanner/productUploader";
    }

    @PostMapping(value = "startScan")
    public void startScan(@ModelAttribute("staticScanner") StaticScanner staticScanner,
                          @RequestParam boolean isZipFileUpload,
                          @RequestParam(required = false) MultipartFile zipFile,
                          @RequestParam(required = false) String url,
                          @RequestParam(required = false) String branch,
                          @RequestParam(required = false) String tag) {

        try {
            URI uri = (new URIBuilder()).setHost(automationManagerHost).setPort(automationManagerPort).setScheme(HTTP).setPath("start")
                    .addParameter("userId", staticScanner.getUserId())
                    .addParameter("ipAddress", staticScanner.getIpAddress())
                    .addParameter("hostPort", String.valueOf(staticScanner.getHostPort()))
                    .addParameter("containerPort", String.valueOf(staticScanner.getContainerPort()))
                    .build();

            HttpResponse response = HttpRequestHandler.sendPostRequest(uri, null);

            if (response != null) {
                if (isZipFileUpload) {
                    uri = (new URIBuilder()).setHost(automationManagerHost).setPort(automationManagerPort).setScheme(HTTP).setPath("upload")
                            .addParameter("userId", staticScanner.getUserId())
                            .addParameter("ipAddress", staticScanner.getIpAddress())
                            .addParameter("hostPort", String.valueOf(staticScanner.getHostPort()))
                            .addParameter("containerPort", String.valueOf(staticScanner.getContainerPort()))
                            .build();

                    response = HttpRequestHandler.sendPostRequest(uri, null);
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
}
