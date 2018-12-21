package com.jeeps.ckan_extractor.service;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.util.function.Consumer;

public class HttpService {
    private HttpClient client;

    public HttpService() {
        SSLContext context;
        try {
            context = SSLContext.getInstance("TLSv1.3");
            context.init(null, null, null);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        client = HttpClient.newBuilder().sslContext(context).build();
    }

    public void sendRequest(Consumer<String> onSuccess, String url) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(onSuccess)
                .join();
    }
}
