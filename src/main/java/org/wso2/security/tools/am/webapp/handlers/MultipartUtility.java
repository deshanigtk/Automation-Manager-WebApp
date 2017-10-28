package org.wso2.security.tools.am.webapp.handlers;/*
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

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;


public class MultipartUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpsURLConnection httpsURLConnection;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    private static final String trustStoreType = "JKS";
    private static final String trustManagerType = "SunX509";
    private static final String protocol = "TLSv1.2";
    private static final String trustStorePath = "org.wso2.security.tools.am.webapp/truststore.jks";
    private static final String trustStorePassword = "wso2carbon";
    private static KeyStore trustStore;
    private static SSLSocketFactory sslSocketFactory;

    private static boolean isInitialized = false;


    private static void init() {
        try {
            trustStore = KeyStore.getInstance(trustStoreType);
            InputStream inputStream = HttpsRequestHandler.class.getClassLoader().getResourceAsStream(trustStorePath);
            assert inputStream != null;
            System.out.println(inputStream);
            trustStore.load(inputStream, trustStorePassword.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(trustManagerType);
            trustManagerFactory.init(trustStore);

            SSLContext sslContext = SSLContext.getInstance(protocol);

            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
            sslSocketFactory = sslContext.getSocketFactory();

            isInitialized = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */

    public MultipartUtility(String requestURL, String charset)
            throws IOException {
        init();
        this.charset = charset;

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";

        URL url = new URL(requestURL);
        httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setSSLSocketFactory(sslSocketFactory);
        httpsURLConnection.setUseCaches(false);
        httpsURLConnection.setDoOutput(true); // indicates POST method
        httpsURLConnection.setDoInput(true);
        httpsURLConnection.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        httpsURLConnection.setRequestProperty("User-Agent", "CodeJava Agent");
        httpsURLConnection.setRequestProperty("Test", "Bonjour");
        outputStream = httpsURLConnection.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=").append(charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, InputStream uploadFile, String fileName)
            throws IOException {

        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(fileName).append("\"").append(LINE_FEED);
        writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = uploadFile.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        uploadFile.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name).append(": ").append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();

        writer.append(LINE_FEED).flush();
        writer.append("--").append(boundary).append("--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        int status = httpsURLConnection.getResponseCode();
        if (status == HttpsURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpsURLConnection.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpsURLConnection.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }

        return response;
    }
}
