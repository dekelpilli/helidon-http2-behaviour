
package com.dekel.client;


import io.helidon.http.Method;
import io.helidon.webclient.api.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final String HTTP2_COMPATIBLE_URI = "https://gitlab.com";
    private static final String HTTP2_INCOMPATIBLE_URI = "http://localhost:3030/test";

    private Main() {
    }

    private static void testHttp2(WebClient client, String uriString) throws URISyntaxException {
        URI uri = new URI(uriString);
        try {
            Class clz = client.method(Method.GET)
                    .uri(uri)
                    .request()
                    .getClass();
            System.out.printf("[CLIENT] Made request to %s, received %s when protocol was not specified.%n", uri, clz);
        } catch (Exception e) {
            System.out.printf("[CLIENT] Made request to %s, threw exception when protocol was not specified.%n", uri);
            e.printStackTrace();
        }

        try {
            Class clz = client.method(Method.GET)
                    .uri(uri)
                    .protocolId("h2")
                    .request()
                    .getClass();
            System.out.printf("[CLIENT] Made request to %s, received %s when protocol was specified as 'h2'.%n", uri, clz);
        } catch (Exception e) {
            System.out.printf("[CLIENT] Made request to %s, threw exception when protocol was specified as 'h2'.%n", uri);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws URISyntaxException {
        WebClient client = WebClient.builder().build();
        System.out.println("[CLIENT] Testing using default client...");
        testHttp2(client, HTTP2_COMPATIBLE_URI);
        testHttp2(client, HTTP2_INCOMPATIBLE_URI);

        List<String> protocols = new ArrayList<>();
        protocols.add("h2");
        protocols.add("http/1.1");

        WebClient protocolSpecifiedClient = WebClient.builder().protocolPreference(protocols).build();
        System.out.println("[CLIENT] Testing using client with HTTP2 priority set manually...");
        testHttp2(protocolSpecifiedClient, HTTP2_COMPATIBLE_URI);
        testHttp2(protocolSpecifiedClient, HTTP2_INCOMPATIBLE_URI);

        //query params don't work when h2 is specified?
        client.method(Method.POST)
                .uri(new URI("https://webhook.site/1a562b31-adba-4aad-9f9a-e1a56ee4b9cc"))
                .queryParam("xyz", "test")
                .protocolId("h2")
                .request()
                .getClass();

        client.method(Method.PATCH)
                .uri(new URI("https://webhook.site/1a562b31-adba-4aad-9f9a-e1a56ee4b9cc?xyz=test"))
                .protocolId("h2")
                .request()
                .getClass();

        System.out.println("[CLIENT] Made requests to webhook.site, check link to see if query params were present in the requests: https://webhook.site/#!/view/1a562b31-adba-4aad-9f9a-e1a56ee4b9cc");
    }
}
