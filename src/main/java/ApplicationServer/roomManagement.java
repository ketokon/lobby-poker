package ApplicationServer;

import LobbyServer.Entity.User;
import java.util.ArrayList;
import java.util.List;

public class roomManagement {

    public void createRoom(List<User> users) {
        List<ApplicationUser> applicationUsers = new ArrayList<>();
        for (User user : users) {
            applicationUsers.add(makeApplicationUser(user));
        }
        // ルーム作成完了の処理
        System.out.println("Room created with users: " + applicationUsers);
    }

    private ApplicationUser makeApplicationUser(User user) {
        return new ApplicationUser(user.getUserID(), user.getUserName());
    }
}
