package com.automationanywhere.botcommand.samples.misc;

import java.security.PrivateKey;
import java.time.Instant;
import java.util.*;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.samples.Utils.HTTPRequest;

import java.io.File;


import com.automationanywhere.botcommand.samples.Utils.PemUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class test {

    public static void main(String[] args) throws Exception {

        String clientId = "9dca6746-d56e-4d88-9736-ad94a608ddc1";
        String url = "https://fhir.epic.com/interconnect-fhir-oauth/";

        String authURL = url + "oauth2/token";
        Instant now = Instant.now();

        // create  instance object
        // print Instant Value
        //System.out.println("Instant: " + now);

        // get epochValue using getEpochSecond
        long epochValue = now.getEpochSecond() + 240;
        int epochInt = (int) epochValue;
        //System.out.println(epochInt);


        PrivateKey key = PemUtils.pemFileLoadPrivateKeyPkcs1OrPkcs8Encoded(new File("C:\\Users\\jamesdickson\\Documents\\FHIR\\PEM\\privatekey.pem"));

        String jwt = Jwts.builder()
                .claim("iss",clientId)
                .claim("sub",clientId)
                .claim("aud", authURL)
                .claim("jti", UUID.randomUUID().toString())
                .claim("exp", epochInt)
                .signWith(key, SignatureAlgorithm.RS384)
                .compact();
        //System.out.print(jwt);

        String response = HTTPRequest.oAuthMethod(authURL, jwt);
        Object obj = new JSONParser().parse(response);
        JSONObject jsonResponse = (JSONObject) obj;
        String accessToken = (String) jsonResponse.get("access_token");


    }
}



