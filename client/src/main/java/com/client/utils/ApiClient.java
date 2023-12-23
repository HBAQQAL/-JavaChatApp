package com.client.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ApiClient {

    public static Map<String, Object> makeHttpRequest(String apiUrl, String jsonInputString, String method) {
        validateInputParameters(apiUrl, jsonInputString, method);

        try {
            HttpURLConnection connection = openHttpConnection(apiUrl, method);
            sendJsonData(connection, jsonInputString);

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            CoockieHandler.getInstance().setCoockieFromHeader(connection.getHeaderField("Set-Cookie"));

            try (InputStream inputStream = getInputStream(connection, responseCode)) {
                return processResponse(inputStream, responseCode);
            }
        } catch (MalformedURLException e) {
            handleMalformedURLException(e);
        } catch (IOException e) {
            handleIOException(e);
        }
        return null;
    }

    private static void validateInputParameters(String apiUrl, String jsonInputString, String method) {
        if (apiUrl == null || apiUrl.isEmpty() || jsonInputString == null || jsonInputString.isEmpty()
                || method == null || method.isEmpty()) {
            throw new IllegalArgumentException("Invalid input parameters");
        }
    }

    private static HttpURLConnection openHttpConnection(String apiUrl, String method) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        configureConnection(connection, method);
        return connection;
    }

    private static void configureConnection(HttpURLConnection connection, String method) throws IOException {
        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        connection.setDoInput(true);
    }

    private static void sendJsonData(HttpURLConnection connection, String jsonInputString) throws IOException {
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
    }

    private static InputStream getInputStream(HttpURLConnection connection, int responseCode) throws IOException {
        return (responseCode >= 200 && responseCode <= 299) ? connection.getInputStream() : connection.getErrorStream();
    }

    private static Map<String, Object> processResponse(InputStream inputStream, int responseCode) {
        String response = readInputStream(inputStream);
        Map<String, Object> responseMap = mapJsonResponse(response);
        responseMap.put("status", (responseCode == 200) ? "success" : "error");
        return responseMap;
    }

    private static String readInputStream(InputStream inputStream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error reading input stream", e);
        }
    }

    private static Map<String, Object> mapJsonResponse(String jsonResponse) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(jsonResponse, type);
    }

    private static void handleMalformedURLException(MalformedURLException e) {
        // More specific exception handling
        e.printStackTrace();
    }

    private static void handleIOException(IOException e) {
        // More specific exception handling
        e.printStackTrace();
    }
}