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

package org.wso2.security.tools.am.webapp.handlers;

import org.apache.http.impl.execchain.RequestAbortedException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Utility methods for HTTPS request handling
 *
 * @author Deshani Geethika
 */
public class HttpsRequestHandler extends AbstractHttpsRequestHandler {


    private static HttpsURLConnection httpsURLConnection;

    public static HttpsURLConnection sendRequest(String link, Map<String, String> requestHeaders, Map<String, Object> requestParams,
                                                 String method, String accessToken) throws RequestAbortedException, UnsupportedEncodingException {

        if (!isInitialized) {
            init();
        }

        StringBuilder postData = new StringBuilder();
        if (requestParams != null) {
            for (Map.Entry<String, Object> param : requestParams.entrySet()) {
                if (postData.length() != 0) {
                    postData.append('&');
                }
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
        }
        try {
            URL url = new URL(link);
            if (requestParams != null) {
                url = new URL(url.toString() + "?" + postData);
            }

            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
            httpsURLConnection.setRequestMethod(method);
            httpsURLConnection.setInstanceFollowRedirects(false);
            if (accessToken != null) {
                httpsURLConnection.setRequestProperty("Authorization", "Bearer " + accessToken);
            }

            if (requestHeaders != null) {
                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                    httpsURLConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            return httpsURLConnection;
        } catch (IOException e) {
            throw new RequestAbortedException("Https request aborted");
        }
    }

    public static String getResponseAsString(HttpsURLConnection httpsURLConnection) {
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            StringBuilder builder = new StringBuilder();

            String line;
            line = bf.readLine();
            while (line != null) {
                builder.append(line);
                line = bf.readLine();
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String printResponse(HttpsURLConnection httpsURLConnection) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(httpsURLConnection.getResponseCode())
                .append(" ")
                .append(httpsURLConnection.getContentLength())
                .append(httpsURLConnection.getContent().toString())
                .append(httpsURLConnection.getResponseMessage())
                .append("\n");

        Map<String, List<String>> headerFields = httpsURLConnection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            if (entry.getKey() == null)
                continue;
            builder.append(entry.getKey())
                    .append(": ");

            List<String> headerValues = entry.getValue();

            Iterator<String> it = headerValues.iterator();
            if (it.hasNext()) {
                builder.append(it.next());

                while (it.hasNext()) {
                    builder.append(", ")
                            .append(it.next());
                }
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public static List<String> getResponseValue(String key, HttpsURLConnection httpsURLConnection) {
        Map<String, List<String>> headerFields = httpsURLConnection.getHeaderFields();
        for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
            if (entry.getKey() == null)
                continue;

            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
