package vaevictis.lobby.ws.lobby;

import java.util.List;

import vaevictis.lobby.Lobby;
import vaevictis.user.User;

public class LobbySearch {
    private List<User> users;
    private int playerNumber;
    private int gameLevel;
    private User lobbyAdmin;

    public LobbySearch(Lobby lobby) {
        this.users = lobby.getUsersIn();
        this.playerNumber = lobby.getPlayerNumber();
        this.gameLevel = lobby.getGameLevel();
        this.lobbyAdmin = lobby.getLobbyAdmin();
    }

    public int getPlayerNumber() {
        return this.playerNumber;
    }

    public int getGameLevel() {
        return this.gameLevel;
    }

    public User getLobbyAdmin() {
        return this.lobbyAdmin;
    }

    public void combineLobby(Lobby lobby) {
        for(User user : lobby.getUsersIn()) {
            if(!this.isUserAlreadyInLobby(user)) {
                this.addUser(user);
            }
        }
    }

    public boolean isUserAlreadyInLobby(User user) {
        for(User lobbyUser : this.getUsers()) {
            if(lobbyUser.getUsername().equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    public void addUser(User user) {
        this.users.add(user);
    }
    public int getUsersLeft() {
        return this.playerNumber - this.users.size();
    }
    public List<User> getUsers() {
        return this.users;
    }
    public void removePlayer(String usernick) {
        this.users.removeIf(user -> user.getUsername().equals(usernick));
    }
    public boolean isSatified() {
        return this.getUsersLeft()==0;
    }
}
