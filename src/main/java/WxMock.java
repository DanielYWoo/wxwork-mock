import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class WxMock {

    private static final Logger logger = LoggerFactory.getLogger("server");

    public static void main(String[] args) throws URISyntaxException {
        System.setProperty("java.util.logging.SimpleFormatter.format","%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        String im = args[0];
        logger.info("mock started, try to connect to IM:" + im);
        WebSocketClient cc = new WebSocketClient(new URI(im)) {

            @Override
            public void onMessage(String message) {
                logger.info("received:" + message);
            }

            @Override
            public void onOpen(ServerHandshake handshake) {
                logger.info("connected to im-server: " + getURI());
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("disconnected from: " + getURI() + "; Code: " + code + " " + reason);
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };

        cc.connect();

    }

}