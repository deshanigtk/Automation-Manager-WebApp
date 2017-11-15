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

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

public class JWTHandler {

    public static boolean validateToken(String signedJWTAsString) {
        try {
            RSAPublicKey publicKey;
            InputStream file = ClassLoader.getSystemResourceAsStream("wso2carbon.jks");
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(file, "wso2carbon".toCharArray());

            String alias = "wso2carbon";

            // Get certificate of public key
            Certificate cert = keystore.getCertificate(alias);
            // Get public key
            publicKey = (RSAPublicKey) cert.getPublicKey();

            // Enter JWT String here
            SignedJWT signedJWT = SignedJWT.parse(signedJWTAsString);

            JWSVerifier verifier = new RSASSAVerifier(publicKey);

            if (signedJWT.verify(verifier)) {
                System.out.println("Signature is Valid");
                return true;
            } else {
                System.out.println("Signature is NOT Valid");
            }
        } catch (IOException | CertificateException | NoSuchAlgorithmException | JOSEException | KeyStoreException | ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String extractEmailFromJWT(String signedJWTAsString) {
        try {
            if (validateToken(signedJWTAsString)) {
                SignedJWT signedJWT = SignedJWT.parse(signedJWTAsString);
                String jsonString = signedJWT.getPayload().toString();
                JSONObject jsonObject = new JSONObject(jsonString);
                return jsonObject.getString("sub");

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
