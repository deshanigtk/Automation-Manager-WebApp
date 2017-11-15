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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wso2.security.tools.am.webapp.entity.StaticScanner;
import org.wso2.security.tools.am.webapp.service.StaticScannerService;

@Controller
@SessionAttributes("staticScanner")
@RequestMapping(value = "staticScanner")
public class StaticScannerController {

    private final StaticScannerService staticScannerService;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StaticScannerController(StaticScannerService staticScannerService) {
        this.staticScannerService = staticScannerService;
    }

    @ModelAttribute("staticScanner")
    public StaticScanner getStaticScanner() {
        return new StaticScanner();
    }

    @PostMapping(value = "scanners")
    public String scanners(@ModelAttribute("staticScanner") StaticScanner staticScanner,
                           @RequestParam String userId, @RequestParam String testName, @RequestParam String ipAddress,
                           @RequestParam String productName, String wumLevel) throws InterruptedException {
        staticScanner.setUserId(userId);
        staticScanner.setTestName(testName);
        staticScanner.setIpAddress(ipAddress);
        staticScanner.setProductName(productName);
        staticScanner.setWumLevel(wumLevel);
        return "staticScanner/scanners";
    }

    @GetMapping(value = "scanners")
    public String scanners(@ModelAttribute("staticScanner") StaticScanner staticScanner) throws InterruptedException {
        return "staticScanner/scanners";
    }

    @PostMapping(value = "productUploader")
    public String productUploader(@ModelAttribute("staticScanner") StaticScanner staticScanner, @RequestParam boolean isFindSecBugs,
                                  @RequestParam boolean isDependencyCheck) {

        staticScanner.setFindSecBugs(isFindSecBugs);
        staticScanner.setDependencyCheck(isDependencyCheck);
        return "staticScanner/productUploader";
    }

    @GetMapping(value = "productUploader")
    public String productUploader(@ModelAttribute("staticScanner") StaticScanner staticScanner) {
        return "staticScanner/productUploader";
    }

    @PostMapping(value = "startScan")
    public String startScan(@ModelAttribute("staticScanner") StaticScanner staticScanner,
                            @RequestParam boolean isFileUpload,
                            @RequestParam(required = false) MultipartFile zipFile,
                            @RequestParam(required = false) String url,
                            @RequestParam(required = false) String branch,
                            @RequestParam(required = false) String tag) {
        String response = staticScannerService.startScan(staticScanner, isFileUpload, zipFile, url, branch, tag);
        LOGGER.info("Start static scanner response: " + response);
        return "common/myScans";
    }
}
