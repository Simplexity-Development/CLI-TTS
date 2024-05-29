package simplexity.httpserver;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.messages.Errors;
import simplexity.util.Util;

import java.io.IOException;
import java.net.InetSocketAddress;

public class AuthServer {
    private static final Logger logger = LoggerFactory.getLogger(AuthServer.class);
    public static HttpServer server;

    public static void run() {
        try {
            setupServer();
        } catch (Exception exception) {
            Util.logAndPrint(logger, Errors.CAUGHT_EXCEPTION.replace("%error%", exception.getMessage()), Level.TRACE);
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
            Util.logAndPrint(logger, Errors.CAUGHT_EXCEPTION.replace("%error%", exception.getMessage()), Level.TRACE);
        }
    }


}
