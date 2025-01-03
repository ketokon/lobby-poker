package LobbyServer.Entity;

public class User {
    private int userID;
    private String userName;
    private String userPass;
    private int loginState;

    // コンストラクタ
    public User() {}
    public User(int userID, String userName, String userPass, int loginState) {
        this.userID = userID;
        this.userName = userName;
        this.userPass = userPass;
        this.loginState = loginState;
    }

    // ゲッター/セッター
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }
    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public int getLoginState() {
        return loginState;
    }
    public void setLoginState(int loginState) {
        this.loginState = loginState;
    }
}
