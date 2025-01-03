package LobbyServer;

import LobbyServer.Entity.User;
import java.sql.*;
import java.util.Optional;

/**
 * DBサーバやアプリケーションサーバとの通信を担当
 * 今回はDB操作用メソッドをまとめたサンプル
 */
public class LobbyCommunication {

    // findUserByName
    public static User findUserByName(String userName) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT id, username, password, loginState FROM User WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("id"));
                    user.setUserName(rs.getString("username"));
                    user.setUserPass(rs.getString("password"));
                    user.setLoginState(rs.getInt("loginState"));
                    return user;
                }
                return null;
            }
        }
    }

    // countUserByName
    public static int countUserByName(String userName) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT COUNT(*) FROM User WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    // insertUser
    public static void insertUser(String userName, String userPass) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "INSERT INTO User (username, password, loginState) VALUES (?, ?, 0)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                pstmt.setString(2, userPass);
                pstmt.executeUpdate();
            }
        }
    }

    // updateLoginState
    public static int updateLoginState(String userName, int newState) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "UPDATE User SET loginState = ? WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, newState);
                pstmt.setString(2, userName);
                return pstmt.executeUpdate();
            }
        }
    }

    // もしアプリケーションサーバへ sendApplicationServer(...) のような通信が必要なら追加
}