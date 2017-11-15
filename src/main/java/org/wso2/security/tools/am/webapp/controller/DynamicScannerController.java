/*
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
package org.wso2.security.tools.am.webapp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wso2.security.tools.am.webapp.entity.DynamicScanner;
import org.wso2.security.tools.am.webapp.service.DynamicScannerService;

/**
 * @author Deshani Geethika
 */
@Controller
@SessionAttributes({"dynamicScanner"})
@RequestMapping("dynamicScanner")
public class DynamicScannerController {

    private final DynamicScannerService dynamicScannerService;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public DynamicScannerController(DynamicScannerService dynamicScannerService) {
        this.dynamicScannerService = dynamicScannerService;
    }

    @ModelAttribute("dynamicScanner")
    public DynamicScanner getDynamicScanner() {
        return new DynamicScanner();
    }

    @PostMapping(value = "scanner")
    public String scanner(@ModelAttribute("dynamicScanner") DynamicScanner dynamicScanner,
                          @RequestParam String userId, @RequestParam String testName, @RequestParam String ipAddress,
                          @RequestParam String productName, @RequestParam String wumLevel) throws InterruptedException {
        dynamicScanner.setUserId(userId);
        dynamicScanner.setTestName(testName);
        dynamicScanner.setIpAddress(ipAddress);
        dynamicScanner.setProductName(productName);
        dynamicScanner.setWumLevel(wumLevel);
        return "dynamicScanner/scanner";
    }

    @GetMapping(value = "scanner")
    public String scanner(@ModelAttribute("dynamicScanner") DynamicScanner dynamicScanner, @ModelAttribute("message") String message) throws InterruptedException {
        return "dynamicScanner/scanner";
    }

    @PostMapping(value = "startScan")
    public String startScan(@ModelAttribute("dynamicScanner") DynamicScanner dynamicScanner,
                            @ModelAttribute("message") String message,
                            @RequestParam MultipartFile urlListFile,
                            @RequestParam boolean isFileUpload,
                            @RequestParam(required = false) MultipartFile zipFile,
                            @RequestParam(required = false) String wso2ServerHost,
                            @RequestParam(required = false, defaultValue = "-1") int wso2ServerPort,
                            @RequestParam boolean isAuthenticatedScan) {

        String response = dynamicScannerService.startScan(dynamicScanner, urlListFile, isFileUpload, zipFile, wso2ServerHost, wso2ServerPort,
                isAuthenticatedScan);
        LOGGER.info("Response from start scan: " + response);
        return "dynamicScanner/scanner";

    }
}
