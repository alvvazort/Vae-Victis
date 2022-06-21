package vaevictis.game.ws;

import vaevictis.game.Game;
import vaevictis.lobby.ws.OutputWebSocket;

public class OutputGame extends OutputWebSocket {

    // partida del usuario. Incluye datos sensibles del usuario a razón de prototipo. @JsonIgnore en User
    public Game game;

    // devuelve la partida del usuario cuando se crea
    public String gameCode;

    public OutputGame(String message, Boolean isError) {
        super(message, isError);
    }
    public OutputGame(Game game) {
        super("Actualizando datos de la partida...");

        this.game = game;
    }

    public Game getGame() {
        return this.game;
    }
}
