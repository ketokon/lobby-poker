package LobbyServer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import LobbyServer.Controller.LobbyController;

import java.io.IOException;
import java.util.Map;

@ServerEndpoint("/lobby")
public class WebsocketServer {

    private ObjectMapper mapper = new ObjectMapper();
    private LobbyController lobbyController = new LobbyController();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("[WebSocket] クライアント接続: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("[WebSocket] 受信: " + message);
        try {
            JsonNode json = mapper.readTree(message);
            String action = json.get("action").asText();

            Map<String, Object> result;
            if ("register".equals(action) || "login".equals(action) || "logout".equals(action) || "checkStats".equals(action)) {
                // アカウント系アクション
                result = lobbyController.callAccountService(json);
            } else if ("startMatching".equals(action)) {
                result = lobbyController.callMatchService(json);
            } else {
                result = Map.of("status", "error", "message", "Unknown action");
            }

            // 結果をクライアントへ送信
            sendJson(session, result);

        } catch (Exception e) {
            e.printStackTrace();
            // エラー応答
            sendJson(session, Map.of(
                    "status", "error",
                    "message", "Invalid JSON or server error"
            ));
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("[WebSocket] クライアント切断: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("[WebSocket] エラー: " + throwable.getMessage());
    }

    private void sendJson(Session session, Map<String, Object> data) {
        try {
            String json = mapper.writeValueAsString(data);
            session.getBasicRemote().sendText(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
