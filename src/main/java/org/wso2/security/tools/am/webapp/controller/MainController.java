/*
*  Copyright (c) ${2017}, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.wso2.security.tools.am.webapp.entity.DynamicScanner;
import org.wso2.security.tools.am.webapp.entity.StaticScanner;
import org.wso2.security.tools.am.webapp.entity.User;
import org.wso2.security.tools.am.webapp.service.DynamicScannerService;
import org.wso2.security.tools.am.webapp.service.StaticScannerService;
import org.wso2.security.tools.am.webapp.service.UserService;

/**
 * @author Deshani Geethika
 */

@SessionAttributes({"user", "staticScanner", "dynamicScanner"})
@Controller
public class MainController {

    private final UserService userService;
    private final DynamicScannerService dynamicScannerService;
    private final StaticScannerService staticScannerService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public MainController(UserService userService, DynamicScannerService dynamicScannerService, StaticScannerService staticScannerService) {
        this.staticScannerService = staticScannerService;
        this.dynamicScannerService = dynamicScannerService;
        this.userService = userService;
    }

    /**
     * Initiate a session attribute to keep user email
     *
     * @return user session attribute
     */
    @ModelAttribute("user")
    public User getUser() {
        return new User();
    }


    /**
     * Initiate a session attribute to keep dynamic scanner related user inputs
     *
     * @return dynamicScanner session attribute
     */
    @ModelAttribute("dynamicScanner")
    public DynamicScanner getDynamicScanner() {
        return new DynamicScanner();
    }

    /**
     * Initiate a model attribute to keep static scanner related user inputs
     *
     * @return staticScanner model attribute
     */
    @ModelAttribute("staticScanner")
    public StaticScanner getStaticScanner() {
        return new StaticScanner();
    }

    @GetMapping(value = "/")
    public String signIn() {
        return "common/signin";
    }

    @PostMapping(value = "signin")
    public String authUser(@ModelAttribute("user") User user, @RequestParam String userId) {
        user.setEmail(userId);
        return "common/mainScanners";
    }

    /**
     * Returns main scanner UI
     *
     * @param user Model attribute
     * @return UI path of main scanners
     */
//    @GetMapping(value = "mainScanners")
//    public String mainScanners(@ModelAttribute("user") User user) {
//        return "common/mainScanners";
//    }

    /**
     * @param dynamicScanner Session attribute
     * @param userId         User id
     * @param testName       Test name
     * @param productName    Product name
     * @param wumLevel       WUM level
     * @return Scanner page of dynamic scanner
     */
    @PostMapping(value = "dynamicScanner/scanners")
    public String scanner(@ModelAttribute("dynamicScanner") DynamicScanner dynamicScanner,
                          @ModelAttribute("user") User user,
                          @RequestParam String userId, @RequestParam String testName,
                          @RequestParam String productName, @RequestParam String wumLevel) {
        dynamicScanner.setUserId(userId);
        dynamicScanner.setTestName(testName);
        dynamicScanner.setProductName(productName);
        dynamicScanner.setWumLevel(wumLevel);
        return "dynamicScanner/scanners";
    }

    /**
     * @param dynamicScanner Model attribute
     * @return Scanner page of dynamic scanner
     */
    @GetMapping(value = "dynamicScanner/scanners")
    public String scanner(@ModelAttribute("dynamicScanner") DynamicScanner dynamicScanner) {
        return "dynamicScanner/scanners";
    }

    /**
     * Sends request to start scan
     *
     * @param dynamicScanner         Dynamic scanner model attribute
     * @param urlListFile            URL list file to be scanned
     * @param productUploadAsZipFile Product uploaded as a zip file. False means product is in up and running state
     * @param zipFile                zip file of the product binary
     * @param wso2ServerHost         Wso2 server host, if the product is in up and running state
     * @param wso2ServerPort         Wso2 server port, if the product is in up and running state
     * @return Scanner page of dynamic scanner
     */
    @PostMapping(value = "dynamicScanner/startScan")
    public String startScan(@ModelAttribute("dynamicScanner") DynamicScanner dynamicScanner,
                            @RequestParam MultipartFile urlListFile,
                            @RequestParam boolean productUploadAsZipFile,
                            @RequestParam(required = false) MultipartFile zipFile,
                            @RequestParam(required = false) String wso2ServerHost,
                            @RequestParam(required = false, defaultValue = "-1") int wso2ServerPort) {
        dynamicScannerService.startScan(dynamicScanner, urlListFile, productUploadAsZipFile, zipFile, wso2ServerHost, wso2ServerPort);
        return "dynamicScanner/scanner";
    }


    @PostMapping(value = "staticScanner/scanners")
    public String scanners(@ModelAttribute("staticScanner") StaticScanner staticScanner,
                           @ModelAttribute("user") User user,
                           @RequestParam String testName,
                           @RequestParam String productName, String wumLevel) throws InterruptedException {
        staticScanner.setUserId(user.getEmail());
        System.out.println(user.getEmail());
        staticScanner.setTestName(testName);
        staticScanner.setProductName(productName);
        staticScanner.setWumLevel(wumLevel);
        return "staticScanner/scanners";
    }

    @GetMapping(value = "staticScanner/scanners")
    public String scanners(@ModelAttribute("staticScanner") StaticScanner staticScanner) throws InterruptedException {
        return "staticScanner/scanners";
    }

    @PostMapping(value = "staticScanner/productUploader")
    public String productUploader(@ModelAttribute("staticScanner") StaticScanner staticScanner, @RequestParam boolean isFindSecBugs,
                                  @RequestParam boolean isDependencyCheck) {
        staticScanner.setFindSecBugs(isFindSecBugs);
        staticScanner.setDependencyCheck(isDependencyCheck);
        return "staticScanner/productUploader";
    }

    @GetMapping(value = "staticScanner/productUploader")
    public String productUploader(@ModelAttribute("staticScanner") StaticScanner staticScanner) {
        return "staticScanner/productUploader";
    }

    @PostMapping(value = "staticScanner/startScan")
    public String startScan(@ModelAttribute("staticScanner") StaticScanner staticScanner,
                            @RequestParam boolean sourceCodeUploadAsZip,
                            @RequestParam(required = false) MultipartFile zipFile,
                            @RequestParam(required = false) String url) {
        String response = staticScannerService.startScan(staticScanner, sourceCodeUploadAsZip,zipFile, url);
        LOGGER.info("Start static scanner response: " + response);
        return "common/myScans";
    }

    @GetMapping(value = "myScanners")
    public String getMyScans(@RequestParam String userId, Model model) {
        JSONArray[] scanners = userService.getMyScanners(userId);
        if (scanners != null) {
            model.addAttribute("staticScanners", scanners[0]);
            model.addAttribute("dynamicScanners", scanners[1]);
        }
        return "common/myScans";
    }
}
