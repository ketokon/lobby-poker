package LobbyServer;

import LobbyServer.Entity.User;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * DBサーバやアプリケーションサーバとの通信を担当
 * 今回はDB操作用メソッドをまとめたサンプル
 */
public class LobbyCommunication {

    // findUserByName
    public static User findUserByName(String userName) throws SQLException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT Userid, username, password, loginState FROM User WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    User user = new User();
                    user.setUserID(rs.getInt("Userid"));
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

    public static Map<String, Integer> getUserStats(String userName) throws SQLException {
        Map<String, Integer> stats = new HashMap<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            String sql = "SELECT count1st, count2nd, count3rd, count4th FROM User WHERE username = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, userName);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    stats.put("count1st", rs.getInt("count1st"));
                    stats.put("count2nd", rs.getInt("count2nd"));
                    stats.put("count3rd", rs.getInt("count3rd"));
                    stats.put("count4th", rs.getInt("count4th"));
                }
            }
        }
        return stats;
    }


    // もしアプリケーションサーバへ sendApplicationServer(...) のような通信が必要なら追加
}