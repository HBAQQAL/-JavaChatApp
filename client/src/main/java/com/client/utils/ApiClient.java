package com.client.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class ApiClient {

    public static Map<String, Object> makeHttpRequest(String apiUrl, String jsonInputString, String method) {
        validateInputParameters(apiUrl, method);

        try {
            HttpURLConnection connection = openHttpConnection(apiUrl, method);
            sendJsonData(connection, jsonInputString);

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            handleResponseHeaders(connection);

            try (InputStream inputStream = getInputStream(connection, responseCode)) {
                return processResponse(inputStream, responseCode);
            }

        } catch (IOException e) {
            handleIOException(e);
        }
        return null;
    }

    // this method is used for get request
    public static Map<String, Object> makeHttpRequest(String apiUrl, String method, Map<String, String> queryParams) {
        try {
            apiUrl = apiUrl + "?" + buildQueryString(queryParams);
            HttpURLConnection connection = openHttpConnection(apiUrl, method);

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            try (InputStream inputStream = getInputStream(connection, responseCode)) {
                try {
                    return processResponse(inputStream, responseCode);
                } catch (java.lang.IllegalStateException e) {
                    Map<String, Object> responseMap = mapJsonResponse("{'status':'success'}");
                    responseMap.put("status", (responseCode == 200) ? "success" : "error");

                    return responseMap;
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        } catch (IOException e) {
            handleIOException(e);
        }
        return null;
    }

    private static void validateInputParameters(String apiUrl, String method) {
        if (apiUrl == null || apiUrl.isEmpty() || method == null || method.isEmpty()) {
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

        sendCookie(connection);
    }

    private static void sendJsonData(HttpURLConnection connection, String jsonInputString) throws IOException {
        if (jsonInputString != null && !jsonInputString.isEmpty()) {
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
    }

    private static void sendQueryParams(HttpURLConnection connection, Map<String, String> queryParams)
            throws IOException {
        if (queryParams != null && !queryParams.isEmpty()) {
            String queryString = buildQueryString(queryParams);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(queryString.getBytes().length));
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = queryString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }
    }

    private static void sendCookie(HttpURLConnection connection) {
        String cookie = CoockieHandler.getInstance().getJSESSIONID() + ";" + CoockieHandler.getInstance().getCoockie();
        if (cookie != null && !cookie.isEmpty()) {
            connection.setRequestProperty("Cookie", cookie);
        }
    }

    private static void handleResponseHeaders(HttpURLConnection connection) {
        try {
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                System.out.println("Header : " + entry.getKey() + " , Values : " + entry.getValue());
            }

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                handleCookies(headers);
            }
        } catch (IOException e) {
            // Handle the IOException here
            e.printStackTrace();
        }
    }

    private static void handleCookies(Map<String, List<String>> headers) {
        List<String> cookies = headers.get("Set-Cookie");
        if (cookies != null) {
            for (String cookie : cookies) {
                // Handle SessionId and JSESSIONID
                if (cookie.contains("SessionId")) {
                    CoockieHandler.getInstance().setCoockie(cookie.split(";")[0]);
                }
                if (cookie.contains("JSESSIONID")) {
                    CoockieHandler.getInstance().setJSESSIONID(cookie.split(";")[0]);
                }
            }
            System.out.println("Cookie: " + CoockieHandler.getInstance().getCoockie());
            System.out.println("JSESSIONID: " + CoockieHandler.getInstance().getJSESSIONID());
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

    private static void handleIOException(IOException e) {
        // More specific exception handling can be added
        e.printStackTrace();
    }

    private static String buildQueryString(Map<String, String> queryParams) {
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            if (queryString.length() > 0) {
                queryString.append("&");
            }
            queryString.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return queryString.toString();
    }
}
