/*
 * Copyright (c) ${date}, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.security.tools.am.webapp.entity;

public class StaticScanner {

    private String userId;
    private String testName;
    private String ipAddress;
    private String productName;
    private String wumLevel;
    private boolean isFindSecBugs;
    private boolean isDependencyCheck;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setWumLevel(String wumLevel) {
        this.wumLevel = wumLevel;
    }

    public void setFindSecBugs(boolean findSecBugs) {
        this.isFindSecBugs = findSecBugs;
    }

    public void setDependencyCheck(boolean dependencyCheck) {
        this.isDependencyCheck = dependencyCheck;
    }

    public String getUserId() {
        return userId;
    }

    public String getTestName() {
        return testName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getProductName() {
        return productName;
    }

    public String getWumLevel() {
        return wumLevel;
    }

    public boolean isFindSecBugs() {
        return isFindSecBugs;
    }

    public boolean isDependencyCheck() {
        return isDependencyCheck;
    }
}
