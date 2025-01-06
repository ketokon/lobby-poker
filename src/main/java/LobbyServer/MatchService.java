package LobbyServer;

import LobbyServer.Entity.User;

import java.util.ArrayList;
import java.util.List;

public class MatchService {

    private List<User> standbyUsers = new ArrayList<>();

    public void addUserToStandby(User user) {
        standbyUsers.add(user);
    }

    public boolean fourPeopleAreOnStandBy() {
        return standbyUsers.size() >= 4;
    }

    public List<User> getStandbyUsers() {
        List<User> selectedUsers = new ArrayList<>(standbyUsers.subList(0, 4));
        standbyUsers.subList(0, 4).clear(); // 先頭の4人を削除
        return selectedUsers;
    }
}
