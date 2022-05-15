package zju.se.management.controller;

import org.springframework.stereotype.Component;
import zju.se.management.utils.HashMapSerializer;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/message", encoders = {HashMapSerializer.class})
public class WebSocketServer {
    private static final CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<>();

    public static Set<WebSocketServer> getWebSocketSet() {
        return webSocketSet;
    }

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
    }

    public void sendObject(HashMap<String, String> object) throws IOException, EncodeException {
        for (WebSocketServer webSocketServer : webSocketSet) {
            if(webSocketServer.session.isOpen()){
                webSocketServer.session.getBasicRemote().sendObject(object);
            }
        }
    }
}
