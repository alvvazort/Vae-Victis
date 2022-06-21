package vaevictis.lobby.ws.lobby;

import java.util.List;

import vaevictis.lobby.ws.OutputWebSocket;

public class OutputLobbyRequest extends OutputWebSocket {
    //búsqueda global de partidas (testing purposes)
    public List<LobbySearch> lobbySearchs;

    // partida del usuario. Incluye datos sensibles del usuario a razón de prototipo. @JsonIgnore en User
    public LobbySearch lobbySearch;

    // devuelve la partida del usuario cuando se crea
    public String gameCode;

    public OutputLobbyRequest(String message, Boolean isError) {
        super(message, isError);
    }
    public OutputLobbyRequest(List<LobbySearch> lobbySearchs) {
        super("Buscando y encontrando partidas...");

        this.lobbySearchs = lobbySearchs;
    }

    public OutputLobbyRequest(LobbySearch lobbySearch) {
        super("Lobby actualizado");

        this.lobbySearch = lobbySearch;
    }
    public OutputLobbyRequest(String gameCode) {
        super("Partida encontrada");
        this.gameCode = gameCode;
    }

    public String getGameCode() {
        return this.gameCode;
    }

    public List<LobbySearch> getLobbySearchs() {
        return this.lobbySearchs;
    }
}
