package simplexity.httpserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import simplexity.config.ConfigHandler;
import simplexity.config.LocaleHandler;
import simplexity.console.Logging;

import java.io.IOException;
import java.net.InetSocketAddress;

public class LocalServer {
    private static final Logger logger = LoggerFactory.getLogger(LocalServer.class);
    public static com.sun.net.httpserver.HttpServer server;

    /**
     * Start (or restart) the local server.
     */
    public static void run() {
        try {
            setupServer();
        } catch (Exception exception) {
            Logging.logAndPrint(logger,
                    LocaleHandler.getInstance()
                            .getErrorGeneral()
                            .replace("%error%", exception.getMessage()),
                    Level.ERROR);
        }
    }

    /**
     * Stop the local server if running.
     */
    public static void stop() {
        if (server != null) {
            try {
                server.stop(0);
                Logging.log(logger, "Local server stopped successfully", Level.INFO);
            } catch (Exception e) {
                Logging.logAndPrint(logger,
                        "Error while stopping local server: " + e.getMessage(),
                        Level.ERROR);
            } finally {
                server = null;
            }
        } else {
            Logging.log(logger, "Local server was not running (stop ignored)", Level.WARN);
        }
    }

    private static void setupServer() throws IOException {
        int port = ConfigHandler.getInstance().getServerPort();
        Logging.log(logger, "Attempting to start local server on port " + port, Level.INFO);

        com.sun.net.httpserver.HttpServer newServer =
                com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);

        newServer.setExecutor(null);
        newServer.createContext("/", new ChatHandler());
        newServer.start();

        server = newServer;
        Logging.log(logger, "Local server started successfully on port " + port, Level.INFO);
    }


}
