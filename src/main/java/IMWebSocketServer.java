import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

public class IMWebSocketServer extends org.java_websocket.server.WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger("server");
    private final int loops;

    public IMWebSocketServer(int port, int loops) {
        super(new InetSocketAddress(port));
        this.loops = loops;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        logger.info(conn.getRemoteSocketAddress().getAddress().getHostAddress() + " connected!");
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < loops; i++) {
            conn.send("message " + i + ", 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890 1234567890");
        }
        long duration = System.currentTimeMillis() - t1;
        logger.info("duration(ms): {}", duration);
        logger.info("TPS: {}", loops * 1000.0 / duration);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        logger.info(conn + " connected!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        logger.info("received:" + message);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        logger.info("received:" + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        if (conn != null) {
            logger.error(conn.toString(), ex);
        } else {
            logger.error("im ws error", ex);
        }
    }

    @Override
    public void onStart() {
        logger.info("Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

    public static void main(String[] args) throws URISyntaxException {
        System.setProperty("java.util.logging.SimpleFormatter.format","%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        int port = Integer.parseInt(args[0]);
        int loops = Integer.parseInt(args[1]);
        IMWebSocketServer s = new IMWebSocketServer(port, loops);
        s.start();
        logger.info("im server started on port " + s.getPort());
    }

}