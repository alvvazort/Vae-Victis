package vaevictis.game.turn.Phases.Doom;

import javax.persistence.Entity;

import vaevictis.game.Game;
import vaevictis.game.GameService;
import vaevictis.game.turn.Phases.Phase;
import vaevictis.game.turn.Phases.PhaseState;
import vaevictis.game.turn.ws.TurnOutput;
import vaevictis.game.turn.ws.TurnRequest;
import vaevictis.player.Player;

@Entity
public class DoomState extends PhaseState {

    public static final String phaseName = "Doom";

    public Game game;
    public Player player;

    public DoomState(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.setName(phaseName);
    }

    public DoomState() {}

    @Override
    public void next(Phase phase) {
        // TODO Auto-generated method stub
        phase.state = new DoomDiceThrowState(game, player);
    }

    @Override
    public void prev(Phase phase) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void printPhase() {
        System.out.println(phaseName);
        
    }

    @Override
    public void phaseHandle(GameService gameService, TurnRequest turnRequest, TurnOutput turnOutput) {

        System.out.println("wups");
    }

    @Override
    public String getPhaseName() {
        // TODO Auto-generated method stub
        return phaseName;
    }

}
