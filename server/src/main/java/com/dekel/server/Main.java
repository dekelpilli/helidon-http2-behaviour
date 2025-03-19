
package com.dekel.server;


import io.helidon.webserver.WebServer;
import io.helidon.webserver.http.HttpRouting;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

public class Main {

    private static final int PORT = 3030;

    private Main() {
    }

    public static void main(String[] args) {
        WebServer webServer = WebServer.builder()
                .port(PORT)
                .routing(Main::routing)
                .build()
                .start();

        System.out.println("[SERVER] WEB server is up! http://localhost:" + webServer.port());
    }

    private static void getTest(ServerRequest req, ServerResponse res) {
        String protocol = req.prologue().protocol() + req.prologue().protocolVersion();
        System.out.printf("[SERVER] Received request with protocol %s.%n", protocol);
        res.status(200).send("Hello via " + protocol);
    }

    private static void routing(HttpRouting.Builder routing) {
        routing.get("/test", Main::getTest);
    }
}
