package vaevictis.lobby.ws.lobby;

public class LobbyRequest {
    private String userAdmin;

    private int playersNumber;
    private int gameLevel;
    private int action;

    public int getPlayersNumber() {
        return this.playersNumber;
    }

    public int getGameLevel() {
        return this.gameLevel;
    }

    public String getUserAdmin() {
        return this.userAdmin;
    }

    /*
     * Si la accion es 0 significa que el usuario 
     * se desconecta y que por lo tanto se tiene que borrar
     * su búsqueda de partida
     */
    public int getAction() {
        return this.action;
    }
}
