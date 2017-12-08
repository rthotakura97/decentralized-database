package com.decentralizeddatabase.utils;

import com.decentralizeddatabase.errors.JailCellServerError;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtility {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpUtility.class);

    public static HttpResponse postToJailCell(JSONObject toPost, String targetUrl) throws JailCellServerError {
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost request = new HttpPost(targetUrl);
            StringEntity params = new StringEntity(toPost.toString());
            request.addHeader("content-type", "application/x-www-form-urlencoded");
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);
            return response;
        } catch (IOException e) {
            LOGGER.error("{}", e.getStackTrace().toString());
        }

        //TODO We need to make these errors more clear
        throw new JailCellServerError("Error connecting to JailCell");
    }

    public static JSONObject postToJailCellWithResponse(JSONObject toPost, String targetUrl) throws JailCellServerError {
        final HttpResponse response = postToJailCell(toPost, targetUrl);
        try {
            final InputStream stream = response.getEntity().getContent();
            final String result = streamToString(stream);
            final JSONObject json = new JSONObject(result);
            return json;
        } catch (IOException e) {
            LOGGER.error("{}", e.getStackTrace().toString());
        }

        //TODO We need to make these errors more clear
        throw new JailCellServerError("Could not parse JSON returned from JailCell");
    }

    private static String streamToString(final InputStream stream) throws IOException {
        final StringBuilder json = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        String ln = null;
        while ((ln = reader.readLine()) != null) {
            json.append(ln+"\n");
        }

        return json.toString();
    }
}
