package LobbyServer.Controller;

import LobbyServer.AccountService;
import com.fasterxml.jackson.databind.JsonNode;
import LobbyServer.AccountService;

import java.util.HashMap;
import java.util.Map;

/**
 * LobbyController:
 *  - WebSocketServer から呼び出され、Serviceを使った処理を行う
 *  - JSONを受け取り、JSONを返す
 */
public class LobbyController {

    private AccountService accountService = new AccountService();

    public Map<String, Object> callAccountService(JsonNode json) {
        // "action" に応じて、AccountService のメソッドを呼び出し、
        // 結果をMapなどで返す
        Map<String, Object> result = new HashMap<>();
        String action = json.get("action").asText();

        switch (action) {
            case "register": {
                String userName = json.get("username").asText();
                String password = json.get("password").asText();
                boolean ok = accountService.createAccount(userName, password);
                if (ok) {
                    result.put("status", "success");
                    result.put("message", "Register success");
                } else {
                    result.put("status", "error");
                    result.put("message", accountService.getErrorMessage());
                }
                break;
            }
            case "login": {
                String userName = json.get("username").asText();
                String password = json.get("password").asText();
                boolean ok = accountService.login(userName, password);
                if (ok) {
                    result.put("status", "success");
                    result.put("message", "Login success");
                    // ロビー画面に移動したい場合:
                    result.put("redirect", "lobby.html");
                } else {
                    result.put("status", "error");
                    result.put("message", accountService.getErrorMessage());
                }
                break;
            }
            case "logout": {
                String userName = json.get("username").asText();
                boolean ok = accountService.logout(userName);
                if (ok) {
                    result.put("status", "success");
                    result.put("message", "Logout success");
                    result.put("redirect", "type.html");
                } else {
                    result.put("status", "error");
                    result.put("message", accountService.getErrorMessage());
                }
                break;
            }
            case "checkStats": { // 戦績確認の新しいアクション
                String userName = json.get("username").asText();
                Map<String, Integer> stats = accountService.getStats(userName);
                if (stats != null) {
                    result.put("status", "success");
                    result.put("message", "Stats fetched successfully");
                    result.put("stats", stats);
                } else {
                    result.put("status", "error");
                    result.put("message", accountService.getErrorMessage());
                }
                break;
            }
            default:
                result.put("status", "error");
                result.put("message", "Unknown action");
        }

        return result;
    }

    public Map<String, Object> callMatchService(JsonNode json) {
        // 例: matchService のロジックを呼び出す
        // 現在のサンプルコードにはマッチング関連処理がないためダミー
        Map<String, Object> result = new HashMap<>();
        result.put("status", "error");
        result.put("message", "MatchService not implemented");
        return result;
    }
}
