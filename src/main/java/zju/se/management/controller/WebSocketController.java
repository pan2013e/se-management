package zju.se.management.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zju.se.management.utils.Response;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/ws")
public class WebSocketController extends BaseController {
    private final WebSocketServer webSocketServer;

    @Autowired
    public WebSocketController(WebSocketServer webSocketServer) {
        this.webSocketServer = webSocketServer;
    }

    @PostMapping("/broadcast")
    public Response<?> wsBroadcast (
            @RequestParam(value = "title") String title,
            @RequestParam(value = "content") String content) throws IOException, EncodeException {
        HashMap<String, String> wsMessage = new HashMap<>();
        wsMessage.put("title", title);
        wsMessage.put("content", content);
        webSocketServer.sendObject(wsMessage);
        return ResponseOK("发送成功");
    }
}
