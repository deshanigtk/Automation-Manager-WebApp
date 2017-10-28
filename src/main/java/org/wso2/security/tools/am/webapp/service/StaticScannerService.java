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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.wso2.security.tools.am.webapp.entity.StaticScanner;
import org.wso2.security.tools.am.webapp.handlers.HttpRequestHandler;
import org.wso2.security.tools.am.webapp.handlers.HttpsRequestHandler;
import org.wso2.security.tools.am.webapp.handlers.MultipartUtility;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.List;

@Service
@PropertySource("classpath:global.properties")
public class StaticScannerService {

    @Value("${AUTOMATION_MANAGER_HOST}")
    private String automationManagerHost;

    @Value("${AUTOMATION_MANAGER_HTTPS_PORT}")
    private int automationManagerPort;

    @Value("${START_STATIC_SCAN}")
    private String startScan;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public String startScan(StaticScanner staticScanner, boolean isFileUpload, MultipartFile zipFile,
                            String url, String branch, String tag) {
        try {
            URI uri = (new URIBuilder()).setHost(automationManagerHost).setPort(automationManagerPort).setScheme("https").setPath(startScan)
                    .build();

//            Map<String, MultipartFile> files = new HashMap<>();
//
//            if (zipFile != null) {
//                files.put("zipFile", zipFile);
//            }
//            HttpResponse response = HttpRequestHandler.sendMultipartRequest(uri, files, null);
//            LOGGER.info("Sending request to Automation Manager to start static scan");
//
//            if (response != null) {
//                return HttpRequestHandler.printResponse(response);
//            }

//            MultipartUtility multipartUtility = new MultipartUtility(uri.toString());
//            multipartUtility.addFilePart("zipFile", zipFile);
//            List<String> res = multipartUtility.finish();
//            for (String s : res) {
//                System.out.println(s);
//            }
//            return "success";

//            MultipartEntityBuilder mb = MultipartEntityBuilder.create();//org.apache.http.entity.mime
//            mb.addTextBody("userId", staticScanner.getUserId());
//            mb.addTextBody("name", staticScanner.getName());
//            mb.addTextBody("ipAddress", staticScanner.getIpAddress());
//            mb.addTextBody("isFileUpload", String.valueOf(isFileUpload));
//            if (url != null) {
//                mb.addTextBody("url", url);
//            }
//            if (branch != null) {
//                mb.addTextBody("branch", branch);
//            }
//            if (tag != null) {
//                mb.addTextBody("tag", tag);
//            }
//            mb.addTextBody("isFindSecBugs", String.valueOf(staticScanner.isFindSecBugs()));
//            mb.addTextBody("isDependencyCheck", String.valueOf(staticScanner.isDependencyCheck()));
//
//
//            HttpURLConnection conn = (HttpURLConnection) new URL(uri.toString()).openConnection();
//            conn.setDoOutput(true);
//            conn.setRequestMethod("POST");
////            conn.setSSLSocketFactory(HttpsRequestHandler.getSSLContext());
//
//            conn.addRequestProperty("Content-Type", "multipart/form-data; boundary=--AAA");//header "Content-Type"...
////            conn.addRequestProperty("Content-Length", String.valueOf(e.getContentLength()));
//
//            OutputStream fout = conn.getOutputStream();
//
//
//            System.out.println(zipFile.getSize());
//            mb.addBinaryBody("zipFile", zipFile.getInputStream());
//            HttpEntity e = mb.build();
//            e.writeTo(fout);//write multi part data...
//
//            fout.close();
////            conn.getInputStream().close();//output of remote url
//
////            System.out.println(HttpRequestHandler.printResponse(conn));
////            System.out.println(conn.getContentType());
//
//            return "success";


            String charset = "UTF-8";

            try {
                MultipartUtility multipart = new MultipartUtility(uri.toString(), charset);

                multipart.addHeaderField("User-Agent", "CodeJava");
                multipart.addHeaderField("Test-Header", "Header-Value");

                multipart.addFormField("userId", staticScanner.getUserId());
                multipart.addFormField("name", staticScanner.getName());
                multipart.addFormField("ipAddress", staticScanner.getIpAddress());
                multipart.addFormField("isFileUpload", String.valueOf(isFileUpload));
                multipart.addFormField( "isFindSecBugs", String.valueOf(staticScanner.isFindSecBugs()));
                multipart.addFormField("isDependencyCheck", String.valueOf(staticScanner.isDependencyCheck()));

                multipart.addFilePart("zipFile", zipFile.getInputStream(), zipFile.getOriginalFilename());
                List<String> response = multipart.finish();

                System.out.println("SERVER REPLIED:");

                for (String line : response) {
                    System.out.println(line);
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }


            return "CCCC";

        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "Failed to execute start static scanner request";
        }


    }

}
