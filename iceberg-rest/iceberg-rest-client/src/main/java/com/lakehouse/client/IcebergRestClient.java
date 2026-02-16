package com.lakehouse.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class IcebergRestClient {

    private static final String BASE_URL = "http://localhost:8181";

    private final HttpClient client;
    private final ObjectMapper mapper;

    public IcebergRestClient() {
        this.client = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    // ---------------------------
    // LIST NAMESPACES
    // ---------------------------
    public void listNamespaces() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/v1/namespaces"))
                .GET()
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("List Namespaces Status: " + response.statusCode());
        System.out.println(mapper.readTree(response.body()).toPrettyString());
    }

    // ---------------------------
    // CREATE NAMESPACE
    // ---------------------------
    public void createNamespace(String name) throws Exception {

        Map<String, Object> body = Map.of(
                "namespace", new String[]{name}
        );

        String jsonBody = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/v1/namespaces"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Create Namespace Status: " + response.statusCode());
        if (!response.body().isBlank()) {
            JsonNode json = mapper.readTree(response.body());
            System.out.println(json.toPrettyString());
        }
    }

    // ---------------------------
    // MAIN
    // ---------------------------
    public static void main(String[] args) throws Exception {

        IcebergRestClient client = new IcebergRestClient();

        System.out.println("Before creation:");
        client.listNamespaces();

        System.out.println("\nCreating namespace 'demo'...");
        client.createNamespace("demo");

        System.out.println("\nAfter creation:");
        client.listNamespaces();
    }
}
