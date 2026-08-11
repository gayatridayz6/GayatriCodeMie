package com.library.qa.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HttpTestClient {
    private final HttpClient client = HttpClient.newHttpClient();

    public HttpResponse<String> get(String url, String basicAuth) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .header("Accept", "application/json");

        if (basicAuth != null && !basicAuth.isBlank()) {
            builder.header("Authorization", basicAuth);
        }

        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> postJson(String url, String jsonBody, String basicAuth) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");

        if (basicAuth != null && !basicAuth.isBlank()) {
            builder.header("Authorization", basicAuth);
        }

        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> delete(String url, String basicAuth) throws IOException, InterruptedException {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .header("Accept", "application/json");

        if (basicAuth != null && !basicAuth.isBlank()) {
            builder.header("Authorization", basicAuth);
        }

        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    public String basicAuthHeader(String username, String password) {
        String toEncode = username + ":" + password;
        String base64 = Base64.getEncoder().encodeToString(toEncode.getBytes(StandardCharsets.UTF_8));
        return "Basic " + base64;
    }
}
