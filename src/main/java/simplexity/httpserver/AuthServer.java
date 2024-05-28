package simplexity.httpserver;

import com.sun.net.httpserver.HttpServer;
import simplexity.messages.Errors;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AuthServer {

    public static HttpServer server;

    public static void run() {
        try {
            setupServer();
        } catch (Exception exception) {
            System.out.println(Errors.CAUGHT_EXCEPTION.replace("%error%", exception.getMessage()));
        }
    }
    public static void stop() {
        server.stop(0);
    }

    private static void setupServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(3000), 0);
            server.createContext("/", new AuthHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException exception) {
            System.out.println(Errors.CAUGHT_EXCEPTION.replace("%error%", exception.getMessage()));
        }
    }
}
