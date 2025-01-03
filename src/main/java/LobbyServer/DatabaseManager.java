package LobbyServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://sql.yamazaki.se.shibaura-it.ac.jp:13308/db_group_a"
            + "?useUnicode=true&character_set_server=utf8mb4&useSSL=false";
    private static final String USER = "group_a";
    private static final String PASSWORD = "group_a";

    // シングルトンパターンなどで管理しても良いが、とりあえず簡易実装
    public static Connection getConnection() throws SQLException {
        // MySQL Connector/J 8.0 からは com.mysql.cj.jdbc.Driver を利用
        // Class.forName("com.mysql.cj.jdbc.Driver"); // Java 6/7なら必要
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}