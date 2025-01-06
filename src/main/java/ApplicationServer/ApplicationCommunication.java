package ApplicationServer;

import LobbyServer.Entity.User;
import java.util.List;

public class ApplicationCommunication {

    public void sendUserInfoToRoomManagement(List<User> users) {
        roomManagement roomManager = new roomManagement();
        roomManager.createRoom(users);
    }
}
