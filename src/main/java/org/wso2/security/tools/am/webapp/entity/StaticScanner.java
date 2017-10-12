package org.wso2.security.tools.am.webapp.entity;
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

public class StaticScanner {
    private String userId;
    private String name;
    private String ipAddress;
    private int containerPort;
    private int hostPort;
    private boolean isProductAvailable;

    private boolean doFindSecBugs;
    private boolean doDependencyCheck;

    public StaticScanner() {
    }

    public StaticScanner(String userId, String ipAddress, int containerPort, int hostPort) {

        this.userId = userId;
//        this.name = name;
        this.ipAddress = ipAddress;
        this.containerPort = containerPort;
        this.hostPort = hostPort;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setContainerPort(int containerPort) {
        this.containerPort = containerPort;
    }

    public void setHostPort(int hostPort) {
        this.hostPort = hostPort;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductAvailable(boolean productAvailable) {
        this.isProductAvailable = productAvailable;
    }

    public void setDoFindSecBugs(boolean doFindSecBugs) {
        this.doFindSecBugs = doFindSecBugs;
    }

    public void setDoDependencyCheck(boolean doDependencyCheck) {
        this.doDependencyCheck = doDependencyCheck;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getContainerPort() {
        return containerPort;
    }

    public int getHostPort() {
        return hostPort;
    }

    public boolean isProductAvailable() {
        return isProductAvailable;
    }

    public boolean isDoFindSecBugs() {
        return doFindSecBugs;
    }

    public boolean isDoDependencyCheck() {
        return doDependencyCheck;
    }


}
