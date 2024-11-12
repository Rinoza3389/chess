package ui;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import ui.reqres.*;
import com.google.gson.Gson;

public class ClientCommunicator {
    public Object doPost(String urlString, Object requestObj) throws IOException {
        HttpURLConnection connection = setUpConnection(urlString, requestObj, "POST");

        writeBodyOut(connection, requestObj);


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
            return dealWithError(connection);
        }
        return null;
    }

    public Object doDelete(String urlString, Object logReq) throws IOException{
        HttpURLConnection connection = setUpConnection(urlString, logReq, "DELETE");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                return null;
            }
            // Read response body from InputStream ...
        }
        else {
            return dealWithError(connection);
        }
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
        HttpURLConnection connection = setUpConnection(urlString, listReq, "GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                return new Gson().fromJson(inputStreamReader, ListResponse.class);

            }
        }
        else {
            return dealWithError(connection);
        }
    }

    public Object doPut(String urlString, JoinRequest joinReq) throws IOException {
        HttpURLConnection connection = setUpConnection(urlString, joinReq, "PUT");

        writeBodyOut(connection, joinReq);

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                return new Gson().fromJson(inputStreamReader, JoinResponse.class);

            }
        }
        else {
            // SERVER RETURNED AN HTTP ERROR
            return dealWithError(connection);
        }
    }

    private HttpURLConnection setUpConnection(String urlString, Object requestObj, String method) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod(method);
        connection.setDoOutput(true);

        if (requestObj instanceof CreateGameRequest) {
            connection.addRequestProperty("authorization", ((CreateGameRequest) requestObj).authToken());
        } else if (requestObj instanceof LogoutRequest) {
            connection.addRequestProperty("authorization", ((LogoutRequest) requestObj).authToken());
        } else if (requestObj instanceof ListRequest) {
            connection.addRequestProperty("authorization", ((ListRequest) requestObj).authToken());
        } else if (requestObj instanceof JoinRequest) {
            connection.addRequestProperty("authorization", ((JoinRequest) requestObj).authToken());
        }

        connection.connect();

        return connection;
    }

    private void writeBodyOut(HttpURLConnection connection, Object joinReq) throws IOException {
        try (OutputStream requestBody = connection.getOutputStream()) {
            // Write request body to OutputStream ...
            var jsonBody = new Gson().toJson(joinReq);
            requestBody.write(jsonBody.getBytes());
        }
    }

    private Object dealWithError(HttpURLConnection connection) throws IOException {
        InputStream responseBody = connection.getErrorStream();
        // Read and process error response body from InputStream ...
        try {
            if (responseBody != null) {
                String errorResponse = readStream(responseBody);
                return new Gson().fromJson(errorResponse, ErrorResponse.class);
            } else {
                return "No error response body.";
            }
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
