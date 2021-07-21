import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class WxMock {

    private static final Logger logger = LoggerFactory.getLogger("server");

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        System.setProperty("java.util.logging.SimpleFormatter.format","%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$-6s %2$s %5$s%6$s%n");
        String im = args[0];
        String clientId = im.substring(im.lastIndexOf('/') + 1);
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
                logger.info("disconnected from: " + getURI() + "; Code: " + code + " " + reason);
            }

            @Override
            public void onError(Exception ex) {
                logger.error("error on connection", ex);
           }

        };

        cc.connect();
        String data = "{\n" +
                "  \"type\":10009,\n" +
                "  \"clientId\":\"" + clientId + "\",\n" +
                "  \"data\":{\n" +
                "    \"hardwareInfo\":{},\n" +
                "    \"content\": \"heartCheck\"\n" +
                "  }\n" +
                "}";
        while (true) {
            try { Thread.sleep(3000); } catch (InterruptedException e) { e.printStackTrace(); }
            if (cc.isOpen()) {
                logger.info("heart beat");
                cc.send(data);
            } else {
                logger.info("sleep 3 seconds to reconnect");
                cc.reconnect();
            }
        }

    }

}