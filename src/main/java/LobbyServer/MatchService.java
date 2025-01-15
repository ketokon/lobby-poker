package LobbyServer;

import LobbyServer.Entity.User;
import com.fasterxml.jackson.databind.JsonNode;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MatchService {
    private static final Queue<JsonNode> waitingQueue = new LinkedList<>();

    // ユーザーを待機キューに追加
    public void addToQueue(JsonNode userJson) {
        waitingQueue.add(userJson);
    }

    // 待機しているユーザー数を取得
    public int getStandByNumber() {
        return waitingQueue.size();
    }

    // 待機中のユーザーが4人かどうかを確認
    public boolean fourPeopleAreOnStandBy() {
        return getStandByNumber() == 4;
    }

    // マッチングされたユーザー情報を返す
    public List<User> matchAndFetchUsers() throws SQLException {
        List<User> matchedUsers = new LinkedList<>();
        for (int i = 0; i < 4; i++) {
            JsonNode userJson = waitingQueue.poll();
            if (userJson != null) {
                String userName = userJson.get("username").asText();
                User user = LobbyCommunication.findUserByName(userName);
                if (user != null) {
                    matchedUsers.add(user);
                }
            }
        }
        return matchedUsers;
    }

    // ログイン状態をリセット
    public void resetLoginState(List<User> users) {
        try {
            for (User user : users) {
                LobbyCommunication.updateLoginState(user.getUserName(), 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
