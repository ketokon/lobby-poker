package LobbyServer;

import LobbyServer.Entity.User;
import LobbyServer.LobbyCommunication; // 後述
import java.sql.SQLException;
import java.util.Map;

/**
 * アカウント関連(ログイン/ログアウト/登録など)のビジネスロジック
 */
public class AccountService {

    private String errorMessage;

    // ログイン
    public boolean login(String userName, String userPass) {
        // 例: LobbyCommunicationを通してDBアクセスする
        // ここで実際にDB確認し、loginStateのチェックなど
        try {
            User user = LobbyCommunication.findUserByName(userName);
            if (user == null) {
                errorMessage = "User not found";
                return false;
            }
            if (!userPass.equals(user.getUserPass())) {
                errorMessage = "Invalid password";
                return false;
            }
            if (user.getLoginState() == 1) {
                errorMessage = "Already logged in";
                return false;
            }
            // ログイン成功 → DBのloginState更新
            LobbyCommunication.updateLoginState(userName, 1);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage = "Database error: " + e.getMessage();
            return false;
        }
    }

    // ログアウト
    public boolean logout(String userName) {
        try {
            // loginStateを0にする
            int updated = LobbyCommunication.updateLoginState(userName, 0);
            if (updated == 0) {
                errorMessage = "User not found or already logged out";
                return false;
            }
            return true;
        } catch (SQLException e) {
            errorMessage = "Database error: " + e.getMessage();
            return false;
        }
    }

    // アカウント作成
    public boolean createAccount(String userName, String userPass) {
        if (userName.isEmpty() || userPass.isEmpty()) {
            errorMessage = "Username or password is blank";
            return false;
        }
        try {
            // 同名ユーザのチェック
            if (LobbyCommunication.countUserByName(userName) > 0) {
                errorMessage = "User already exists";
                return false;
            }
            // DBに新規登録
            LobbyCommunication.insertUser(userName, userPass);
            return true;
        } catch (SQLException e) {
            errorMessage = "Database error: " + e.getMessage();
            return false;
        }
    }

    public Map<String, Integer> getStats(String userName) {
        try {
            // `LobbyCommunication`を使ってデータベースから戦績を取得
            return LobbyCommunication.getUserStats(userName);
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage = "Database error: " + e.getMessage();
            return null;
        }
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    // checkResult, falseLogin, successLogin などUMLに合わせて必要なら追加
}