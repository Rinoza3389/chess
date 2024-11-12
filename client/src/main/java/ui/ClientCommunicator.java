package ui;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import ui.reqRes.*;
import com.google.gson.Gson;

public class ClientCommunicator {
    public Object doPost(String urlString, Object requestObj) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        if (requestObj instanceof CreateGameRequest) {
            connection.addRequestProperty("authorization", ((CreateGameRequest) requestObj).authToken());
        }

        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream()) {
            // Write request body to OutputStream ...
            var jsonBody = new Gson().toJson(requestObj);
            requestBody.write(jsonBody.getBytes());
        }


        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                if (requestObj instanceof RegisterRequest) {
                    return new Gson().fromJson(inputStreamReader, RegisterResponse.class);
                }
                else if (requestObj instanceof LoginRequest) {
                    return new Gson().fromJson(inputStreamReader, LoginResponse.class);
                }
                else if (requestObj instanceof CreateGameRequest) {
                    return new Gson().fromJson(inputStreamReader, CreateGameResponse.class);
                }
            }
            // Read response body from InputStream ...
        }
        else {
            // SERVER RETURNED AN HTTP ERROR
            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
            try {
                if (responseBody != null) {
                    String errorResponse = readStream(responseBody);
                    return new Gson().fromJson(errorResponse, ErrorResponse.class);
                } else {
                    System.out.println("No error response body.");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public Object doDelete(String urlString, Object logReq) throws IOException{
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("DELETE");

        if (logReq instanceof LogoutRequest) {
        connection.addRequestProperty("authorization", ((LogoutRequest) logReq).authToken()); }

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                return null;
            }
            // Read response body from InputStream ...
        }
        else {
            // SERVER RETURNED AN HTTP ERROR
            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
            try {
                if (responseBody != null) {
                    String errorResponse = readStream(responseBody);
                    return new Gson().fromJson(errorResponse, ErrorResponse.class);
                } else {
                    System.out.println("No error response body.");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return new ErrorResponse(404, "Connection Failure");
    }

    private static String readStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder responseBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            responseBuilder.append(line).append("\n");
        }

        return responseBuilder.toString().trim();
    }

    public Object doGet(String urlString, ListRequest listReq) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        connection.addRequestProperty("authorization", listReq.authToken());

        connection.connect();

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                return new Gson().fromJson(inputStreamReader, ListResponse.class);

            }
        }
        else {
            // SERVER RETURNED AN HTTP ERROR
            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
            try {
                if (responseBody != null) {
                    String errorResponse = readStream(responseBody);
                    return new Gson().fromJson(errorResponse, ErrorResponse.class);
                } else {
                    System.out.println("No error response body.");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public Object doPut(String urlString, JoinRequest joinReq) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("PUT");
        connection.setDoOutput(true);

        connection.addRequestProperty("authorization", joinReq.authToken());

        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream()) {
            // Write request body to OutputStream ...
            var jsonBody = new Gson().toJson(joinReq);
            requestBody.write(jsonBody.getBytes());
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                return new Gson().fromJson(inputStreamReader, JoinResponse.class);

            }
        }
        else {
            // SERVER RETURNED AN HTTP ERROR
            InputStream responseBody = connection.getErrorStream();
            // Read and process error response body from InputStream ...
            try {
                if (responseBody != null) {
                    String errorResponse = readStream(responseBody);
                    return new Gson().fromJson(errorResponse, ErrorResponse.class);
                } else {
                    System.out.println("No error response body.");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
}
