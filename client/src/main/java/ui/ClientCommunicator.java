package ui;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import server.*;
import com.google.gson.Gson;

public class ClientCommunicator {
    public Object doPost(String urlString, RegisterRequest regReq) throws IOException {
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        connection.connect();

        var body = Map.of("username", regReq.username(), "email", regReq.email(), "password", regReq.password());

        try (OutputStream requestBody = connection.getOutputStream();) {
            // Write request body to OutputStream ...
            var jsonBody = new Gson().toJson(body);
            requestBody.write(jsonBody.getBytes());
        }

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (InputStream responseBody = connection.getInputStream()) {
                InputStreamReader inputStreamReader = new InputStreamReader(responseBody);
                return new Gson().fromJson(inputStreamReader, RegisterResponse.class);
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
                e.printStackTrace();
            }
        }
        return null;
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
}
