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

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wso2.security.tools.am.webapp.service.MainService;

@Controller
public class MainController {

    private final MainService mainService;

    @Autowired
    public MainController(MainService mainService) {
        this.mainService = mainService;
    }

    @GetMapping(value = "/signin")
    public String signIn() {
        return "common/signin";
    }

    @GetMapping(value = "/mainScanners")
    public String mainScanners() {
        return "common/mainScanners";
    }

    @GetMapping(value = "/myScans")
    public String myScans() {
        return "common/myScans";
    }

    @GetMapping(value = "getMyScans")
    public String getMyScans(String userId, Model model) {
        JSONArray array = mainService.getMyScanners(userId);
        System.out.println("array length");
        System.out.println(array.length());
        model.addAttribute("staticScanners", array);
        return "common/myScans";

    }

}
