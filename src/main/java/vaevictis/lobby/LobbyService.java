package vaevictis.lobby;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vaevictis.lobby.ws.lobby.LobbySearch;
import vaevictis.user.User;

import org.springframework.beans.factory.annotation.Autowired;


@Service
public class LobbyService {

    @Autowired
    private LobbyRepository lobbyRepository;

    @Autowired
	public LobbyService(LobbyRepository lobbyRepository) {
		this.lobbyRepository = lobbyRepository;
	}

    public Lobby saveLobby(Lobby lobby) {
        return this.lobbyRepository.save(lobby);
    }

    public Lobby getLobby(int roomId) {
        return this.lobbyRepository.findById(roomId);
    }

    @Transactional
    public Lobby getUserLobbyAdmin(User user) {
        return this.lobbyRepository.findByLobbyAdmin(user);
    }

    public void cleanUserLobbies(String username) {
        System.out.println("Sacando al usuario "+username+" de todos los lobbies.");

        List<Lobby> lobbies = this.lobbyRepository.findByUsersIn_Username(username);
        for(Lobby lobby : lobbies){
            if(lobby.getUsersIn().size() > 1){
                lobby.removeUserIn(username);
                lobby.setLobbyAdmin(lobby.getUsersIn().get(0));
                this.saveLobby(lobby);
            }
            else{
                lobby.removeUserIn(username);
                this.saveLobby(lobby);
            }
        }

        this.cleanEmptyLobbies();
    }

    public void cleanEmptyLobbies() {
        System.out.println("Eliminando lobbies vacíos.");
        this.lobbyRepository.deleteEmptyLobbies();
    }

    public List<Lobby> getUserInvitedLobbies(String username) {
        List<Lobby> lobbies = this.lobbyRepository.findByUsersInvited_Username(username);
        return lobbies;
    }

    /*
     * Métodos WebSocket
     */
    public LobbySearch addLobbySearch(Lobby newLobby, List<LobbySearch> lobbySearchs) {
        this.removeLobbyRequest(newLobby, lobbySearchs);

        int lobbyPlayers = newLobby.getUsersIn().size();

        for(int i=0; i<lobbySearchs.size(); i++) {
            LobbySearch lobby = lobbySearchs.get(i);

            if(lobby.getGameLevel()==newLobby.getGameLevel() && lobby.getPlayerNumber() == newLobby.getPlayerNumber() && lobby.getUsersLeft() >= lobbyPlayers) {
                lobby.combineLobby(newLobby);
                return lobby;
            }
        }

        LobbySearch lobby = new LobbySearch(newLobby);
        lobbySearchs.add(lobby);
        return lobby;
    }

    /*
     *  Elimina algunos usuarios de un LobbyRequest
     */
    public LobbySearch removeLobbyRequest(Lobby lobby, List<LobbySearch> lobbySearchs) {
        LobbySearch search = null;
        for(LobbySearch lobbySearch : lobbySearchs) {
            List<User> usersLobbySearch = lobbySearch.getUsers();
            for(int i=0; i<usersLobbySearch.size(); i++) {
                User userSearch = usersLobbySearch.get(i);
                for(User userLobby : lobby.getUsersIn()) {
                    if(userSearch.getUsername().equals(userLobby.getUsername())) {
                        usersLobbySearch.remove(i);
                        search = lobbySearch;
                    }
                }
            }
        }
        return search;
    }

    /*
     *  Elimina un LobbyRequest al completo de la lista
     */
    public Boolean removeLobbyRequest(LobbySearch lobby, List<LobbySearch> lobbySearchs) {
        // se comprueba cada búsqueda actual en memoria
        for(int j=0; j<lobbySearchs.size(); j++) {
            LobbySearch lobbySearch = lobbySearchs.get(j);

            // lista de usuarios de búsqueda j
            List<User> usersLobbySearch = lobbySearch.getUsers();

            for(int i=0; i<usersLobbySearch.size(); i++) {
                User userSearch = usersLobbySearch.get(i);
                for(User userLobby : lobby.getUsers()) {
                    if(userSearch.getUsername().equals(userLobby.getUsername())) {
                        lobbySearchs.remove(j);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
