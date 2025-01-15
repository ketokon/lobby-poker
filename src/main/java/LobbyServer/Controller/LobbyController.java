package LobbyServer.Controller;

import LobbyServer.AccountService;
import LobbyServer.Entity.User;
import LobbyServer.MatchService;
import com.fasterxml.jackson.databind.JsonNode;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * LobbyController:
 *  - WebSocketServer から呼び出され、Serviceを使った処理を行う
 *  - JSONを受け取り、JSONを返す
 */
public class LobbyController {

    private AccountService accountService = new AccountService();
    private MatchService matchService = new MatchService();

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
        Map<String, Object> result = new HashMap<>();
        String action = json.get("action").asText();

        switch (action) {
            case "startMatching": {
                matchService.addToQueue(json);

                if (matchService.fourPeopleAreOnStandBy()) {
                    try {
                        List<User> matchedUsers = matchService.matchAndFetchUsers();
                        List<Map<String, Object>> userInfoList = new LinkedList<>();

                        for (User user : matchedUsers) {
                            Map<String, Object> userInfo = new HashMap<>();
                            userInfo.put("userID", user.getUserID());
                            userInfo.put("serverIP", "192.168.0.1"); // アプリケーションサーバーのIPアドレス
                            userInfo.put("port", 12345); // アプリケーションサーバーのポート番号
                            userInfoList.add(userInfo);
                        }

                        result.put("status", "success");
                        result.put("message", "Matching complete!");
                        result.put("users", userInfoList);

                        // マッチングしたユーザーのloginStateをリセット
                        matchService.resetLoginState(matchedUsers);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        result.put("status", "error");
                        result.put("message", "Database error: " + e.getMessage());
                    }
                } else {
                    result.put("status", "waiting");
                    result.put("message", "Waiting for more players...");
                }
                break;
            }
            default:
                result.put("status", "error");
                result.put("message", "Unknown action");
        }

        return result;
    }

}
